package de.akquinet.jbosscc.needle.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
public @interface InjectInto {
	/**
	 * @return the injection target
	 */
	String targetComponent();

	/**
	 *
	 * @return fieldName of the injection target
	 */
	String fieldName();
}
