package com.google.gwt.sample.vanfood.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.sample.vanfood.shared.Vendor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TwitterPopup extends PopupPanel {


	private VerticalPanel mainPanel = new VerticalPanel();

	public TwitterPopup(Vendor vendor) {

		super(true);
		this.setStyleName("PopupPanel");

		int left = (Window.getClientWidth()) / 3;
		int top = (Window.getClientHeight()) / 2;
		this.setPopupPosition(left, top);

		String vendorName = null;
		try {
			vendorName = vendor.getName();
		} catch (Exception e) {

		}
		if ((vendorName==null)||(vendorName.equalsIgnoreCase("Name not available")))
			vendorName = "a street vendor";
		Label desc = new Label("You've added " + vendorName + " to your favourites list." +"\n"
				+ "(Click outside the box to close)");
		mainPanel.add(desc);

		String url = "<a href=\"https://twitter.com/share\" class=\"twitter-share-button\" data-text=\"I just added " +vendorName+ " to my list of favourite street vendors\" target=\"_blank\">Tweet</a>";
		HTML html = new HTML(url);

		mainPanel.add(html);
		
		Document doc = Document.get();
	    ScriptElement script = doc.createScriptElement();
	    script.setSrc("http://platform.twitter.com/widgets.js");
	    script.setType("text/javascript");
	    script.setLang("javascript");
	    doc.getBody().appendChild(script);



		this.add(mainPanel);

	}
}


