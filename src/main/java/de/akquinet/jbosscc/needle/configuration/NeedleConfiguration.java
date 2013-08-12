package de.akquinet.jbosscc.needle.configuration;

import java.lang.annotation.Annotation;
import java.util.Set;

import de.akquinet.jbosscc.needle.db.operation.AbstractDBOperation;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionProviderInstancesSupplier;
import de.akquinet.jbosscc.needle.mock.MockProvider;

public final class NeedleConfiguration implements Cloneable {

    private Set<Class<Annotation>> customInjectionAnnotations;
    private Set<Class<InjectionProvider<?>>> customInjectionProviderClasses;
    private Set<Class<InjectionProviderInstancesSupplier>> customInjectionProviderInstancesSupplierClasses;
    private String persistenceunitName;
    private String hibernateCfgFilename;
    private Class<? extends MockProvider> mockProviderClass;
    private Class<? extends AbstractDBOperation> dbOperationClass;
    private String jdbcUrl;
    private String jdbcDriver;
    private String jdbcUser;
    private String jdbcPassword;

    /**
     * Returns the configured custom {@link Annotation} classes for default mock
     * injections.
     * 
     * @return a {@link Set} of {@link Annotation} classes
     */
    public Set<Class<Annotation>> getCustomInjectionAnnotations() {
        return customInjectionAnnotations;
    }

    public void setCustomInjectionAnnotations(final Set<Class<Annotation>> customInjectionAnnotations) {
        this.customInjectionAnnotations = customInjectionAnnotations;
    }

    /**
     * Returns the configured custom {@link InjectionProvider} classes.
     * 
     * @return a {@link Set} of {@link InjectionProvider} classes
     */
    public Set<Class<InjectionProvider<?>>> getCustomInjectionProviderClasses() {
        return customInjectionProviderClasses;
    }

    public void setCustomInjectionProviderClasses(final Set<Class<InjectionProvider<?>>> customInjectionProviderClasses) {
        this.customInjectionProviderClasses = customInjectionProviderClasses;
    }

    /**
     * Returns the configured database operation class name.
     * 
     * @return database operation class name or null
     */
    public Class<? extends AbstractDBOperation> getDBOperationClass() {
        return dbOperationClass;
    }

    public void setDBOperationClass(final Class<? extends AbstractDBOperation> dbOperationClass) {
        this.dbOperationClass = dbOperationClass;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(final String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(final String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcUser() {
        return jdbcUser;
    }

    public void setJdbcUser(final String jdbcUser) {
        this.jdbcUser = jdbcUser;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(final String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    /**
     * Returns the configured mock provider class
     * 
     * @return mock provider class name or null
     */
    public Class<? extends MockProvider> getMockProviderClass() {
        return mockProviderClass;
    }

    public void setMockProviderClass(final Class<? extends MockProvider> mockProviderClass) {
        this.mockProviderClass = mockProviderClass;
    }

    /**
     * Returns the configured jpa persistence unit name.
     * 
     * @return jpa persistence unit name
     */
    public String getPersistenceunitName() {
        return persistenceunitName;
    }

    public void setPersistenceunitName(final String persistenceunitName) {
        this.persistenceunitName = persistenceunitName;
    }

    /**
     * Returns the name of the configured hibernate.cfg file
     * 
     * @return name of hibernate.cfg file
     */
    public String getHibernateCfgFilename() {
        return hibernateCfgFilename;
    }

    public void setHibernateCfgFilename(final String hibernateCfgFilename) {
        this.hibernateCfgFilename = hibernateCfgFilename;
    }

    public Set<Class<InjectionProviderInstancesSupplier>> getCustomInjectionProviderInstancesSupplierClasses() {
        return customInjectionProviderInstancesSupplierClasses;
    }

    public void setCustomInjectionProviderInstancesSupplierClasses(
            final Set<Class<InjectionProviderInstancesSupplier>> supplier) {
        this.customInjectionProviderInstancesSupplierClasses = supplier;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\nPU_NAME=").append(getPersistenceunitName());
        builder.append("\nCFG_FILE=").append(getHibernateCfgFilename());
        builder.append("\nDB_OPERATION=").append(getDBOperationClass());
        builder.append("\nMOCK_PROVIDER=").append(getMockProviderClass());

        return builder.toString();
    }

    @Override
    public NeedleConfiguration clone() throws CloneNotSupportedException {
        return (NeedleConfiguration) super.clone();
    }

}
