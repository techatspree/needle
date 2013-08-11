package de.akquinet.jbosscc.needle.injection;

import de.akquinet.jbosscc.needle.mock.MockProvider;

public class CDIInstanceInjectionProvider extends DefaultMockInjectionProvider {

    public CDIInstanceInjectionProvider(Class<?> type, MockProvider mockProvider) {
        super(type, mockProvider);
    }

    @Override
    public Object getKey(InjectionTargetInformation injectionTargetInformation) {
        return injectionTargetInformation.getGenericTypeParamerter();
    }
}
