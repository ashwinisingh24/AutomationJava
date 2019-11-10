/**
 * 
 */
package common.xlUtil;

/**
 * @author SRajawat
 *
 */

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DataTable_Static {
	public static void main(String[] args) {
		killExcelProcess();
	}

	public static void killExcelProcess() {
		try{
			//Runtime.getRuntime().exec("cmd /c taskkill /f /im "+common.configData_Util.Constant.chromeDriverFileName_Win);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static Workbook getWorkbookObject(String workBookPath) {
		File file = null;
		FileInputStream inputStream =null;
		Workbook workBookObj=null;
		try{
			file = new File(workBookPath);
			inputStream = new FileInputStream(file);
			workBookObj = WorkbookFactory.create(inputStream);
		}catch(Exception e){
			e.printStackTrace();
		}
		return workBookObj;
	}

	public static Sheet getSheetObject(Workbook workBookObj,String sheetName) {
		Sheet sheetObj=null;
		try{
			sheetObj = workBookObj.getSheet(sheetName);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sheetObj;
	}	
	
	public static Sheet getSheetObject(Workbook workBookObj,int sheetIndex) {
		Sheet sheetObj=null;
		try{
			sheetObj = workBookObj.getSheetAt(sheetIndex);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sheetObj;
	}	
	
	
	
	public static int getRowCount(String Constant,String sheetName) {
		File file = null;
		FileInputStream inputStream =null;
		Workbook workBookObj = null;
		Sheet sheetObj=null; 
		int rowCount=0;
		try{
			file = new File(Constant);
			inputStream = new FileInputStream(file);
			workBookObj = WorkbookFactory.create(inputStream);
			sheetObj = workBookObj.getSheet(sheetName);
			rowCount=sheetObj.getPhysicalNumberOfRows();
			inputStream.close();
			workBookObj.close();

		}catch(Exception e){
			e.printStackTrace();
		}
		return rowCount;
	}
	
	public static String getValueCoordinates(String Constant,String sheetName,int rowNum,int colNum){
		File file = null;
		FileInputStream inputStream =null;
		String val="";
		Workbook workBookObj = null;
		Sheet sheetObj=null; 
		Row row=null;
		Cell cell=null;
		try{
			file = new File(Constant);
			inputStream = new FileInputStream(file);
			workBookObj = WorkbookFactory.create(inputStream);
			sheetObj = workBookObj.getSheet(sheetName);

			row = sheetObj.getRow(rowNum);
			if(row!=null){
				cell=row.getCell(colNum);
				if(cell!=null){
					val= DataTable_Static.getCellValue(workBookObj,cell);
				}
			}	

			inputStream.close();
			workBookObj.close();

		}catch(Exception e){
			e.printStackTrace();
		}
		return val;
	}

	public static void setValueCoordinates(String Constant,String sheetName,int rowNum,int colNum,String data){
		File file = null;
		FileInputStream inputStream =null;
		FileOutputStream outputStream=null;
		Workbook workBookObj = null;
		Sheet sheetObj=null; 
		Row row=null;
		Cell cell=null;
		try{
			file = new File(Constant);
			inputStream = new FileInputStream(file);
			workBookObj = WorkbookFactory.create(inputStream);
			sheetObj = workBookObj.getSheet(sheetName);
			row = sheetObj.getRow(rowNum);
			if (row == null) {
				sheetObj.createRow(rowNum);
				row=sheetObj.getRow(rowNum);
			}
			cell=row.getCell(colNum);
			if (cell == null) {
				cell = row.createCell(colNum);
				cell.setCellValue(data);
			} else {
				cell.setCellValue(data);
			}
			outputStream = new FileOutputStream(file);
			workBookObj.write(outputStream);
			inputStream.close();
			outputStream.close();
			workBookObj.close();
		}catch(Exception e){
			e.printStackTrace();
		}

	}	

	public static void setCellValueCoordinates(File file, Workbook workBookObj,Sheet sheetObj,int rowNum,int colNum,String data){
		FileOutputStream outputStream=null;
		Row row=null;
		Cell cell=null;
		try{
			row = sheetObj.getRow(rowNum);
			if (row == null) {
				sheetObj.createRow(rowNum);
				row=sheetObj.getRow(rowNum);
			}
			cell=row.getCell(colNum);
			if (cell == null) {
				cell = row.createCell(colNum);
				cell.setCellValue(data);
			} else {
				cell.setCellValue(data);
			}
			outputStream = new FileOutputStream(file);
			workBookObj.write(outputStream);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void UpdateExcel(String Constant,String sheetName,int rowNum,String colName,String data){
		File file = null;
		FileInputStream inputStream =null;
		FileOutputStream outputStream=null;
		Workbook workBookObj = null;
		Sheet sheetObj=null; 
		Row row=null;
		Cell cell=null;
		try{
			file = new File(Constant);
			inputStream = new FileInputStream(file);
			workBookObj = WorkbookFactory.create(inputStream);
			sheetObj = workBookObj.getSheet(sheetName);
			row = sheetObj.getRow(0);
			if(row!=null){
				for (int j = 0; j < row.getLastCellNum(); j++) {
					cell=row.getCell(j);
					if(cell!=null){
						if(DataTable_Static.getCellValue(workBookObj,cell).equalsIgnoreCase(colName)){
							row=sheetObj.getRow(rowNum);
							if (row == null) {
								sheetObj.createRow(rowNum);
								row=sheetObj.getRow(rowNum);
							}

							cell=row.getCell(j);
							if (cell == null) {
								cell = sheetObj.getRow(rowNum).createCell(j);
								cell.setCellValue(data);
							} else {
								cell.setCellValue(data);
							}

						}

					}
				}
			}
			outputStream = new FileOutputStream(file);
			workBookObj.write(outputStream);
			inputStream.close();
			outputStream.close();
			workBookObj.close();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public static String Value(String Constant,String sheetName,int rowNum,String colName) {
		File file = null;
		FileInputStream inputStream =null;
		String val="";
		Workbook workBookObj = null;
		Sheet sheetObj=null; 
		Row row=null;
		Cell cell=null;
		try{
			file = new File(Constant);
			inputStream = new FileInputStream(file);
			workBookObj = WorkbookFactory.create(inputStream);
			sheetObj = workBookObj.getSheet(sheetName);
			row = sheetObj.getRow(0);
			if(row!=null){
				for (int j = 0; j < row.getLastCellNum(); j++) {
					cell=row.getCell(j);
					if(cell!=null){
						if(DataTable_Static.getCellValue(workBookObj,cell).equalsIgnoreCase(colName)){
							row = sheetObj.getRow(rowNum);
							if(row!=null){
								cell=row.getCell(j);
								if(cell!=null){
									val= DataTable_Static.getCellValue(workBookObj,cell);
								}
							}	
							break;		
						}
					}
				}
			}
			inputStream.close();
			workBookObj.close();

		}catch(Exception e){
			e.printStackTrace();
		}
		return val;
	}

	
	public static String getCellValueCoordinates(Workbook workBookObj,Sheet sheetObj,int rowNum,int colNum){
		String val=""; 
		Row row=null;
		Cell cell=null;
		row=sheetObj.getRow(rowNum);
		if(row!=null){
			cell=row.getCell(colNum);
			val= DataTable_Static.getCellValue(workBookObj, cell);
		}
		return val;
	}
		
	public static String getCellValue(Workbook workBookObj,Cell cell){
		String val="";
		if(cell!=null){
			DataFormatter objDefaultFormat = new DataFormatter();
			FormulaEvaluator objFormulaEvaluator = workBookObj.getCreationHelper().createFormulaEvaluator();
			objFormulaEvaluator.evaluate(cell); // This will evaluate the cell, And any type of cell will return string value
			val = objDefaultFormat.formatCellValue(cell,objFormulaEvaluator);
		}
		return val.trim();
	}

}
