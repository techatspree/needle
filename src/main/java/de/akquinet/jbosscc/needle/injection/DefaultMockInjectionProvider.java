package de.akquinet.jbosscc.needle.injection;

import java.lang.annotation.Annotation;

import de.akquinet.jbosscc.needle.mock.MockProvider;

public class DefaultMockInjectionProvider implements InjectionProvider<Object> {

	private final Class<?> type;

	private final MockProvider mockProvider;

	public DefaultMockInjectionProvider(final Class<?> type, final MockProvider mockProvider) {
		super();

		this.type = type;
		this.mockProvider = mockProvider;
	}

	@Override
	public Object getInjectedObject(final Class<?> type) {
		return mockProvider.createMockComponent(type);
	}

	@SuppressWarnings("unchecked")
    @Override
	public boolean verify(final InjectionTargetInformation injectionTargetInformation) {

		if (injectionTargetInformation.getType() == type
		        || (type.isAnnotation() && injectionTargetInformation
		                .isAnnotationPresent((Class<? extends Annotation>) type))) {
			return true;
		}
		return false;
	}

	protected Class<?> getType() {
		return type;
	}

	@Override
	public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
		return injectionTargetInformation.getType();
	}
}
