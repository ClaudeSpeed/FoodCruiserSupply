package org.apache.camel.example.jmstofile;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyMailProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("MailProcessor");
		System.out.println(exchange.getIn().getBody().toString());
		System.out.println("EndMailProcessor");
	}

}
