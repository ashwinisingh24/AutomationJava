package common.customReporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import common.configData_Util.Constant;
import common.configData_Util.Util;
import common.extentReportingv3.ExtentManager;
import common.xlUtil.DataTable_Static;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class CustomReporter {
	static Map<Integer, List<Result>> customTestMap = new HashMap<Integer, List<Result>>();
	private static List<Result> customTestList;
	private static List<ArrayList<Result>> listOfList;
	private static final String reportPath= Constant.getResultExcelFilePath();
	private static final String sheetName="Results";

	public static void initialize() {
		listOfList = new ArrayList<ArrayList<Result>>();
	}

	public static void onStart(String inParallel) {
		Result.setInParallel(inParallel);
		Result.setTestExecutionStartDate((new Date()).getTime());
	}

	public static synchronized void onExecutionFinish() {
		File file=new File(Constant.getResultLocation());
		deleteDirectory(file);
		createExcelTemplate();
		long time_start = Long.MAX_VALUE;
		long time_end = Long.MIN_VALUE;
		//System.out.println("Finished: ");
		Result.setTestExecutionEndDate((new Date()).getTime());
		time_start = Math.min(Result.getTestExecutionStartDate(), time_start);
		time_end = Math.max(Result.getTestExecutionEndDate(), time_end);
		Result.setTestExecutionTime(Util.timeConversion(((time_end - time_start) / 1000)));
		processXlsThenCreateHtmlReport();
	}


	public static synchronized void onTestStart(String testName,String browser,String platform, String testNG_testName, String testNG_suiteName){
		customTestList= GetCustomTestList();
		Result r = new Result();
		r.setStatus(Constant.PASS);
		r.setScenario(testName);
		r.setDescription("");
		r.setStartTime((new Date()).getTime());
		r.setBrowser(browser);
		r.setPlatform(platform);
		r.setTimeStamp(Util.convertToHHMMSS((new Date()).getTime()));
		r.setTestNG_SuiteName(testNG_suiteName);
		r.setTestNG_TestName(testNG_testName);
		System.out.println(Thread.currentThread().getId()+" | "+"Scenario: "+testName+"| Browser: "+browser+"| Platform: "+platform);
		customTestList.add(r);
	}

	public static synchronized void onTestEnd() {
		customTestList= GetCustomTestList();
		Result r = new Result();
		r.setEndTime((new Date()).getTime());
		customTestList.add(r);
		listOfList.add((ArrayList<Result>) GetCustomTestList());
		customTestMap.remove((int) (long) (Thread.currentThread().getId()));
	}

	private static void processXlsThenCreateHtmlReport(){
		//System.out.println("processXlsThenCreateHtmlReport is called ");
		//calling reportNumber to create numbering in xlsx file
		createFinalXlReport();
		reportNumber();
		calcExecutionTimeForEachScenario();
		removeEndTimeRow();
		CustomReportHTML.createHTML();
	}

	//called by @test methods
	private static List<Result> GetCustomTestList(){
		List<Result> tempListObj=customTestMap.get((int) (long) (Thread.currentThread().getId()));
		if(tempListObj==null){
			tempListObj= new ArrayList<Result>();
			customTestMap.put((int) (long) (Thread.currentThread().getId()), tempListObj);
			//System.out.println("Created an ArrayList for storing the results thread: "+Thread.currentThread().getId());
		}else{
			//System.out.println("Existing ArrayList size: "+tempListObj.size()+" for storing the results thread: "+Thread.currentThread().getId());
		}
		return tempListObj; 
	}

	private static void createExcelTemplate() {
		Workbook workBookObj = null;
		Sheet sheetObj=null;
		Row row=null;
		FileOutputStream outputStream=null;

		try{
			//Creating a new sheet
			workBookObj = new XSSFWorkbook();
			sheetObj = workBookObj.createSheet(sheetName);
			String cellA1="No",cellB1="Scenario_Name",cellC1="Test_case_Steps",
					cellD1="Status",cellE1="Start Time",cellF1="End Time",cellG1="Execution_Time",
					cellH1="Platform",cellI1="Browser",cellJ1="Error Screenshot",cellK1="TimeStamp",cellL1="TestNGSuite",cellM1="TestNGTest";
			row=sheetObj.createRow(0);
			row.createCell(0).setCellValue(cellA1);
			row.createCell(1).setCellValue(cellB1);
			row.createCell(2).setCellValue(cellC1);
			row.createCell(3).setCellValue(cellD1);
			row.createCell(4).setCellValue(cellE1);
			row.createCell(5).setCellValue(cellF1);
			row.createCell(6).setCellValue(cellG1);
			row.createCell(7).setCellValue(cellH1);
			row.createCell(8).setCellValue(cellI1);
			row.createCell(9).setCellValue(cellJ1);
			row.createCell(10).setCellValue(cellK1);
			row.createCell(11).setCellValue(cellL1);
			row.createCell(12).setCellValue(cellM1);

			outputStream=new FileOutputStream(new File(reportPath));
			workBookObj.write(outputStream);
			outputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Use this method to add the snapshot of passed web element object in Report
	 * */
	public static synchronized void report(String Constan, String description, Object element){
		//SessionId session = ((ChromeDriver)BaseTest.getDriver()).getSessionId();
		if(Constant.enableConsoleLogs){
			System.out.println(Thread.currentThread().getId()+" | "+Constan+" | "+description);
		}
		String url="";
		customTestList=GetCustomTestList();
		Result r = new Result();
		r.setTimeStamp(Util.convertToHHMMSS((new Date()).getTime()));
		r.setStatus(Constan);
		r.setDescription(description);
		r.setScenario("");
		
		if(Constan.equalsIgnoreCase(Constant.FAIL) || Constan.equalsIgnoreCase(Constant.FATAL) ){
			url= SnapshotManager.takeSnapShot("failure");
			r.setSnapshotURL(url);
		}else if(element!=null){
			url= SnapshotManager.takeSnapShot("",element);
			r.setSnapshotURL(url);
		}
		//System.out.println("List Size "+customTestList.size()+" for Thread: "+Thread.currentThread().getId());
		customTestList.add(r);
		logExtentTest(Constan, description, url);
		manageAssertions(Constan, description);
	}
	
	public static synchronized void report(String Constan, String description){
		//SessionId session = ((ChromeDriver)BaseTest.getDriver()).getSessionId();
		if(Constant.enableConsoleLogs){
			System.out.println(Thread.currentThread().getId()+" | "+Constan+" | "+description);
		}
		String url="";
		customTestList=GetCustomTestList();
		Result r = new Result();
		r.setTimeStamp(Util.convertToHHMMSS((new Date()).getTime()));
		r.setStatus(Constan);
		r.setDescription(description);
		r.setScenario("");
		if(Constan.equalsIgnoreCase(Constant.FAIL) || Constan.equalsIgnoreCase(Constant.FATAL) ){
			url= SnapshotManager.takeSnapShot("failure");
			r.setSnapshotURL(url);
		}
		//System.out.println("List Size "+customTestList.size()+" for Thread: "+Thread.currentThread().getId());
		customTestList.add(r);
		logExtentTest(Constan, description, url);
		manageAssertions(Constan, description);
	}

	private static void logExtentTest(String Constan, String description,String url){
		try {
			ExtentTest test=ExtentManager.GetExtentTest();
			if(Constan.equalsIgnoreCase(Constant.FAIL) || Constan.equalsIgnoreCase(Constant.FATAL) ){
				if(url!=null || !"".equals(url)){
					test.log(ExtentManager.getStatus(Constan), description,MediaEntityBuilder.createScreenCaptureFromPath(url).build());
				}else{
					test.log(ExtentManager.getStatus(Constan), description);
				}
			}else{
				test.log(ExtentManager.getStatus(Constan), description);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void manageAssertions(String Constan, String description) {
		if(Constant.enableAssertions){
			if(Constan.equalsIgnoreCase(Constant.FAIL) || Constan.equalsIgnoreCase(Constant.FATAL) ){
				Assert.fail(description);
			}
		}
	}


	private static  void reportNumber() {
		Workbook workBookObj = null;
		Sheet sheetObj=null; 
		Row row=null; 
		FileOutputStream outputStream=null;

		try{
			workBookObj = DataTable_Static.getWorkbookObject(reportPath);
			sheetObj = DataTable_Static.getSheetObject(workBookObj,"Results");
			String scenario="",scenarioCounter="";
			int tempCount=1,totalRow=0;


			totalRow=sheetObj.getPhysicalNumberOfRows();
			for (int i = 1; i < totalRow; i++) {
				scenario= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 1);
				//scenario= DataTable.Value(reportPath, sheetName, i, "Scenario_Name");
				if(scenario!=""){
					scenarioCounter=Integer.toString(tempCount);
					tempCount++;
					row=sheetObj.getRow(i);
					row.createCell(0).setCellValue(scenarioCounter);
				}
			}
			outputStream=new FileOutputStream(new File(reportPath));
			workBookObj.write(outputStream);
			outputStream.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static  void removeEndTimeRow(){
		try{
			Workbook workBookObj = null;
			Sheet sheetObj=null;

			workBookObj = DataTable_Static.getWorkbookObject(reportPath);
			sheetObj = DataTable_Static.getSheetObject(workBookObj,sheetName);
			FileOutputStream outputStream=null;

			int totalRow=sheetObj.getPhysicalNumberOfRows();
			for (int i = 1; i <= totalRow; i++) {
				//String endTime=DataTable.Value(reportPath, sheetName, i, "End Time").trim();
				String endTime= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 5);
				if(endTime!=""){
					long tempVal=Long.parseLong(endTime);
					if(tempVal!=0){
						removeRow(sheetObj, i);
						outputStream=new FileOutputStream(new File(reportPath));
						workBookObj.write(outputStream);
						i=1;

					}
				}
			}

			outputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}	
	}

	private static void removeRow(Sheet sheet, int rowIndex) {
		int lastRowNum = sheet.getLastRowNum();
		if (rowIndex >= 0 && rowIndex < lastRowNum) {
			sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
		}
		if (rowIndex == lastRowNum) {
			Row removingRow = sheet.getRow(rowIndex);
			if (removingRow != null) {
				sheet.removeRow(removingRow);
			}
		}
	}

	private static  void calcExecutionTimeForEachScenario(){
		Workbook workBookObj = null;
		Sheet sheetObj=null;
		File file=new File(reportPath);
		workBookObj = DataTable_Static.getWorkbookObject(reportPath);
		sheetObj = DataTable_Static.getSheetObject(workBookObj,sheetName);

		int totalRow=0;
		totalRow=sheetObj.getPhysicalNumberOfRows();
		for (int i = 1; i <= totalRow; i++) {
			//Getting the execution time for each method
			int nextScenarioRow=0;
			long time1,time2;
			String tempScenario="",execTime="";
			for(int j=i+1;j<=totalRow;j++){
				//tempScenario=DataTable.Value(reportPath, sheetName, j, "Scenario_Name").trim();
				tempScenario= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, j, 1);
				if(tempScenario!=""){
					nextScenarioRow=j-1;
					break;
				}else if(j==totalRow){
					nextScenarioRow=totalRow;
					break;
				}
			}

			if(i!=totalRow){
				//String time1_str=DataTable.Value(reportPath, sheetName, i, "Start Time").trim();
				String time1_str= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 4);
				if(!time1_str.contains(":")){
					time1=Long.parseLong(time1_str);
					if(nextScenarioRow==totalRow){
						time2= Result.getTestExecutionEndDate();
					}else{
						//time2=Long.parseLong(DataTable.Value(reportPath, sheetName, nextScenarioRow, "End Time").trim());
						time2=Long.parseLong(DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj,nextScenarioRow,5));
					}
					execTime= Util.timeConversion((time2 - time1) / 1000);
					//DataTable.UpdateExcel(reportPath, sheetName, i, "Execution_Time", execTime);
					DataTable_Static.setCellValueCoordinates(file, workBookObj, sheetObj, i, 6, execTime);
				}
			}

			if(nextScenarioRow!=0)
				i=nextScenarioRow;
			else
				i=totalRow;
		}
	}

	private static  void createFinalXlReport(){
		Workbook workBookObj = null;
		Sheet sheetObj=null; 
		Row row=null;

		try{
			workBookObj = DataTable_Static.getWorkbookObject(reportPath);
			sheetObj = DataTable_Static.getSheetObject(workBookObj,sheetName);
			for (List<Result> tempObj : listOfList) {
				int totalRow=0,arraySize=0;
				arraySize=tempObj.size();
				for (int i = 0; i < arraySize; i++) {
					totalRow=sheetObj.getPhysicalNumberOfRows();
					row=sheetObj.createRow(totalRow);

					CellStyle cellStyle = workBookObj.createCellStyle();
					DataFormat format = workBookObj.createDataFormat();
					cellStyle.setDataFormat(format.getFormat("0000000000000"));
					//System.out.println("Status : "+customTestList.get(i).getStatus()+" Scenario : "+customTestList.get(i).getScenario()+ " desc : "+customTestList.get(i).getDescription());
					row.createCell(1).setCellValue(tempObj.get(i).getScenario());
					row.createCell(2).setCellValue(tempObj.get(i).getDescription());
					row.createCell(3).setCellValue(tempObj.get(i).getStatus());
					Cell cell=row.createCell(4);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(tempObj.get(i).getStartTime());
					cell=row.createCell(5);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(tempObj.get(i).getEndTime());
					row.createCell(6).setCellValue("");
					row.createCell(7).setCellValue(tempObj.get(i).getPlatform());
					row.createCell(8).setCellValue(tempObj.get(i).getBrowser());
					row.createCell(9).setCellValue(tempObj.get(i).getSnapshotURL());
					row.createCell(10).setCellValue(tempObj.get(i).getTimeStamp());
					row.createCell(11).setCellValue(tempObj.get(i).getTestNG_SuiteName());
					row.createCell(12).setCellValue(tempObj.get(i).getTestNG_TestName());

				}

			}

			FileOutputStream outputStream=new FileOutputStream(new File(reportPath));
			workBookObj.write(outputStream);
			outputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	private static  boolean deleteDirectory(File dir) {
		boolean bool=false;
		if (dir.isDirectory()) { 
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) { 
				boolean success = deleteDirectory(children[i]);
				if (!success) { 
					return false; 
				} 
			} 
		} 
		// either file or an empty directory
		if(dir.getName().equals(Constant.resultFolderName)){
			//System.out.println("Skipping file or directory : " + dir.getName());
			bool=true;
		}else{
			//System.out.println("Removing file or directory : " + dir.getName());
			bool=dir.delete();
		}
		return bool;
	}

	public static void main(String[] args) {
		extentAndCustomReportTester();
	} 
	

	protected static void onlyExtentTester() {
		ExtentReports extentReport;
		extentReport = ExtentManager.GetExtentReports("Suite1","meth");
		
		ExtentTest test=ExtentManager.createTest("Test1","Test1", null, null);
		ExtentTest test1=test.createNode("Node1.1");
		test1.log(Status.PASS, "Log1.1.1");
		test1.log(Status.FAIL, "Log1.1.2");
		test1=test.createNode("Node1.2");
		test1.log(Status.PASS, "Log1.2.1");
		test1.log(Status.PASS, "Log1.2.2");
		
		test=ExtentManager.createTest("Test2","Test2", null, null);
		test1=test.createNode("Node2.1");
		test1.log(Status.PASS, "Log2.1.1");
		test1.log(Status.PASS, "Log2.1.2");
		test1=test.createNode("Node2.2");
		test1.log(Status.PASS, "Log2.2.1");
		test1.log(Status.PASS, "Log2.2.2");
		
		extentReport.flush();
	}

	protected static void extentAndCustomReportTester(){
			ExtentReports extentReport;
			CustomReporter.initialize();
			
			CustomReporter.onStart("methods");
			extentReport = ExtentManager.GetExtentReports("Suite1","meth");
			
			CustomReporter.onTestStart("Test1","Browser","Platform", null, null);
			ExtentManager.createTest("Test1","Test1", null, null);
			CustomReporter.report(Constant.PASS,"Test1.1");
			CustomReporter.report(Constant.PASS,"Test1.2");
			CustomReporter.report(Constant.PASS,"Test1.3");
			CustomReporter.report(Constant.PASS,"Test1.4");
			CustomReporter.report(Constant.PASS,"Test1.5");
			CustomReporter.report(Constant.PASS,"Test1.6");
			CustomReporter.onTestEnd();
			
			CustomReporter.onTestStart("Test3","Browser","Platform", null, null);
			ExtentManager.createTest("Test3","Test3", null, null);
			CustomReporter.report(Constant.SKIP,"It depends on methods which got failed");
			CustomReporter.onTestEnd();
			
			CustomReporter.onTestStart("Test2","Browser","Platform", null, null);
			ExtentManager.createTest("Test2","Test2", null, null);
			CustomReporter.report(Constant.PASS,"Test2.1");
			CustomReporter.report(Constant.PASS,"Test2.2");
			CustomReporter.report(Constant.PASS,"Test2.3");
			CustomReporter.report(Constant.PASS,"Test2.4");
			CustomReporter.report(Constant.PASS,"Test2.5");
			CustomReporter.report(Constant.PASS,"Test2.6");
			CustomReporter.onTestEnd();
			
			CustomReporter.onExecutionFinish();
			extentReport.flush();
	}
	
}
