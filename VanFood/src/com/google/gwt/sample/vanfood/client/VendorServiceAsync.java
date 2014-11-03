package com.google.gwt.sample.vanfood.client;
import com.google.gwt.sample.vanfood.server.Vendor;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VendorServiceAsync {

	void getVendors(AsyncCallback<Vendor[]> callback);

}
