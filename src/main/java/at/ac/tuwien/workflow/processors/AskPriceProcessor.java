package at.ac.tuwien.workflow.processors;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import at.ac.tuwien.workflow.dao.Order;
import at.ac.tuwien.workflow.helper.Helper;

import com.thoughtworks.xstream.XStream;

public class AskPriceProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		System.out.println("AskPriceProcessor");
		Helper helper = new Helper();
		exchange.getIn().setHeader("to", helper.getRecipientList());
		exchange.getIn().setHeader("from", "foodsupplycruiser@gmail.com");
		exchange.getIn().setHeader("subject", "PriceRequest");
		
		Order order = exchange.getIn().getMandatoryBody(Order.class);
		
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
	    
	    exchange.getIn().setBody("Please fill out the order");
		exchange.getIn().addAttachment("Order", new DataHandler(new FileDataSource(tempFile)));
		
		System.out.println("EndAskPriceProcessor");
	}
}
