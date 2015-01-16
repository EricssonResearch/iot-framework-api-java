package com.ericsson.research;

import com.ericsson.research.dataset.*;
import com.ericsson.research.errors.MalformedJsonResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ProxyServer;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

/*
 * ##_BEGIN_LICENSE_##
 * IoT-Framework API Java
 * ----------
 * Copyright (C) 2014 Ericsson AB
 * ----------
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the Ericsson AB nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * ##_END_LICENSE_##
 */

public class IoTFrameworkEndpoint {

    private final AsyncHttpClient httpClient;
    private final String apiUrl;
    private ProxyServer proxyServer = null;
    private final AsyncHandler handler = new AsyncHandler();
//    private Connection connection;
    private final Gson gson = new Gson();
    private final String accessToken;
    private final String userId;

    private final String ContentTypeHeader = "Content-Type";
    private final String jsonPayloadConst = "application/json";
    private final String AccessTokenHeader = "Access-Token";
    //private final String FreeGeoIpUrl = "http://freegeoip.net/json/";

    //todo make ports configurable, also make it possible to

    /**
     * IoT Framework end point constructor without proxy
     * @param HostName the host name for the iot framework, assumes that default port is 8000
     * @param UserId the user's id
     * @param AccessToken the access token for authenticating usage of the iot framework
     * @throws IOException
     */
    public IoTFrameworkEndpoint(String HostName, String UserId, String AccessToken) throws IOException {
        apiUrl = "http://" + HostName + ":8000";
        accessToken = AccessToken;
        httpClient = new AsyncHttpClient();
        userId = UserId;
    }

    /**
     * IoT Framework end point constructor with proxy
     * @param HostName the host name for the iot framework, assumes that default port is 8000
     * @param UserId the user's id
     * @param AccessToken the access token for authenticating usage of the iot framework
     * @param ProxyServer the http proxy server for making http requests
     * @param Port the port of the http proxy server
     * @throws IOException
     */
    public IoTFrameworkEndpoint(String HostName, String UserId, String AccessToken, String ProxyServer, int Port) throws IOException {
        //ConnectionFactory factory = new ConnectionFactory();
        //factory.setHost(HostName); // listening on port 5672
        //connection = factory.newConnection();
        accessToken = AccessToken;
        apiUrl = "http:// " + HostName + ":8000";
        httpClient = new AsyncHttpClient();
        proxyServer = new ProxyServer(ProxyServer, Port);
        userId = UserId;
    }

    public void close() throws IOException {
        httpClient.close();
        //connection.close();
    }

    private String getUserId() {
        return ""; //implement a function that return the user id
    }

    public Resource[] getResources(String Query) throws IOException, ExecutionException, InterruptedException, MalformedJsonResponse {
        Type type = new TypeToken<ESSearchResponse<Resource>>() {}.getType();
        ESSearchResponse<Resource> esresponse = getData(Query, type, "resources");
        Vector<Resource> resources = new Vector<Resource>();
        for ( Hits<Resource> hit : esresponse.getPreambleHits().getHits() ) {
            for ( Stream stream : hit.getSource().getSuggestedStreams()) {
                stream.setResourceType(new ResourceType(hit.getId(), ""));
            }
            resources.add(hit.getSource());
        }
        return resources.toArray(new Resource[resources.size()]);
    }

    public ESCreatedResponse createStream(Stream stream) throws IOException, ExecutionException, InterruptedException {
        if ( stream.getUserId() == null )
            stream.setUserId(userId);
        AsyncHttpClient.BoundRequestBuilder request = buildCreateStreamRequest(stream);
        String response = request.execute().get().getResponseBody();
        return gson.fromJson(response, ESCreatedResponse.class );
    }

    public ESFoundResponse deleteStream(String streamId) throws IOException, ExecutionException, InterruptedException {
        String url = apiUrl + "/streams/" + streamId;
        AsyncHttpClient.BoundRequestBuilder request = httpClient.prepareDelete(url);

        if (proxyServer != null)
            request.setProxyServer(proxyServer);

        String response = request.execute().get().getResponseBody();
        return gson.fromJson(response, ESFoundResponse.class);
    }

    public String getPublicIpAddress() throws IOException, ExecutionException, InterruptedException {
        String url = "http://www.telize.com/ip";
        AsyncHttpClient.BoundRequestBuilder request = httpClient.prepareGet(url);

        if (proxyServer != null)
            request.setProxyServer(proxyServer);

        String publicIpAddress = request.execute().get().getResponseBody();

        return publicIpAddress;
    }

    private AsyncHttpClient.BoundRequestBuilder buildCreateStreamRequest(Stream stream) throws IOException {
        String url = apiUrl + "/streams";
        String jsonPayload = gson.toJson(stream);
        return buildPostRequest(url, jsonPayload);
    }

    /**
     * postDatapoint inserts a new a datapoint into an existing stream
     * @param streamId the id of the stream where the datapoint would be inserted
     * @param value the numeric value of the data point
     * @return the id of the newly created datapoint
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public ESCreatedResponse postDatapoint(String streamId, float value) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient.BoundRequestBuilder request = buildDataPointPostRequest(streamId, value);
        String response = request.execute().get().getResponseBody();
        return gson.fromJson(response, ESCreatedResponse.class);
    }

    public void postDatapointAsync(String streamId, float value) throws IOException {
        AsyncHttpClient.BoundRequestBuilder request = buildDataPointPostRequest(streamId, value);
        request.execute(handler);
    }

    /**
     * getStreamForQuery returns an array of streams that satisfies the input query
     * @param Query the input query
     * @return array of relevant strams
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws MalformedJsonResponse
     */
    public Stream[] getStreamsForQuery(String Query) throws IOException, ExecutionException, InterruptedException, MalformedJsonResponse {
        Type type = new TypeToken<ESSearchResponse<Stream>>() {}.getType();
        ESSearchResponse<Stream> esresponse = getData(Query, type, "streams");
        Vector<Stream> streams = new Vector<Stream>();
        for ( Hits<Stream> hit : esresponse.getPreambleHits().getHits() ) {
            Stream stream = hit.getSource();
            stream.setId(hit.getId());
            streams.add(stream);
        }
        return streams.toArray(new Stream[streams.size()]);
    }

    private ESSearchResponse getData(String Query, Type type, String resource) throws MalformedJsonResponse, IOException, ExecutionException, InterruptedException {
        AsyncHttpClient.BoundRequestBuilder request = buildSearchPostRequest(resource, Query, 10);
        String response = request.execute().get().getResponseBody();
        ESSearchResponse<?> esresponse = null;

        try {
            esresponse = gson.fromJson(response, type);
        }
        catch( JsonSyntaxException x) {
            throw new MalformedJsonResponse(x, response);
        }

        // Todo, use pagination in order to re-use previous results

        int size = esresponse.getPreambleHits().getTotal();

        if ( size > 10 ) {
            request = buildSearchPostRequest(resource, Query, size);
            response = request.execute().get().getResponseBody();
            esresponse = gson.fromJson(response, type);
        }

        return esresponse;
    }

    private AsyncHttpClient.BoundRequestBuilder buildSearchPostRequest(String resource, String query, int Size) {
        String url = apiUrl + "/" + resource + "/_search" + ( (Size >10 ) ? "?size=" + Size : "" );
        String jsonPayload = new Query(query).toJsonString();
        return buildPostRequest(url, jsonPayload);
    }

    private AsyncHttpClient.BoundRequestBuilder buildSearchPostRequestWithPagination(String resource, String query, int From, int Size) {
        String url = apiUrl + "/" + resource + "/_search?size=" + Size + "&from=" + From;
        String jsonPayload = new Query(query).toJsonString();
        return buildPostRequest(url, jsonPayload);
    }

    private AsyncHttpClient.BoundRequestBuilder buildDataPointPostRequest(String streamId, float value) {
        String url = apiUrl + "/streams/" + streamId + "/data";
        String jsonPayload = gson.toJson(new Datapoint(value));
        return buildPostRequest(url, jsonPayload);
    }

    private AsyncHttpClient.BoundRequestBuilder buildPostRequest(String url, String jsonPayload) {
        AsyncHttpClient.BoundRequestBuilder request = httpClient.preparePost(url).
                setBody(jsonPayload).
                setHeader(ContentTypeHeader, jsonPayloadConst).
                setHeader(AccessTokenHeader, accessToken);

        if (proxyServer != null)
            request.setProxyServer(proxyServer);

        return request;
    }

    //public getSubscription() {
    //    ConnectionFactory factory = new ConnectionFactory();
        //factory.setHost(HostName); // listening on port 5672
        //connection = factory.newConnection();

//    }

    /*public void subscribe(String StreamId, final StringCallback stringCallback) throws IOException {
        final Channel channel = connection.createChannel();
        final String EXCHANGE_NAME = "streams." + StreamId;

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        boolean autoAck = true;
        channel.basicConsume(queueName, autoAck, "myConsumerTag",
                new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               AMQP.BasicProperties properties,
                                               byte[] body)
                            throws IOException {
                        String notification = new String(body);
                        stringCallback.handleNotification(notification);
                    }
                });
    }*/
}

//TODOs
//1. Would be interesting to enable location range related queries - i.e. find all sensors in a particular range - the feature is supported by our web-site
//2. API should be extended in order to support OpenID - is the access token enough?