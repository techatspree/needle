package de.akquinet.jbosscc.needle.injection;

import java.lang.reflect.Field;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InjectionQualifierTest {

  private final User currentUser = new User();

  private final InjectionProvider<User> currentUserprovider = new InjectionProvider<User>() {
    @Override
    public boolean verify(final Field field) {
      return field.getAnnotation(CurrentUser.class) != null;
    }

    @Override
    public Object getKey(final Field field) {
      return CurrentUser.class;
    }

    @Override
    public User getInjectedObject(final Class<?> type) {
      return currentUser;
    }
  };

  @Rule
  public NeedleRule needleRule = new NeedleRule(currentUserprovider);

  @ObjectUnderTest
  private UserDao userDao;

  @Test
  public void testInject() throws Exception {
    Assert.assertNotNull(userDao);
    Assert.assertEquals(currentUser, userDao.getCurrentUser());
    Assert.assertNotNull(userDao.getUser());
    Assert.assertNotSame(currentUser, userDao.getUser());

    Assert.assertEquals(currentUser, needleRule.getInjectedObject(CurrentUser.class));
  }

}
