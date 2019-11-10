package objectRepository.common;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import common.configData_Util.Constant;
import common.driverManager.DriverFactory;
import common.seleniumExceptionHandling.SeleniumMethods;
import common.xlUtil.DataTable_NonStatic;

public class LoginPage {

	private SeleniumMethods com;

	@FindBy(name="userName")
	private WebElement text_UserName;

	@FindBy(name="password")
	private WebElement text_Password;

	@FindBy(name="login")
	private WebElement button_Login;

	

	public static String title="Welcome: Mercury Tours";
	public LoginPage(){
		com=new SeleniumMethods();
		PageFactory.initElements(DriverFactory.getDriver(), this);
	}

	
	public boolean performLogin(){
		DataTable_NonStatic obj= new DataTable_NonStatic(Constant.getTestDataFilePath(), Constant.getEnvironmentInfoSheet());
		return fillUserNameAndPassword(obj.getValue(1, "username"), obj.getValue(1, "password"));
	}
	
	private boolean fillUserNameAndPassword(String user, String pass){
		boolean bool=false;
		com.sendKeys(text_UserName,user);
		com.sendKeys(text_Password,pass);
		com.click(button_Login);

		if(com.verifyPageTitle(FindAFlight.title,true)){
			bool=true;
		}
		return bool;
	}


}
