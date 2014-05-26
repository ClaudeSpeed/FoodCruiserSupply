package org.apache.camel.example.jmstofile;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spring.Main;

public class CruiseRouteBuilder extends RouteBuilder {
	
	public static void main(String[] args) throws Exception {
        new Main().run(args);
    }
	
    public void configure() {
    	System.out.println("configuration");
    	
    	//Apparently it works to create a new CamelContext and work with it here.
    	//I'm not sure if this is best practice..
//    	CamelContext context = new DefaultCamelContext();
//    	ProducerTemplate template = context.createProducerTemplate();
        
//    	from jmsToFileExample
    	//from("test-jms:queue:test.queue").to("file://test");
    	
    	
//    	Luke's playground
    	String domain = "gmail.com";
    	String username = "foodsupplycruiser@gmail.com";
    	String password = "foodSC01";
    	
    	Endpoint fromMail = endpoint("imaps://imap." + domain + ":993?username=" + username + "&password=" + password + "&fetchSize=1&unseen=true&consumer.delay=60000");
    	Endpoint toMail = endpoint("smtps://smtp." + domain + ":465?username=" + username + "&password=" + password);
    	
//    	SendMail
    	String receiver = "foodsupplycruiser@gmail.com";
    	
//    	Map<String, Object> map = new HashMap<String, Object>();
//    	map.put("To", "davsclaus@apache.org");
//    	map.put("From", "jstrachan@apache.org");
//    	map.put("Subject", "Camel rocks");
//    	 
//    	String body = "Hello Claus.\nYes it does.\n\nRegards James.";
//    	template.sendBodyAndHeaders("smtp://davsclaus@apache.org", body, map);
    	from("test-jms:queue:test.queue").setHeader("subject", constant("This is a test")).setHeader("to", constant(receiver)).to(toMail);
    	
//    	ReceiveMail
        from(fromMail).process(new MyMailProcessor()).to("file://test");
        
//      end Luke's playground
        
    }
}
