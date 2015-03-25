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

import com.ericsson.research.DataPointCallback;
import com.ericsson.research.IoTFrameworkEndpoint;
import com.ericsson.research.dataset.DataPointNotification;

import java.io.IOException;

public class SubscribeTest {

    private static String hostName = "your_host_name";
    private static String streamId = "your_stream_id";
    private static String rabbitMqServer = "mq_ip";

    public static void main(String[] args) throws IOException {

        System.out.println("Connecting to " + hostName);
        IoTFrameworkEndpoint endpoint = new IoTFrameworkEndpoint(hostName, "test");

        DataPointCallback callback = new DataPointCallback() {
            @Override
            public void handleNotification(DataPointNotification notification) {
                System.out.println("Received notification");
                System.out.println("Stream id: " + notification.getStreamId());
                System.out.println("Value: " + notification.getValue());
                System.out.println("Timestamp: " + notification.getTimeStamp());
            }
        };

        System.out.println("Subscribing to " + streamId + " on " + rabbitMqServer);
        endpoint.subscribe(rabbitMqServer, streamId, callback);

        synchronized (Thread.currentThread())
        {
            try {
                System.out.println("Waiting for notification. Press Ctrl+C to cancel.");
                Thread.currentThread().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Closing the connection to rabbitMq");
        callback.close();

        System.out.println("Closing IoT-Framework endpoint");
        endpoint.close();
    }
}


