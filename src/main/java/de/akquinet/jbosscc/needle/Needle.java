package de.akquinet.jbosscc.needle;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class Needle extends BlockJUnit4ClassRunner {

	private NeedleRule needleRule = new NeedleRule();

	public Needle(Class<?> testClass) throws InitializationError {

		super(testClass);

	}

}
