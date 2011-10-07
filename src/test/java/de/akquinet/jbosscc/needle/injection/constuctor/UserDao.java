package de.akquinet.jbosscc.needle.injection.constuctor;

import javax.inject.Inject;

import de.akquinet.jbosscc.needle.db.User;
import de.akquinet.jbosscc.needle.injection.CurrentUser;

public class UserDao {

	private final User user;

	private final User currentUser;

	@Inject
	public UserDao(User user, @CurrentUser User currentUser) {
		super();
		this.user = user;
		this.currentUser = currentUser;
	}

	public User getUser() {
		return user;
	}

	public User getCurrentUser() {
		return currentUser;
	}

}
