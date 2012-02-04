package de.akquinet.jbosscc.needle.mock;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.Mock;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class MockAnnotationTest {

	@Rule
	public NeedleRule needleRule = new NeedleRule();

	@Mock
	private EntityManager entityManagerMock;


	@Test
	public void testMockAnnotation() throws Exception {
		Assert.assertNotNull(entityManagerMock);
    }
}
