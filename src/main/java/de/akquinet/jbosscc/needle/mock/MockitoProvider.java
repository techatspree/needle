package de.akquinet.jbosscc.needle.mock;

import java.lang.reflect.Modifier;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockitoProvider implements MockProvider {
	private static final Logger LOG = LoggerFactory.getLogger(MockitoProvider.class);

	@Override
	public <T> T createMockComponent(Class<T> type) {
		if (Modifier.isFinal(type.getModifiers()) || type.isPrimitive()) {
			LOG.warn("Skipping creation of a mock : {} as it is final or primitive type.", type.getSimpleName());
			return null;
		}

		return Mockito.mock(type);
	}

}
