package common.configData_Util;

import java.io.File;

public class Constant {

	/**
	 * {@code captureSnapshots} : set false for speeding up the execution, It
	 * will stop capturing snapshots of each and every method performed on
	 * application, but it will not stop the snapshots capturing of failed/fatal
	 * methods
	 */
	public static final boolean captureSnapshots = false;

	/**
	 * {@code enableAssertions} : set true to enable TestNG Assertion feature while
	 * execution, this will stop the execution of a method once a failure is reported through
	 * <br><b><code>CustomReporter.report(Constant.FAIL, "");</code></b>, or in case any exception.
	 */
	public static final boolean enableAssertions = false;
	
	/**
	 * {@code enableConsoleLogs} : set true to enable the report pass,fail etc
	 * statements to be printed on console while execution
	 */
	public static final boolean enableConsoleLogs = true;


	/**Assign 0 to stop manageHistory Code*/
	public static final int reportingHistoryFolderLimit = 500;
	public static final int reportingHistoryFolderPerPage = 20;
	public static final String reportingHistoryFolderName = "ReportingHistory";
	public static final String reportingHistoryHTMLFolderName= "HTML";
	
	
	
	public static final String PASS = "Pass";
	public static final String FAIL = "Fail";
	public static final String FATAL = "Fatal";
	public static final String ERROR = "Error";
	public static final String WARNING = "Warning";
	public static final String SKIP = "Skip";
	public static final String INFO = "Info";
	public static final long wait = 60;
	public static final long implicitWait = 0;
	public static final int width = 5000;
	public static final int height = 3000;
	
	private static final String root = System.getProperty("user.dir");
	
	private static final String serverExesFolderName = "ServerExes";
	public static final String geckoDriverFileName_Win="geckodriver.exe";
	public static final String geckoDriverFileName_Linux="geckodriver-v0_18_0-linux64";
	public static final String chromeDriverFileName_Win="chromedriver.exe";
	public static final String chromeDriverFileName_Linux="chromedriver_linux64_FILE";
	public static final String ieDriverFileName = "IEDriverServer_win32_3.4.0.exe";
	private static final String testDataFolderName = "TestData";
	private static final String testDataFileName = "TestData.xlsx";
	public static final String resultFolderName = "Result";
	public static final String downloadFolderName = "Downloads";
	public static final String snapshotsFolderName = "Snapshots";
	private static final String resultExcelFileName = "Result.xlsx";
	public static final String resultHTMLFileName = "Result.html";
	public static final String resultExtentHTMLFileName = "Result_Extentv3.html";

	private static String environmentInfoSheet="UAT";
	public static final String testsUserMappingSheet="testsUserMapping";

	public static String getEnvironmentInfoSheet() {
		return environmentInfoSheet;
	}

	public static void setEnvironmentInfoSheet(String environment) {
		String key=environment.toUpperCase();
		switch (key) {
		case "PREPROD":
			environmentInfoSheet = key;
			break;
				
		case "FDEVAPEX5":
			environmentInfoSheet = key;
			break;
				
		case "FUATAPEX5":
			environmentInfoSheet = key;
			break;
				
		default:
			environmentInfoSheet = "UAT";
			System.err.println("Warning: TestNG parameter 'environment' value= {"+key+"} is incorrectly provided, running the tests on 'UAT' enviroment");
			break;
		}

	}

	public static String getTestDataFilePath() {
		String path = root + "/" + testDataFolderName + "/" + testDataFileName;
		return path;
	}

	public static String getSnapShotsFolderPath() {
		String path = root + "/" + snapshotsFolderName;
		return path;
	}

	public static String getResultExcelFilePath() {
		String path = root + "/" + resultFolderName + "/" + resultExcelFileName;
		return path;
	}

	public static String getResultextenthtmlfilePath() {
		String path = root + "/" + resultFolderName + "/" + resultExtentHTMLFileName;
		return path;
	}

	public static String getResultHtmlFilePath() {
		String path = root + "/" + resultFolderName + "/" + resultHTMLFileName;
		return path;
	}

	public static String getResultLocation() {
		String val = root + "/" + resultFolderName;
		//Creating the Result Folder in case it is not already present
		File fileObj= new File(val);
		if(!fileObj.exists()) {
			fileObj.mkdir();
		}
		return val;
	}

	public static String getDownloadsPath() {
		String val = root + "/" + downloadFolderName;
		//Creating the Result Folder in case it is not already present
		File fileObj= new File(val);
		if(!fileObj.exists()) {
			fileObj.mkdir();
		}
		return val;
	}
	
	
	public static String getReportingHistoryFolderPath() {
		String val = root + "/" + reportingHistoryFolderName;
		//Creating the Result Folder in case it is not already present
		File fileObj= new File(val);
		if(!fileObj.exists()) {
			fileObj.mkdir();
		}
		return val;
	}
	
	public static String getReportingHistoryHTMLfolderPath(){
		String val = root + "/" + reportingHistoryFolderName + "/" + reportingHistoryHTMLFolderName;
		//Creating the Result Folder in case it is not already present
		File fileObj= new File(val);
		if(!fileObj.exists()) {
			fileObj.mkdir();
		}
		return val;
	}
	
	public static String getFirefoxDriverLocation(String platform) {
		String val="";
		if(platform.toLowerCase().contains("lin")){
			val=root+"/"+serverExesFolderName+"/"+geckoDriverFileName_Linux;
		}else if(platform.toLowerCase().contains("win")){
			val=root+"/"+serverExesFolderName+"/selenium_standalone_binaries/windows/marionette/32bit/"+geckoDriverFileName_Win;
		}
		System.out.println("ASHWINI: "+val);
		return val;
	}

	public static String getChromeDriverLocation(String platform) {
		String val="";
		if(platform.toLowerCase().contains("lin")){
			val=root+"/"+serverExesFolderName+"/"+chromeDriverFileName_Linux;
		}else if(platform.toLowerCase().contains("win")){
			val=root+"/"+serverExesFolderName+"/selenium_standalone_binaries/windows/googlechrome/64bit/"+chromeDriverFileName_Win;
		}
		System.out.println("ASHWINI: "+val);
		return val;
	}

	public static String getIEDriverLocation() {
		String val = root + "/" + serverExesFolderName + "/" + ieDriverFileName;
		return val;
	}

	public static void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
