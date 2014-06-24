package at.ac.tuwien.workflow.processors;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.asm.Handle;

import com.thoughtworks.xstream.XStream;

import at.ac.tuwien.workflow.dao.Order;
import at.ac.tuwien.workflow.helper.Helper;

public class ToMailProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		System.out.println("ToMailProcessor");
		Order order = exchange.getIn().getBody(Order.class);
		String i = order.getOrderNr();
		
		Helper h = new Helper();
		//exchange.getIn().setHeader("to", h.getRecipientList());
		//exchange.getIn().setHeader("to", exchange.getIn().getHeader("from"));
		exchange.getIn().setHeader("to", "foodsupplycruiser@gmail.com");
		exchange.getIn().setHeader("from", "foodsupplycruiser@gmail.com");
		exchange.getIn().setHeader("subject", "OrderNr " + order.getOrderNr());
		
		XStream xstream = new XStream();
		String plXML = xstream.toXML(order);
		
		File tempFile = File.createTempFile("Order", ".xml");
	    PrintWriter writer = null;
	    try {
	        writer = new PrintWriter(new FileWriter(tempFile));
	        writer.println(plXML);
	    } finally {
	        if (writer != null)
	            writer.close();
	    }
	    
	    exchange.getIn().setBody("Please fulfill the order request");
		exchange.getIn().addAttachment("Order", new DataHandler(new FileDataSource(tempFile)));
		
		System.out.println("EndToMailProcessor");
	}

}
