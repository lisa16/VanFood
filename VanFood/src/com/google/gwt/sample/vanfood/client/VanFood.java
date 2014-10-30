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
import com.google.gwt.user.client.ui.ListBox;
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
		addDropDownMenu();

	    // Assemble Main panel.
	    mainPanel.add(signOutLink);
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
	
	/**
	 * Dropdown menu for food descriptions
	 */
	public void addDropDownMenu(){
		ListBox lb = new ListBox();
		for(Vendor v: vendors){
			if(!isInMenu(lb, v.getFoodtype())){
			lb.addItem(v.getFoodtype());
			}
		}
		lb.setVisibleItemCount(1);
		buttonsPanel.add(lb);
	}
	
	public boolean isInMenu(ListBox lb, String foodType){
		for(int i=0; i<lb.getItemCount(); i++){
			if(lb.getItemText(i).equals(foodType)){
				return true;				
			}
		}
		return false;		
	}
	
	private void handleError(Throwable error) {
	    Window.alert(error.getMessage());
	    if (error instanceof NotLoggedInException) {
	      Window.Location.replace(loginInfo.getLogoutUrl());
	    }
	  }
}
