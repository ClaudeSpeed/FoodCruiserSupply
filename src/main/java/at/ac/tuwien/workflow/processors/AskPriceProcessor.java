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
		
		//Get order from JMS
		Order order = exchange.getIn().getMandatoryBody(Order.class);
		
		//Translate order to xml
		XStream xstream = new XStream();
		String plXML = xstream.toXML(order);
		//Create xml-File
		File tempFile = File.createTempFile("Order", ".xml");
	    PrintWriter writer = null;
	    try {
	        writer = new PrintWriter(new FileWriter(tempFile));
	        writer.println(plXML);
	    } finally {
	        if (writer != null)
	            writer.close();
	    }
	    
	    //Genereate Msg
	    StringBuilder sb = new StringBuilder();
	    sb.append("<h2>Dear Retailer!</h2><br />");
	    sb.append("We are approaching your haven and want to order some supplies.<br />");
	    sb.append("Please fill out the attached xml-File and send it back to us within 10 sec.<br /><br />");
	    sb.append("We will decide on the total price which retailer will get the order.<br />");
	    sb.append("Sincerly, The FoodSupplyCruiser.");
	   
	    //set Msg
	    exchange.getIn().setBody(sb.toString());
	    //set order as Attachment
		exchange.getIn().addAttachment("Order", new DataHandler(new FileDataSource(tempFile)));
		
		System.out.println("EndAskPriceProcessor");
	}
}
