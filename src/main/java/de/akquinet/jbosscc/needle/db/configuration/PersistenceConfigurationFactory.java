package de.akquinet.jbosscc.needle.db.configuration;

import de.akquinet.jbosscc.needle.db.configuration.EJB3Configuration;
import de.akquinet.jbosscc.needle.db.configuration.PersistenceUnitConfiguration;

import java.util.HashMap;
import java.util.Map;

public class PersistenceConfigurationFactory {

	private static final Map<String, PersistenceConfiguration> PERSISTENCE_PU_CONFIG_MAP = new HashMap<String, PersistenceConfiguration>();
	private static final Map<Class<?>[], PersistenceConfiguration> PERSISTENCE_CLASS_CONFIG_MAP = new HashMap<Class<?>[], PersistenceConfiguration>();

	private PersistenceConfigurationFactory() {
		super();
	}

	public static PersistenceConfiguration getPersistenceConfiguration(String pu) {
		PersistenceConfiguration result = PERSISTENCE_PU_CONFIG_MAP.get(pu);

		if (result == null) {
			result = new PersistenceUnitConfiguration(pu);
			PERSISTENCE_PU_CONFIG_MAP.put(pu, result);
		}

		return result;
	}

	public static PersistenceConfiguration getPersistenceConfiguration(Class<?>[] classes) {
		PersistenceConfiguration result = PERSISTENCE_CLASS_CONFIG_MAP.get(classes);

		if (result == null) {
			result = new EJB3Configuration(classes);
			PERSISTENCE_CLASS_CONFIG_MAP.put(classes, result);
		}

		return result;
	}

}
