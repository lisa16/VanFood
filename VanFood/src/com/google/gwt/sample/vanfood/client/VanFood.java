package com.google.gwt.sample.vanfood.client;

import java.util.ArrayList;

import com.google.gwt.sample.vanfood.server.Favourite;
import com.google.gwt.sample.vanfood.shared.Vendor;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;  
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class VanFood implements EntryPoint {

	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel vendorPanel = new HorizontalPanel();
	private FlexTable vendorsFlexTable = new FlexTable();
	private FlexTable favouritesTable = new FlexTable();
	private ScrollPanel vendorsScrollPanel = new ScrollPanel();
	private ScrollPanel favouritesScrollPanel = new ScrollPanel();
	private TabPanel vendorsTabPanel = new TabPanel();
	private VerticalPanel mapPanel = new VerticalPanel();
	private FlowPanel buttonsPanel = new FlowPanel();
	private ArrayList<Vendor> vendors = new ArrayList<Vendor>();
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access the VanFood application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	public ListBox lb = new ListBox();
	private VerticalPanel adminPanel = new VerticalPanel();
	private VerticalPanel contactPanel = new VerticalPanel();
	private Label lastUpdatedLabel = new Label();
	private VendorServiceAsync VendorSvc = GWT.create(VendorService.class);
	private MailServiceAsync mailSvc = GWT.create(MailService.class);
	private ArrayList<Vendor> favouriteVendors = new ArrayList<Vendor>();

	private final FavouriteServiceAsync favouriteService = GWT.create(FavouriteService.class);

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
		Button adminButton = new Button("Sign in as Administrator");
		adminButton.getElement().setClassName("btn btn-default btn-primary");
		adminButton.addClickHandler(new AdminButtonHandler());
		loginPanel.add(adminButton);
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
		Label emailLabel = new Label("Your e-mail address: ");
		final TextBox from = new TextBox();
		from.setText(loginInfo.getEmailAddress());
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
				final String msgFrom = from.getText();
				final String message = msg.getText();
				final String msgSubject = subject.getText();
				System.out.println("Submit button clicked");
				mailSvc.sendMail(msgFrom, msgSubject,"miss.lisa7102@gmail.com", message, new AsyncCallback<String>()
						{
					public void onFailure(Throwable caught) {
						System.out.println("Didn't work");
						Window.alert("Didn't work");
					}

					@Override
					public void onSuccess(String result) {
						System.out.println("Message sent");
						Window.alert("Message sent");
					}

						});
			}});




		contactPanel.add(emailLabel);
		contactPanel.add(from);
		contactPanel.add(subjectLabel);
		contactPanel.add(subject);
		contactPanel.add(msgLabel);
		contactPanel.add(msg);
		contactPanel.add(submitButton);
		RootPanel.get("contactPage").add(contactPanel);

	}

	private void loadAdminPage(){
		DOM.getElementById("vendorList").getStyle().setDisplay(Display.NONE);
		Button updateButton = new Button("Parse Updated Data");
		updateButton.getElement().setClassName("btn btn-default btn-primary");


		adminPanel.add(updateButton);
		updateButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {

				VendorSvc.parseVendors(new AsyncCallback<String>()
						{
					public void onFailure(Throwable caught) {
						System.out.println("Data NOT parsed/stored!");
						Window.alert("Data NOT parsed/stored!");
					}

					@Override
					public void onSuccess(String result) {
						System.out.println("Data parsed & Stored!");
						Window.alert("Data parsed & Stored!");
					}

						});
			}});




		RootPanel.get("adminPage").add(adminPanel);
	}

	private void loadVanFood() {

		// Set up sign out hyperlink.
		signOutLink.setHref(loginInfo.getLogoutUrl());

		//highlights row clicked
		vendorsFlexTable.addClickHandler(userRowCheck);

		// Create table for vendor data.
		// Add styles to elements in the stock list table.
		vendorsFlexTable.getRowFormatter().addStyleName(0, "vendorListHeader");
		vendorsFlexTable.addStyleName("vendorList");
		vendorsFlexTable.setText(0, 0, "Vendor");  
		vendorsFlexTable.setText(0, 1, "Location");  
		vendorsFlexTable.setText(0, 2, "Food Type"); 
		vendorsFlexTable.setText(0, 3, "Add to Favourite");

		// create table for favourites
		favouritesTable.getRowFormatter().addStyleName(0, "vendorListHeader");
		favouritesTable.addStyleName("vendorList");
		favouritesTable.setText(0, 0, "Vendor");  
		favouritesTable.setText(0, 1, "Location");  
		favouritesTable.setText(0, 2, "Food Type"); 
		favouritesTable.setText(0, 3, "Remove Favourite");

		//call to service proxy
		loadVendorList();
		loadFavouritesList();

		// hard-code a random map for now
		Image map = new Image();
		map.setUrl("http://maps.googleapis.com/maps/api/staticmap?center=Vancouver,+BC&zoom=12&size=900x500&maptype=roadmap");
		mapPanel.add(map);

		//create scroll for vendors 
		vendorsScrollPanel.add(vendorsFlexTable);
		vendorsScrollPanel.setSize("45em", "35em");  

		//create scroll for favourites
		favouritesScrollPanel.add(favouritesTable);
		favouritesScrollPanel.setSize("45em", "35em");  

		//create tab area
		vendorsTabPanel.add(vendorsScrollPanel, "Vendors");
		vendorsTabPanel.add(favouritesScrollPanel, "Favourites");
		vendorsTabPanel.selectTab(0);

		// Add margins for map and table.
		mapPanel.addStyleName("addPanel");
		vendorsTabPanel.addStyleName("addPanel");

		//add vendors list and map to main panel to display
		vendorPanel.add(vendorsTabPanel);
		vendorPanel.add(mapPanel);

		//add contact us button
		Button contactButton = new Button("Contact Us");
		contactButton.getElement().setClassName("btn btn-default btn-primary");
		buttonsPanel.add(contactButton);
		contactButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				loadContactPage();

			}

		});
		// Assemble Main panel.
		mainPanel.add(signOutLink);
		mainPanel.add(buttonsPanel);
		mainPanel.add(vendorPanel);

		// Associate the Main panel with the HTML host page.
		RootPanel.get("vendorList").add(mainPanel);
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
	ClickHandler userRowCheck = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			Cell src = null;
			try {
				src = vendorsFlexTable.getCellForEvent(event);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int rowIndex=0;
			if (src!=null)
				rowIndex = src.getRowIndex();
			if (rowIndex==0)
				return;
			if (vendorsFlexTable.getRowFormatter().getStyleName(rowIndex).trim().equals("FlexTable-noHighlight") ||
					vendorsFlexTable.getRowFormatter().getStyleName(rowIndex).trim().equals("")) {
				vendorsFlexTable.getRowFormatter().addStyleName(rowIndex, "FlexTable-Highlight");
				vendorsFlexTable.getRowFormatter().removeStyleName(rowIndex, "FlexTable-noHighlight");
				for (Vendor v : vendors) {
					if (vendorsFlexTable.getText(rowIndex, 0).equalsIgnoreCase(v.getName()) &&
							vendorsFlexTable.getText(rowIndex, 1).equalsIgnoreCase(v.getAddress())) 
						v.setHighlighted(true);
				}
			} else {
				vendorsFlexTable.getRowFormatter().addStyleName(rowIndex, "FlexTable-noHighlight");
				vendorsFlexTable.getRowFormatter().removeStyleName(rowIndex, "FlexTable-Highlight");
				for (Vendor v : vendors) {
					if (vendorsFlexTable.getText(rowIndex, 0).equalsIgnoreCase(v.getName()) &&
							vendorsFlexTable.getText(rowIndex, 1).equalsIgnoreCase(v.getAddress()))
						v.setHighlighted(false);
				}
			}
		}	
	};

	// handles drop down menu 
	class MenuHandler implements ChangeHandler{

		@Override
		public void onChange(ChangeEvent event) {
			HTMLTable.RowFormatter rf = vendorsFlexTable.getRowFormatter();
			HTMLTable.RowFormatter rfFav = favouritesTable.getRowFormatter();
			for(int r=1; r<vendorsFlexTable.getRowCount();r++){
				rf.setStyleName(r, "FlexTable-noHighlight");
				rfFav.setStyleName(r, "FlexTable-noHighlight");
			}
			for (Vendor v : vendors)
				v.setHighlighted(false);
			int index = lb.getSelectedIndex();
			String foodType = lb.getItemText(index);
			for(int r=0; r<vendors.size(); r++){
				if(vendors.get(r).getFoodtype().equals(foodType)){
					int i = (r+1);
					rf.setStyleName(i, "FlexTable-Highlight");
					rfFav.setStyleName(i, "FlexTable-Highlight");
					vendors.get(r).setHighlighted(true);
				}				
			}			
		}		
	}

	class AdminButtonHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			loadAdminPage();

		}

	}

	// last updated time stamp
	private void addTimeStamp(){
		lastUpdatedLabel.setText("Last update : "  
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));

		mainPanel.add(lastUpdatedLabel);
	}


	//service proxy 
	private void loadVendorList() {
		// Initialize the service proxy.
		if (VendorSvc == null) {
			VendorSvc = GWT.create(VendorService.class);
		}

		// Set up the callback object.
		AsyncCallback<Vendor[]> callback = new AsyncCallback<Vendor[]>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
			}

			@Override
			public void onSuccess(Vendor[] result) {
				displayVendors(result);
				addDropDownMenu(result);
				addTimeStamp();
			}
		};
		// Make the call to the vendor service.
		VendorSvc.getVendors(callback);
	}

	private void loadFavouritesList() {
		favouriteService.getFavourite(new AsyncCallback<Vendor[]>() {
			public void onFailure (Throwable error) { }
			@Override
			public void onSuccess(Vendor[] result) {
				displayFavourites(result);

			}

		});
	}

	//remove all data and display vendor table with new data
	private void displayVendors(Vendor[] result) {
		for (int i=1; vendorsFlexTable.getRowCount() < i; i++ ) {
			vendorsFlexTable.removeRow(i);
		}

		vendors.clear();

		for (Vendor v : result) {
			//add vendor to array list of vendors
			vendors.add(v);
			//display vendor in table
			addVendor(v);
		}
	}

	//display favourites table
	private void displayFavourites(Vendor[] result) {
		for (Vendor vendor: result) {
			displayFavourites(vendor);
		}
	}

	// helper function for displayVendors
	private void addVendor(final Vendor vendor) {
		int row = vendorsFlexTable.getRowCount();

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
				new TwitterPopup(null).show();
				addFavourites(vendor);
			}
		});
	}

	private void addFavourites(Vendor vendor){
		//don't add vendor if it's already in the list of favourites
		if (favouriteVendors.contains(vendor))
			return;
		addFavourite(vendor);
	}

	private void addFavourite(final Vendor vendor){
		//	favouriteService.addFavourite(vendor, new AsyncCallback<Void>(){
		//		public void onFailure(Throwable error) {
		//		}
		//		public void onSuccess(Void ignore) {
		displayFavourites(vendor);
		//		}
		//	});
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
				int removedIndex = favouriteVendors.indexOf(vendor);
				favouriteVendors.remove(removedIndex);
				favouritesTable.removeRow(removedIndex);
			}
		});
	}
}



