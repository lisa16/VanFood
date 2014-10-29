package com.google.gwt.sample.vanfood.client;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ListServiceAsync {

	void getVendors(String[] symbols, AsyncCallback<Vendor[]> callback);

}
