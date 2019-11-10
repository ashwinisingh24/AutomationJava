package common.customReporting;

import common.configData_Util.Constant;
import common.configData_Util.Util;
import common.xlUtil.DataTable_Static;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;

class CustomReportHTML {
	private static final String reportPath= Constant.getResultExcelFilePath();
	private static final String htmlreportPath= Constant.getResultHtmlFilePath();
	private static final String sheetName="Results";

	private final static String pickerStartPlaceholder = "<!-- PICKER1_START -->";
	private final static String pickerEndPlaceholder = "<!-- PICKER1_END -->";
	private final static String resultPlaceholder = "<!-- INSERT_RESULTS -->";
	private final static String totalScenarioPlaceholder = "<!-- TOTAL_SCENARIOS -->";
	private final static String passScenarioPlaceholder = "<!-- PASSED_SCENARIOS -->";
	private final static String failScenarioPlaceholder = "<!-- FAILED_SCENARIOS -->";
	private final static String skippedScenarioPlaceholder = "<!-- SKIPPED_SCENARIOS -->";
	private final static String errorScenarioPlaceholder = "<!-- ERROR_SCENARIOS -->";
	private final static String warningScenarioPlaceholder = "<!-- WARNING_SCENARIOS -->";
	private final static String fatalScenarioPlaceholder = "<!-- FATAL_SCENARIOS -->";

	private final static String totalStepPlaceholder = "<!-- TOTAL_Step -->";
	private final static String passStepPlaceholder = "<!-- PASSED_Step -->";
	private final static String failStepPlaceholder = "<!-- FAILED_Step -->";
	private final static String skippedStepPlaceholder = "<!-- SKIPPED_Step -->";
	private final static String errorStepPlaceholder = "<!-- ERROR_Step -->";
	private final static String warningStepPlaceholder = "<!-- WARNING_Step -->";
	private final static String fatalStepPlaceholder = "<!-- FATAL_Step -->";
	
	private static int stepCounter=0;

	private static String styleHTML="<style>"
			+ "body{font-family: calibri;background-color: #DFDFDF;}"
			+ "h2{color:midnightblue;}"
			+ "table{border: 1px solid #AAAAAA;}"
			+ "th{border-bottom: 1px solid #AAAAAA;}"
			+ "tr{background: aliceblue;}"
			+ "p{max-height: 200px;overflow: auto;}"
			+ ".dashboard-header{border-radius: 20px; background: #8a8a8a; color: #fff; padding: 4px 0px; text-align: center; font-weight: 600;}"
			+ ".properties-div{border: solid black 0px; margin: 0px; padding: 0px; width: max-content; color:midnightblue; text-align: left;}"
			+ ".properties-span{border: solid black 0px; margin: 5px; padding: 3px; }"
			+ "</style>";
	
	private static String testNG_test, testNG_suite, scenario, status, description, errorScreenshotURL,scenarioCounter="",startTime,executionTime,browser,platform,timeStamp;
	
	public static synchronized void createHTML(){
		Workbook workBookObj = null;
		Sheet sheetObj=null; 
		String reportIn="";
		try{
			//SETTING REPORT DASHBOARD DATA
			prepareDashboardCountData();

			workBookObj = DataTable_Static.getWorkbookObject(reportPath);
			sheetObj = DataTable_Static.getSheetObject(workBookObj,sheetName);

			
			
			reportIn = "<html>" + "<head>"
					+ "<link rel='icon href='http://www.seleniumhq.org/selenium-favicon.ico' type='image/vnd.microsoft.icon'>"
					+ "<title> Functional Automation Test Result Report</title>"
					+ styleHTML
					+ "<head>"
					+ "<body><center>"
					+ "<h2>Functional Automation Test Result Report</h2>"

				+ "<div class='properties-div'><fieldset><legend>Framework Cofiguration</legend>"
				+ "<span class='properties-span'>Parallel Mode:<b> "+ Result.getInParallel() +"</b></span>"
				+ "<span class='properties-span'>Assertion Enabled:<b> "+ Constant.enableAssertions +"</b></span>"
				+ "<span class='properties-span'>Capturing Snapshots:<b> "+ Constant.captureSnapshots +"</b></span>"
				+ "</fieldset></div>"

				+ "<h4 >"
				+pickerStartPlaceholder

				+ "<table border='0'>"
				+ "<tbody>"

				+ "<tr><td colspan='8' text-align='center' class='dashboard-header'>Dashboard - "+ Constant.getEnvironmentInfoSheet()+" </td></tr>"

				+ "<tr>"
				+ "<td colspan='3' style='font-weight: bold; text-align: center;'>"+ "Start"+ "</td>"
				+ "<td  colspan='3' style='font-weight: bold; text-align: center;'>"+ "End"+ "</td>"
				+ "<td  colspan='2' style='font-weight: bold; text-align: center;'>"+ "Time Taken"+ "</td>"
				+ "</tr>"

				+ "<tr>"
				+ "<td colspan='3' style='text-align: center;'>"+ new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(Result.getTestExecutionStartDate())+ "</td>"
				+ "<td colspan='3' style='text-align: center;'>"+ new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(Result.getTestExecutionEndDate())+ "</td>"
				+ "<td colspan='2' style='text-align: center;'>"+ Result.getTestExecutionTime()+ "</td>"
				+ "</tr>"

				+ "<tr></tr>"
				+ "<tr></tr>"



				+ "<tr style='font-weight: 600;'>"
				+ "<td></td>"
				+ "<td style='color:midnightblue;'>#Total</td>"
				+ "<td style='color:green;'>#Passed</td>"
				+ "<td style='color:red;'>#Failed</td>"
				+ "<td style='color:brown;'>#Skipped</td>"
				+ "<td style='color:#ec407a;'>#Error</td>"
				+ "<td style='color:orange;'>#Warning</td>"
				+ "<td style='color:#B71C1C;'>#Fatal</td>"
				+ "</tr>"

				+ "<tr>"
				+ "<td style='font-weight: 600;'>Scenario</td>"
				+ "<td style='text-align: center;'>"+totalScenarioPlaceholder+"</td>"
				+ "<td style='text-align: center;'>"+passScenarioPlaceholder+"</td>"
				+ "<td style='text-align: center;'>"+failScenarioPlaceholder+"</td>"
				+ "<td style='text-align: center;'>"+skippedScenarioPlaceholder+"</td>"
				+ "<td style='text-align: center;'>"+errorScenarioPlaceholder+"</td>"
				+ "<td style='text-align: center;'> "+warningScenarioPlaceholder+"</td>"
				+ "<td style='text-align: center;'>"+fatalScenarioPlaceholder+"</td>"
				+ "</tr>"

				+ "<tr>"
				+ "<td style='font-weight: 600;'>Steps</td>"
				+ "<td style='text-align: center;'>"+totalStepPlaceholder+"</td>"
				+ "<td style='text-align: center;'>"+passStepPlaceholder+"</td>"
				+ "<td style='text-align: center;'>"+failStepPlaceholder+"</td>"
				+ "<td style='text-align: center;'>"+skippedStepPlaceholder+"</td>"
				+ "<td style='text-align: center;'>"+errorStepPlaceholder+"</td>"
				+ "<td style='text-align: center;'> "+warningStepPlaceholder+"</td>"
				+ "<td style='text-align: center;'>"+fatalStepPlaceholder+"</td>"
				+ "</tr>"


				+ "</tbody></table>"
				+ pickerEndPlaceholder
				+ "</h4>"

				+"<pre style='text-align: left;'><b>Note:</b> <i>Report follows hh:mm:ss time format</i></pre>"

				+ "<table border='0'>"
				+ "<tr>"
				+ "<th >Sr.No.</th>"
				+ "<th >Start Time</th>"
				+ "<th >TestNG- Suite</th>"
				+ "<th >TestNG- Test</th>"
				+ "<th width='40%'>Test Case Name</th>"
				+ "<th >#</th>"
				+ "<th width='40%'>Step Description</th>"
				+ "<th >Status</th>"
				+ "<th >Time Stamp</th>"
				+ "<th >Execution Time</th>"
				+ "<th >Platform</th>"
				+ "<th >Browser</th>"
				+ "<th >Screenshot</th>"
				+ "</tr>"
				+ resultPlaceholder 
				+ "</table>" 
				+ "<p><b>End of Report</b></p>"
				+ "</center></body></html>";

			reportIn = reportIn.replaceFirst(totalScenarioPlaceholder,getDashBoardNumberColor(Result.getTotalScenario(),"total"));
			reportIn = reportIn.replaceFirst(passScenarioPlaceholder,getDashBoardNumberColor(Result.getPassedScenario(),"pass"));
			reportIn = reportIn.replaceFirst(failScenarioPlaceholder,getDashBoardNumberColor(Result.getFailedScenario(),"fail"));
			reportIn = reportIn.replaceFirst(skippedScenarioPlaceholder,getDashBoardNumberColor(Result.getSkippedScenario(),"skip"));
			reportIn = reportIn.replaceFirst(errorScenarioPlaceholder,getDashBoardNumberColor(Result.getErrorScenario(),"error"));
			reportIn = reportIn.replaceFirst(warningScenarioPlaceholder,getDashBoardNumberColor(Result.getWarningScenario(),"warn"));
			reportIn = reportIn.replaceFirst(fatalScenarioPlaceholder,getDashBoardNumberColor(Result.getFatalScenario(),"fatal"));

			reportIn = reportIn.replaceFirst(totalStepPlaceholder,getDashBoardNumberColor(Result.getTotalStep(),"total"));
			reportIn = reportIn.replaceFirst(passStepPlaceholder,getDashBoardNumberColor(Result.getPassedStep(),"pass"));
			reportIn = reportIn.replaceFirst(failStepPlaceholder,getDashBoardNumberColor(Result.getFailedStep(),"fail"));
			reportIn = reportIn.replaceFirst(skippedStepPlaceholder,getDashBoardNumberColor(Result.getSkippedStep(),"skip"));
			reportIn = reportIn.replaceFirst(errorStepPlaceholder,getDashBoardNumberColor(Result.getErrorStep(),"error"));
			reportIn = reportIn.replaceFirst(warningStepPlaceholder,getDashBoardNumberColor(Result.getWarningStep(),"warn"));
			reportIn = reportIn.replaceFirst(fatalStepPlaceholder,getDashBoardNumberColor(Result.getFatalStep(),"fatal"));


			int totalRow=sheetObj.getPhysicalNumberOfRows();
			for (int i = 1; i < totalRow; i++) {

				scenarioCounter= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 0);
				scenario= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 1);
				status= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 3);
				description= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 2);
				startTime=formattedStartTimeCellValue(workBookObj,sheetObj,i);
				executionTime= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 6);
				platform= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 7);
				browser= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 8);
				errorScreenshotURL= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 9);
				timeStamp= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 10);
				testNG_suite= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 11);
				testNG_test= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 12);
				
				reportIn = reportIn.replaceFirst(resultPlaceholder,
						"<tr "+getScenarioRowStyle(scenario,status)+">"
								+ "<td id="+scenarioCounter+">" + scenarioCounter + "</td>"
								+ "<td>" +startTime+"</td>"
								+ "<td>" +testNG_suite+"</td>"
								+ "<td>" +testNG_test+"</td>"
								+ "<td>" + scenario + "</td>"
								+ "<td><p style='color:gray;'>" + getStepCounter(scenario) + "</p></td>"
								+ "<td><p "+getDescriptionTextStyle(status)+">" +Matcher.quoteReplacement(description) + "</p></td>"
								+ "<td "+getStatusTextStyle(status)+">" + status + "</td>"
								+ "<td><p style='color:gray;'>" +timeStamp+"</p></td>"
								+ "<td>" +executionTime+"</td>"
								+ "<td>" +platform+"</td>"
								+ "<td>" +browser+"</td>"
								+ "<td>" +constructScreenshotElementTag(errorScreenshotURL)+"</td>"
								+ "</tr>" 
								+ resultPlaceholder);

			}


			File file = new File(reportPath);
			FileOutputStream outputStream;
			outputStream = new FileOutputStream(file);
			workBookObj.write(outputStream);

			//Creating the new file
			Files.write(Paths.get(htmlreportPath),reportIn.getBytes(),StandardOpenOption.CREATE); 
		}catch(Exception e){
			System.out.println("EXCEPTION FOR DATA:/n");
			System.out.println("<tr "+getScenarioRowStyle(scenario,status)+">"
								+ "<td id="+scenarioCounter+">" + scenarioCounter + "</td>"
								+ "<td>" +startTime+"</td>"
								+ "<td>" + scenario + "</td>"
								+ "<td><p style='color:gray;'>" + getStepCounter(scenario) + "</p></td>"
								+ "<td><p "+getDescriptionTextStyle(status)+">" +Matcher.quoteReplacement(description) + "</p></td>"
								+ "<td "+getStatusTextStyle(status)+">" + status + "</td>"
								+ "<td><p style='color:gray;'>" +timeStamp+"</p></td>"
								+ "<td>" +executionTime+"</td>"
								+ "<td>" +platform+"</td>"
								+ "<td>" +browser+"</td>"
								+ "<td>" +errorScreenshotURL+"</td>"
								+ "</tr>" );
			
			e.printStackTrace();
		}

	}

	private static String constructScreenshotElementTag(String errorScreenshotURL) {
		return 	"<a href='"+errorScreenshotURL+"' target ='_blank'><img src='"+errorScreenshotURL+"' style='max-width: 250px;max-height: 250px;'></a>";
	}

	private static String getStepCounter(String scenario) {
		if(!scenario.equals("")){
			stepCounter=0;
			return "";
		}else{
			stepCounter++;
			return ""+stepCounter;
		}
	}

	private static String getDashBoardNumberColor(long number, String string) {
		String placeHolderWithColor="";
		if("0".equals(number+"")){
			placeHolderWithColor="<span style='color:white;'>"+number+"</span>";
		}else{
			switch (string) {
			case "total":
				placeHolderWithColor="<span style='color:midnightblue'>"+number+"</span>";
				break;
			case "pass":
				placeHolderWithColor="<span style='color:green;'>"+number+"</span>";
				break;
			case "fail":
				placeHolderWithColor="<span style='color:red;'>"+number+"</span>";
				break;
			case "skip":
				placeHolderWithColor="<span style='color:brown;'>"+number+"</span>";
				break;
			case "error":
				placeHolderWithColor="<span style='color:#ec407a;'>"+number+"</span>";
				break;
			case "warn":
				placeHolderWithColor="<span style='color:orange;'>"+number+"</span>";
				break;
			case "fatal":
				placeHolderWithColor="<span style='color:#B71C1C;'>"+number+"</span>";
				break;
			}
		}

		return placeHolderWithColor;
	}


	private static synchronized void prepareDashboardCountData(){
		try {
			//This method will get the counts of (pass, fail, skip, error, fatal) scenarios, and Also change the scenario status as per the overall status of steps 
			Workbook workBookObj = DataTable_Static.getWorkbookObject(reportPath);
			Sheet sheetObj= DataTable_Static.getSheetObject(workBookObj,sheetName);
			int totalRow=sheetObj.getPhysicalNumberOfRows();


			updateScenarioStatusBasedOnSteps(workBookObj,sheetObj,totalRow, Constant.WARNING);
			updateScenarioStatusBasedOnSteps(workBookObj,sheetObj,totalRow, Constant.ERROR);
			updateScenarioStatusBasedOnSteps(workBookObj,sheetObj,totalRow, Constant.FAIL);
			updateScenarioStatusBasedOnSteps(workBookObj,sheetObj,totalRow, Constant.FATAL);



			//Getting the skipped scenarios count
			int skipCount=0;
			for (int i = 1; i <= totalRow; i++) {
				String tempScenario= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 1);
				if(tempScenario!=""){
					String tempStatus= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i+1, 3);
					if(tempStatus.equals(Constant.SKIP)){
						DataTable_Static.setCellValueCoordinates(new File(reportPath), workBookObj, sheetObj, i, 3, Constant.SKIP);
						//sheetObj.getRow(i).getCell(3).setCellValue(Constant.SKIP);
						skipCount++;
					}

				}

			}

			//Getting the fatal scenario count
			int passCount=getScenarioCountBasedOnStatus(workBookObj,sheetObj,totalRow, Constant.PASS);

			//Getting the fatal scenario count
			int fatalCount=getScenarioCountBasedOnStatus(workBookObj,sheetObj,totalRow, Constant.FATAL);

			//Getting the fail scenarios count
			int failCount=getScenarioCountBasedOnStatus(workBookObj,sheetObj,totalRow, Constant.FAIL);

			//Getting the error scenario count
			int errorCount=getScenarioCountBasedOnStatus(workBookObj,sheetObj,totalRow, Constant.ERROR);

			//Getting the warning scenario count
			int warningCount=getScenarioCountBasedOnStatus(workBookObj,sheetObj,totalRow, Constant.WARNING);

			Result.setTotalScenario(passCount+failCount+skipCount+errorCount+fatalCount+warningCount);
			Result.setPassedScenario(passCount);
			Result.setFailedScenario(failCount);
			Result.setSkippedScenario(skipCount);
			Result.setErrorScenario(errorCount);
			Result.setFatalScenario(fatalCount);
			Result.setWarningScenario(warningCount);


			int infoStep=getStepCountBasedOnStatus(workBookObj,sheetObj,totalRow, Constant.INFO);
			int passStep=getStepCountBasedOnStatus(workBookObj,sheetObj,totalRow, Constant.PASS);
			int failStep=getStepCountBasedOnStatus(workBookObj,sheetObj,totalRow, Constant.FAIL);
			int skipStep=getStepCountBasedOnStatus(workBookObj,sheetObj,totalRow, Constant.SKIP);
			int errorStep=getStepCountBasedOnStatus(workBookObj,sheetObj,totalRow, Constant.ERROR);
			int warnStep=getStepCountBasedOnStatus(workBookObj,sheetObj,totalRow, Constant.WARNING);
			int fatalStep=getStepCountBasedOnStatus(workBookObj,sheetObj,totalRow, Constant.FATAL);

			Result.setTotalStep(infoStep+passStep+failStep+skipStep+errorStep+warnStep+fatalStep);
			Result.setPassedStep(passStep+infoStep);
			Result.setFailedStep(failStep);
			Result.setSkippedStep(skipStep);
			Result.setErrorStep(errorStep);
			Result.setWarningStep(warnStep);
			Result.setFatalStep(fatalStep);

			


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int getStepCountBasedOnStatus(Workbook workBookObj, Sheet sheetObj, int totalRow, String status) {
		int count=0;
		for (int i = 1; i <= totalRow; i++) {
			String tempDesc= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 2);
			if(tempDesc!=""){
				String tempStatus= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 3);
				if(tempStatus.equals(status)){
					count++;
				}
			}
		}
		return count;
	}


	private static int getScenarioCountBasedOnStatus(Workbook workBookObj,Sheet sheetObj,int totalRow,String status){
		int count=0;
		for (int i = 1; i <= totalRow; i++) {
			String tempScenario= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 1);
			if(tempScenario!=""){
				String tempStatus= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 3);
				if(tempStatus.equals(status)){
					count++;
				}
			}
		}
		return count;
	}

	private static void updateScenarioStatusBasedOnSteps(Workbook workBookObj,Sheet sheetObj,int totalRow,String status){
		try{
			File file=new File(reportPath);
			for (int i = 1; i <= totalRow; i++) {
				//System.out.println("i loop"+i+" : " +status);
				int nextScenarioRow=0;
				String tempScenario="",tempStatus="";
				for(int j=i+1;j<=totalRow;j++){
					tempScenario= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, j, 1);
					if(tempScenario!=""){
						nextScenarioRow=j;
						break;

					}else if(j==totalRow){
						nextScenarioRow=totalRow;
						break;
					}
				}
				for(int k=i+1;k<=nextScenarioRow;k++){
					tempStatus= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, k, 3);
					if(tempStatus.equals(status)){
						//System.out.println("k loop: updating status of scenario"+DataTable.getCellValue(workBookObj, sheetObj.getRow(i).getCell(1))+" : " +status);
						//sheetObj.getRow(i).getCell(3).setCellValue(status);
						DataTable_Static.setCellValueCoordinates(file, workBookObj, sheetObj, i, 3, status);
						break;
					}
				}
				if(nextScenarioRow!=0)
					i=nextScenarioRow-1;
				else
					i=totalRow;
			}


		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String formattedStartTimeCellValue(Workbook workBookObj,Sheet sheetObj,int i) {
		String startTime="";
		String startTime_str= DataTable_Static.getCellValueCoordinates(workBookObj, sheetObj, i, 4);
		//System.out.println(row+" : "+startTime_str);
		if(!startTime_str.equals("")){
			if(startTime_str.contains(":")){
				startTime=startTime_str;
			}else{
				long startTimeLngVal=Long.parseLong(startTime_str);
				if(startTimeLngVal>0){
					startTime= Util.convertToHHMMSS(startTimeLngVal);
					sheetObj.getRow(i).getCell(4).setCellValue(startTime);
				}else{
					sheetObj.getRow(i).getCell(4).setCellValue("");
				}
			}
		}


		return startTime;
	}

	private static synchronized String getStatusTextStyle(String status){
		String font_color_val="blue";
		//Setting up the font color based on status value
		if(status.contains(Constant.PASS)){
			font_color_val="green";
		}else if(status.contains(Constant.FAIL)){
			font_color_val="#F44336";
		}else if(status.contains(Constant.INFO)){
			font_color_val="blue";
		}else if(status.contains(Constant.WARNING)){
			font_color_val="orange";
		}else if(status.contains(Constant.SKIP)){
			font_color_val="brown";
		}else if(status.contains(Constant.ERROR)){
			font_color_val="#ec407a";
		}else if(status.contains(Constant.FATAL)){
			font_color_val="#B71C1C";
		}
		return "style='color:"+font_color_val+";'";
	}

	private static synchronized String getDescriptionTextStyle(String status){
		String style="style='word-break:break-all;max-width:700px;";
		
		String font_color_val="gray";
		//Setting up the font color based on status value
		if(status.contains(Constant.INFO)){
			font_color_val="cornflowerblue";
		}else if(status.contains(Constant.FATAL)){
			font_color_val="#B71C1C;";
		}
		style=style+"color:"+font_color_val+";'";
		return style; 
	}

	private static synchronized String getScenarioRowStyle(String scenario,String status){
		String color_val="aliceblue";
		//Setting up the background color based on status value
		if(!scenario.equals("")){
			if(status.contains(Constant.FAIL)){
				color_val="lightpink";
			}else if(status.contains(Constant.SKIP)){
				color_val="lightgray";
			}else if(status.contains(Constant.PASS)){
				color_val="#CCFFCC";
			}else if(status.contains(Constant.WARNING)){
				color_val="#FFFFCC";
			}else if(status.contains(Constant.ERROR)){
				color_val="#FDC7E3";
			}else if(status.contains(Constant.FATAL)){
				color_val="#FFCEAD";
			}
		}
		return "style='background: "+color_val+";'";
	}



}
