package de.akquinet.jbosscc.needle.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InjectionTargetInformation {

	private final Class<?> type;

	private final AccessibleObject accessibleObject;

	private final Annotation[] parameterAnnotations;

	private InjectionTargetInformation(final Class<?> type, final AccessibleObject accessibleObject,
	        final Annotation... parameterAnnotations) {
		this.type = type;
		this.accessibleObject = accessibleObject;
		this.parameterAnnotations = parameterAnnotations;
	}

	public InjectionTargetInformation(final Class<?> type, final Field field) {
		this(type, (AccessibleObject) field);

	}

	public InjectionTargetInformation(final Class<?> type, final Method method, final Annotation[] parameterAnnotations) {
		this(type, (AccessibleObject) method, parameterAnnotations);
	}

	public InjectionTargetInformation(final Class<?> type, final Constructor<?> constructor,
	        final Annotation[] parameterAnnotations) {
		this(type, (AccessibleObject) constructor, parameterAnnotations);
	}

	public Class<?> getType() {
		return type;
	}

	public Annotation[] getAnnotations() {
		final Annotation[] accessibleObjectAnnotations = accessibleObject.getAnnotations();
		final Annotation[] annotations = new Annotation[accessibleObjectAnnotations.length
		        + parameterAnnotations.length];

		System.arraycopy(accessibleObjectAnnotations, 0, annotations, 0, accessibleObjectAnnotations.length);
		System.arraycopy(parameterAnnotations, 0, annotations, accessibleObjectAnnotations.length,
		        parameterAnnotations.length);

		return annotations;
	}

	public AccessibleObject getAccessibleObject() {
		return accessibleObject;
	}

	public boolean isAnnotationPresent(final Class<? extends Annotation> annotationClass) {
		return getAnnotation(annotationClass) != null ? true : false;
	}

	@SuppressWarnings("unchecked")
	public <T> T getAnnotation(final Class<? extends Annotation> annotationClass) {
		Annotation annotation = accessibleObject.getAnnotation(annotationClass);
		if (annotation != null) {
			return (T) annotation;
		}

		for (Annotation parameterAnnoation : parameterAnnotations) {
			if (parameterAnnoation.annotationType() == annotationClass) {
				return (T) parameterAnnoation;
			}
		}

		return null;

	}

}
