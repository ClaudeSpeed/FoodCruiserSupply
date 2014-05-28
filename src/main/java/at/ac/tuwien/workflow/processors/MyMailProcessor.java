package at.ac.tuwien.workflow.processors;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyMailProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("MailProcessor");
		Map<String, Object> headers = exchange.getIn().getHeaders();
		String header = "";
		for (String key : headers.keySet()) {
			Object obj = headers.get(key);
			if(key.equals("from"))
				header += "From: " + obj + "\n";
			if(key.equals("subject"))
				header += "Subject: " + obj + "\n";
		}
		String body = exchange.getIn().getBody(String.class);
		System.out.println(body);
		header = header + body;
//		exchange.getIn().setBody(header);
		System.out.println("EndMailProcessor");
	}

}
