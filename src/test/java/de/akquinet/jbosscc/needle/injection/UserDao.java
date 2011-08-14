package de.akquinet.jbosscc.needle.injection;

import javax.inject.Inject;

public class UserDao {

	@Inject
	@CurrentUser
	private User currentUser;

	@Inject
	private User user;

	public User getCurrentUser() {
		return currentUser;
	}

	public User getUser() {
		return user;
	}

}
