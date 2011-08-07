package de.akquinet.jbosscc.needle.mock;

import org.easymock.EasyMock;

public class EasyMockProvider implements MockProvider {

	@Override
	public <T> T createMock(Class<T> type) {
		return EasyMock.createNiceMock(type);
	}

}
