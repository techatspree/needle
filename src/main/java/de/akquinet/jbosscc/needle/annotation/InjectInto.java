package de.akquinet.jbosscc.needle.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ FIELD })
@Retention(RUNTIME)
public @interface InjectInto {
  /**
   * (Optional)  id of the  object under test component
   * <p> Default are all declared  object under test component
   */
  String targetComponentId();

  /**
   *
   * (Optional)  fieldName of the injection target
   * <p> Default is the assignable type
   */
  String fieldName() default "";
}
