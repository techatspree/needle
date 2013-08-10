package de.akquinet.jbosscc.needle.junit;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.rules.MethodRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.configuration.PropertyBasedConfigurationFactory;
import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionProviderInstancesSupplier;
import de.akquinet.jbosscc.needle.mock.MockProvider;

public class NeedleRuleBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(NeedleRuleBuilder.class);

    private Class<? extends MockProvider> withMockProviderClass;
    private InjectionProvider<?>[] injectionProvider = {};
    private Class<?>[] withAnnotations = {};
    private String configFile;
    
    private List<MethodRule> methodRuleChain = new ArrayList<MethodRule>();

    private Set<InjectionProvider<?>> provider = new HashSet<InjectionProvider<?>>();

    public NeedleRuleBuilder with(final Class<? extends MockProvider> mockProviderClass) {
        this.withMockProviderClass = mockProviderClass;
        return this;
    }

    public NeedleRuleBuilder add(final InjectionProvider<?>... injectionProvider) {
        this.injectionProvider = injectionProvider;
        return this;
    }

    public NeedleRuleBuilder with(final String configFile) {
        this.configFile = configFile;
        return this;
    }

    public NeedleRuleBuilder add(Class<?>... annotations) {
        this.withAnnotations = annotations;
        return this;
    }

    public NeedleRuleBuilder add(final InjectionProviderInstancesSupplier... supplier) {
        for (InjectionProviderInstancesSupplier injectionProviderInstancesSupplier : supplier) {
            final Set<InjectionProvider<?>> provider = injectionProviderInstancesSupplier.get();
            this.provider.addAll(provider);
        }
        return this;
    }
    
    public NeedleRuleBuilder withOuter(final MethodRule rule){
        methodRuleChain.add(0, rule);
        return this;
    }

    private NeedleConfiguration getNeedleConfiguration() {
        return configFile == null ? PropertyBasedConfigurationFactory.get() : PropertyBasedConfigurationFactory
                .get(configFile);
    }

    private Class<? extends MockProvider> getMockProviderClass(final NeedleConfiguration needleConfiguration) {
        return withMockProviderClass != null ? withMockProviderClass : needleConfiguration.getMockProviderClass();
    }

    @SuppressWarnings("unchecked")
    private Set<Class<Annotation>> getCustomInjectionAnnotations() {
        Set<Class<Annotation>> annotations = new HashSet<Class<Annotation>>();
        for (Class<?> annotationClass : withAnnotations) {
            if (annotationClass.isAnnotation()) {
                annotations.add((Class<Annotation>) annotationClass);
            } else {
                LOG.warn("ignore class {}", annotationClass);
            }
        }

        return annotations;
    }

    public NeedleRule build() {
        final NeedleConfiguration needleConfiguration = getNeedleConfiguration();
        final Class<? extends MockProvider> mockProviderClass = getMockProviderClass(needleConfiguration);

        InjectionConfiguration injectionConfiguration = new InjectionConfiguration(needleConfiguration,
                mockProviderClass);

        injectionConfiguration.addGlobalInjectionAnnotation(getCustomInjectionAnnotations());
        InjectionProvider<?>[] providerArray = provider.toArray(new InjectionProvider<?>[provider.size()]); 
        
        injectionConfiguration.addInjectionProvider(providerArray);
        NeedleRule needleRule = new NeedleRule(injectionConfiguration, injectionProvider);
        
        for (MethodRule rule : methodRuleChain) {
            needleRule.withOuter(rule);
        }
        return needleRule;
    }
}
