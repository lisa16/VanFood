package com.google.gwt.sample.vanfood.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.sample.vanfood.shared.Vendor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class VanFood implements EntryPoint {

	private LoginInfo loginInfo = null;

	//Panels=====================================================
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel vendorPanel = new HorizontalPanel();
	private ScrollPanel vendorsScrollPanel = new ScrollPanel();
	private ScrollPanel favouritesScrollPanel = new ScrollPanel();
	private TabPanel vendorsTabPanel = new TabPanel();
	private VerticalPanel mapPanel = new VerticalPanel();
	private FlowPanel buttonsPanel = new FlowPanel();
	//============================================================

	//Tables & Lists ===================================================
	private FlexTable vendorsFlexTable = new FlexTable();
	private FlexTable favouritesTable = new FlexTable();
	private ArrayList<Vendor> vendors = new ArrayList<Vendor>();
	private ArrayList<Vendor> favouriteVendors = new ArrayList<Vendor>();
	private ListBox lb = new ListBox();
	//====================================================================

	//Map=================================================================
	private MapWidget map;
	private String apiKey = "AIzaSyC9HJfCdipVC6W6qo8ewsZkJz0mCpmviHQ";
	//=====================================================================

	//Services===============================================================================
	private VendorServiceAsync vendorSvc = GWT.create(VendorService.class);
	private MailServiceAsync mailSvc = GWT.create(MailService.class);
	private final FavouriteServiceAsync favouriteService = GWT.create(FavouriteService.class);
	//========================================================================================

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
				if(loginInfo.isLoggedIn() && loginInfo.isAdmin()) {
					Maps.loadMapsApi(apiKey, "2", false, new Runnable() {
						public void run() {
							loadVanFood();
							loadAdminPage();
						}
					});	
				}
				else if(loginInfo.isLoggedIn()) {
					Maps.loadMapsApi(apiKey, "2", false, new Runnable() {
						public void run() {
							loadVanFood();
						}
					});


				} else {
					loadLogin();
				}
			}
		});		
	}

	private void loadLogin(){
		Anchor signInLink = new Anchor("Sign In");
		signInLink.setHref(loginInfo.getLoginUrl());
		Label loginLabel = new Label(
				"Please sign in to your Google Account to access the VanFood application.");
		VerticalPanel loginPanel = new VerticalPanel();
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("vendorList").add(loginPanel);
	}

	//handles not logged in error
	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}

	private void loadContactPage(){
		DOM.getElementById("vendorList").getStyle().setDisplay(Display.NONE);
		DOM.getElementById("adminPage").getStyle().setDisplay(Display.NONE);
		Label subjectLabel = new Label("Subject: ");
		final TextBox subject= new TextBox();
		Label msgLabel = new Label("Enter your feedback: ");
		final TextArea msg = new TextArea();
		msg.setPixelSize(300, 200);
		Button submitButton = new Button("Submit");
		submitButton.getElement().setClassName("btn btn-default btn-primary");
		submitButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				final String msgFrom = loginInfo.getEmailAddress();
				final String message = msg.getText();
				final String msgSubject = subject.getText();
				if(msgSubject == ""){
					Window.alert("There's no subject, please fill it out and press the submit button again!");
					return;
				}
				if(message == ""){
					Window.alert("There's no message to send, please fill it out and press the submit button again!");
					return;
				}
				mailSvc.sendMail(msgFrom, msgSubject,"miss.lisa7102@gmail.com", message, new AsyncCallback<String>()
						{
					public void onFailure(Throwable caught) {
						Window.alert("The message was not sent:( Try again!");
					}

					@Override
					public void onSuccess(String result) {
						Window.alert("Thank you for the feedback, we will get back to you soon!");
					}

						});
			}});

		VerticalPanel contactPanel = new VerticalPanel();
		contactPanel.add(subjectLabel);
		contactPanel.add(subject);
		contactPanel.add(msgLabel);
		contactPanel.add(msg);
		contactPanel.add(submitButton);
		RootPanel.get("contactPage").add(contactPanel);

	}

	private void loadAdminPage(){
		Button updateButton = new Button("Parse Updated Data");
		updateButton.getElement().setClassName("btn btn-default btn-primary");
		VerticalPanel adminPanel = new VerticalPanel();
		adminPanel.add(updateButton);
		updateButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {

				vendorSvc.parseVendors(new AsyncCallback<String>()
						{
					public void onFailure(Throwable caught) {
						Window.alert("Data NOT parsed/stored!");
					}

					@Override
					public void onSuccess(String result) {
						Window.alert(result);
					}

						});
			}});

		RootPanel.get("adminPage").add(adminPanel);
	}

	private void loadVanFood() {

		// Set up sign out hyperlink.
		Anchor signOutLink = new Anchor("Sign Out");
		signOutLink.setHref(loginInfo.getLogoutUrl());

		// Create table for vendor data.
		createVendorsTable();

		// Create table for favourites
		createFavouritesTable();

		// Call to service proxy
		loadVendorList();
		loadFavouritesList();

		// Open a map centered on, Vancouver BC
		LatLng van = LatLng.newInstance(49.2500, -123.1000);

		map = new MapWidget(van, 12);
		map.setSize("35em", "35em");
		mapPanel.add(map);

		//create tab area
		vendorsTabPanel.add(vendorsScrollPanel, "Vendors");
		vendorsTabPanel.add(favouritesScrollPanel, "Favourites");
		vendorsTabPanel.selectTab(1);

		// Add margins for map and table.
		mapPanel.addStyleName("addPanel");
		vendorsTabPanel.addStyleName("addPanel");

		//add vendors list and map to main panel to display
		vendorPanel.add(vendorsTabPanel);
		vendorPanel.add(mapPanel);

		//add contact us button
		addContactButton();

		// Assemble Main panel.
		mainPanel.add(signOutLink);
		mainPanel.add(buttonsPanel);
		mainPanel.add(vendorPanel);

		// Associate the Main panel with the HTML host page.
		RootPanel.get("vendorList").add(mainPanel);
	}

	private void addContactButton(){
		Button contactButton = new Button("Contact Us");
		contactButton.getElement().setClassName("btn btn-default btn-primary");
		buttonsPanel.add(contactButton);
		contactButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				loadContactPage();

			}

		});

	}

	private void createVendorsTable(){
		vendorsFlexTable.getRowFormatter().addStyleName(0, "vendorListHeader");
		vendorsFlexTable.addStyleName("vendorList");
		vendorsFlexTable.setText(0, 0, "Vendor");  
		vendorsFlexTable.setText(0, 1, "Location");  
		vendorsFlexTable.setText(0, 2, "Food Type"); 
		vendorsFlexTable.setText(0, 3, "Add to Favourite");

		//create scroll for vendors 
		vendorsScrollPanel.add(vendorsFlexTable);
		vendorsScrollPanel.setSize("45em", "35em"); 

		//highlights row clicked
		vendorsFlexTable.addClickHandler(new ClickHandler () {
			public void onClick(ClickEvent event) {
				selectVendor(event, vendorsFlexTable, vendors);
			}
		});

	}

	private void createFavouritesTable(){
		favouritesTable.getRowFormatter().addStyleName(0, "vendorListHeader");
		favouritesTable.addStyleName("vendorList");
		favouritesTable.setText(0, 0, "Vendor");  
		favouritesTable.setText(0, 1, "Location");  
		favouritesTable.setText(0, 2, "Food Type"); 
		favouritesTable.setText(0, 3, "Remove Favourite");

		//create scroll for favourites
		favouritesScrollPanel.add(favouritesTable);
		favouritesScrollPanel.setSize("45em", "35em");  

		//highlights row clicked
		favouritesTable.addClickHandler(new ClickHandler () {
			public void onClick(ClickEvent event) {
				selectVendor(event, favouritesTable, favouriteVendors);
			}
		});

	}

	/**
	 * Dropdown menu for food descriptions
	 */
	private void addDropDownMenu(Vendor[] result){
		lb.addChangeHandler(new MenuHandler());
		lb.addItem("Food Descriptions");
		for(Vendor v: result){
			if(!isInMenu(lb, v.getFoodtype())){
				lb.addItem(v.getFoodtype());
			}
		}
		lb.setVisibleItemCount(1);
		lb.addStyleName("dropdownMenu");
		buttonsPanel.add(lb);
	}

	private boolean isInMenu(ListBox lb, String foodType){
		for(int i=0; i<lb.getItemCount(); i++){
			if(lb.getItemText(i).equals(foodType)){
				return true;				
			}
		}
		return false;		
	}


	// handle clicking on a table row (so a vendor can be selected)
	public void selectVendor (ClickEvent event, FlexTable table, ArrayList<Vendor> vendors) {
		Cell src = null;
		try {
			src = table.getCellForEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int rowIndex=0;
		if (src!=null)
			rowIndex = src.getRowIndex();
		if ((rowIndex==0)||(src.getCellIndex()==3))
			return;
		map.clearOverlays();
		String rowStyle = table.getRowFormatter().getStyleName(rowIndex).trim();
		String vendorName = table.getText(rowIndex, 0);
		String vendorAddress = table.getText(rowIndex, 1);
		if (rowStyle.equals("FlexTable-noHighlight") ||
				rowStyle.equals("")) {
			setRowHighlighted(table, rowIndex, true);
			for (Vendor v : vendors) {
				if (vendorName.equalsIgnoreCase(v.getName()) &&
						vendorAddress.equalsIgnoreCase(v.getAddress())) 
					v.setHighlighted(true);
			}

		} else {
			setRowHighlighted(table, rowIndex, false);
			for (Vendor v : vendors) {
				if (vendorName.equalsIgnoreCase(v.getName()) &&
						vendorAddress.equalsIgnoreCase(v.getAddress()))
					v.setHighlighted(false);
			}
		}
		for(Vendor v : vendors){
			if(v.isHighlighted()){
				map.addOverlay(createMarker(v));
			}
		}
	}	

	private void setRowHighlighted(FlexTable table, int rowIndex, boolean isHighlight){
		if (isHighlight){
			table.getRowFormatter().addStyleName(rowIndex, "FlexTable-Highlight");
			table.getRowFormatter().removeStyleName(rowIndex, "FlexTable-noHighlight");			
		}
		else {
			table.getRowFormatter().addStyleName(rowIndex, "FlexTable-noHighlight");
			table.getRowFormatter().removeStyleName(rowIndex, "FlexTable-Highlight");			
		}
		
	}

	// handles drop down menu 
	class MenuHandler implements ChangeHandler{

		@Override
		public void onChange(ChangeEvent event) {
			map.clearOverlays();
			HTMLTable.RowFormatter rf = vendorsFlexTable.getRowFormatter();
			// Make sure everything on the table is not highlighted 
			for(int r=1; r<vendorsFlexTable.getRowCount();r++){
				rf.setStyleName(r, "FlexTable-noHighlight");
			}
			for (Vendor v : vendors)
				v.setHighlighted(false);
			highlightSelectedFoodType(rf);
			for(Vendor v : vendors){
				if(v.isHighlighted()){
					map.addOverlay(createMarker(v));
				}}	
		}}

	// Highlight vendors that serve the selected food type
	private void highlightSelectedFoodType(HTMLTable.RowFormatter rf){
		int index = lb.getSelectedIndex();
		String foodType = lb.getItemText(index);
		for(int r=0; r<vendors.size(); r++){
			if(vendors.get(r).getFoodtype().equals(foodType)){
				int i = (r+1);
				rf.setStyleName(i, "FlexTable-Highlight");
				vendors.get(r).setHighlighted(true);
				map.addOverlay(createMarker(vendors.get(r)));
			}				
		}
	}

	// last updated time stamp
	private void addTimeStamp(){
		Label lastUpdatedLabel = new Label();
		lastUpdatedLabel.setText("Last update : "  
				+ DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM).format(new Date()));

		mainPanel.add(lastUpdatedLabel);
	}


	//------------------------- Vendors Table Starts ---------------
	//service proxy 
	private void loadVendorList() {
		// Initialize the service proxy.
		if (vendorSvc == null) {
			vendorSvc = GWT.create(VendorService.class);
		}

		vendorSvc.getVendors(new AsyncCallback<Vendor[]>() {
			public void onFailure (Throwable error) { 
				handleError(error);
			}
			@Override
			public void onSuccess(Vendor[] result) {
				displayVendors(result);
				addDropDownMenu(result);
				addTimeStamp();
			}
		});
	}

	//remove all data and display vendor table with new data
	private void displayVendors(Vendor[] result) {
		for (Vendor v : result) {
			//display vendor in table
			displayVendors(v);
		}
	}

	// helper function for displayVendors(Vendor[] result) 
	private void displayVendors(final Vendor vendor) {
		// don't add vendor if it already exists 
		if (vendors.contains(vendor)){
			return;
		}

		int row = vendorsFlexTable.getRowCount();
		vendors.add(vendor);

		vendorsFlexTable.getRowFormatter().addStyleName(row, "FlexTable-noHighlight");
		vendorsFlexTable.setText(row, 0, vendor.getName());
		vendorsFlexTable.getColumnFormatter().addStyleName(0, "vendorColumn");
		vendorsFlexTable.setText(row, 1, vendor.getAddress());
		vendorsFlexTable.getColumnFormatter().addStyleName(1, "vendorColumn"); 
		vendorsFlexTable.setText(row, 2, vendor.getFoodtype());
		vendorsFlexTable.getColumnFormatter().addStyleName(2, "vendorColumn");

		// Add a button to add this vendor to favourites
		Button favouriteButton = new Button("Favourite");
		vendorsFlexTable.setWidget(row, 3, favouriteButton);
		favouriteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				new TwitterPopup(vendor).show();
				addFavourites(vendor);
			}
		});
	}
	//------------------------- Vendors Table Ends ---------------

	//------------------------- Favourites Table Starts ---------------

	//load user's favourites
	private void loadFavouritesList() {
		favouriteService.getFavourite(new AsyncCallback<Vendor[]>() {
			public void onFailure (Throwable error) { 
				handleError(error);
			}
			@Override
			public void onSuccess(Vendor[] result) {
				displayFavourites(result);
			}

		});
	}

	//display favourites table
	private void displayFavourites(Vendor[] result) {
		for (Vendor vendor: result) {
			displayFavourites(vendor);
		}
	}

	// adds vendor to favourites (from favourite click)
	private void addFavourites(final Vendor vendor){
		//don't add vendor if it's already in the list of favourites
		if (favouriteVendors.contains(vendor))
			return;

		favouriteService.addFavourite(vendor, new AsyncCallback<Void>(){
			public void onFailure(Throwable error) {
				handleError(error);	
			}
			public void onSuccess(Void ignore) {
				displayFavourites(vendor);
			}
		});
	}

	private void displayFavourites (final Vendor vendor) {
		int row = favouritesTable.getRowCount();
		favouriteVendors.add(vendor);

		favouritesTable.getRowFormatter().addStyleName(row, "FlexTable-noHighlight");
		favouritesTable.setText(row, 0, vendor.getName());
		favouritesTable.getColumnFormatter().addStyleName(0, "vendorColumn");
		favouritesTable.setText(row, 1, vendor.getAddress());
		favouritesTable.getColumnFormatter().addStyleName(1, "vendorColumn"); 
		favouritesTable.setText(row, 2, vendor.getFoodtype());
		favouritesTable.getColumnFormatter().addStyleName(2, "vendorColumn");

		Button removeButton = new Button("Remove!!");
		favouritesTable.setWidget(row, 3, removeButton);
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeFavourite(vendor);
			}
		});
	}


	private void removeFavourite(final Vendor vendor) {
		favouriteService.removeFavourite(vendor, new AsyncCallback<Void>(){
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				undisplayFavourite(vendor);
			}
		});
	}

	private void undisplayFavourite(Vendor vendor) {
		int removedIndex = favouriteVendors.indexOf(vendor);
		favouriteVendors.remove(removedIndex);
		favouritesTable.removeRow(removedIndex +1);
	}


	//------------------------- Favourites Table Ends ---------------


	// handle removing favourites
	ClickHandler RemoveHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			Cell src = null;
			try {
				src = favouritesTable.getCellForEvent(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
			int rowIndex=0;
			if (src!=null)
				rowIndex = src.getRowIndex();
			if (rowIndex==0)
				return;
		}

	};

	private Marker createMarker(Vendor v) {
		double lat =v.getLat();
		double lon = v.getLon();
		final String name = v.getName();
		final String address = v.getAddress();
		LatLng point = LatLng.newInstance(lat, lon);
		final Marker marker = new Marker(point);

		marker.addMarkerClickHandler(new MarkerClickHandler() {
			public void onClick(MarkerClickEvent event) {
				InfoWindow info = map.getInfoWindow();
				info.open(marker,
						new InfoWindowContent("<b>" + name + "<br>" + address + "</b>"));
			}
		});
		System.out.println(v.toString());
		return marker;
	}

}




