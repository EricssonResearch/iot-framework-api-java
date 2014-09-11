package com.ericsson.research;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ProxyServer;
import com.rabbitmq.client.*;

import java.io.IOException;
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

    public void close() throws IOException {
        httpClient.close();
        connection.close();
    }

    public IoTFrameworkEndpoint(String HostName, String ProxyServer, int Port) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HostName); // listening on port 5672
        connection = factory.newConnection();

        apiUrl = "http:// " + HostName + ":8000";
        httpClient = new AsyncHttpClient();
        proxyServer = new ProxyServer(ProxyServer, Port);
    }

    // TODO - The point with this function is to receive a resource Id as input and to produce a set of streams as input that already contain the metadata required

    public String createStreamsForResource(String ResourceId) {
        return null;
    }

    //TODO this function
    public String createStream(String resourceId) {
        return null;
    }

    public String createStream(String type, String accuracy) {
        AsyncHttpClient.BoundRequestBuilder request = buildCreateStreamRequest();

        /*'{"accuracy":"0.2",
        "data_type":"application/json",
                "description":"test",
                "location":{"lat":59.357709,"lon":17.998635799999988},
        "max_val":"30",
                "min_val":"-30","name":"t4",
                "parser":"",
                "polling":false,
                "polling_freq":0,
                "private":false,
                "resource":{"resource_type":"","uuid":""},
        "tags":"temperature",
                "type":"test",
                "unit":"celsius",
                "uri":"",
                "user_id":"user"}'*/

        return null;
    }

    private AsyncHttpClient.BoundRequestBuilder buildCreateStreamRequest() {
        String url = apiUrl + "/streams/";
        //String payload = "{\"query\" : { \"query_string\" : {\"query\" : \"" + Query + "\"}}}";
        //AsyncHttpClient.BoundRequestBuilder request = httpClient.preparePost(url).
          //      setBody(payload).setHeader("Content-type", "application/json");

        //if (proxyServer != null)
          //  request.setProxyServer(proxyServer);

        //return request;
        return null;
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
        while ( (index = result.indexOf("\"_id\":", index)) >= 0 ) {
            indexes.add(index);
            index+=1;
        }

        for ( Integer t : indexes ) {
            results.add( result.substring(t+7, result.indexOf( "\",\"", t+7) ) );
        }
        return results.toArray(new String[]{});
    }

    private AsyncHttpClient.BoundRequestBuilder buildSearchPostRequest(String Query) {
        String url = apiUrl + "/streams/_search";
        String payload = "{\"query\" : { \"query_string\" : {\"query\" : \"" + Query + "\"}}}";
        AsyncHttpClient.BoundRequestBuilder request = httpClient.preparePost(url).
                setBody(payload).setHeader("Content-type", "application/json");

        if (proxyServer != null)
            request.setProxyServer(proxyServer);

        return request;
    }

    private AsyncHttpClient.BoundRequestBuilder buildDataPointPostRequest(String streamId, float value) {
        String url = apiUrl + "/streams/" + streamId + "/data";
        String payload = "{\"value\":" + value + "}";
        AsyncHttpClient.BoundRequestBuilder request = httpClient.preparePost(url).
                setBody(payload).setHeader("Content-type", "application/json");

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