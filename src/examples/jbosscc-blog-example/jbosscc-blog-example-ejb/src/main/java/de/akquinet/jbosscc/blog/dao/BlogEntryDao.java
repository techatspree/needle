package de.akquinet.jbosscc.blog.dao;

import java.util.List;

import javax.ejb.Local;

import de.akquinet.jbosscc.blog.BlogEntry;
import de.akquinet.jbosscc.blog.dao.common.Dao;

@Local
public interface BlogEntryDao extends Dao<BlogEntry> {

	List<BlogEntry> find(int maxresults, int firstresult);

	Long count();

}
