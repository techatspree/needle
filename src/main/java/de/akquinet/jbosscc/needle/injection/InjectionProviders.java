package de.akquinet.jbosscc.needle.injection;

import static de.akquinet.jbosscc.needle.common.Annotations.assertIsQualifier;
import static de.akquinet.jbosscc.needle.common.Preconditions.checkArgument;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Named;

/**
 * Utility class for creating {@link InjectionProvider}s with default behavior.
 * <p/>
 * Usage:<br/>
 * 
 * <pre>
 * import static de.akquinet.jbosscc.needle.injection.InjectionProviders.*;
 * </pre>
 * <p/>
 * then call static factory methods to create providers.
 * 
 * @author Jan Galinski, Holisticon AG
 * 
 */
public final class InjectionProviders {

    /**
     * InjectionProvider that provides a singleton instance of type T whenever
     * injection is required.
     * 
     * @param instance
     *            the instance to return whenever assignable
     * @return InjectionProvider for instance
     */
    public static <T> InjectionProvider<T> providerForInstance(final T instance) {
        return new DefaultInstanceInjectionProvider<T>(instance);
    }

    /**
     * InjectionProvider that provides a singleton instance of type T for every
     * injection point that is annotated with a {@link Named} qualifier with
     * value "name".
     * 
     * @param name
     *            value of Named annotation
     * @param instance
     *            the instance to return whenever needed
     * @return InjectionProvider for instance
     */
    public static <T> InjectionProvider<T> providerForNamedInstance(final String name, final T instance) {
        return new NamedInstanceInjectionProvider<T>(name, instance);
    }

    /**
     * InjectionProvider that provides a singleton instance of type T for every
     * injection point that is annotated with the given qualifier.
     * 
     * @param qualifier
     *            qualifying annotation of injection point
     * @param instance
     *            the instance to return whenever needed
     * @return InjectionProvider for instance
     */
    public static <T> InjectionProvider<T> providerForQualifiedInstance(final Class<? extends Annotation> qualifier, final T instance) {
        return new QualifiedInstanceInjectionProvider<T>(qualifier, instance);
    }

    /**
     * Creates a new Set.
     * 
     * @param providers
     *            vararg array of providers
     * @return set containing providers
     */
    private static Set<InjectionProvider<?>> newProviderSet(final InjectionProvider<?>... providers) {
        final Set<InjectionProvider<?>> result = new LinkedHashSet<InjectionProvider<?>>();

        if (providers != null && providers.length > 0) {

            for (final InjectionProvider<?> provider : providers) {
                result.add(provider);
            }

        }
        return result;
    }

    /**
     * Creates a new Supplier.
     * 
     * @param providers
     *            vararg array of providers
     * @return new supplier
     */
    public static InjectionProviderInstancesSupplier supplierForInjectionProviders(final InjectionProvider<?>... providers) {
        return new InjectionProviderInstancesSupplier() {

            @Override
            public Set<InjectionProvider<?>> get() {
                return newProviderSet(providers);
            }
        };
    }

    /**
     * Creates new supplier containing all providers in a new set.
     * 
     * @param suppliers
     *            vararg array of existing suppliers
     * @return new instance containing all providers
     */
    private static InjectionProviderInstancesSupplier mergeSuppliers(final InjectionProviderInstancesSupplier... suppliers) {
        final Set<InjectionProvider<?>> result = new LinkedHashSet<InjectionProvider<?>>();

        if (suppliers != null && suppliers.length > 0) {

            for (final InjectionProviderInstancesSupplier supplier : suppliers) {
                result.addAll(supplier.get());
            }

        }

        return new InjectionProviderInstancesSupplier() {

            @Override
            public Set<InjectionProvider<?>> get() {
                return result;
            }
        };
    }

    /**
     * Create array of providers from given suppliers.
     * 
     * @param suppliers
     *            vararg array of suppliers
     * @return array of providers for use with vararg method
     */
    public static InjectionProvider<?>[] providersForInstancesSuppliers(final InjectionProviderInstancesSupplier... suppliers) {
        final InjectionProviderInstancesSupplier supplier = mergeSuppliers(suppliers);
        return supplier.get().toArray(new InjectionProvider<?>[supplier.get().size()]);
    }

    private InjectionProviders() {
        // hide default constructor
    }


    /**
     * Base class for all instance injection providers.
     * 
     * @param <T>
     *            type of instance
     */
    private abstract static class InstanceInjectionProvider<T> implements InjectionProvider<T> {

        protected final T instance;

        public InstanceInjectionProvider(final T instance) {
            checkArgument(instance != null, "Parameter instance must not be null!");
            this.instance = instance;
        }

        @Override
        public T getInjectedObject(final Class<?> unusedInjectionPointType) {
            return instance;
        }

        /**
         * <code>true</code> when injection target is or extends/implements
         * instance type
         * 
         * @param injectionTargetInformation
         * @return true when type is assignable from instance
         */
        protected boolean isTargetAssignable(final InjectionTargetInformation injectionTargetInformation) {
            return injectionTargetInformation.getType().isAssignableFrom(instance.getClass());
        }

        protected boolean isTargetQualifierPresent(final InjectionTargetInformation injectionTargetInformation, final Class<? extends Annotation> qualifier) {
            assertIsQualifier(qualifier);
            return injectionTargetInformation.isAnnotationPresent(qualifier);
        }
    }

    private static class DefaultInstanceInjectionProvider<T> extends InstanceInjectionProvider<T> {

        public DefaultInstanceInjectionProvider(final T instance) {
            super(instance);
        }

        @Override
        public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
            return injectionTargetInformation.getType();
        }

        @Override
        public boolean verify(final InjectionTargetInformation injectionTargetInformation) {
            return isTargetAssignable(injectionTargetInformation);
        }

    }

    private static class QualifiedInstanceInjectionProvider<T> extends InstanceInjectionProvider<T> {

        protected final Class<? extends Annotation> qualifier;

        /**
         * Constructs a provider for given @Named element encapsulating the
         * value.
         * 
         * @param name
         *            name of the interest.
         * @param value
         *            value to return.
         */
        protected QualifiedInstanceInjectionProvider(final Class<? extends Annotation> qualifier, final T instance) {
            super(instance);
            assertIsQualifier(qualifier);
            this.qualifier = qualifier;
        }

        @Override
        public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
            return String.format("%s#%s", injectionTargetInformation.getType().getCanonicalName(), qualifier.getName());
        }

        @Override
        public boolean verify(final InjectionTargetInformation injectionTargetInformation) {
            return isTargetQualifierPresent(injectionTargetInformation, qualifier) //
                    && isTargetAssignable(injectionTargetInformation);
        }

    }

    private static class NamedInstanceInjectionProvider<T> extends QualifiedInstanceInjectionProvider<T> {

        protected final String name;

        /**
         * Constructs a provider for given @Named element encapsulating the
         * value.
         * 
         * @param name
         *            name of the interest.
         * @param value
         *            value to return.
         */
        protected NamedInstanceInjectionProvider(final String name, final T instance) {
            super(Named.class, instance);
            checkArgument(name != null && !"".equals(name.trim()), "Parameter name must not be null nor empty!");
            this.name = name;
        }

        @Override
        public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
            return String.format("%s#%s", injectionTargetInformation.getType().getCanonicalName(), name);
        }

        @Override
        public boolean verify(final InjectionTargetInformation injectionTargetInformation) {
            return isTargetQualifierPresent(injectionTargetInformation, qualifier) //
                    && ((Named) injectionTargetInformation.getAnnotation(qualifier)).value().equals(name) //
                    && isTargetAssignable(injectionTargetInformation);
        }

    }
}
