package de.akquinet.jbosscc.needle.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import de.akquinet.jbosscc.needle.mock.MockProvider;

public class DefaultMockInjectionProvider implements InjectionProvider<Object> {
  private final InjectionVerifier verifyer;

  private final Class<?> type;

  private final MockProvider mockProvider;

  public DefaultMockInjectionProvider(final Class<?> type, final MockProvider mockProvider) {
    super();

    this.type = type;
    this.mockProvider = mockProvider;

    verifyer = new InjectionVerifier() {

      @SuppressWarnings("unchecked")
      @Override
      public boolean verify(final Field field) {
        if (field.getType() == type || (type.isAnnotation() && field.getAnnotation((Class<? extends Annotation>) type) != null)) {
          return true;
        }
        return false;
      }
    };
  }

  @Override
  public Object getInjectedObject(final Class<?> type) {
    return mockProvider.createMockComponent(type);
  }

  @Override
  public boolean verify(final Field field) {
    return verifyer.verify(field);
  }

  protected Class<?> getType() {
    return type;
  }

  @Override
  public Object getKey(final Field field) {
    return field.getType();
  }
}
