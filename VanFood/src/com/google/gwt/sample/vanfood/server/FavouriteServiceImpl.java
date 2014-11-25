package com.google.gwt.sample.vanfood.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.sample.vanfood.client.NotLoggedInException;
import com.google.gwt.sample.vanfood.client.FavouriteService;
import com.google.gwt.sample.vanfood.client.VendorService;
import com.google.gwt.sample.vanfood.client.VendorServiceAsync;
import com.google.gwt.sample.vanfood.shared.Vendor;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;



public class FavouriteServiceImpl extends RemoteServiceServlet
implements FavouriteService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6909150698955113860L;
	private static final Logger LOG = Logger.getLogger(FavouriteServiceImpl.class.getName());

	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");


	public void addFavourite(Vendor vendor) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Favourite.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<Favourite> favourites = (List<Favourite>) q.execute(getUser());
			
			Long vendorID = vendor.getVendorID();
		
			for (Favourite favourite : favourites) {
				if (vendorID.equals(favourite.getVendorID())) {
					return;
				}
			} 
			pm.makePersistent(new Favourite(getUser(), vendorID));
		} finally {
			pm.close();
		}
	}

	public void removeFavourite(Vendor vendor) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			long deleteCount = 0;
			Query q = pm.newQuery(Favourite.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<Favourite> favourites = (List<Favourite>) q.execute(getUser());
			Long vendorID = vendor.getVendorID();

			for (Favourite favourite : favourites) {
				if (vendorID.equals(favourite.getVendorID())) {
					deleteCount++;
					pm.deletePersistent(favourite);
				}
			}
			if (deleteCount != 1) {
				LOG.log(Level.WARNING, "removeFavourite deleted "+deleteCount+" Favourites");
			}
		} finally {
			pm.close();
		}
	}

	@Override
	public Vendor[] getFavourite() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<Vendor> favouriteVendors = new ArrayList<Vendor>();
		try {
			Query q = pm.newQuery(Favourite.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			q.setOrdering("createDate");
			List<Favourite> favourites = (List<Favourite>) q.execute(getUser());

			for (Favourite favourite : favourites) {
				Long vendorID = favourite.getVendorID();	
				if (!vendorID.equals(null)){
					Vendor vendor = pm.getObjectById(Vendor.class, vendorID);
					favouriteVendors.add(vendor);
				}
			}
		} finally {
			pm.close();
		}
		return (Vendor[]) favouriteVendors.toArray(new Vendor[0]);
	}

	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {
			throw new NotLoggedInException("Not logged in.");
		}
	}

	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
}
