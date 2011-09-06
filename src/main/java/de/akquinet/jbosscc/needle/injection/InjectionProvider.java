package de.akquinet.jbosscc.needle.injection;

import java.lang.reflect.Field;

public interface InjectionProvider<T> {
  T getInjectedObject(final Class<?> injectionPointType);

  boolean verify(Field field);

  Object getKey(Field field);
}
