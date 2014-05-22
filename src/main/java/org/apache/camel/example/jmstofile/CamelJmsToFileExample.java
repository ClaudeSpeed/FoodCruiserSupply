/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.example.jmstofile;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * An example class for demonstrating some of the basics behind Camel. This
 * example sends some text messages on to a JMS Queue, consumes them and
 * persists them to disk
 */
public final class CamelJmsToFileExample {

    private CamelJmsToFileExample() {        
    }
    
    public static void main(String args[]) throws Exception {
    	
        CamelContext context = new DefaultCamelContext();
        
//        from example jmsToFile
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        context.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        
        RouteBuilder rb = new CruiseRouteBuilder();
        
        context.addRoutes(rb);
        
        ProducerTemplate template = context.createProducerTemplate();
        
        context.start();
        
        for (int i = 0; i < 2; i++) {
            template.sendBody("test-jms:queue:test.queue", "Luketest Message: " + i);
        }
        
//        private void sendEmail(String body) {
//            // send the email to your mail server
//            String url = "smtp://someone@localhost?password=secret&to=incident@mycompany.com";
//            template.sendBodyAndHeader(url, body, "subject", "New incident reported");
//        }
        
        Thread.sleep(1000);
        context.stop();
    }
    
    
//    <purchaseList>
//    	<meal name="Cake" postAsHighlight="true">
//    		<ingredient name="egg" quantity="20" unit="trays" price="20" priceCurrency="EUR" />
//    	</meal>
//    </purchaseList>
    
}