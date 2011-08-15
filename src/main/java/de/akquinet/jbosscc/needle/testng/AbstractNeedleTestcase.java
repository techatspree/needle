package de.akquinet.jbosscc.needle.testng;

import javax.persistence.EntityManager;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import de.akquinet.jbosscc.needle.NeedleTestcase;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;

public abstract class AbstractNeedleTestcase extends NeedleTestcase {

	private DatabaseTestcase databaseTestcase;

	public AbstractNeedleTestcase(DatabaseTestcase databaseTestcase, InjectionProvider... injectionProvider) {

		super(databaseTestcase, injectionProvider);
		this.databaseTestcase = databaseTestcase;
	}

	public AbstractNeedleTestcase(InjectionProvider... injectionProvider) {
		super(injectionProvider);
	}

	@BeforeMethod
	public final void beforeNeedleTestcase() throws Exception {
		initTestcase(this);
	}

	@AfterMethod
	public final void afterNeedleTestcase() throws Exception {
		if (databaseTestcase != null) {
			databaseTestcase.after();
		}
	}

	protected EntityManager getEntityManager() {
		if (databaseTestcase != null) {
			return databaseTestcase.getEntityManager();
		}

		return null;
	}

}
