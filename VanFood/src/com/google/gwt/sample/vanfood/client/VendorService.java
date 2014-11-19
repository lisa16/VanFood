package com.google.gwt.sample.vanfood.client;

import com.google.gwt.sample.vanfood.shared.Vendor;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("vendorService")
public interface VendorService extends RemoteService {
	Vendor[] getVendors();
	String parseVendors();
}
