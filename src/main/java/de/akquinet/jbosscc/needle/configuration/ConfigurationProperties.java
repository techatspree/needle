package de.akquinet.jbosscc.needle.configuration;

public interface ConfigurationProperties {
	String MOCK_PROVIDER_KEY = "mock.provider";
    String CUSTOM_INJECTION_ANNOTATIONS_KEY = "custom.injection.annotations";
    String CUSTOM_INJECTION_PROVIDER_CLASSES_KEY = "custom.injection.provider.classes";
    String CUSTOM_INSTANCES_SUPPLIER_CLASSES_KEY = "custom.instances.supplier.classes";
    String DB_OPERATION_KEY = "db.operation";
    String PERSISTENCEUNIT_NAME_KEY = "persistenceUnit.name";
    String JDBC_URL_KEY = "jdbc.url";
    String JDBC_DRIVER_KEY = "jdbc.driver";
    String JDBC_USER_KEY = "jdbc.user";
    String JDBC_PASSWORD_KEY = "jdbc.password";
    String HIBERNATE_CFG_FILENAME_KEY = "hibernate.cfg.filename";
}
