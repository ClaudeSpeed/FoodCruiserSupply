package at.ac.tuwien.workflow.processors;

import java.util.Map;

import javax.activation.DataHandler;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import at.ac.tuwien.workflow.dao.Order;
import at.ac.tuwien.workflow.helper.Helper;

import com.thoughtworks.xstream.XStream;

public class MyMailProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("MyMailProcessor");

		// Mock received Attachment
		Helper h = new Helper();
		exchange.getIn().addAttachment("Order", h.getAttachment(exchange.getIn().getHeader("from").toString()));
		// endMock

		//debug
		Map<String, Object> headers = exchange.getIn().getHeaders();
		String header = "";
		for (String key : headers.keySet()) {
			Object obj = headers.get(key);
			if (key.equals("from"))
				header += "From: " + obj + "\n";
			if (key.equals("subject"))
				header += "Subject: " + obj + "\n";
		}
		//System.out.println("Header" + header);
		
		//Handle Attachment
		String nachricht = "";
		Map<String, DataHandler> attachments = exchange.getIn()
				.getAttachments();
		if (attachments.size() > 0) {
			for (String name : attachments.keySet()) {
				DataHandler dh = attachments.get(name);
				String data = exchange.getContext().getTypeConverter()
						.convertTo(String.class, dh.getInputStream());
				nachricht = data.toString();
			}
		}
		
		//Transform XML to Order
		Order order = new Order();
		try {
			XStream xstream = new XStream();
			order = (Order) xstream.fromXML(nachricht);
			exchange.getIn().setBody(order);
		} catch (Exception e) {
			System.out.println("Exception:" + e.getMessage());
		}
		
		//set header with significant data for easier handling
		if(order.getOrderNr() != null){
			exchange.getIn().setHeader("OrderID", order.getOrderNr());
		}
		if(order.getPrice() != 0){
			exchange.getIn().setHeader("TotalPrice", order.getPrice());
		}

		System.out.println("EndMyMailProcessor");
	}

}
