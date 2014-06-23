package at.ac.tuwien.workflow.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import at.ac.tuwien.workflow.dao.*;

import com.thoughtworks.xstream.XStream;

public class Helper {
	
	public String getRecipientList()
	{
		return "foodsupplycruiser@gmail.com,brummbear1@gmail.com";
	}

	public Order getPricedOder()
	{
		Meal carbonara = new Meal("Spaghetti a la Carbonara",true);
		carbonara.addIngredient(new Ingredient("Spaghetti",60,"kg",300));
		carbonara.addIngredient(new Ingredient("Ham",30,"kg",1000));
		carbonara.addIngredient(new Ingredient("Cream",10,"l",200));
		carbonara.addIngredient(new Ingredient("Eggs",600,"units",500));
		carbonara.addIngredient(new Ingredient("Parmesan",10,"kg",650));
		
		PurchaseList pl = new PurchaseList(new Date(),"lunch");
		pl.addMeal(carbonara);
		
		Order order = new Order();
		order.setList(pl);
		order.setDateCreated(new Date());
		order.setOrderNr("723");
        return order;
	}
	
	public DataHandler getAttachment() throws IOException
	{
		XStream xstream = new XStream();
		//xstream.alias("org.apache.camel.example.jmstofile.PurchaseList", Order.class);
		String plXML = xstream.toXML(getPricedOder());
		
		File tempFile = File.createTempFile("Order", ".xml");
	    PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
	    writer.println(plXML);
	    writer.close();
		return new DataHandler(new FileDataSource(tempFile));
	}
}
