package de.akquinet.jbosscc.needle.mock;

import java.lang.reflect.Modifier;

import org.easymock.EasyMockSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyMockProvider extends EasyMockSupport implements MockProvider {

	private static final Logger LOG = LoggerFactory.getLogger(EasyMockProvider.class);

	@Override
	public <T> T createMock(Class<T> type) {
		int modifiers = type.getModifiers();
		if (Modifier.isFinal(modifiers)) {
			LOG.warn("Skipping creation of a mock : {} as it is final.", type.getSimpleName());

			return null;
		}

		return createNiceMock(type);
	}

	public static EasyMockProvider getInstance(final MockProvider mockProvider) {
		return (EasyMockProvider) mockProvider;
	}

}
