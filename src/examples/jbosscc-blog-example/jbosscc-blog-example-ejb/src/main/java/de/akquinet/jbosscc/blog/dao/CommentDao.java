package de.akquinet.jbosscc.blog.dao;

import java.util.List;

import javax.ejb.Local;

import de.akquinet.jbosscc.blog.BlogEntry;
import de.akquinet.jbosscc.blog.Comment;
import de.akquinet.jbosscc.blog.dao.common.Dao;

@Local
public interface CommentDao extends Dao<Comment> {

	List<Comment> findComments(BlogEntry blogEntry);

}
