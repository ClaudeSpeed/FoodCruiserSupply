package org.apache.camel.example.jmstofile;

import java.util.ArrayList;
import java.util.List;

public class PurchaseList {

	private List<Meal> meals = new ArrayList<Meal>();
	
	public List<Meal> getMeals() {
		return meals;
	}

	public void setMeals(List<Meal> meals) {
		this.meals = meals;
	}
	
	public void addMeal(Meal meal){
		this.meals.add(meal);
	}
}
