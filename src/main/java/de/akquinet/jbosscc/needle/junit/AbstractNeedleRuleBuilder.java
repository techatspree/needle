package de.akquinet.jbosscc.needle.junit;

import static de.akquinet.jbosscc.needle.injection.InjectionProviders.providersToSet;
import static de.akquinet.jbosscc.needle.injection.InjectionProviders.providersForInstancesSuppliers;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.common.Builder;
import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.configuration.PropertyBasedConfigurationFactory;
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
public abstract class AbstractNeedleRuleBuilder<B, R> implements Builder<R> {

    private final Logger logger = LoggerFactory.getLogger(AbstractNeedleRuleBuilder.class);

    protected Class<? extends MockProvider> mockProviderClass;
    protected InjectionProvider<?>[] injectionProvider = {};
    protected Class<?>[] withAnnotations = {};
    protected String configFile;
    protected final Set<InjectionProvider<?>> providers = new HashSet<InjectionProvider<?>>();

    public B with(final String configFile) {
        this.configFile = configFile;
        return (B) this;
    }

    public B with(final Class<? extends MockProvider> mockProviderClass) {
        this.mockProviderClass = mockProviderClass;
        return (B) this;
    }

    public B add(final InjectionProvider<?>... injectionProvider) {
        this.injectionProvider = injectionProvider;
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

    protected NeedleConfiguration getNeedleConfiguration() {
        return configFile == null ? PropertyBasedConfigurationFactory.get() : PropertyBasedConfigurationFactory
                .get(configFile);
    }

    protected Class<? extends MockProvider> getMockProviderClass(final NeedleConfiguration needleConfiguration) {
        return mockProviderClass != null ? mockProviderClass : needleConfiguration.getMockProviderClass();
    }

    protected Set<Class<Annotation>> getCustomInjectionAnnotations() {
        Set<Class<Annotation>> annotations = new HashSet<Class<Annotation>>();
        for (Class<?> annotationClass : withAnnotations) {
            if (annotationClass.isAnnotation()) {
                annotations.add((Class<Annotation>) annotationClass);
            } else {
                logger.warn("ignore class {}", annotationClass);
            }
        }
    
        return annotations;
    }

}
