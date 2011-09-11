package de.akquinet.jbosscc.needle.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ FIELD })
@Retention(RUNTIME)
public @interface InjectIntoMany {
  /**
   * (Optional) the injection targets
   * <p> Default are all {@link ObjectUnderTest} annotated fields
   */
  InjectInto[] value() default {};
}
