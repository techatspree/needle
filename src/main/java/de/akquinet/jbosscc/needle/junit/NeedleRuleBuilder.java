package de.akquinet.jbosscc.needle.junit;

import de.akquinet.jbosscc.needle.configuration.NeedleConfiguration;
import de.akquinet.jbosscc.needle.configuration.PropertyBasedConfigurationFactory;
import de.akquinet.jbosscc.needle.injection.InjectionConfiguration;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.mock.MockProvider;

public class NeedleRuleBuilder {

    private Class<? extends MockProvider> withMockProviderClass;
    private InjectionProvider<?>[] injectionProvider;
    private String configFile;

    public NeedleRuleBuilder with(final Class<? extends MockProvider> mockProviderClass) {
        this.withMockProviderClass = mockProviderClass;
        return this;
    }

    public NeedleRuleBuilder add(final InjectionProvider<?>... injectionProvider) {
        this.injectionProvider = injectionProvider;
        return this;
    }
    
    public NeedleRuleBuilder with(final String configFile){
        this.configFile = configFile;
        return this;
    }
    
    private NeedleConfiguration getNeedleConfiguration(){
        return configFile == null ? PropertyBasedConfigurationFactory.get() : PropertyBasedConfigurationFactory.get(configFile); 
    }
    
    private Class<? extends MockProvider> getMockProviderClass(final NeedleConfiguration needleConfiguration){
        return withMockProviderClass != null ? withMockProviderClass : needleConfiguration.getMockProviderClass();
        
    }

    public NeedleRule build() {
        final NeedleConfiguration needleConfiguration = getNeedleConfiguration();
        final Class<? extends MockProvider> mockProviderClass = getMockProviderClass(needleConfiguration);
        
        InjectionConfiguration injectionConfiguration = new InjectionConfiguration(needleConfiguration, mockProviderClass);
        

        return new NeedleRule(injectionConfiguration);
    }

}
