package de.akquinet.jbosscc.needle.db;

import java.sql.SQLException;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.db.dialect.DBDialect;

public class DatabaseTestcase  {

	private static final Logger LOG = LoggerFactory.getLogger(DatabaseTestcase.class);

	private final DatabaseTestcaseConfiguration configuration;

	public DatabaseTestcase() {
		this(NeedleConfiguration.getPersistenceunitName());
	}

	public DatabaseTestcase(String puName) {
		configuration = new DatabaseTestcaseConfiguration(puName);

	}

	public DatabaseTestcase(Class<?>[] clazzes) {
		configuration = new DatabaseTestcaseConfiguration(clazzes);
	}

	protected void after() {
		DBDialect dialect = configuration.getDBDialect();
		if (dialect != null) {
			deleteAll(dialect);
		}
	}

	/**
	 * Delete everything from the DB: This cannot be done with the JPA, because
	 * the order of deletion matters. Instead we directly use a JDBC connection.
	 */
	protected void deleteAll(DBDialect dbDialect) {

		try {
			dbDialect.openConnection();
			Set<String> tableNames = dbDialect.getTableNames();

			dbDialect.disableReferentialIntegrity();

			dbDialect.deleteContent(tableNames);

			dbDialect.commit();

		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			try {
				dbDialect.rollback();
			} catch (SQLException e1) {
				LOG.error(e1.getMessage(), e1);
			}
		} finally {
			try {
				dbDialect.closeConnection();
			} catch (SQLException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	public EntityManager getEntityManager() {
		return configuration.getEntityManager();
	}

	public EntityManagerFactory getEntityManagerFactory(){
		return configuration.getEntityManagerFactory();
	}
}
