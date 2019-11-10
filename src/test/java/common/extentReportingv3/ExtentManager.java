package common.extentReportingv3;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import common.configData_Util.Constant;

import java.util.HashMap;
import java.util.Map;

public class ExtentManager {
	private static ExtentReports extentReport;

	private static ExtentHtmlReporter htmlReporter;
	private static final String filePath = Constant.getResultextenthtmlfilePath();
	static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();

	//called by onStart
	public static synchronized ExtentReports GetExtentReports(String suiteName,String inParallel){
		if (extentReport != null)
			return extentReport; //avoid creating new instance of html file
		extentReport = new ExtentReports();
		extentReport.setSystemInfo("Test Environment", Constant.getEnvironmentInfoSheet());
		extentReport.setSystemInfo("Parallel Mode", inParallel);
		extentReport.setSystemInfo("Assertion Enabled", Constant.enableAssertions+"");
		extentReport.setSystemInfo("Capturing Snapshots", Constant.captureSnapshots+"");
		extentReport.attachReporter(getHtmlReporter("Suite: '" +suiteName + "'"));
		extentReport.setAnalysisStrategy(AnalysisStrategy.TEST);
		return extentReport;
	}

	//called by onTestStart
	public static synchronized ExtentTest createTest(String name, String description, String testNG_testName, String testNG_suiteName){
		ExtentTest extentTest = extentReport.createTest(name, description+" | TestNG-test : "+testNG_testName+" | TestNG-suite : "+testNG_suiteName);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), extentTest);
		return extentTest;
	}

	//called by @test methods
	public static synchronized ExtentTest GetExtentTest(){
		return extentTestMap.get((int) (long) (Thread.currentThread().getId())); 
	}

	//called by local GetExtentReports method
	private static synchronized ExtentHtmlReporter getHtmlReporter(String suiteName) {
		htmlReporter = new ExtentHtmlReporter(filePath);
		htmlReporter.setAppendExisting(false);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setDocumentTitle("ExtentReports 3.0");
		htmlReporter.config().setReportName(suiteName);
		htmlReporter.config().setTheme(Theme.STANDARD);
		return htmlReporter;
	}

	public static Status getStatus(String status) {
		Status val=null;
		switch(status){
		case Constant.PASS:
			val=Status.PASS;
			break;
		case Constant.FAIL:
			val=Status.FAIL;
			break;
		case Constant.SKIP:
			val=Status.SKIP;
			break;
		case Constant.ERROR:
			val=Status.ERROR;
			break;
		case Constant.FATAL:
			val=Status.FATAL;
			break;
		case Constant.INFO:
			val=Status.INFO;
			break;
		case Constant.WARNING:
			val=Status.WARNING;
			break;
		}
		return val;
	}
	
	
}