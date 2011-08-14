package de.akquinet.jbosscc.needle.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import de.akquinet.jbosscc.needle.mock.MockProvider;

public class DefaultMockInjectionProvider implements InjectionProvider {

	private final InjectionVerifier verifyer;

	private final Class<?> type;

	private final MockProvider mockProvider;

	public DefaultMockInjectionProvider(final Class<?> type, final MockProvider mockProvider) {
		super();

		this.type = type;
		this.mockProvider = mockProvider;

		verifyer = new InjectionVerifier() {

			@SuppressWarnings("unchecked")
			@Override
			public boolean verify(Field field) {
				if (field.getType() == type
				        || (type.isAnnotation() && field.getAnnotation((Class<? extends Annotation>) type) != null)) {
					return true;
				}
				return false;
			}
		};
	}

	@Override
	public <T> T get(Class<T> type) {
		return (T) mockProvider.createMock(type);
	}

	@Override
	public boolean verify(Field field) {
		return verifyer.verify(field);
	}

	protected Class<?> getType() {
		return type;
	}

	@Override
	public Object getKey(final Field field) {
		return field.getType();
	}
}
