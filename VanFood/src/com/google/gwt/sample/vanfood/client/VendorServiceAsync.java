package com.google.gwt.sample.vanfood.client;
import com.google.gwt.sample.vanfood.shared.Vendor;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VendorServiceAsync {
	void getVendors(AsyncCallback<Vendor[]> callback);
	void parseVendors(AsyncCallback<String> asyncCallback);
}
