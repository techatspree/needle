package de.akquinet.jbosscc.needle.configuration;

import java.lang.annotation.Annotation;

public interface MockInjectionHandler {

	 <T> T create(final Class<T> type);

	 Object getKey(final Class<?> type, final Annotation annotation);
}
