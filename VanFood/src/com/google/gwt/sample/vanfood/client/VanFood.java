package com.google.gwt.sample.vanfood.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class VanFood implements EntryPoint {
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel vendorPanel = new HorizontalPanel();
	private FlexTable vendorsFlexTable = new FlexTable();
	private VerticalPanel mapPanel = new VerticalPanel();
	private FlowPanel buttonsPanel = new FlowPanel();
	private ArrayList<Vendor> vendors = new ArrayList<Vendor>();
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
    "Please sign in to your Google Account to access the VanFood application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	
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
		LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	          handleError(error);
	      }
	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	          loadVanFood();
	        } else {
	          loadLogin();
	        }
	      }
	    });		
	}
	
	private void loadLogin(){
	    signInLink.setHref(loginInfo.getLoginUrl());
	    loginPanel.add(loginLabel);
	    loginPanel.add(signInLink);
	    RootPanel.get("vendorList").add(loginPanel);
	}

	private void loadVanFood() {
		
	    // Set up sign out hyperlink.
	    signOutLink.setHref(loginInfo.getLogoutUrl());

		// Create table for vendor data.
		// Add styles to elements in the stock list table.
	    vendorsFlexTable.getRowFormatter().addStyleName(0, "vendorListHeader");
	    vendorsFlexTable.addStyleName("vendorList");
		vendorsFlexTable.setText(0, 0, "Vendor");  
		vendorsFlexTable.setText(0, 1, "Location");  
		vendorsFlexTable.setText(0, 2, "Add to Favourites");
		
		// hard-code some vendors for now
		Vendor vendor1 = new Vendor("vendor1name", "vendor1addr", "vendor1food");
		Vendor vendor2 = new Vendor("vendor2name", "vendor2addr", "vendor2food");
		vendors.add(vendor1);
		vendors.add(vendor2);
		for (Vendor v : vendors) {
			addVendor(v);
		}
		
		// hard-code a random map for now
		Image map = new Image();
		map.setUrl("http://maps.googleapis.com/maps/api/staticmap?center=Vancouver,+BC&zoom=12&size=900x500&maptype=roadmap");
		mapPanel.add(map);
				
	    // Assemble table and map panel.
		mapPanel.addStyleName("addPanel");
		vendorsFlexTable.addStyleName("addPanel");
		vendorPanel.add(vendorsFlexTable);
		vendorPanel.add(mapPanel);
		
		// Add food type buttons
		addButtons();

	    // Assemble Main panel.
	    mainPanel.add(vendorPanel);
	    mainPanel.add(buttonsPanel);
	    
	    // Associate the Main panel with the HTML host page.
	    RootPanel.get("vendorList").add(mainPanel);
	}
	
	/**
	 * Add vendor to FlexTable. 
	 * Executed at startup.
	 */
	private void addVendor(Vendor vendor) {
		int row = vendorsFlexTable.getRowCount();
	    vendorsFlexTable.setText(row, 0, vendor.getName());
	    vendorsFlexTable.getCellFormatter().addStyleName(row, 0, "vendorColumn");
	    vendorsFlexTable.setText(row, 1, vendor.getAddress());
	    vendorsFlexTable.getCellFormatter().addStyleName(row, 1, "vendorColumn");
	}

	private void addButtons() {
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
		
		
		buttonsPanel.add(pizzaButton);
		buttonsPanel.add(ukranianButton);
		buttonsPanel.add(frenchButton);
		buttonsPanel.add(seafoodButton);
		buttonsPanel.add(chineseButton);
		buttonsPanel.add(caribbeanButton);
		buttonsPanel.add(comfortButton);
		buttonsPanel.add(breakfastButton);
		buttonsPanel.add(chestnutsButton);
		buttonsPanel.add(mediterraneanButton);
		buttonsPanel.add(israeliButton);
		buttonsPanel.add(japaneseButton);
		buttonsPanel.add(persianButton);
		buttonsPanel.add(sandwichesButton);
		buttonsPanel.add(mexicanButton);
		buttonsPanel.add(porkButton);
		buttonsPanel.add(bbqButton);
		buttonsPanel.add(mideasternButton);
		buttonsPanel.add(vegButton);
		buttonsPanel.add(crepesButton);
		buttonsPanel.add(greekButton);
		buttonsPanel.add(vietnameseButton);
		buttonsPanel.add(thaiButton);
		buttonsPanel.add(elsalvadorianButton);
		buttonsPanel.add(saladButton);	
		buttonsPanel.add(westButton);
	}
	
	private void handleError(Throwable error) {
	    Window.alert(error.getMessage());
	    if (error instanceof NotLoggedInException) {
	      Window.Location.replace(loginInfo.getLogoutUrl());
	    }
	  }
}
