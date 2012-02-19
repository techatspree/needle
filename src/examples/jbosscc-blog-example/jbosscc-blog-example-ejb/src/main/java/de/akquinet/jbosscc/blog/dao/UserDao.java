package de.akquinet.jbosscc.blog.dao;

import javax.ejb.Local;

import de.akquinet.jbosscc.blog.User;
import de.akquinet.jbosscc.blog.dao.common.Dao;

@Local
public interface UserDao extends Dao<User> {

	User findByUsername(String username);

}
