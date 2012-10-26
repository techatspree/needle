package de.akquinet.jbosscc.needle.postconstruct;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.akquinet.jbosscc.needle.ObjectUnderTestInstantiationException;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

public class PostConstructProcessor {

	private final List<Class<? extends Annotation>> postConstructAnnotations = new ArrayList<Class<? extends Annotation>>();

	public PostConstructProcessor(final String... postConstructFQNs) {
		for (final String postConstructFQN : postConstructFQNs) {
			@SuppressWarnings("unchecked")
			final Class<? extends Annotation> postConstructClass = (Class<? extends Annotation>) ReflectionUtil.forName(postConstructFQN);
			if (postConstructClass != null) {
				postConstructAnnotations.add(postConstructClass);
			}
		}
	}

	public List<Class<? extends Annotation>> getPostConstructAnnotations() {
		return postConstructAnnotations;
	}

	public void process(final Field objectUnderTestField, final Object instance) throws ObjectUnderTestInstantiationException {
		if (isConfiguredForPostConstruct(objectUnderTestField)) {
			process(instance);
		}
	}

	void process(final Object instance) throws ObjectUnderTestInstantiationException {
		for (final Method postConstructMethod : filterPostConstructMethods(instance)) {
			try {
				ReflectionUtil.invokeMethod(postConstructMethod, instance);
			} catch (final Exception e) {
				throw new ObjectUnderTestInstantiationException("error executing postConstruction method '" + postConstructMethod.getName() + "'", e);
			}
		}
	}

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

	public boolean isConfiguredForPostConstruct(final Field objectUnderTestField) {
		return objectUnderTestField.getAnnotation(ObjectUnderTest.class).postConstruct();
	}
}
