package de.akquinet.jbosscc.needle.testng;

import javax.persistence.EntityManager;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import de.akquinet.jbosscc.needle.NeedleTestcase;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;

public abstract class AbstractNeedleTestcase extends NeedleTestcase {

	private DatabaseTestcase databaseTestcase;

	public AbstractNeedleTestcase(InjectionProvider<?>... injectionProvider) {
		super(injectionProvider);

		for (InjectionProvider<?> provider : injectionProvider) {
			if (provider instanceof DatabaseTestcase) {
				databaseTestcase = (DatabaseTestcase) provider;
			}
		}
	}

	@BeforeMethod
	public final void beforeNeedleTestcase() throws Exception {
		initTestcase(this);

		if (databaseTestcase != null) {
			databaseTestcase.before();
		}
	}

	@AfterMethod
	public final void afterNeedleTestcase() throws Exception {
		if (databaseTestcase != null) {
			databaseTestcase.after();
		}
	}

	/**
	 * Returns {@link EntityManager}, if the test is constructed with a
	 * {@link DatabaseTestcase} instance.
	 *
	 * @return {@link EntityManager} or null
	 */
	protected EntityManager getEntityManager() {
		if (databaseTestcase != null) {
			return databaseTestcase.getEntityManager();
		}

		return null;
	}

}
