package com.google.gwt.sample.vanfood.client;


import java.io.Serializable;

public class NotLoggedInException extends Exception implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5097846309032067489L;

	public NotLoggedInException() {
		super();
	}

	public NotLoggedInException(String message) {
		super(message);
	}


}