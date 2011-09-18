package de.akquinet.jbosscc.needle.mock;

import java.lang.reflect.Modifier;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Mockito specific {@link MockProvider} implementation. For more details, see
 * the Mockito documentation.
 *
 */
public class MockitoProvider implements MockProvider {
	private static final Logger LOG = LoggerFactory.getLogger(MockitoProvider.class);

	/**
	 * {@inheritDoc} Skipping creation, if the type is final or primitive.
	 *
	 * @return the mock object or null, if the type is final or primitive.
	 */
	@Override
	public <T> T createMockComponent(Class<T> type) {
		if (Modifier.isFinal(type.getModifiers()) || type.isPrimitive()) {
			LOG.warn("Skipping creation of a mock : {} as it is final or primitive type.", type.getSimpleName());
			return null;
		}

		return Mockito.mock(type);
	}

}
