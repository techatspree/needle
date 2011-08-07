package de.akquinet.jbosscc.needle.annotation;

import java.lang.annotation.Annotation;

public interface AnnotationProcessor<T extends Annotation> {
	void process(Object instance);
}
