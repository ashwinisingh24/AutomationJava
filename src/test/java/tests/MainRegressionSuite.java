package tests;

import org.testng.annotations.Test;

import common.configData_Util.Constant;
import common.customReporting.CustomReporter;
import objectRepository.common.FindAFlight;
import objectRepository.common.LoginPage;

public class MainRegressionSuite {
	/**@author some Tester*/
	@Test(description="DemoTest")
	public void DemoTest() 
	{
		LoginPage login= new LoginPage();
		
		if(login.performLogin())
		{
			FindAFlight homePage=new FindAFlight();
			CustomReporter.report(Constant.INFO, "Logging out");
			homePage.logout();
		}
	}
}
