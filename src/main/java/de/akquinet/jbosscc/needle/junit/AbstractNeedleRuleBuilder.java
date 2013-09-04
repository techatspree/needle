package de.akquinet.jbosscc.needle.junit;

import static de.akquinet.jbosscc.needle.injection.InjectionProviders.providersForInstancesSuppliers;
import static de.akquinet.jbosscc.needle.injection.InjectionProviders.providersToArray;
import static de.akquinet.jbosscc.needle.injection.InjectionProviders.providersToSet;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.NeedleTestcase;
import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionProviderInstancesSupplier;
import de.akquinet.jbosscc.needle.mock.MockProvider;

/**
 * 
 * 
 * @param <B>
 *            type of builder, needed for type-safe "return this"
 * @param <R>
 *            type of rule to build
 */
@SuppressWarnings("unchecked")
public abstract class AbstractNeedleRuleBuilder<B, R extends NeedleTestcase> extends AbstractRuleBuilder<B, R> {

    private final Logger logger = LoggerFactory.getLogger(AbstractNeedleRuleBuilder.class);

    private Class<? extends MockProvider> mockProviderClass;
    private Class<?>[] withAnnotations = {};
    private final Set<InjectionProvider<?>> providers = new HashSet<InjectionProvider<?>>();

    public B with(final Class<? extends MockProvider> mockProviderClass) {
        this.mockProviderClass = mockProviderClass;
        return (B) this;
    }

    public B add(final InjectionProvider<?>... injectionProviders) {
        providers.addAll(providersToSet(injectionProviders));
        return (B) this;
    }

    public B add(final Class<?>... annotations) {
        this.withAnnotations = annotations;
        return (B) this;
    }

    public B add(final InjectionProviderInstancesSupplier... suppliers) {
        this.providers.addAll(providersToSet(providersForInstancesSuppliers(suppliers)));
        return (B) this;
    }

    private Class<? extends MockProvider> getMockProviderClass(final NeedleConfiguration needleConfiguration) {
        return mockProviderClass != null ? mockProviderClass : InjectionConfiguration
                .lookupMockProviderClass(needleConfiguration.getMockProviderClassName());
    }

    private Set<Class<Annotation>> getCustomInjectionAnnotations() {
        final Set<Class<Annotation>> annotations = new HashSet<Class<Annotation>>();
        for (final Class<?> annotationClass : withAnnotations) {
            if (annotationClass.isAnnotation()) {
                annotations.add((Class<Annotation>) annotationClass);
            } else {
                logger.warn("ignore class {}", annotationClass);
            }
        }

        return annotations;
    }

    @Override
    protected final R build(final NeedleConfiguration needleConfiguration) {
        final Class<? extends MockProvider> mockProviderClass = getMockProviderClass(needleConfiguration);

        final InjectionConfiguration injectionConfiguration = new InjectionConfiguration(needleConfiguration,
                mockProviderClass);

        injectionConfiguration.addGlobalInjectionAnnotation(getCustomInjectionAnnotations());

        return build(injectionConfiguration, providersToArray(providers));
    }

    protected abstract R build(final InjectionConfiguration injectionConfiguration,
            final InjectionProvider<?>... injectionProvider);

}
