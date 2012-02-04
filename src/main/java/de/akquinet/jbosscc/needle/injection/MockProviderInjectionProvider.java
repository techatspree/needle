package de.akquinet.jbosscc.needle.injection;

import de.akquinet.jbosscc.needle.mock.MockProvider;

public class MockProviderInjectionProvider implements InjectionProvider<MockProvider> {

	private final MockProvider mockProvider;

	public MockProviderInjectionProvider(MockProvider mockProvider) {
		super();
		this.mockProvider = mockProvider;
	}

	@Override
	public MockProvider getInjectedObject(Class<?> injectionPointType) {
		return mockProvider;
	}

	@Override
	public boolean verify(InjectionTargetInformation injectionTargetInformation) {
		final Class<?> type = injectionTargetInformation.getType();
		return type.isAssignableFrom(mockProvider.getClass());
	}

	@Override
	public Object getKey(InjectionTargetInformation injectionTargetInformation) {
		return MockProvider.class;
	}

}
