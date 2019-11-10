package common.seleniumExceptionHandling;

import common.customReporting.SnapshotManager;
import common.driverManager.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

class WebUtils {

	/*
	 * @return webelement object
	 * @param element WebElement or By object
	 * This method takes WebElement or By object as
	 *         an input and then returns a WebElement object
	 */
	WebElement getWebElement(Object element) {
		WebElement elem = null;
		WebDriver driver = DriverFactory.getDriver();
		if (element instanceof By) {
			By byObj = (By) element;
			elem = driver.findElement(byObj);
		} else if (element instanceof WebElement) {
			elem = (WebElement) element;
		}
		return elem;
	}

	/**
	 * Pause the current Thread execution.
	 * 
	 * @param seconds
	 *            The double seconds to pause the thread for fraction values
	 *            like 0.5 or 0.75 seconds.
	 */
	public void wait(double seconds) {
		int val = (int) (seconds * 1000);
		ThreadSleep(val);
	}

	/**
	 * Pause the current Thread execution.
	 * 
	 * @param seconds
	 *            The int seconds to pause the thread for whole number values
	 *            like 1 or 5 seconds.
	 */
	public void wait(int seconds) {
		int val = seconds * 1000;
		ThreadSleep(val);
	}

	private void ThreadSleep(int microseconds) {
		try {
			Thread.sleep(microseconds);
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
	}


	/**
	 * Will perform java script click on the passed element Object
	 * 
	 * @param element
	 *            A WebElement/By object
	 * 
	 */
	public void javaScriptClick(Object element) {
		//String new Object(){}.getClass().getEnclosingMethod().getName() = "javaScriptClick method";
		try {
			WebElement elem = getWebElement(element);
			((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].click();", elem);
			SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
	}

	/**
	 * Bring the Element into Screen BOTTOM view, and also changes its background to
	 * Yellow
	 * 
	 * @param element
	 *            A WebElement/By object
	 */
	public void javaScriptScrollInto_BOTTOM_ViewAndHighlight(Object element) {
		//String new Object(){}.getClass().getEnclosingMethod().getName() = "javaScriptScrollIntoViewAndHighlight method";
		try {
			WebElement elem = getWebElement(element);
			((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].scrollIntoView(false);", elem);
			((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].style=''", elem);
			((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].style='background-color:Yellow;border: 1px solid red;color: red;'", elem);
			SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
	}

	/**
	 * Bring the Element into Screen TOP view, and also changes its background to
	 * Yellow
	 * 
	 * @param element
	 *            A WebElement/By object
	 */
	public void javaScriptScrollInto_TOP_ViewAndHighlight(Object element) {
		//String new Object(){}.getClass().getEnclosingMethod().getName() = "javaScriptScrollIntoViewAndHighlight method";
		try {
			WebElement elem = getWebElement(element);
			((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].scrollIntoView(true);", elem);
			((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].style=''", elem);
			((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].style='background-color:Yellow;border: 1px solid red;color: red;'", elem);
			SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
	}

}
