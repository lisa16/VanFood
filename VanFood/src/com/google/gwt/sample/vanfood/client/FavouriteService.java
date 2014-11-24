package com.google.gwt.sample.vanfood.client;

import com.google.gwt.sample.vanfood.shared.Vendor;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("favourite")
public interface FavouriteService extends RemoteService {
	public void addFavourite (Vendor vendor) throws NotLoggedInException;
	public void removeFavourite (Vendor vendor) throws NotLoggedInException;
	public Vendor[] getFavourite () throws NotLoggedInException;
}
