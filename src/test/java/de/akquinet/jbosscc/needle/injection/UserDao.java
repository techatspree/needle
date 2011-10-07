package de.akquinet.jbosscc.needle.injection;

import java.util.Queue;

import javax.inject.Inject;

import de.akquinet.jbosscc.needle.db.User;

public class UserDao {

	@Inject
	@CurrentUser
	private User currentUser;

	@Inject
	private User user;

	private Queue<?> queue;

	public User getCurrentUser() {
		return currentUser;
	}

	public User getUser() {
		return user;
	}

	public Queue<?> getQueue() {
		return queue;
	}

}
