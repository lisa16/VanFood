package com.google.gwt.sample.vanfood.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.sample.vanfood.client.LoginInfo;
import com.google.gwt.sample.vanfood.client.LoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements
LoginService {

/**
	 * 
	 */
	private static final long serialVersionUID = 5420868065655917797L;

public LoginInfo login(String requestUri) {
UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();
LoginInfo loginInfo = new LoginInfo();

if (user != null) {
  loginInfo.setLoggedIn(true);
  loginInfo.setEmailAddress(user.getEmail());
  loginInfo.setNickname(user.getNickname());
  loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
} else {
  loginInfo.setLoggedIn(false);
  loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
}
return loginInfo;
}}
