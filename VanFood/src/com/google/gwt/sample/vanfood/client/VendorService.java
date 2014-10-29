package com.google.gwt.sample.vanfood.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("vendorService")
public interface VendorService extends RemoteService {
	//assuming we get String[] as input like StockWatcher
	Vendor[] getVendors(String[] symbols);
}
