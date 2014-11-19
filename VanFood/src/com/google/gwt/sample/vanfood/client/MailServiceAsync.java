package com.google.gwt.sample.vanfood.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MailServiceAsync {
	void sendMail(String from, String subject, String to, String msg, AsyncCallback<String> callback);
   
}
