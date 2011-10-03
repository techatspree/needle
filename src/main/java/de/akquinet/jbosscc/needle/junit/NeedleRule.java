package de.akquinet.jbosscc.needle.junit;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import de.akquinet.jbosscc.needle.NeedleTestcase;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;

/**
 * JUnit {@link MethodRule} for the initialization of the test. The Rule process
 * and initialize all Fields annotated with {@link ObjectUnderTest}.
 *
 * <pre>
 * Example:
 *
 * public class UserDaoBeanTest {
 *
 * 	&#064;Rule
 * 	public NeedleRule needle = new NeedleRule();
 *
 * 	&#064;ObjectUnderTest
 * 	private UserDaoBean userDao;
 *
 * 	&#064;Test
 * 	public void test() {
 * 	 ...
 * 	 userDao.someAction();
 * 	 ...
 * 	}
 * }
 *
 * </pre>
 *
 * @see NeedleTestcase
 */
public class NeedleRule extends NeedleTestcase implements MethodRule {

	/**
	 * @see NeedleTestcase#NeedleTestcase(InjectionProvider...)
	 */
	public NeedleRule(final InjectionProvider<?>... injectionProvider) {
		super(injectionProvider);
	}

	/**
	 * {@inheritDoc} Before evaluation of the base statement, the test instance
	 * will initialized.
	 */
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
