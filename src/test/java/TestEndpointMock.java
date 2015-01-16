import com.ericsson.research.IoTFrameworkEndpoint;
import com.ericsson.research.dataset.ESCreatedResponse;
import com.ericsson.research.dataset.ESFoundResponse;
import com.ericsson.research.dataset.Location;
import com.ericsson.research.dataset.Stream;
import com.ericsson.research.errors.MalformedJsonResponse;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

/*
 * IoT-Framework API Java
 * ----------
 * Copyright (C) 2014 Ericsson AB
 * ----------
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
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
 */

public class TestEndpointMock {

    private static final String hostName = "localhost";
    private static final String streamId = "wqc95xXwREyU-ZD2lrGajQ";
    private static final String accessToken = "ya29.rQBEPYOs-t5HtvpnYL6teA1v5sLIPJas1sl4RsDXE46cuMnJ5wnlU-e_qKJgGEbTNXEopJ1Kr3LHEw";
    private static final String userId = "113402477981219582276";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8000);

    @Test
    // purpose of this test is to mock the successful case of submitting a data point
    public void testPostDataPoint() throws IOException {

        stubFor(post(urlEqualTo("/streams/" + streamId + "/data"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"_index\":\"sensorcloud\",\"_type\":\"datapoint\",\"_id\":\"IoD1M0zVSA6LNpvNM2v1rg\",\"_version\":1,\"created\":true}")));


        IoTFrameworkEndpoint endpoint = new IoTFrameworkEndpoint(hostName, userId, accessToken);

        try {
            ESCreatedResponse response = endpoint.postDatapoint(streamId, 1f);
            assertEquals( response.getId(), "IoD1M0zVSA6LNpvNM2v1rg");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetStreamsForQuery() throws IOException {
        IoTFrameworkEndpoint endpoint = new IoTFrameworkEndpoint(hostName, userId, accessToken);
        Stream[] ids = null;

        stubFor(post(urlEqualTo("/streams/_search"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"took\":7,\"timed_out\":false,\"_shards\":{\"total\":5,\"successful\":5,\"failed\":0},\"hits\":{\"total\":5,\"max_score\":3.0479846,\"hits\":[{\"_index\":\"sensorcloud\",\"_type\":\"stream\",\"_id\":\"MXN4KL2-SiO6bhyaXuh8eg\",\"_score\":3.0479846,\"_source\":{\"accuracy\":null,\"active\":true,\"creation_date\":\"2014-10-31\",\"description\":\"measures presence in rabalder\",\"last_updated\":\"2014-10-28T18:41:20.000\",\"location\":{\"lat\":59.403121399999996,\"lon\":17.94857300000001},\"max_val\":\"1\",\"min_val\":\"0\",\"name\":\"presence in rabalder\",\"nr_subscribers\":0,\"polling\":false,\"polling_freq\":0,\"private\":false,\"quality\":1.0,\"resource\":{\"resource_type\":\"\",\"uuid\":\"\"},\"subscribers\":[],\"tags\":\"\",\"type\":\"\",\"unit\":\"boolean\",\"user_id\":\"113402477981219582276\",\"user_ranking\":{\"average\":0.0,\"nr_rankings\":0}}},{\"_index\":\"sensorcloud\",\"_type\":\"stream\",\"_id\":\"TccjcPb7QqCan7Hgg92GJg\",\"_score\":2.61256,\"_source\":{\"accuracy\":null,\"active\":true,\"creation_date\":\"2014-10-31\",\"description\":\"temperature measurement in room rabalder\",\"last_updated\":\"2015-01-08T15:10:53.000\",\"location\":{\"lat\":59.403213799999996,\"lon\":17.948658499999965},\"max_val\":\"120\",\"min_val\":\"0\",\"name\":\"temperature in rabalder\",\"nr_subscribers\":0,\"polling\":false,\"polling_freq\":0,\"private\":false,\"quality\":1.0,\"resource\":{\"resource_type\":\"\",\"uuid\":\"\"},\"subscribers\":[],\"tags\":\"temperature\",\"type\":\"temperature\",\"unit\":\"celsius\",\"user_id\":\"113402477981219582276\",\"user_ranking\":{\"average\":0.0,\"nr_rankings\":0}}},{\"_index\":\"sensorcloud\",\"_type\":\"stream\",\"_id\":\"5Zbzr0EVSUaOMoBXEpv-FA\",\"_score\":2.6125581,\"_source\":{\"accuracy\":null,\"active\":true,\"creation_date\":\"2014-10-31\",\"description\":\"measures co2 concentration in rabalder\",\"last_updated\":\"2014-10-10T16:19:09.000\",\"location\":{\"lat\":59.403176200000004,\"lon\":17.948541299999988},\"max_val\":\"100\",\"min_val\":\"0\",\"name\":\"co2 concentration in rabalder\",\"nr_subscribers\":0,\"polling\":false,\"polling_freq\":0,\"private\":false,\"quality\":1.0,\"resource\":{\"resource_type\":\"\",\"uuid\":\"\"},\"subscribers\":[],\"tags\":\"co2\",\"type\":\"co2\",\"unit\":\"percentage\",\"user_id\":\"113402477981219582276\",\"user_ranking\":{\"average\":0.0,\"nr_rankings\":0}}},{\"_index\":\"sensorcloud\",\"_type\":\"stream\",\"_id\":\"xPQxq5UbScyvYbz9Fv0CMw\",\"_score\":2.6125028,\"_source\":{\"accuracy\":null,\"active\":true,\"creation_date\":\"2014-10-31\",\"description\":\"measures humidity in room rabalder\",\"last_updated\":\"2014-10-23T11:58:46.000\",\"location\":{\"lat\":59.40316730000001,\"lon\":17.948527900000045},\"max_val\":\"100\",\"min_val\":\"0\",\"name\":\"humidity in rabalder\",\"nr_subscribers\":0,\"polling\":false,\"polling_freq\":0,\"private\":false,\"quality\":1.0,\"resource\":{\"resource_type\":\"\",\"uuid\":\"\"},\"subscribers\":[],\"tags\":\"humidity\",\"type\":\"humidity\",\"unit\":\"percentage\",\"user_id\":\"113402477981219582276\",\"user_ranking\":{\"average\":0.0,\"nr_rankings\":0}}},{\"_index\":\"sensorcloud\",\"_type\":\"stream\",\"_id\":\"iSUkzBqiRdGC4RVNEJS4og\",\"_score\":2.6125028,\"_source\":{\"accuracy\":null,\"active\":true,\"creation_date\":\"2014-10-31\",\"description\":\"measures o2 in rabalder\",\"last_updated\":\"2014-10-10T17:36:48.000\",\"location\":{\"lat\":59.40309929999999,\"lon\":17.948904099999936},\"max_val\":\"100\",\"min_val\":\"0\",\"name\":\"o2 in rabalder\",\"nr_subscribers\":0,\"polling\":false,\"polling_freq\":0,\"private\":false,\"quality\":1.0,\"resource\":{\"resource_type\":\"\",\"uuid\":\"\"},\"subscribers\":[],\"tags\":\"o2\",\"type\":\"o2\",\"unit\":\"percentage\",\"user_id\":\"113402477981219582276\",\"user_ranking\":{\"average\":0.0,\"nr_rankings\":0}}}]}}")));

        String[] result_ids = new String[]{ "MXN4KL2-SiO6bhyaXuh8eg", "TccjcPb7QqCan7Hgg92GJg", "5Zbzr0EVSUaOMoBXEpv-FA", "xPQxq5UbScyvYbz9Fv0CMw", "iSUkzBqiRdGC4RVNEJS4og"};

        try {
            ids = endpoint.getStreamsForQuery("Rabalder");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MalformedJsonResponse malformedJsonResponse) {
            malformedJsonResponse.printStackTrace();
        }

        int i=0;
        for (Stream stream : ids) {
            assertEquals(stream.getId(), result_ids[i++]);
        }
    }

    @Test
    public void testCreateAndDeleteStream() throws IOException {

        String id = "VYcfG5D-TyOc8I5bWouYrQ";

        stubFor(post(urlEqualTo("/streams"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"_index\":\"sensorcloud\",\"_type\":\"stream\",\"_id\":\"" + id + "\",\"_version\":1,\"created\":true}")));

        stubFor(delete(urlEqualTo("/streams/" + id))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"found\":true,\"_index\":\"sensorcloud\",\"_type\":\"stream\",\"_id\":\"" + id + "\",\"_version\":2}")));

        IoTFrameworkEndpoint endpoint = new IoTFrameworkEndpoint(hostName, userId, accessToken);

        Location location = new Location("65.0", "17.0");

        Stream stream = new Stream("123","mystream","this is a description", location);

        try {
            ESCreatedResponse result = endpoint.createStream(stream);
            assertEquals(result.getId(), id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ESFoundResponse response = endpoint.deleteStream(id);
            assertEquals( response.found(), true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
