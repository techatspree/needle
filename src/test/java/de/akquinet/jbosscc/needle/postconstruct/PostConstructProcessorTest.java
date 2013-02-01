package de.akquinet.jbosscc.needle.postconstruct;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

/**
 * 
 * 
 * @author Jan Galinski, Holisticon AG (jan.galinski@holisticon.de)
 * 
 */
public class PostConstructProcessorTest {

	public static final String POSTCONSTRUCT_NAME = "javax.annotation.PostConstruct";

	/**
	 * a dummy class without init()
	 */
	public static class A {

		protected boolean postConstructed = false;

	}

	/**
	 * a dummy class with init()
	 */
	public static class B extends A {

		@PostConstruct
		public void init() {
			postConstructed = true;
		}

	}

	private final PostConstructProcessor postConstructProcessor = new PostConstructProcessor(POSTCONSTRUCT_NAME);

	// This Processor test does not use the NeeldeRule!
	@ObjectUnderTest(postConstruct = true)
	private A isConfiguredForPostConstructionButDoesNotContainMethod = new A();

	// This Processor test does not use the NeeldeRule!
	@ObjectUnderTest(postConstruct = true)
	private B isConfiguredForPostConstruction = new B();

	// This Processor test does not use the NeeldeRule!
	@ObjectUnderTest
	private B isNotConfiguredForPostConstruction = new B();

	@Before
	public void setUp() {
		assertNotNull(postConstructProcessor);
	}

	@Test
	public void shouldInitializeListOfPostConstructAnnotations() throws Exception {
		assertFalse(postConstructProcessor.getPostConstructAnnotations().isEmpty());
		assertTrue(postConstructProcessor.getPostConstructAnnotations().contains(PostConstruct.class));
	}

	@Test
	public void shouldEvaluateAnnotationConfigPostConstruct() {
		assertTrue(postConstructProcessor.isConfiguredForPostConstruct(getField("isConfiguredForPostConstruction")));
		assertFalse(postConstructProcessor.isConfiguredForPostConstruct(getField("isNotConfiguredForPostConstruction")));
	}

	private Field getField(final String name) {
		return ReflectionUtil.getField(getClass(), name);
	}

	@Test
	public void shouldFilterNoMethodsForClassA() throws Exception {
		assertTrue(postConstructProcessor.filterPostConstructMethods(isConfiguredForPostConstructionButDoesNotContainMethod).isEmpty());
	}

	@Test
	public void shouldFilterInitMethodForClassB() throws Exception {
		final List<Method> postConstructMethods = postConstructProcessor.filterPostConstructMethods(isConfiguredForPostConstruction);
		assertTrue(postConstructMethods.size() == 1);
	}

	@Test
	public void shouldNotProcessInitForA() throws Exception {
		postConstructProcessor.process(isConfiguredForPostConstructionButDoesNotContainMethod);
		assertFalse(isConfiguredForPostConstructionButDoesNotContainMethod.postConstructed);
	}

	@Test
	public void shouldProcessInitForBIfConfigured() throws Exception {
		// do not invoke
		postConstructProcessor.process(getField("isNotConfiguredForPostConstruction"), isNotConfiguredForPostConstruction);
		assertFalse(isNotConfiguredForPostConstruction.postConstructed);

		// do invoke
		postConstructProcessor.process(getField("isConfiguredForPostConstruction"), isConfiguredForPostConstruction);
		assertTrue(isConfiguredForPostConstruction.postConstructed);
	}

}
