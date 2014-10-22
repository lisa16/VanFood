package com.google.gwt.sample.vanfood.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class VanFood implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
//	private static final String SERVER_ERROR = "An error occurred while "
//			+ "attempting to contact the server. Please check your network "
//			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
//	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Button pizzaButton = new Button("Pizza");
		pizzaButton.getElement().setClassName("btn btn-xs btn-primary");
		Button ukranianButton = new Button("Ukranian");
		ukranianButton.getElement().setClassName("btn btn-xs btn-primary");
		Button frenchButton = new Button("French");
		frenchButton.getElement().setClassName("btn btn-xs btn-primary");
		Button seafoodButton = new Button("Seafood");
		seafoodButton.getElement().setClassName("btn btn-xs btn-primary");
		Button chineseButton = new Button("Chinese");
		chineseButton.getElement().setClassName("btn btn-xs btn-primary");
		Button caribbeanButton = new Button("Caribbean");
		caribbeanButton.getElement().setClassName("btn btn-xs btn-primary");
		Button juiceButton = new Button("Juice and Smoothies");
		juiceButton.getElement().setClassName("btn btn-xs btn-primary");
		Button comfortButton = new Button("Comfort Food");
		comfortButton.getElement().setClassName("btn btn-xs btn-primary");
		Button hotdogButton = new Button("Hot Dogs");
		hotdogButton.getElement().setClassName("btn btn-xs btn-primary");
		Button breakfastButton = new Button("Breakfast Foods");
		breakfastButton.getElement().setClassName("btn btn-xs btn-primary");
		Button chestnutsButton = new Button("Chestnuts");
		chestnutsButton.getElement().setClassName("btn btn-xs btn-primary");
		Button indianButton = new Button("Indian");
		indianButton.getElement().setClassName("btn btn-xs btn-primary");
		Button mediterraneanButton = new Button("Mediterranean");
		mediterraneanButton.getElement().setClassName("btn btn-xs btn-primary");
		Button fusionButton = new Button("Fusion");
		fusionButton.getElement().setClassName("btn btn-xs btn-primary");
		Button coffeeButton = new Button("Coffee and Baked Goods");
		coffeeButton.getElement().setClassName("btn btn-xs btn-primary");
		Button israeliButton = new Button("Israeli Kosher");
		israeliButton.getElement().setClassName("btn btn-xs btn-primary");
		Button japaneseButton = new Button("Japanese");
		japaneseButton.getElement().setClassName("btn btn-xs btn-primary");
		Button persianButton = new Button("Persian");
		persianButton.getElement().setClassName("btn btn-xs btn-primary");
		Button sandwichesButton = new Button("Sandwiches");
		sandwichesButton.getElement().setClassName("btn btn-xs btn-primary");
		Button mexicanButton = new Button("Mexican");
		mexicanButton.getElement().setClassName("btn btn-xs btn-primary");
		Button porkButton = new Button("Pulled Pork");
		porkButton.getElement().setClassName("btn btn-xs btn-primary");
		Button bbqButton = new Button("BBQ");
		bbqButton.getElement().setClassName("btn btn-xs btn-primary");
		Button mideasternButton = new Button("Middle Eastern");
		mideasternButton.getElement().setClassName("btn btn-xs btn-primary");
		Button vegButton = new Button("Vegan and Vegetarian");
		vegButton.getElement().setClassName("btn btn-xs btn-primary");
		Button crepesButton = new Button("Crepes");
		crepesButton.getElement().setClassName("btn btn-xs btn-primary");
		Button greekButton = new Button("Greek");
		greekButton.getElement().setClassName("btn btn-xs btn-primary");
		Button vietnameseButton = new Button("Vietnamese");
		vietnameseButton.getElement().setClassName("btn btn-xs btn-primary");
		Button thaiButton = new Button("Thai");
		thaiButton.getElement().setClassName("btn btn-xs btn-primary");
		Button elsalvadorianButton = new Button("El Salvadorian");
		elsalvadorianButton.getElement().setClassName("btn btn-xs btn-primary");
		Button saladButton = new Button("Salads");
		saladButton.getElement().setClassName("btn btn-xs btn-primary");
		Button westButton = new Button("West Coast Fare");
		westButton.getElement().setClassName("btn btn-xs btn-primary");
		
		
		RootPanel.get().add(pizzaButton);
		RootPanel.get().add(ukranianButton);
		RootPanel.get().add(frenchButton);
		RootPanel.get().add(seafoodButton);
		RootPanel.get().add(chineseButton);
		RootPanel.get().add(caribbeanButton);
		RootPanel.get().add(comfortButton);
		RootPanel.get().add(breakfastButton);
		RootPanel.get().add(chestnutsButton);
		RootPanel.get().add(mediterraneanButton);
		RootPanel.get().add(israeliButton);
		RootPanel.get().add(japaneseButton);
		RootPanel.get().add(persianButton);
		RootPanel.get().add(sandwichesButton);
		RootPanel.get().add(mexicanButton);
		RootPanel.get().add(porkButton);
		RootPanel.get().add(bbqButton);
		RootPanel.get().add(mideasternButton);
		RootPanel.get().add(vegButton);
		RootPanel.get().add(crepesButton);
		RootPanel.get().add(greekButton);
		RootPanel.get().add(vietnameseButton);
		RootPanel.get().add(thaiButton);
		RootPanel.get().add(elsalvadorianButton);
		RootPanel.get().add(saladButton);		
		RootPanel.get().add(westButton);
	}
}
