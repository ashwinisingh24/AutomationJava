package common.testNgListener;

import com.aventstack.extentreports.ExtentReports;
import common.configData_Util.Constant;
import common.configData_Util.Util;
import common.customReporting.CustomReporter;
import common.customReporting.ReportingHistoryManager;
import common.customReporting.SnapshotManager;
import common.driverManager.DriverFactory;
import common.extentReportingv3.ExtentManager;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomListener implements ITestListener,IExecutionListener{

	private ExtentReports extentReport;
	private String parallelFlag;
	
	public void onExecutionStart() {
		//System.out.println("onExecutionStart for Thread: "+Thread.currentThread().getId());
		Util.deleteFolderContentRecursively(new File(Constant.getDownloadsPath()), Constant.downloadFolderName);
		SnapshotManager.initialize();
		CustomReporter.initialize();
	}

	public void onStart(ITestContext context) {
		//System.out.println("bro onStart "+Thread.currentThread().getId());
		parallelFlag=context.getSuite().getParallel();
		String browser=context.getCurrentXmlTest().getParameter("browser");
		String platform=context.getCurrentXmlTest().getParameter("platform");
		String remoteURL=context.getCurrentXmlTest().getParameter("remoteURL");
		String environment=context.getCurrentXmlTest().getParameter("environment");
		Constant.setEnvironmentInfoSheet(environment);
		
		String suiteName=context.getSuite().getName();
		extentReport = ExtentManager.GetExtentReports(suiteName,parallelFlag);
		CustomReporter.onStart(parallelFlag);
		
		if(parallelFlag.equals("none") || parallelFlag.equals("tests")){
			DriverFactory.setUp(remoteURL,browser,platform);
			SnapshotManager.setUp(context.getName());
		}
		
	}

	public void onTestStart(ITestResult result) {
		//System.out.println("onTestStart "+Thread.currentThread().getId());
		//System.out.println("Started: "+result.getMethod().getDescription());
		String browser=result.getMethod().findMethodParameters(result.getTestContext().getCurrentXmlTest()).get("browser");
		String suiteName = result.getTestContext().getCurrentXmlTest().getSuite().getName();
		String testName = result.getTestContext().getCurrentXmlTest().getName();
		//String browser=result.getTestContext().getCurrentXmlTest().getAllParameters().get("browser");
		String platform=result.getTestContext().getCurrentXmlTest().getAllParameters().get("platform");
		String remoteURL=result.getTestContext().getCurrentXmlTest().getAllParameters().get("remoteURL");
		ExtentManager.createTest(result.getMethod().getDescription()+" |os: "+platform+"| browser: "+browser+"|",result.getMethod().getDescription(),testName,suiteName);

		CustomReporter.onTestStart(result.getMethod().getDescription(),browser,platform,testName,suiteName);
		if(!parallelFlag.equals("none") && !parallelFlag.equals("tests")){
			DriverFactory.setUp(remoteURL,browser,platform);
			SnapshotManager.setUp(result.getName());
		}

	}

	public void onTestSuccess(ITestResult result){
		//System.out.println("onTestSuccess "+Thread.currentThread().getId());
		CustomReporter.onTestEnd();
		if(!parallelFlag.equals("none") && !parallelFlag.equals("tests")){
			DriverFactory.tearDown();
			SnapshotManager.tearDown();
		}
	}

	public void onTestFailure(ITestResult result) {
		//System.out.println("onTestFailure "+Thread.currentThread().getId());
		CustomReporter.onTestEnd();
		if(!parallelFlag.equals("none") && !parallelFlag.equals("tests")){
			DriverFactory.tearDown();
			SnapshotManager.tearDown();
		}
	}
	public void onTestSkipped(ITestResult result) {
		//System.out.println("onTestSkip "+Thread.currentThread().getId());
		String suiteName = result.getTestContext().getCurrentXmlTest().getSuite().getName();
		String testName = result.getTestContext().getCurrentXmlTest().getName();
		String browser=result.getTestContext().getCurrentXmlTest().getAllParameters().get("browser");
		String platform=result.getTestContext().getCurrentXmlTest().getAllParameters().get("platform");
		ExtentManager.createTest(result.getMethod().getDescription()+" |os: "+platform+"| browser: "+browser+"|",result.getMethod().getDescription(),testName,suiteName);
		
		CustomReporter.onTestStart(result.getMethod().getDescription(),browser,platform,testName,suiteName);
		CustomReporter.report(Constant.SKIP,"It depends on methods which got failed: '"+(result.getMethod().getMethodsDependedUpon().length>0?Arrays.toString(result.getMethod().getMethodsDependedUpon()):null)+"'");
		CustomReporter.onTestEnd();
	}

	public void onFinish(ITestContext context) {
		//System.out.println("onFinish "+Thread.currentThread().getId());
		if(parallelFlag.equals("none") || parallelFlag.equals("tests")){
			DriverFactory.tearDown();
			SnapshotManager.tearDown();
		}
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}
	public static List<String> methodList=Collections.synchronizedList(new ArrayList<String>());
	
	public void onExecutionFinish() {
		//System.out.println("Execution Finish"+CommonUtils.getTimeStamp());
		CustomReporter.onExecutionFinish();
		try{extentReport.flush();
		}catch(Exception e){e.printStackTrace();}
		System.out.println("Execution Finished "+ Util.getTimeStamp());
		methodList.forEach((temp)->{
			System.out.println(temp);
		});
		ReportingHistoryManager.manageReportingHistory();
	}

	
	
}