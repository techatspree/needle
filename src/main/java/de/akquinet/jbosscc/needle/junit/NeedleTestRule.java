package de.akquinet.jbosscc.needle.junit;

import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import de.akquinet.jbosscc.needle.NeedleTestcase;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;

/**
 * JUnit {@link TestRule} for the initialization of the test. The Rule processes
 * and initializes all fields annotated with {@link ObjectUnderTest}.
 * <p/>
 * This is an updated Rule to reflect the API change (MethodRule vs. TestRule) in junit. Using this TestRule
 * implementation has the drawback that the calling test-instance has to be passed when the Rule is created, since the
 * new junit api does not pass the caller to the statement execution.<br/>
 * Using this Rule enables the {@link RuleChain}s feature of junit >= 4.10.
 * 
 * <pre>
 * Example:
 * 
 * public class UserDaoBeanTest {
 * 
 *  &#064;Rule
 *  public final NeedleTestRule needle = new NeedleTestRule(this);
 * 
 *  &#064;ObjectUnderTest
 *  private UserDaoBean userDao;
 * 
 *  &#064;Test
 *  public void test() {
 *   ...
 *   userDao.someAction();
 *   ...
 *  }
 * }
 * 
 * </pre>
 * 
 * @author Jan Galinski, Holisticon AG (jan.galinski@holisticon.de)
 * @see NeedleRule
 * @see NeedleTestcase
 */
public class NeedleTestRule extends NeedleTestcase implements TestRule {

    private final Object testInstance;

    /**
     * @param testInstance - target of injection
     */
    public NeedleTestRule(final Object testInstance) {
        this.testInstance = testInstance;
    }

    /**
     * @param testInstance - target of injection
     * @see NeedleTestcase#NeedleTestcase(InjectionProvider...)
     */
    public NeedleTestRule(final Object testInstance, final InjectionProvider<?>... injectionProvider) {
        super(injectionProvider);
        this.testInstance = testInstance;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                initTestcase(testInstance);
            }
        };
    }
}
