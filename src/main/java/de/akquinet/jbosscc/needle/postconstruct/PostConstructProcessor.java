package de.akquinet.jbosscc.needle.postconstruct;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.akquinet.jbosscc.needle.ObjectUnderTestInstantiationException;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

/**
 * Handles execution of postConstruction methods of instances marked with @ObjectUnderTest(postProcess=true)
 * 
 * @author Jan Galinski, Holisticon AG (jan.galinski@holisticon.de)
 * 
 */
public class PostConstructProcessor {

	/**
	 * internal Container of all Annotations that trigger invokation
	 */
	private final List<Class<? extends Annotation>> postConstructAnnotations = new ArrayList<Class<? extends Annotation>>();

	/**
	 * initializes list of annotations via {@link ReflectionUtil#forName(String)}
	 * 
	 * @param postConstructFQNs
	 */
	@SuppressWarnings("unchecked")
	public PostConstructProcessor(final String... postConstructFQNs) {
		for (final String postConstructFQN : postConstructFQNs) {
			final Class<? extends Annotation> postConstructClass = (Class<? extends Annotation>) ReflectionUtil.forName(postConstructFQN);
			if (postConstructClass != null) {
				postConstructAnnotations.add(postConstructClass);
			}
		}
	}

	public List<Class<? extends Annotation>> getPostConstructAnnotations() {
		return postConstructAnnotations;
	}

	/**
	 * calls process(instance) only if field is marked with @ObjectUNderTest(postProcess=true), else ignored
	 * 
	 * @param objectUnderTestField
	 * @param instance
	 * @throws ObjectUnderTestInstantiationException
	 */
	public void process(final Field objectUnderTestField, final Object instance) throws ObjectUnderTestInstantiationException {
		if (isConfiguredForPostConstruct(objectUnderTestField)) {
			process(instance);
		}
	}

	/**
	 * invokes the annotated methods without validation of ObjectUnderTest-annotation
	 * 
	 * @param instance
	 * @throws ObjectUnderTestInstantiationException
	 */
	void process(final Object instance) throws ObjectUnderTestInstantiationException {
		for (final Method postConstructMethod : filterPostConstructMethods(instance)) {
			try {
				ReflectionUtil.invokeMethod(postConstructMethod, instance);
			} catch (final Exception e) {
				throw new ObjectUnderTestInstantiationException("error executing postConstruction method '" + postConstructMethod.getName() + "'", e);
			}
		}
	}

	/**
	 * @param instance
	 * @return all instance methods that are marked as postConstruction methods
	 */
	public List<Method> filterPostConstructMethods(final Object instance) {
		final List<Method> postConstructMethods = new ArrayList<Method>();

		for (final Method method : ReflectionUtil.getMethods(instance.getClass())) {
			for (final Class<? extends Annotation> postConstructAnnotation : postConstructAnnotations) {
				if (method.getAnnotation(postConstructAnnotation) != null) {
					postConstructMethods.add(method);
				}
			}

		}
		return postConstructMethods;
	}

	/**
	 * @param objectUnderTestField
	 * @return <code>true</code> if postConstruct is activated. Default is <code>false</code>
	 */
	public boolean isConfiguredForPostConstruct(final Field objectUnderTestField) {
		return objectUnderTestField.getAnnotation(ObjectUnderTest.class).postConstruct();
	}
}
