package de.akquinet.jbosscc.needle.junit;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import de.akquinet.jbosscc.needle.NeedleTestcase;
import de.akquinet.jbosscc.needle.db.DatabaseTestcase;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;

public class NeedleRule extends NeedleTestcase implements MethodRule {

  public NeedleRule(final DatabaseTestcase databaseTestcase, final InjectionProvider... injectionProvider) {
    super(databaseTestcase, injectionProvider);
  }

  public NeedleRule(final InjectionProvider... injectionProvider) {
    super(injectionProvider);
  }

  @Override
  public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        initTestcase(target);
        base.evaluate();
      }
    };
  }
}
