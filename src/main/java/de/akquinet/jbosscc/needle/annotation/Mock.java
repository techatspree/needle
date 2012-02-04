package de.akquinet.jbosscc.needle.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.akquinet.jbosscc.needle.mock.MockProvider;

/**
 * Allows shorthand mock creation with the configured {@link MockProvider}
 *
 * <pre>
 * Example:
 *
 * public void Test {
 *
 *  &#064;Rule
 *  public NeedleRule needle = new NeedleRule();
 *
 *  &#064;Mock
 *  private Queue queue;
 *
 *  &#064;Test
 *  public void test(){
 *    ...
 *  }
 * }
 * </pre>
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface Mock {

}
