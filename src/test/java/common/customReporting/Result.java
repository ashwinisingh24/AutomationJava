package common.customReporting;

public class Result {
	
	private String suiteName;
	private String testName;
	private String status;
	private String scenario;
	private String description;
	private static String inParallel;
	private String snapshotURL;
	private String timeStamp;
	private long startTime;
	private long endTime;
	private String browser;
	private String platform;
	private static long testExecutionStartDate=0l;
	private static long testExecutionEndDate=0l;
	private static String testExecutionTime;
	private static long totalScenario=0;
	private static long passedScenario=0;
	private static long failedScenario=0;
	private static long skippedScenario=0;
	private static long errorScenario=0;
	private static long warningScenario=0;
	private static long fatalScenario=0;
	
	private static long totalStep=0;
	private static long passedStep=0;
	private static long failedStep=0;
	private static long skippedStep=0;
	private static long errorStep=0;
	private static long warningStep=0;
	private static long fatalStep=0;
	
	public String getTestNG_SuiteName() {
		return suiteName;
	}
	public void setTestNG_SuiteName(String suiteName) {
		this.suiteName = suiteName;
	}
	public String getTestNG_TestName() {
		return testName;
	}
	public void setTestNG_TestName(String testName) {
		this.testName = testName;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public static long getTotalStep() {
		return totalStep;
	}
	public static void setTotalStep(long totalStep) {
		Result.totalStep = totalStep;
	}
	public static long getPassedStep() {
		return passedStep;
	}
	public static void setPassedStep(long passedStep) {
		Result.passedStep = passedStep;
	}
	public static long getFailedStep() {
		return failedStep;
	}
	public static void setFailedStep(long failedStep) {
		Result.failedStep = failedStep;
	}
	public static long getSkippedStep() {
		return skippedStep;
	}
	public static void setSkippedStep(long skippedStep) {
		Result.skippedStep = skippedStep;
	}
	public static long getErrorStep() {
		return errorStep;
	}
	public static void setErrorStep(long errorStep) {
		Result.errorStep = errorStep;
	}
	public static long getWarningStep() {
		return warningStep;
	}
	public static void setWarningStep(long warningStep) {
		Result.warningStep = warningStep;
	}
	public static long getFatalStep() {
		return fatalStep;
	}
	public static void setFatalStep(long fatalStep) {
		Result.fatalStep = fatalStep;
	}
	public static long getErrorScenario() {
		return errorScenario;
	}
	public static void setErrorScenario(long errorScenario) {
		Result.errorScenario = errorScenario;
	}
	public static long getWarningScenario() {
		return warningScenario;
	}
	public static void setWarningScenario(long warningScenario) {
		Result.warningScenario = warningScenario;
	}
	public static long getFatalScenario() {
		return fatalScenario;
	}
	public static void setFatalScenario(long fatalScenario) {
		Result.fatalScenario = fatalScenario;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getSnapshotURL() {
		return snapshotURL;
	}
	public void setSnapshotURL(String snapshotURL) {
		this.snapshotURL = snapshotURL;
	}
	public static String getTestExecutionTime() {
		
		return testExecutionTime;
	}
	public static void setTestExecutionTime(String testExecutionTime) {
		Result.testExecutionTime = testExecutionTime;
	}
	public static String getInParallel() {
		return inParallel;
	}
	public static void setInParallel(String inParallel) {
		Result.inParallel = inParallel;
	}
	public String getStatus() {
		return status;
	}
	public String getScenario() {
		return scenario;
	}
	public String getDescription() {
		return description;
	}
	public long getStartTime() {
		return startTime;
	}
	public static long getTestExecutionStartDate() {
		return testExecutionStartDate;
	}
	public static long getTestExecutionEndDate() {
		return testExecutionEndDate;
	}
	public String getBrowser() {
		return browser;
	}
	public String getPlatform() {
		return platform;
	}
	public static long getTotalScenario() {
		return totalScenario;
	}
	public static long getPassedScenario() {
		return passedScenario;
	}
	public static long getFailedScenario() {
		return failedScenario;
	}
	public static long getSkippedScenario() {
		return skippedScenario;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setScenario(String scenario) {
		this.scenario = scenario;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setStartTime(long l) {
		this.startTime = l;
	}
	public static void setTestExecutionStartDate(long l) {
		if(Result.testExecutionStartDate==0l){
			Result.testExecutionStartDate = l;
		}
	}
	public static void setTestExecutionEndDate(long l) {
		Result.testExecutionEndDate = l;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public static void setTotalScenario(long totalScenario) {
		Result.totalScenario = totalScenario;
	}
	public static void setPassedScenario(long passedScenario) {
		Result.passedScenario = passedScenario;
	}
	public static void setFailedScenario(long failedScenario) {
		Result.failedScenario = failedScenario;
	}
	public static void setSkippedScenario(long skippedScenario) {
		Result.skippedScenario = skippedScenario;
	}
	
}