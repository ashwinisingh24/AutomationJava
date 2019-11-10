package common.customReporting;

import common.configData_Util.Constant;
import common.configData_Util.Util;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

class ReportingHistoryHTML {

	private static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMMdd_HHmmss");
	private final static String pickerStartPlaceholder = "<!-- PICKER1_START -->";
	private final static String pickerEndPlaceholder = "<!-- PICKER1_END -->";
	private final static String rowPlaceholder = "<!-- INSERT_ROW -->";
	private final static String totalRowsPlaceholder= "<!-- TOTAL ROWS -->";
	private final static String paginationPlaceholder_TOP = "<!-- INSERT_TOP_PAGINATION -->";
	private final static String paginationPlaceholder_BOTTOM = "<!-- INSERT_BOTTOM_PAGINATION -->";
	private static String reportingHistoryFolderPath= Constant.getReportingHistoryFolderPath();
	private static String htmlreportingHistoryFolderPath= Constant.getReportingHistoryHTMLfolderPath();

	public static String styleHTML="<style>"
			+ "body{font-family: calibri;background-color: #DFDFDF;}"
			+ "h2{color:midnightblue;}"
			+ "table{border: 1px solid #AAAAAA;}"
			+ "th{border-bottom: 1px solid #AAAAAA;}"
			+ "tr{background: aliceblue;}"
			+ "td{border: 1px solid lightgray;padding: 0px;margin: 0px;}"
			+ "p{max-height: 125px;overflow: auto;}"
			+ ".dashboard-header{border-radius: 20px; background: #8a8a8a; color: #fff; padding: 4px 0px; text-align: center; font-weight: 600;}"
			+ ".properties-div{border: solid black 0px; margin: 0px; padding: 0px; width: max-content; color:midnightblue; text-align: left;}"
			+ ".properties-span{border: solid black 0px; margin: 5px; padding: 3px; }"
			+ "</style>";



	public static void manageHTML() {
		//deleteOlderHTMLFile();
		Util.deleteSpecificFile(reportingHistoryFolderPath+"/index.html");
		Util.deleteFolderContentRecursively(new File(htmlreportingHistoryFolderPath), Constant.reportingHistoryHTMLFolderName);
		createHTML();
	}

	private static void createHTML() {
		try{
			String tempFolderName,customReportPath,customReportPathHTML,extentReportPathHTML;
			String reportBase="<html>" + "<head>"
					+ "<link rel='icon href='http://www.seleniumhq.org/selenium-favicon.ico' type='image/vnd.microsoft.icon'>"
					+ "<title>Test Result History</title>"
					+ styleHTML
					+ "<head>"
					+ "<body><center>"
					+ "<h2>IOTRON Test Result History</h2>"
					+ "<i>Total rows "+totalRowsPlaceholder+" and Displaying "+ Constant.reportingHistoryFolderPerPage+" rows per page.<br/>"
					+paginationPlaceholder_TOP+"</i>"
					+ "<table border='0'>"
					+ "<tbody>"

				+ "<tr>"
				+ "<td style='font-weight: bold; text-align: center;'>"+ "Sr#"+ "</td>"
				+ "<td style='font-weight: bold; text-align: center;'>"+ "Folder"+ "</td>"
				+ "<td style='font-weight: bold; text-align: center;'>"+ "DashBoards"+ "</td>"
				+ "<td style='font-weight: bold; text-align: center;'>"+ "Quick Report View"+ "</td>"
				+ "<td style='font-weight: bold; text-align: center;'>"+ "Reports Link"+ "</td>"
				+ "</tr>"

				+ rowPlaceholder 


				+ "</tbody></table>"
				+"<i>"+paginationPlaceholder_BOTTOM+"</i>"
				;


			List<Date> foldersNameList= ReportingHistoryManager.getFoldersNameList();
			Collections.sort(foldersNameList);


			int foldersCount=foldersNameList.size();
			int totalPages=	1;
			int diff= Constant.reportingHistoryFolderPerPage;
			if(foldersCount > diff){
				totalPages=(foldersCount + diff - 1) / diff;
			}

			String data="Page ";
			for (int page = 1; page <= totalPages; page++) {
				data=data+"<a href='page"+page+".html'>"+page+"</a> ";
			}
			reportBase=reportBase.replaceFirst(totalRowsPlaceholder,foldersCount+totalRowsPlaceholder);
			reportBase=reportBase.replaceFirst(paginationPlaceholder_TOP,data+paginationPlaceholder_TOP);
			reportBase=reportBase.replaceFirst(paginationPlaceholder_BOTTOM,data+paginationPlaceholder_BOTTOM);
			
			diff=diff-1;
			int startDiff=0,start=foldersCount,end=0,srNum=1;
			for (int page = 1; page <= totalPages; page++) {
				String reportIn= reportBase;
				start=start-startDiff-1;
				startDiff=diff;
				end=(start-diff)<=0?0:(start-diff);
				for (int i = start; i >=end ; i--) {
					tempFolderName=dateFormat.format(foldersNameList.get(i));
					customReportPath= reportingHistoryFolderPath+ "/" +tempFolderName+ "/" + Constant.resultFolderName+ "/" + Constant.resultHTMLFileName;
					customReportPathHTML= tempFolderName+ "/" + Constant.resultFolderName+ "/" + Constant.resultHTMLFileName;
					extentReportPathHTML= tempFolderName+ "/" + Constant.resultFolderName+ "/" + Constant.resultExtentHTMLFileName;

					reportIn = reportIn.replaceFirst(rowPlaceholder,"<tr>"
							+ "<td>" + srNum++ + "</td>"
							+ "<td>" + tempFolderName + "</td>"
							+ "<td>" + getDashboardContent(customReportPath) + "</td>"
							+ "<td width='55%'> "
							+ "<iframe src='../"+customReportPathHTML+"#1' width='99%'></iframe> "
							+ "</td>"
							+ "<td>"
							+ " <a href='../"+customReportPathHTML+"#1' target ='_blank'>" +"Custom"+"</a> <br/>"
							+ " <a href='../"+extentReportPathHTML+"' target ='_blank'>" +"Extent"+"</a> </td>"
							+ "</tr>" 
							+ rowPlaceholder);


				}
				Files.write(Paths.get(htmlreportingHistoryFolderPath+"/page"+page+".html"),reportIn.getBytes(),StandardOpenOption.CREATE);
			}


		}catch(Exception e){
			e.printStackTrace();
		}		
	}

	private static String getDashboardContent(String customReportPath){
		String dashBoardContent="";
		try {
			File f=new File(customReportPath);
			if(f.exists()){
				String wholeHtmlContent = IOUtils.toString(new FileInputStream(f));
				int pickerStart=wholeHtmlContent.indexOf(pickerStartPlaceholder);
				int pickerEnd=wholeHtmlContent.indexOf(pickerEndPlaceholder);
				dashBoardContent=wholeHtmlContent.substring(pickerStart, pickerEnd);
			}else{
				System.err.println("The system cannot find the file specified :{"+customReportPath+"}");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dashBoardContent;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		manageHTML();
	}

}
