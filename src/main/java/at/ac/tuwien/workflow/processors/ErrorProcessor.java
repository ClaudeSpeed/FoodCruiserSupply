package at.ac.tuwien.workflow.processors;

import java.util.Map;

import javax.activation.DataHandler;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ErrorProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("ERROR");

		boolean debug = true;
		if (debug) {
			System.out.println(exchange.getIn().getBody().getClass());

			DataHandler dh = exchange.getIn().getAttachment("Order");
			String data = exchange.getContext().getTypeConverter()
					.convertTo(String.class, dh.getInputStream());

			System.out.println("HeaderFrom: "
					+ exchange.getIn().getHeader("From"));
			System.out.println("HeaderTo: " + exchange.getIn().getHeader("To"));
			System.out.println("HeaderSubject: "
					+ exchange.getIn().getHeader("Subject"));
			System.out.println("Attachment: " + data.toString());
		}
	}

}
