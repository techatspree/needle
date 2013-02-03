package de.akquinet.jbosscc.needle.injection;

import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InjectionPriorityTest {

  private final Map<Object, Object> map = new HashMap<Object, Object>();

  private final InjectionProvider<Map<Object, Object>> injectionProvider = new CustomMapInjectionProvider() {
    @Override
    public Map<Object, Object> getInjectedObject(final java.lang.Class<?> injectionPointType) {
      return map;
    };
  };

  @Rule
  public NeedleRule needleRule = new NeedleRule(injectionProvider);

  @ObjectUnderTest
  private CustomInjectionTestComponent component;

  @Test
  public void testInjectionProviderPriority() throws Exception {
    assertSame(map, component.getMap());
  }

}
