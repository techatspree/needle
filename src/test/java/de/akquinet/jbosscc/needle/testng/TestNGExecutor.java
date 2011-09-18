package de.akquinet.jbosscc.needle.testng;

import java.util.List;

import org.junit.Test;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

public class TestNGExecutor {

	@Test
	public void testTestNG() throws Exception {

		TestListenerAdapter testListener = new TestListenerAdapter();

		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { NeedleTestNGTest.class });
		testng.addListener(testListener);
		testng.run();

		if (!testListener.getFailedTests().isEmpty()) {
			StringBuilder report = new StringBuilder();
			List<ITestResult> failedTests = testListener.getFailedTests();
			for (ITestResult iTestResult : failedTests) {
				report.append(iTestResult.getThrowable()).append("\n").append(iTestResult).append("\n");

			}
			throw new AssertionError(report.toString());

		}

	}

}
