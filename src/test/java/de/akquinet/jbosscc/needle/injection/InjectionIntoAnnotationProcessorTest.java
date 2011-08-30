package de.akquinet.jbosscc.needle.injection;

import static org.junit.Assert.assertSame;

import org.junit.Rule;
import org.junit.Test;

import de.akquinet.jbosscc.needle.annotation.InjectInto;
import de.akquinet.jbosscc.needle.annotation.InjectManyInto;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class InjectionIntoAnnotationProcessorTest {
  @Rule
  public NeedleRule _needleRule = new NeedleRule();

  @ObjectUnderTest
  private final UserDao _userDao1 = new UserDao();

  @ObjectUnderTest
  private final UserDao _userDao2 = new UserDao();

  @InjectManyInto({ @InjectInto(fieldName = "currentUser", targetComponent = "_userDao1"),
      @InjectInto(fieldName = "currentUser", targetComponent = "_userDao2") })
  private final User _user = new User();

  @Test
  public void testInjectMany() throws Exception {
    assertSame(_user, _userDao1.getCurrentUser());
    assertSame(_user, _userDao2.getCurrentUser());
  }
}
