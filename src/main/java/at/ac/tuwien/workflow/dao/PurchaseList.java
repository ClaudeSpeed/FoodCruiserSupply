package at.ac.tuwien.workflow.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseList implements Serializable {

	private static final long serialVersionUID = -1000618730560561234L;
	private List<Meal> meals = new ArrayList<Meal>();
	private Date menueDate;
	private String menueType;
	
	public PurchaseList(Date menueDate, String menueType) {
		super();
		this.menueDate = menueDate;
		this.menueType = menueType;
	}
	
	public String getHighlights() {
		String ret = "";
		
		for (Meal m : meals) {
			if (m.isPostAsHighlight()) {
				if (!ret.equals("")) ret += ", ";
				ret += m.getName();
			}
		}
		
		return ret;
	}

	public List<Meal> getMeals() {
		return meals;
	}

	public void setMeals(List<Meal> meals) {
		this.meals = meals;
	}
	
	public void addMeal(Meal meal){
		this.meals.add(meal);
	}

	public Date getMenueDate() {
		return menueDate;
	}

	public void setMenueDate(Date menueDate) {
		this.menueDate = menueDate;
	}

	public String getMenueType() {
		return menueType;
	}

	public void setMenueType(String menueType) {
		this.menueType = menueType;
	}
	
}
