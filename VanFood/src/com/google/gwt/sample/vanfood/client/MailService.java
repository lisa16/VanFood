package com.google.gwt.sample.vanfood.client;

import java.io.UnsupportedEncodingException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("mailService")
public interface MailService extends RemoteService {
	String sendMail(String from, String subject, String to, String msg) throws UnsupportedEncodingException;
}
