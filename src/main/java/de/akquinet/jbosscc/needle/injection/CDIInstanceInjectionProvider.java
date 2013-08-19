package de.akquinet.jbosscc.needle.injection;

import de.akquinet.jbosscc.needle.mock.MockProvider;

public class CDIInstanceInjectionProvider extends DefaultMockInjectionProvider {

    public CDIInstanceInjectionProvider(final Class<?> type, final MockProvider mockProvider) {
        super(type, mockProvider);
    }

    @Override
    public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
        return injectionTargetInformation.getGenericTypeParameter();
    }
}
