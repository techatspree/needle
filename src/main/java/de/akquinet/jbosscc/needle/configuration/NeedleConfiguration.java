package de.akquinet.jbosscc.needle.configuration;

import static de.akquinet.jbosscc.needle.common.Preconditions.checkState;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionProviderInstancesSupplier;

/**
 * Loads an holds the configuration of needle.
 * Needle configuration can be defined in <b>needle.properties</b> files
 * anywhere in the classpath. If required, the user can provide a custom resource name.
 */
public final class NeedleConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(NeedleConfiguration.class);
    static final String ALREADY_INITITIALIZED = "NeedleConfiguration.INSTANCE is already inititialized!";

    private static NeedleConfiguration INSTANCE = null;

    public static NeedleConfiguration init() {
        return init(ConfigurationLoader.CUSTOM_CONFIGURATION_FILENAME);
    }

    public static NeedleConfiguration init(final String resourceName) {
        checkState(INSTANCE == null, ALREADY_INITITIALIZED);
        INSTANCE = new NeedleConfiguration(resourceName);
        return get();
    }

    public static NeedleConfiguration get() {
        if (INSTANCE == null) {
            LOG.info("NeedleConfiguration.INSTANCE is null. Initializing with default: init()");
            INSTANCE = init();
        }
        return INSTANCE;
    }

    public static final String MOCK_PROVIDER_KEY = "mock.provider";
    public static final String CUSTOM_INJECTION_ANNOTATIONS_KEY = "custom.injection.annotations";
    public static final String CUSTOM_INJECTION_PROVIDER_CLASSES_KEY = "custom.injection.provider.classes";
    public static final String CUSTOM_INSTANCES_SUPPLIER_CLASSES_KEY = "custom.instances.supplier.classes";
    public static final String DB_OPERATION_KEY = "db.operation";
    public static final String PERSISTENCEUNIT_NAME_KEY = "persistenceUnit.name";
    public static final String JDBC_URL_KEY = "jdbc.url";
    public static final String JDBC_DRIVER_KEY = "jdbc.driver";
    public static final String JDBC_USER_KEY = "jdbc.user";
    public static final String JDBC_PASSWORD_KEY = "jdbc.password";
    public static final String HIBERNATE_CFG_FILENAME_KEY = "hibernate.cfg.filename";

    private final Map<String, String> configurationProperties;

    private final Set<Class<InjectionProvider<?>>> customInjectionProviderClasses;
    private final Set<Class<Annotation>> customInjectionAnnotations;
    private final Set<Class<InjectionProviderInstancesSupplier>> customInjectionProviderInstancesSupplierClasses;

    /**
     * @see #NeedleConfiguration(Map)
     * @param resourceName - properties file to load, default is "needle.properties"
     */
    private NeedleConfiguration(final String resourceName) {
        this(ConfigurationLoader.loadResourceAndDefault(resourceName));
    }

    private NeedleConfiguration(final Map<String, String> configurationProperties) {
        this.configurationProperties = configurationProperties;

        final LookupCustomClasses lookupCustomClasses = new LookupCustomClasses(configurationProperties);
        this.customInjectionAnnotations = lookupCustomClasses.apply(CUSTOM_INJECTION_ANNOTATIONS_KEY);
        this.customInjectionProviderClasses = lookupCustomClasses.apply(CUSTOM_INJECTION_PROVIDER_CLASSES_KEY);
        this.customInjectionProviderInstancesSupplierClasses = lookupCustomClasses.apply(CUSTOM_INSTANCES_SUPPLIER_CLASSES_KEY);

        LOG.info("Needle Configuration: {}", toString());
    }

    /**
     * Returns the configured jpa persistence unit name.
     * 
     * @return jpa persistence unit name
     */
    public String getPersistenceunitName() {
        return configurationProperties.get(PERSISTENCEUNIT_NAME_KEY);
    }

    /**
     * Returns the name of the configured hibernate.cfg file
     * 
     * @return name of hibernate.cfg file
     */
    public String getHibernateCfgFilename() {
        return configurationProperties.get(HIBERNATE_CFG_FILENAME_KEY);
    }

    /**
     * Returns the configured database operation class name.
     * 
     * @return database operation class name or null
     */
    public String getDBOperationClassName() {
        return configurationProperties.get(DB_OPERATION_KEY);
    }

    public String getJdbcUrl() {
        return configurationProperties.get(JDBC_URL_KEY);
    }

    public String getJdbcDriver() {
        return configurationProperties.get(JDBC_DRIVER_KEY);
    }

    public String getJdbcUser() {
        return configurationProperties.get(JDBC_USER_KEY);
    }

    public String getJdbcPassword() {
        return configurationProperties.get(JDBC_PASSWORD_KEY);
    }

    /**
     * Returns the configured mock provider class name
     * 
     * @return mock provider class name or null
     */
    public String getMockProviderClassName() {
        return configurationProperties.get(MOCK_PROVIDER_KEY);
    }

    /**
     * Returns the configured custom {@link Annotation} classes for default mock
     * injections.
     * 
     * @return a {@link Set} of {@link Annotation} classes
     */
    public Set<Class<Annotation>> getCustomInjectionAnnotations() {
        return customInjectionAnnotations;
    }

    /**
     * Returns the configured custom {@link InjectionProvider} classes.
     * 
     * @return a {@link Set} of {@link InjectionProvider} classes
     */
    public Set<Class<InjectionProvider<?>>> getCustomInjectionProviderClasses() {
        return customInjectionProviderClasses;
    }

    public Set<Class<InjectionProviderInstancesSupplier>> getCustomInjectionProviderInstancesSupplierClasses() {
        return customInjectionProviderInstancesSupplierClasses;
    }


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\nPU_NAME=").append(getPersistenceunitName());
        builder.append("\nCFG_FILE=").append(getHibernateCfgFilename());
        builder.append("\nDB_OPERATION=").append(getDBOperationClassName());
        builder.append("\nMOCK_PROVIDER=").append(getMockProviderClassName());

        return builder.toString();
    }


}
