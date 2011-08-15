package de.akquinet.jbosscc.needle.junit;

import junit.framework.AssertionFailedError;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import de.akquinet.jbosscc.needle.NeedleTestcase;
import de.akquinet.jbosscc.needle.db.DatabaseTestcase;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;

public class NeedleRule extends NeedleTestcase implements MethodRule {


	public NeedleRule(DatabaseTestcase databaseTestcase, InjectionProvider... injectionProvider) {
	    super(databaseTestcase, injectionProvider);
    }

	public NeedleRule(InjectionProvider... injectionProvider) {
		super(injectionProvider);

		System.out.println("new Needle Rule");
    }

	@Override
	public Statement apply(Statement base, FrameworkMethod method, Object target) {
		try {
			initTestcase(target);
		} catch (Exception e) {
			throw new AssertionFailedError(e.getMessage());
		}
		return base;
	}

}
