package at.ac.tuwien.workflow.hazelcast;

import at.ac.tuwien.workflow.dao.Meal;
import at.ac.tuwien.workflow.dao.Ingredient;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;

public class DatabaseBuilder {

	private static Config cfg;
	private static HazelcastInstance hz;
	
    public static void buildDatabase() {
    	
        cfg = new Config();
        
        //create database instance
        hz = Hazelcast.newHazelcastInstance(cfg);
        
        //insert meals and ingredients
        MultiMap<Integer, Meal> mmMeals = hz.getMultiMap("meals");
        MultiMap<Integer, Ingredient> mmIngredients = hz.getMultiMap("ingredients");
        
        mmMeals.put(1, new Meal("Tuna salad", true));
        mmIngredients.put(1, new Ingredient("Head lettuce",120,"units",200));
        mmIngredients.put(1, new Ingredient("Eggs",100,"units",100));
        mmIngredients.put(1, new Ingredient("Tuna fish",15,"kg",1500));
        mmIngredients.put(1, new Ingredient("Tomato paste",5,"kg",300));
        
        mmMeals.put(2, new Meal("Spaghetti a la Carbonara", true));
        mmIngredients.put(2, new Ingredient("Spaghetti",60,"kg",300));
        mmIngredients.put(2, new Ingredient("Ham",30,"kg",1000));
        mmIngredients.put(2, new Ingredient("Cream",10,"l",200));
        mmIngredients.put(2, new Ingredient("Eggs",600,"units",600));
        mmIngredients.put(2, new Ingredient("Parmesan",10,"kg",650));
        
        mmMeals.put(3, new Meal("Tiramisu", false));
        mmIngredients.put(3, new Ingredient("Eggs",200,"units",200));
        mmIngredients.put(3, new Ingredient("Mascarpone",20,"kg",400));
        mmIngredients.put(3, new Ingredient("Ladyfingers",15,"kg",500));
        mmIngredients.put(3, new Ingredient("Coffee liqueur",3,"l",150));
        mmIngredients.put(3, new Ingredient("Kakao powder",1,"kg",50));
        mmIngredients.put(3, new Ingredient("Sugar",5,"kg",30));
        

    }

	public static HazelcastInstance getHz() {
		return hz;
	} 

    public static void shutdownDb() {
    	hz.shutdown();
    }
	
}
