package de.akquinet.jbosscc.needle.configuration;

import java.lang.annotation.Annotation;

public class DefaultMockInjectionHandler implements MockInjectionHandler {

    @Override
    public <T> T create(Class<T> type) {
	    return (T) NeedleConfiguration.getMockProvider().createMock(type);
    }

	@Override
    public Object getKey(Class<?> type, Annotation annotation) {
	    return type;
    }







}
