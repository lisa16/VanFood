package com.google.gwt.sample.vanfood.client;

import java.util.ArrayList;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
	public ListBox lb = new ListBox();
	private VerticalPanel adminPanel = new VerticalPanel();

	private VendorServiceAsync ListSvc = GWT.create(VendorService.class);
	
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
	
	private void loadAdminPage(){
		//ftp://webftp.vancouver.ca/OpenData/xls/new_food_vendor_locations.xls
		DOM.getElementById("vendorList").getStyle().setDisplay(Display.NONE);
		// Create a Form Panel
		final FormPanel form = new FormPanel();
		final TextBox text = new TextBox();

		Label selectLabel = new Label("Enter URL of file:");
		Button uploadButton = new Button("Upload File");
		uploadButton.getElement().setClassName("btn btn-default btn-primary");
		 //pass action to the form to point to service handling file 
	      //receiving operation.
	      form.setAction(text.getValue());
	   // set form to use the POST method, and multipart MIME encoding.
	      form.setEncoding(FormPanel.ENCODING_MULTIPART);
	      form.setMethod(FormPanel.METHOD_POST);
	      
	      adminPanel.add(selectLabel);
	      adminPanel.add(text);
	      adminPanel.add(uploadButton);
	      uploadButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				String filename = text.getValue();
				if(filename.length() == 0){
					Window.alert("No file Specified!");
				}
				else{
					form.submit();
				}				
			}
	    	  
	      });
	      form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	          @Override
	          public void onSubmitComplete(SubmitCompleteEvent event) {
	             // When the form submission is successfully completed, this 
	             //event is fired. Assuming the service returned a response 
	             //of type text/html, we can get the result text here 
	             Window.alert(event.getResults());				
	          }
	       });
	       adminPanel.setSpacing(10);
	 	  
	       // Add form to the root panel.      
	       form.add(adminPanel);      
	       RootPanel.get("adminPage").add(form);	      	      
	}

	private void loadVanFood() {

		// Set up sign out hyperlink.
		signOutLink.setHref(loginInfo.getLogoutUrl());

		// Create table for vendor data.
		// Add styles to elements in the stock list table.

	    vendorsFlexTable.addClickHandler(userRowCheck);

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

		// Add drop down menu
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

		vendorsFlexTable.getRowFormatter().addStyleName(row, "FlexTable-noHighlight");
	    vendorsFlexTable.setText(row, 0, vendor.getName());
	    vendorsFlexTable.getColumnFormatter().addStyleName(0, "vendorColumn");
	    vendorsFlexTable.setText(row, 1, vendor.getAddress());
	    vendorsFlexTable.getColumnFormatter().addStyleName(1, "vendorColumn");   

	}

	/**
	 * Dropdown menu for food descriptions
	 */
	private void addDropDownMenu(){
		lb.addChangeHandler(new MenuHandler());
		lb.addItem("Food Descriptions");
		for(Vendor v: vendors){
			if(!isInMenu(lb, v.getFoodtype())){
				lb.addItem(v.getFoodtype());
			}
		}
		lb.setVisibleItemCount(1);
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
			int rowIndex=-1;
			if (src!=null)
				rowIndex = src.getRowIndex();
            if (rowIndex==0)
            	return;
            if (vendorsFlexTable.getRowFormatter().getStyleName(rowIndex).trim().equals("FlexTable-noHighlight") ||
            		vendorsFlexTable.getRowFormatter().getStyleName(rowIndex).trim().equals("")) {
            	vendorsFlexTable.getRowFormatter().addStyleName(rowIndex, "FlexTable-Highlight");
            	vendorsFlexTable.getRowFormatter().removeStyleName(rowIndex, "FlexTable-noHighlight");
            } else {
            	vendorsFlexTable.getRowFormatter().addStyleName(rowIndex, "FlexTable-noHighlight");
            	vendorsFlexTable.getRowFormatter().removeStyleName(rowIndex, "FlexTable-Highlight");
            }
        }	
    };

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
	

	class MenuHandler implements ChangeHandler{

		@Override
		public void onChange(ChangeEvent event) {
			HTMLTable.RowFormatter rf = vendorsFlexTable.getRowFormatter();
			for(int r=1; r<vendorsFlexTable.getRowCount();r++){
				rf.setStyleName(r, "FlexTable-noHighlight");
			}
			int index = lb.getSelectedIndex();
			String foodType = lb.getItemText(index);
			for(int r=0; r<vendors.size(); r++){
				if(vendors.get(r).getFoodtype().equals(foodType)){
					int i = (r+1);
					rf.setStyleName(i, "FlexTable-Highlight");
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
	
	//service proxy 
	  private void refresVendorList() {
		    // Initialize the service proxy.
		    if (ListSvc == null) {
		    	ListSvc = GWT.create(VendorService.class);
		    }

		    // Set up the callback object.
		    AsyncCallback<Vendor[]> callback = new AsyncCallback<Vendor[]>() {
		      public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
		      }

			@Override
			public void onSuccess(Vendor[] result) {
				// TODO Auto-generated method stub
				
			}
		    };

		    // Make the call to the vendor service.
		    ListSvc.getVendors(  callback);
		  }
}



