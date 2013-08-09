package de.akquinet.jbosscc.needle.injection;


import java.util.LinkedHashSet;
import java.util.Set;

import de.akquinet.jbosscc.needle.NeedleTestcase;

/**
 * <a href="http://javadocs.techempower.com/jdk18/api/java/util/function/Supplier.html">Supplies</a> a Set of
 * InjectionProvider instances.
 * @author Jan Galinski, Holisticon AG
 */
public interface InjectionProviderInstancesSupplier {

    /**
     * Factory to transform InjectionProviders to Supplier and vice versa.
     */
    public static class Factory {

        /**
         * Hide constructor for util class.
         */
        private Factory() {
            // empty
        }

        /**
         * Creates a new Set.
         * @param providers vararg array of providers
         * @return set containing providers
         */
        public static Set<InjectionProvider<?>> newProviderSet(final InjectionProvider<?>... providers) {
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
         * @param providers vararg array of providers
         * @return new supplier
         */
        public static InjectionProviderInstancesSupplier createSupplierFor(final InjectionProvider<?>... providers) {
            return new InjectionProviderInstancesSupplier() {

                @Override
                public Set<InjectionProvider<?>> get() {
                    return newProviderSet(providers);
                }
            };
        }

        /**
         * Creates new supplier containing all providers in a new set.
         * @param suppliers vararg array of existing suppliers
         * @return new instance containing all providers
         */
        public static InjectionProviderInstancesSupplier merge(final InjectionProviderInstancesSupplier... suppliers) {
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
         * @param suppliers vararg array of suppliers
         * @return array of providers for use with vararg method
         */
        public static InjectionProvider<?>[] createProvidersFor(final InjectionProviderInstancesSupplier... suppliers) {
            final InjectionProviderInstancesSupplier supplier = merge(suppliers);
            return supplier.get().toArray(new InjectionProvider<?>[supplier.get().size()]);
        }
    }

    /**
     * <a href="http://javadocs.techempower.com/jdk18/api/java/util/function/Supplier.html">Supplies</a> a Set of
     * InjectionProvider instances.
     * @return InjectionProviders that can be added to {@link NeedleTestcase}
     */
    Set<InjectionProvider<?>> get();
}
