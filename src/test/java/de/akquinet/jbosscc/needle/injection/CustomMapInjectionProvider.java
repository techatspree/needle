package de.akquinet.jbosscc.needle.injection;

import java.util.HashMap;
import java.util.Map;

public class CustomMapInjectionProvider implements InjectionProvider<Map<Object, Object>> {

  public static final Map<Object, Object> MAP = new HashMap<Object, Object>();

  @Override
  public Map<Object, Object> getInjectedObject(final Class<?> injectionPointType) {
    return MAP;
  }

  @Override
  public boolean verify(final InjectionTargetInformation injectionTargetInformation) {
    if (injectionTargetInformation.getType() == Map.class) {
      return true;
    }
    return false;
  }

  @Override
  public Object getKey(final InjectionTargetInformation injectionTargetInformation) {
    return injectionTargetInformation.getType();
  }

}
