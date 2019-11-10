package objectRepository.common;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import common.driverManager.DriverFactory;
import common.seleniumExceptionHandling.SeleniumMethods;

public class FindAFlight {
	
	private SeleniumMethods com;

	@FindBy(linkText = "SIGN-OFF")
	private WebElement link_Logout;
	
	public static String title="Find a Flight: Mercury Tours:";
	public FindAFlight(){
		PageFactory.initElements(DriverFactory.getDriver(), this);
		com = new SeleniumMethods();
	}
	public void logout() {
		com.click(link_Logout);
	}
	
}
