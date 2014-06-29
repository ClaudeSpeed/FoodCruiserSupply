package at.ac.tuwien.workflow.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meal implements Serializable {

	private static final long serialVersionUID = -8875818395446799129L;
	private List<Ingredient> ingredients = new ArrayList<Ingredient>();
	private String Name;
	private boolean PostAsHighlight;

	public Meal(String name, boolean postAsHighlight) {
		super();
		Name = name;
		PostAsHighlight = postAsHighlight;
	}

	public Meal() {
	}
	
	public double getPrice(){
		double price = 0;
		for(Ingredient in : getIngredients()){
			price += in.getPrice();
		}
		return price;
	}

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