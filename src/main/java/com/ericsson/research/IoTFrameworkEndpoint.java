package com.ericsson.research;

import com.ericsson.research.dataset.Datapoint;
import com.ericsson.research.dataset.Location;
import com.ericsson.research.dataset.Query;
import com.ericsson.research.dataset.Stream;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ProxyServer;
import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class IoTFrameworkEndpoint {

    private final AsyncHttpClient httpClient;
    private final String apiUrl;
    private ProxyServer proxyServer = null;
    private final AsyncHandler handler = new AsyncHandler();
    private final Connection connection;

    public IoTFrameworkEndpoint(String HostName) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HostName); // listening on port 5672
        connection = factory.newConnection();

        apiUrl = "http://" + HostName + ":8000";
        httpClient = new AsyncHttpClient();
    }

    public IoTFrameworkEndpoint(String HostName, String ProxyServer, int Port) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HostName); // listening on port 5672
        connection = factory.newConnection();
        apiUrl = "http:// " + HostName + ":8000";
        httpClient = new AsyncHttpClient();
        proxyServer = new ProxyServer(ProxyServer, Port);
    }

    public void close() throws IOException {
        httpClient.close();
        connection.close();
    }

    // TODO - The point with this function is to receive a resource Id as input and to produce a set of streams as input that already contain the metadata required
    public String createStreamsForResource(String ResourceId) {
        return null;
    }

    public String createStream(Stream stream) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient.BoundRequestBuilder request = buildCreateStreamRequest(stream);
        return request.execute().get().getResponseBody();
    }

    public String deleteStream(String streamId) throws IOException, ExecutionException, InterruptedException {
        String url = apiUrl + "/streams/" + streamId;
        AsyncHttpClient.BoundRequestBuilder request = httpClient.prepareDelete(url);

        if (proxyServer != null)
            request.setProxyServer(proxyServer);

        return request.execute().get().getResponseBody();
    }

    public String getLocationFromFreeIP( String ipAddress ) throws IOException, ExecutionException, InterruptedException {
        String url = "http://freegeoip.net/json/"+ipAddress;
        AsyncHttpClient.BoundRequestBuilder request = httpClient.prepareGet(url);

        if (proxyServer != null)
            request.setProxyServer(proxyServer);

        String location = request.execute().get().getResponseBody();

        int indexOf = location.indexOf("\"latitude\":")+11;
        String latitude = location.substring(indexOf, location.indexOf(",\"longitude\":"));
        indexOf = location.indexOf("\"longitude\":")+12;
        String longitude = location.substring(indexOf, location.indexOf(",\"metro_code\":" ));
        return new Location(latitude, longitude).toJsonString();
    }

    private AsyncHttpClient.BoundRequestBuilder buildCreateStreamRequest(Stream stream) throws IOException {
        String url = apiUrl + "/streams/";
        AsyncHttpClient.BoundRequestBuilder request = httpClient.preparePost(url).
                setBody(stream.toJsonString()).setHeader("Content-type", "application/json");

        if (proxyServer != null)
            request.setProxyServer(proxyServer);

        return request;
    }

    public String postDatapoint(String streamId, float value) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient.BoundRequestBuilder request = buildDataPointPostRequest(streamId, value);
        return request.execute().get().getResponseBody();
    }

    public void postDatapointAsync(String streamId, float value) throws IOException {
        AsyncHttpClient.BoundRequestBuilder request = buildDataPointPostRequest(streamId, value);
        request.execute(handler);
    }

    public String[] getStreamIdsForQuery(String Query) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient.BoundRequestBuilder request = buildSearchPostRequest(Query);
        String result = request.execute().get().getResponseBody();
        String[] ids = processSearchResult(result);
        return ids;
    }

    //TODO this should be avoided, instead elasticsearch should be configured to return on specific fields
    private String[] processSearchResult(String result) {
        Vector<String> results = new Vector<String>();
        Vector<Integer> indexes = new Vector<Integer>();
        int index = 0;
        while ((index = result.indexOf("\"_id\":", index)) >= 0) {
            indexes.add(index);
            index += 1;
        }

        for (Integer t : indexes) {
            results.add(result.substring(t + 7, result.indexOf("\",\"", t + 7)));
        }
        return results.toArray(new String[]{});
    }

    private AsyncHttpClient.BoundRequestBuilder buildSearchPostRequest(String query) {
        String url = apiUrl + "/streams/_search";
        AsyncHttpClient.BoundRequestBuilder request = httpClient.preparePost(url).
                setBody((new Query(query)).toJsonString()).setHeader("Content-type", "application/json");

        if (proxyServer != null)
            request.setProxyServer(proxyServer);

        return request;
    }

    private AsyncHttpClient.BoundRequestBuilder buildDataPointPostRequest(String streamId, float value) {
        String url = apiUrl + "/streams/" + streamId + "/data";
        AsyncHttpClient.BoundRequestBuilder request = httpClient.preparePost(url).
                setBody((new Datapoint(value)).toJsonString()).setHeader("Content-type", "application/json");

        if (proxyServer != null)
            request.setProxyServer(proxyServer);

        return request;
    }

    public void subscribe(String StreamId, final StringCallback stringCallback) throws IOException {
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
    }
}