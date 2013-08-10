package de.akquinet.jbosscc.needle.junit;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.configuration.PropertyBasedConfigurationFactory;
import de.akquinet.jbosscc.needle.db.operation.AbstractDBOperation;

public class DatabaseRuleBuilder {
    private String configFile;

    private Class<? extends AbstractDBOperation> dbOperationClass;

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

    public DatabaseRuleBuilder with(final Class<? extends AbstractDBOperation> dbOperationClass) {
        this.dbOperationClass = dbOperationClass;
        return this;
    }

    public DatabaseRuleBuilder withJdbcUser(final String jdbcUser) {
        this.jdbcUser = jdbcUser;
        return this;
    }

    public DatabaseRuleBuilder withJdbcPassword(final String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
        return this;
    }

    public DatabaseRuleBuilder withJdbcUrl(final String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    public DatabaseRuleBuilder withPersistenceUnit(final String name) {
        this.persistenceUnitName = name;
        return this;
    }

    public DatabaseRuleBuilder withHibernateCfgFilename(final String name) {
        this.hibernateCfgFilename = name;
        return this;
    }

    public DatabaseRuleBuilder withJdbcDriver(final String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
        return this;
    }

    private NeedleConfiguration getNeedleConfiguration() throws Exception {
        return configFile == null ? PropertyBasedConfigurationFactory.get().clone() : PropertyBasedConfigurationFactory
                .get(configFile);
    }

    public DatabaseRule build() {
        try {
            NeedleConfiguration needleConfiguration = getNeedleConfiguration();

            needleConfiguration.setDBOperationClass(dbOperationClass != null ? dbOperationClass : needleConfiguration
                    .getDBOperationClass());
            needleConfiguration.setJdbcDriver(jdbcDriver != null ? jdbcDriver : needleConfiguration.getJdbcDriver());
            needleConfiguration.setJdbcUser(jdbcUser != null ? jdbcUser : needleConfiguration.getJdbcUser());
            needleConfiguration.setJdbcPassword(jdbcPassword != null ? jdbcPassword : needleConfiguration
                    .getJdbcPassword());
            needleConfiguration.setJdbcUrl(jdbcUrl != null ? jdbcUrl : needleConfiguration.getJdbcUrl());
            needleConfiguration.setPersistenceunitName(persistenceUnitName != null ? persistenceUnitName
                    : needleConfiguration.getPersistenceunitName());
            needleConfiguration.setHibernateCfgFilename(hibernateCfgFilename != null ? hibernateCfgFilename
                    : needleConfiguration.getHibernateCfgFilename());

            DatabaseRule databaseRule = new DatabaseRule(needleConfiguration);

            return databaseRule;
        } catch (Exception e) {
            throw new RuntimeException("could not build database rule", e);
        }
    }

}
