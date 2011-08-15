package de.akquinet.jbosscc.needle.mock;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyMockProvider extends EasyMockSupport implements MockProvider {

	private static final Logger LOG = LoggerFactory.getLogger(EasyMockProvider.class);

	private Map<Class<?>, Object> mocks = new HashMap<Class<?>, Object>();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T createMockComponent(final Class<T> type) {



		if (mocks.containsKey(type)) {

			for (Object iterable_element : mocks.values()) {
	            System.out.println(iterable_element);
            }

			Object mock = mocks.get(type);
			resetToNice(mock);
			return (T) mock;
		}

		int modifiers = type.getModifiers();
		if (Modifier.isFinal(modifiers)) {
			LOG.warn("Skipping creation of a mock : {} as it is final.", type.getSimpleName());

			return null;
		}
		T mock = createNiceMock(type);
		mocks.put(type, mock);

		return mock;

	}

	public void resetToNice(final Object mock) {
		EasyMock.resetToNice(mock);
	}

	public void resetToStrict(final Object mock) {
		EasyMock.resetToStrict(mock);
	}

	public void resetToDefault(final Object mock) {
		EasyMock.resetToDefault(mock);
	}

}
