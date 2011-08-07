package de.akquinet.jbosscc.needle.configuration;

import java.lang.annotation.Annotation;

import javax.annotation.Resource;

public class RessourceMockInjectionHandler extends DefaultMockInjectionHandler {

	@Override
	public Object getKey(Class<?> type, Annotation annotation) {

		if (annotation instanceof Resource) {
			Resource resourceAnnotation = (Resource) annotation;
			String mappedName = resourceAnnotation.mappedName();

			if (mappedName != null && !mappedName.equals("")) {
				return mappedName;
			}
		}
		return type;
	}

}
