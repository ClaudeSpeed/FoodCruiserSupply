package org.apache.camel.example.jmstofile;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyMailProcessor implements Processor {

	public void process(Exchange exchange) throws Exception {
		System.out.println("MailProcessor");
		System.out.println("Exchange");
		System.out.println(exchange);
		Map<String, Object> headers = exchange.getIn().getHeaders();
		System.out.println("Headers");
		for (Object header : headers.values()) {
			System.out.println(header.toString());
		}
		System.out.println("Body");
		System.out.println(exchange.getIn().getBody().toString());
		System.out.println("EndMailProcessor");
	}

}
