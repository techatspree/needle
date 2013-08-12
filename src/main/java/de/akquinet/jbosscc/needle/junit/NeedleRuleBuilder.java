package de.akquinet.jbosscc.needle.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.rules.MethodRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.mock.MockProvider;

public class NeedleRuleBuilder extends AbstractNeedleRuleBuilder<NeedleRuleBuilder, NeedleRule> {

    static final Logger LOG = LoggerFactory.getLogger(NeedleRuleBuilder.class);

    private final List<MethodRule> methodRuleChain = new ArrayList<MethodRule>();

    public AbstractNeedleRuleBuilder<NeedleRuleBuilder, NeedleRule> withOuter(final MethodRule rule) {
        methodRuleChain.add(0, rule);
        return this;
    }

    @Override
    public NeedleRule build() {
        final NeedleConfiguration needleConfiguration = getNeedleConfiguration();
        final Class<? extends MockProvider> mockProviderClass = getMockProviderClass(needleConfiguration);

        InjectionConfiguration injectionConfiguration = new InjectionConfiguration(needleConfiguration,
                mockProviderClass);

        injectionConfiguration.addGlobalInjectionAnnotation(getCustomInjectionAnnotations());
        InjectionProvider<?>[] providerArray = providers.toArray(new InjectionProvider<?>[providers.size()]);
        
        injectionConfiguration.addInjectionProvider(providerArray);
        NeedleRule needleRule = new NeedleRule(injectionConfiguration, injectionProvider);
        
        for (MethodRule rule : methodRuleChain) {
            needleRule.withOuter(rule);
        }
        return needleRule;
    }
}
