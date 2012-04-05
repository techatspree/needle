package de.akquinet.jbosscc.needle.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Is used to inject an instance into multiple {@link ObjectUnderTest} instances.
 *
 * <pre>
 *  Example 1:
 *
 *  &#064;InjectIntoMany
 *  private User user = new User();
 *
 *  Example 2:
 *
 *  &#064;InjectIntoMany(value = {
 *   	InjectInto(targetComponentId = "obejctUnderTest1"),
 *   	InjectInto(targetComponentId = "obejctUnderTest2", fieldName = "user")
 *  })
 *  private User user = new User();
 * </pre>
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface InjectIntoMany {
	/**
	 * (Optional) the injection targets
	 * <p>
	 * Default are all {@link ObjectUnderTest} annotated fields
	 */
	InjectInto[] value() default {};
}
