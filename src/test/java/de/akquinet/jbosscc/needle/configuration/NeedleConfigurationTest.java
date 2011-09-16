package de.akquinet.jbosscc.needle.configuration;

import java.lang.annotation.Annotation;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import de.akquinet.jbosscc.needle.db.dialect.AbstractDBDialect;
import de.akquinet.jbosscc.needle.db.dialect.HSQLDialect;
import de.akquinet.jbosscc.needle.injection.CustomInjectionAnnotation1;
import de.akquinet.jbosscc.needle.injection.CustomInjectionAnnotation2;
import de.akquinet.jbosscc.needle.mock.EasyMockProvider;
import de.akquinet.jbosscc.needle.mock.MockProvider;

public class NeedleConfigurationTest {

	@Test
	public void testGetMockProviderClass_Default() throws Exception {
		Class<? extends MockProvider> mockProviderClass = NeedleConfiguration.getMockProviderClass();
		Assert.assertEquals(EasyMockProvider.class, mockProviderClass);
	}

	@Test
	public void testGetDBDialectClass_HSQDialect() throws Exception {
		Class<? extends AbstractDBDialect> dbDialectClass = NeedleConfiguration.lookupDBDialectClass(HSQLDialect.class
		        .getName());
		Assert.assertEquals(HSQLDialect.class, dbDialectClass);
	}

	@Test
	public void testGetDBDialectClass_UnknownClass() throws Exception {
		Class<? extends AbstractDBDialect> dbDialectClass = NeedleConfiguration.lookupDBDialectClass("my.class");
		Assert.assertNull(dbDialectClass);
	}

	@Test
	public void testGetCustomInjectionAnnotations() throws Exception {
		Set<Class<? extends Annotation>> customInjectionAnnotations = NeedleConfiguration
		        .getCustomInjectionAnnotations();
		Assert.assertEquals(2, customInjectionAnnotations.size());
		Assert.assertTrue(customInjectionAnnotations.contains(CustomInjectionAnnotation1.class));
		Assert.assertTrue(customInjectionAnnotations.contains(CustomInjectionAnnotation2.class));
	}

}
