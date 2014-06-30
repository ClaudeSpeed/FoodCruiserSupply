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

	public String getRecipientList() {
		return "reailersuperduper@gmail.com,brummbear1@gmail.com";
	}

	public Order getPricedOder(String from) {
		//Purchaselist for 400 persons
		PurchaseList purchList = new PurchaseList(new Date(), "lunch");
        
        Meal tunasalad = new Meal("Tuna salad", true);
        tunasalad.addIngredient(new Ingredient("Spaghetti", 60, "kg", 300));
        tunasalad.addIngredient(new Ingredient("Eggs",100,"units",100));
        tunasalad.addIngredient(new Ingredient("Tuna fish",15,"kg",1500));
        tunasalad.addIngredient(new Ingredient("Tomato paste",5,"kg",300));
        purchList.addMeal(tunasalad);
        
		Meal carbonara = new Meal("Spaghetti a la Carbonara", true);
		carbonara.addIngredient(new Ingredient("Spaghetti", 60, "kg", 300));
		carbonara.addIngredient(new Ingredient("Ham", 30, "kg", 1000));
		carbonara.addIngredient(new Ingredient("Cream", 10, "l", 200));
		carbonara.addIngredient(new Ingredient("Eggs", 600, "units", 600));
		carbonara.addIngredient(new Ingredient("Parmesan", 10, "kg", 650));
		purchList.addMeal(carbonara);

		Meal tiramisu = new Meal("Tiramisu", false);
		if(from.contains("Lukas Haupt"))
			tiramisu.addIngredient(new Ingredient("Eggs", 200, "units", 300));
		else
			tiramisu.addIngredient(new Ingredient("Eggs", 200, "units", 200));
		tiramisu.addIngredient(new Ingredient("Sugar", 5, "kg", 30));
		tiramisu.addIngredient(new Ingredient("Mascarpone", 20, "kg", 400));
		tiramisu.addIngredient(new Ingredient("Ladyfingers", 15, "kg", 500));
		tiramisu.addIngredient(new Ingredient("Coffee liqueur", 3, "l", 150));
		tiramisu.addIngredient(new Ingredient("Kakao powder", 1, "kg", 50));
		purchList.addMeal(tiramisu);

		Order order = new Order();
		order.setList(purchList);
		order.setDateCreated(new Date());
		order.setOrderNr("723");
		order.setCurrency("FJD");
		return order;
	}

	public DataHandler getAttachment(String from) throws IOException {
		XStream xstream = new XStream();
		// xstream.alias("org.apache.camel.example.jmstofile.PurchaseList",
		// Order.class);
		String plXML = xstream.toXML(getPricedOder(from));

		File tempFile = File.createTempFile("Order", ".xml");
		PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
		writer.println(plXML);
		writer.close();
		return new DataHandler(new FileDataSource(tempFile));
	}

	public String getRetailer(String eMail) {
		return "Fiji Food Delivery Inc.";
	}
}
