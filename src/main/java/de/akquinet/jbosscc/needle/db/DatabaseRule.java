package de.akquinet.jbosscc.needle.db;

import java.sql.SQLException;
import java.util.Set;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.rules.ExternalResource;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.db.configuration.PersistenceConfiguration;
import de.akquinet.jbosscc.needle.db.configuration.PersistenceConfigurationFactory;
import de.akquinet.jbosscc.needle.db.dialect.DBDialect;

public class DatabaseRule extends ExternalResource {

	private String puName;
	private Class<?>[] clazzes;

	private EntityManager entityManager;

	public DatabaseRule() {
		this(NeedleConfiguration.getPersistenceunitName());
	}

	public DatabaseRule(String puName) {
		Assert.assertNotNull(puName);
		this.puName = puName;
	}

	public DatabaseRule(Class<?>[] clazzes) {
		Assert.assertNotNull(clazzes);
		this.clazzes = clazzes;
	}

	@Override
	protected void after() {
		DBDialect dialect = NeedleConfiguration.getDBDialect();

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
			try {
				dbDialect.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
			}
		} finally {
			try {
				dbDialect.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public EntityManager getEntityManager() {
		if (entityManager == null) {
			final PersistenceConfiguration factory;

			if (puName != null) {
				factory = PersistenceConfigurationFactory.getPersistenceConfiguration(puName);
			} else {
				factory = PersistenceConfigurationFactory.getPersistenceConfiguration(clazzes);
			}

			entityManager = factory.getEntityManager();

		}
		return entityManager;
	}
}
