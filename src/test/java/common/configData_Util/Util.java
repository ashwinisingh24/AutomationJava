package common.configData_Util;

import common.customReporting.CustomReporter;
import common.seleniumExceptionHandling.CustomExceptionHandler;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Util {

	public static String getFirstDay(String format,String mmYY){
		SimpleDateFormat form = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH,Integer.parseInt(mmYY.substring(0, 2))-1);
		cal.set(Calendar.DAY_OF_MONTH,1);
		cal.set(Calendar.YEAR,Integer.parseInt("20"+mmYY.substring(2, 4)));
		return form.format(cal.getTime());
	}

	public static String getLastDay(String format,String mmYY){
		SimpleDateFormat form = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH,Integer.parseInt(mmYY.substring(0, 2))-1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		String currentYear=cal.get(Calendar.YEAR)+"";
		cal.set(Calendar.YEAR,Integer.parseInt(currentYear.substring(0, 2)+mmYY.substring(2, 4)));
		return form.format(cal.getTime());
	}

	public static String getTimeStamp(){
		return (new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss_S")).format(new Date());
	}

	public static String convertToHHMMSS(long millis) {
		String hhmmss="";
		try{
			DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(millis);
			hhmmss=formatter.format(calendar.getTime());
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return hhmmss;
	}

	public static String timeConversion(long seconds) {
		final int MINUTES_IN_AN_HOUR = 60;
		final int SECONDS_IN_A_MINUTE = 60;
		int minutes = (int) (seconds / SECONDS_IN_A_MINUTE);
		seconds -= minutes * SECONDS_IN_A_MINUTE;
		int hours = minutes / MINUTES_IN_AN_HOUR;
		minutes -= hours * MINUTES_IN_AN_HOUR;
		return prefixZeroToDigit(hours) + ":" + prefixZeroToDigit(minutes) + ":" + prefixZeroToDigit((int)seconds);
	}

	private static String prefixZeroToDigit(int num){
		int number=num;
		if(number<=9){
			String sNumber="0"+number;
			return sNumber;
		}
		else
			return ""+number;
	}

	public static String removeCommas(String val){
		if(val.contains(",")){
			val=val.replaceAll(",", "");
		}
		return val;
	}

	public static BigDecimal BD(String ip){
		BigDecimal bd=null;
		try {
			bd=new BigDecimal(removeCommas(ip));
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return bd;
	}

	public static String getDownloadedFilePath(String fileNameContains) {
		String filePath=null;
		try{
			String folderPath= Constant.getDownloadsPath();
			File dir= new File(folderPath);
			if (dir.isDirectory()) { 
				File[] children = dir.listFiles();
				for (int i = 0; i < children.length; i++) { 
					if (children[i].getName().contains(fileNameContains)) { 
						filePath=folderPath+"\\"+children[i].getName();
						break;
					} 
				} 
			} 		
		}catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return filePath;
	}

	public static boolean deleteFolderContentRecursively(File dir,String skipDirectory) {
		boolean bool=false;
		try{
			if (dir.isDirectory()) { 
				File[] children = dir.listFiles();
				for (int i = 0; i < children.length; i++) { 
					boolean success = deleteFolderContentRecursively(children[i],skipDirectory);
					if (!success) { 
						return false; 
					} 
				} 
			} 
			// either file or an empty directory
			if(skipDirectory!=null && dir.getName().equals(skipDirectory)){
				//System.out.println("Skipping file or directory : " + dir.getName());
				bool=true;
			}else{
				//System.out.println("Removing file or directory : " + dir.getName());
				bool=dir.delete();
			}
		}catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return bool;
	}

	public static boolean deleteSpecificFile(String absoluteFilePath) {
		boolean bool=false;
		try{
			File file=new File(absoluteFilePath);
			bool=file.delete();
			if(bool){
				System.out.println("File Deleted: "+absoluteFilePath);
			}else{
				System.out.println("File NOT Deleted: "+absoluteFilePath);
			}
		}catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return bool;
	}

	/**"Displayed Value of ["+objectAndpageDesc+"] : '" + onPage + "' has matched with expected value: '"+ expected +"'"*/
	public static void comparator_PageValues(String expected, String onPage, String objectAndpageDesc) {
		String exp= Util.removeCommas(expected).toLowerCase();
		String act= Util.removeCommas(onPage).toLowerCase();
		if(act.contains(exp)){
			CustomReporter.report(Constant.PASS, "Displayed Value of [<b>"+objectAndpageDesc+"</b>] : '<b>" + onPage + "</b>' has matched with expected value: '<b>"+ expected +"</b>'");
		}else{
			CustomReporter.report(Constant.FAIL,"Displayed Value of [<b>"+objectAndpageDesc+"</b>] : '<b>" + onPage + "</b>' has failed to match with expected value: '<b>"+ expected +"</b>'");
		}
	}

	/**"Displayed Value of ["+comparingObject+"] on ["+page1Name+"] : '" + page1Val + "' has matched with value of: ["+page2Name+"] : '" + page2Val +"'"*/
	public static void comparator_PageValues(String comparingObject,String page1Name, String page1Val, String page2Name, String page2Val) {
		page1Val= Util.removeCommas(page1Val);
		page2Val= Util.removeCommas(page2Val);
		if(compare(page1Val,page2Val)){
			CustomReporter.report(Constant.PASS, "Displayed Value of [<b>"+comparingObject+"</b>] on [<b>"+page1Name+"</b>] : '<b>" + page1Val + "</b>' has matched with value of: [<b>"+page2Name+"</b>] : '<b>" + page2Val +"</b>'");
		}else{
			CustomReporter.report(Constant.FAIL, "Displayed Value of [<b>"+comparingObject+"</b>] on [<b>"+page1Name+"</b>] : '<b>" + page1Val + "</b>' has NOT matched with value of: [<b>"+page2Name+"</b>] : '<b>" + page2Val +"</b>'");
		}
	}

	/**"Displayed Value of ["+comparingObject+"] on ["+page1Name+"] : '" + page1Val + "' has matched with value of: ["+page2Name+"] : '" + page2Val +"'"*/
	public static void comparator_NonNumbers(String comparingObject,String page1Name, String page1Val, String page2Name, String page2Val) {
		if(page1Val.toLowerCase().contains(page2Val.toLowerCase()) || page2Val.toLowerCase().contains(page1Val.toLowerCase())){
			CustomReporter.report(Constant.PASS, "Displayed Value of [<b>"+comparingObject+"</b>] on [<b>"+page1Name+"</b>] : '<b>" + page1Val + "</b>' has matched with value of: [<b>"+page2Name+"</b>] : '<b>" + page2Val +"</b>'");
		}else{
			CustomReporter.report(Constant.FAIL, "Displayed Value of [<b>"+comparingObject+"</b>] on [<b>"+page1Name+"</b>] : '<b>" + page1Val + "</b>' has NOT matched with value of: [<b>"+page2Name+"</b>] : '<b>" + page2Val +"</b>'");
		}
	}
	
	public static void main(String[] args) {

		System.out.println(compare("2.37", "-2.37".substring(1,"-2.37".length())));
		
	}

	private static boolean compare(String d1,String d2) {
		try{
			double n1=Double.parseDouble(d1);
			double n2=Double.parseDouble(d2);
			/*
			 if((100d>=n1 && n1>=0d) && (100d>=n2 && n2>=0d)){
			 	if(d1.toString().contains(d2.toString()) || d2.toString().contains(d1.toString())){
					return true;
				}
			}else if((-1d>=n1 && n1>=-100d) && (-1d>=n2 && n2>=-100d)){
				if(d1.toString().contains(d2.toString()) || d2.toString().contains(d1.toString())){
				return true;
				}
			}*/

			if((1d>=n1 && n1>=-1d) && (1d>=n2 && n2>=-1d)){
				BigDecimal bd1=new BigDecimal(d1);
				BigDecimal bd2=new BigDecimal(d2);
				d1 = bd1.setScale(2, RoundingMode.HALF_UP).toPlainString();
				d2 = bd2.setScale(2, RoundingMode.HALF_UP).toPlainString();
				if(d1.equals(d2)){
					return true;
				}    
			}else{
				double temp=n1-n2;
				if(1d>=temp && -1d<=temp){
					return true;
				}
			}
		}catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return false;
	}

	/**"Expected value of [<b>"+desc+"</b>] : '<b>" + expected + "</b>' and on Page value displayed is '<b>"+onPage +"</b>'"*/
	public static void comparator_NonNumbers(String expected, String onPage, String desc) {
		if(expected.toLowerCase().contains(onPage.toLowerCase()) || onPage.toLowerCase().contains(expected.toLowerCase())){
			CustomReporter.report(Constant.PASS, "Expected value of [<b>"+desc+"</b>] : '<b>" + expected + "</b>' and on Page value displayed is '<b>"+onPage +"</b>'");
		}else{
			CustomReporter.report(Constant.FAIL,"Expected value of [<b>"+desc+"</b>] : '<b>" + expected + "</b>' and on Page value displayed is '<b>"+onPage +"</b>'");
		}
	}

	
	/**"Calulated value of ["+desc+"] : '" + calculated + "' and on Page value displayed is '"+onPage +"'"*/
	public static void comparator(String calculated, String onPage, String desc) {

		if(onPage.equals("-")){
			onPage=onPage.replace("-", "0");
		}

		if(compare(Util.removeCommas(calculated), Util.removeCommas(onPage))){
			CustomReporter.report(Constant.PASS, "Calculated value of [<b>"+desc+"</b>] : '<b>" + calculated + "</b>' and on Page value displayed is '<b>"+onPage +"</b>'");
		}else{
			CustomReporter.report(Constant.FAIL,"Calculated value of [<b>"+desc+"</b>] : '<b>" + calculated + "</b>' and on Page value displayed is '<b>"+onPage +"</b>'");
		}

	}

	/** Does only case sensitive matching
	 * "Values of ["+desc+"] are properly displayed as "+expected*/
	public static void comparator_List(List<String> expected, List<String> onPage, String desc) {
		try{
			if(onPage.containsAll(expected)){
				CustomReporter.report(Constant.PASS, "Values of [<b>"+desc+"</b>] are properly displayed as <b>"+expected+"</b>");
			} else {
				CustomReporter.report(Constant.FAIL, "Values of [<b>"+desc+"</b>] are not matching expected: <b>"+expected+"</b> actual: <b>"+onPage+"</b>");
			}
		}catch (Exception e) {
			CustomReporter.report(Constant.ERROR, "Values of [<b>"+desc+"</b>] are not matching expected: <b>"+expected+"</b> actual: <b>"+onPage+"</b>");
			new CustomExceptionHandler(e);
		}
	}



}
