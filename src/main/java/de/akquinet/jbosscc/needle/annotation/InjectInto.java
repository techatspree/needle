package de.akquinet.jbosscc.needle.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ FIELD })
@Retention(RUNTIME)
public @interface InjectInto {
  /**
   * Id of the object under test. This is the field name of the component, by default. You can specify the id within the @ObjectUnderTest
   * annotation.
   * 
   * @see ObjectUnderTest#id()
   */
  String targetComponentId();

  /**
   * (Optional) fieldName of the injection target
   * 
   * <p>
   * Default is the assignable type
   */
  String fieldName() default "";
}
