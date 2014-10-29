package com.google.gwt.sample.vanfood.client;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MapServiceAsync {
	void getVendors(String[] symbols, AsyncCallback<Vendor[]> callback);
}
