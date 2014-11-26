package com.google.gwt.sample.vanfood.server;

import com.google.gwt.sample.vanfood.client.MailService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailServiceImpl extends  RemoteServiceServlet implements MailService {

	@Override
	public String sendMail(String from, String subject, String to, String message) throws UnsupportedEncodingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try{
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from, ""));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(to, ""));
			msg.setSubject(subject);
			msg.setText(message);
			Transport.send(msg);
		}
		catch (AddressException e) {            
			return("AddressException");
		}  
		catch (MessagingException e) {
			return("MessageException");			
		}
		return ("message sent!");

	}
}


