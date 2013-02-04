package de.akquinet.jbosscc.needle.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.akquinet.jbosscc.needle.NeedleTestcase;

/**
 * Is used to specify an object under test. The annotated fields will be
 * initialized by the {@link NeedleTestcase}.
 * 
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface ObjectUnderTest {

    /**
     * (Optional) The implementation class of the object under test.
     * <p>
     * Default is the field type.
     */
    Class<?> implementation() default Void.class;

    /**
     * (Optional) The id of the object under test.
     * <p>
     * Default is the field name.
     */
    String id() default "";

    /**
     * (Optional) execute @PostConstruct method of a class under test
     * <p>
     * Note: Behavior in an inheritance hierarchy is not defined by the common
     * annotations specification
     * </p>
     * <p>
     * Default is false
     * 
     */
    boolean postConstruct() default false;
}
