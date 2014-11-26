package com.google.gwt.sample.vanfood.client;

import com.google.gwt.sample.vanfood.shared.Vendor;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FavouriteServiceAsync {
	public void addFavourite(Vendor vendor, AsyncCallback<Void> async);
	public void getFavourite(AsyncCallback<Vendor[]> async);
	public void removeFavourite(Vendor vendor, AsyncCallback<Void> async);

}
