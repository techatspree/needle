package de.akquinet.jbosscc.needle.db.configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public final class PersistenceConfigurationFactory implements PersistenceConfiguration {

	private static final Map<PersistenceConfigurationFactory, PersistenceConfiguration> PERSISTENCE_MAP = new HashMap<PersistenceConfigurationFactory, PersistenceConfiguration>();

	private String persistenceUnit;
	private Class<?>[] cfgClazzes;

	public PersistenceConfigurationFactory(final String persistenceUnit) {
		super();
		this.persistenceUnit = persistenceUnit;
	}

	public PersistenceConfigurationFactory(final Class<?>[] cfgClazzes) {
		super();
		this.cfgClazzes = Arrays.copyOf(cfgClazzes, cfgClazzes.length);
	}

	private PersistenceConfiguration getPersistenceConfiguration() {
		return getPersistenceConfiguration(this);
	}


	String getPersistenceUnit() {
		return persistenceUnit;
	}

	Class<?>[] getCfgClazzes() {
		return cfgClazzes;
	}

	@Override
	public EntityManager getEntityManager() {
		return getPersistenceConfiguration().getEntityManager();
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return getPersistenceConfiguration().getEntityManagerFactory();
	}

	private static PersistenceConfiguration getPersistenceConfiguration(PersistenceConfigurationFactory configuration) {
		PersistenceConfiguration result = PERSISTENCE_MAP.get(configuration);

		if (result == null) {
			result = createPersistenceConfiguration(configuration);
			PERSISTENCE_MAP.put(configuration, result);
		}

		return result;
	}

	private static PersistenceConfiguration createPersistenceConfiguration(
	        final PersistenceConfigurationFactory configuration) {
		if (configuration.getPersistenceUnit() != null) {
			return new PersistenceUnitConfiguration(configuration.getPersistenceUnit());
		} else if (configuration.getCfgClazzes() != null) {
			return new EJB3Configuration(configuration.getCfgClazzes());
		}

		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(cfgClazzes);
		result = prime * result + ((persistenceUnit == null) ? 0 : persistenceUnit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersistenceConfigurationFactory other = (PersistenceConfigurationFactory) obj;
		if (!Arrays.equals(cfgClazzes, other.cfgClazzes))
			return false;
		if (persistenceUnit == null) {
			if (other.persistenceUnit != null)
				return false;
		} else if (!persistenceUnit.equals(other.persistenceUnit))
			return false;
		return true;
	}

}
