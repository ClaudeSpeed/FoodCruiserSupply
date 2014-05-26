package org.apache.camel.example.jmstofile;

import java.util.ArrayList;
import java.util.List;

public class Meal {

	private List<Ingredient> ingredients = new ArrayList<Ingredient>();
	private String Name = "";
	private boolean PostAsHighlight = false;

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	public void addIngredient(Ingredient ingredient){
		this.ingredients.add(ingredient);
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public boolean isPostAsHighlight() {
		return PostAsHighlight;
	}

	public void setPostAsHighlight(boolean postAsHighlight) {
		PostAsHighlight = postAsHighlight;
	}

}