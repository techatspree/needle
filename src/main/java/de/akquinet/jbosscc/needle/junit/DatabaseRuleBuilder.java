package de.akquinet.jbosscc.needle.junit;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.configuration.PropertyBasedConfigurationFactory;
import de.akquinet.jbosscc.needle.db.operation.DBOperation;

public class DatabaseRuleBuilder {
    private String configFile;

    private Class<? extends DBOperation> dbOperationClass;

    private String persistenceUnitName;
    private String jdbcUrl;
    private String jdbcDriver;
    private String jdbcUser;
    private String jdbcPassword;
    private String hibernateCfgFilename;

    public DatabaseRuleBuilder with(final String configFile) {
        this.configFile = configFile;
        return this;
    }

    public DatabaseRuleBuilder with(final Class<? extends DBOperation> dbOperationClass) {
        this.dbOperationClass = dbOperationClass;
        return this;
    }

    private NeedleConfiguration getNeedleConfiguration() throws Exception {
        return configFile == null ? PropertyBasedConfigurationFactory.get().clone() : PropertyBasedConfigurationFactory
                .get(configFile);

    }

    public DatabaseRule build() {
        try {
            NeedleConfiguration needleConfiguration = getNeedleConfiguration();
            
            DatabaseRule databaseRule = new DatabaseRule(needleConfiguration);
            
            
            return databaseRule;
        } catch (Exception e) {
            throw new RuntimeException("could not build database rule", e);
        }
    }

}
