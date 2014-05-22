package org.apache.camel.example.jmstofile;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
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
    	//I'm not sure if this is best practice..dd
//    	CamelContext context = new DefaultCamelContext(); 
//    	ProducerTemplate template = context.createProducerTemplate();
        
//    	from jmsToFileExample
    	//from("test-jms:queue:test.queue").to("file://test");
    	
    	
//    	Luke's playground
    	String domain = "gmail.com";
    	String username = "";
    	String password = "";
    	
    	Endpoint fromMail = endpoint("imaps://imap." + domain + ":993?username=" + username + "&password=" + password + "&fetchSize=1&unseen=true&debugMode=true");
    	Endpoint toMail = endpoint("smtps://smtp." + domain + ":465?username=" + username + "&password=" + password);
    	
//    	SendMail
    	//Map<String, Object> headers = new HashMap<String, Object>();
    	//headers.put("to", "lukas.haupt@gmx.at");
    	String receiver = "lukas.haupt@gmx.at";
    	
    	//template.sendBodyAndHeaders(toMail, "Hello World", headers);
    	from("test-jms:queue:test.queue").setHeader("subject", constant("This is a test")).setHeader("to", constant(receiver)).to(toMail);
    	
//    	ReceiveMail
        from(fromMail).process(new MyMailProcessor()).to("file://test");
        
//      end Luke's playground
        
    }
}
