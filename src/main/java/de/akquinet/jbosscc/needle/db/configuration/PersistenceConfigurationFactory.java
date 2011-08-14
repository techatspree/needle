package de.akquinet.jbosscc.needle.db.configuration;

import de.akquinet.jbosscc.needle.db.configuration.EJB3Configuration;
import de.akquinet.jbosscc.needle.db.configuration.PersistenceUnitConfiguration;

import java.util.HashMap;
import java.util.Map;


public class PersistenceConfigurationFactory {

    private static final Map<String, PersistenceConfiguration> configMap = new HashMap<String, PersistenceConfiguration>();
    private static final Map<Class<?>[], PersistenceConfiguration> config2Map = new HashMap<Class<?>[], PersistenceConfiguration>();

    public static PersistenceConfiguration getPersistenceConfiguration(String pu) {
        PersistenceConfiguration result = configMap.get(pu);

        if (result == null) {
            result = new PersistenceUnitConfiguration(pu);
            configMap.put(pu, result);
        }

        return result;
    }

    public static PersistenceConfiguration getPersistenceConfiguration(Class<?>[] classes) {
        PersistenceConfiguration result = config2Map.get(classes);

        if (result == null) {
            result = new EJB3Configuration(classes);
            config2Map.put(classes, result);
        }

        return result;
    }



}
