package de.akquinet.jbosscc.needle.configuration;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

public class InjectionTypes {

	private static final Map<Class<? extends Annotation>, MockInjectionHandler> INJECTION_ANNOTATION_FILTER = new HashMap<Class<? extends Annotation>, MockInjectionHandler>();
	private static final Map<Class<?>, MockInjectionHandler> INJECTION_TYPE_FILTER = new HashMap<Class<?>, MockInjectionHandler>();

	private static final MockInjectionHandler DEFAULT_INJECTION_HANDLER = new DefaultMockInjectionHandler();

	public static <A extends Annotation> void addAnnotation(final Class<A> annotation,
	        final MockInjectionHandler injectionHandler) {
		INJECTION_ANNOTATION_FILTER.put(annotation, injectionHandler);
	}

	public static <A extends Annotation> void removeAnnotation(final Class<A> annotation) {
		INJECTION_ANNOTATION_FILTER.remove(annotation);
	}

	public static void addType(final Class<?> type, final MockInjectionHandler injectionHandler) {
		INJECTION_TYPE_FILTER.put(type, injectionHandler);
	}

	public static void removeType(final Class<?> type) {
		INJECTION_TYPE_FILTER.remove(type);
	}

	static {

        Class<? extends Annotation> ejb = (Class<? extends Annotation>) getClass("javax.ejb.EJB");

        if(ejb != null){
        	addAnnotation(ejb, DEFAULT_INJECTION_HANDLER);
        }

        Class<? extends Annotation> resource = (Class<? extends Annotation>) getClass("javax.annotation.Resource");

        if(resource != null){
        	addAnnotation(resource, new RessourceMockInjectionHandler());

        }
	}



	private static Class<?> getClass(final String className){
		try {
	        return Class.forName(className);
        } catch (ClassNotFoundException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }

        return null;
	}

	public static MockInjectionHandler getTypeHandler(Class<?> type) {
		return INJECTION_TYPE_FILTER.get(type);
	}

	public static MockInjectionHandler getAnnotationHandler(Class<? extends Annotation> type) {
		return INJECTION_ANNOTATION_FILTER.get(type);
	}

}
