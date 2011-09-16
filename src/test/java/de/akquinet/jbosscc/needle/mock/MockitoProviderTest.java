package de.akquinet.jbosscc.needle.mock;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class MockitoProviderTest {

	private MockitoProvider mockitoProvider = new MockitoProvider();

	@Test
	public void testCreateMockComponent() throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> mapMock = mockitoProvider.createMockComponent(Map.class);

		String key = "key";
		String value = "value";

		Mockito.when(mapMock.get(key)).thenReturn(value);

		Assert.assertEquals(value, mapMock.get(key));

	}

	@Test
	public void testCreateMockComponent_Final() throws Exception {
		Assert.assertNull(mockitoProvider.createMockComponent(String.class));
		Assert.assertNull(mockitoProvider.createMockComponent(double.class));

	}

}
