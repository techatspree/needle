package de.akquinet.jbosscc.needle.injection;

import java.lang.reflect.Field;

import javax.annotation.Resource;

import de.akquinet.jbosscc.needle.mock.MockProvider;

public class ResourceMockInjectionProvider extends DefaultMockInjectionProvider {
  public ResourceMockInjectionProvider(final MockProvider mockProvider) {
    super(Resource.class, mockProvider);
  }

  @Override
  public Object getKey(final Field field) {
    final Resource annotation = field.getAnnotation(Resource.class);

    if (annotation != null && !annotation.mappedName().equals("")) {
      return annotation.mappedName();
    }

    return super.getKey(field);
  }
}
