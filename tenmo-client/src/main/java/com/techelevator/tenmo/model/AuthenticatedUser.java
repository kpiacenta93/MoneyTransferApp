package com.techelevator.tenmo.model;

import java.security.Principal;

public class AuthenticatedUser {

	private Principal currentUser;

	private String token;
	private User user;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Principal getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Principal currentUser) {
		this.currentUser = currentUser;
	}

}
