package common.xlUtil;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataTable_NonStatic {
	private String workbookPath;
	private Workbook workbook;
	private Sheet sheet;
	private List<String> colHeader;
	public DataTable_NonStatic(String workbookPath,String sheetName) {
		this.workbookPath=workbookPath;
		this.workbook=getWorkbookObject(workbookPath);
		this.sheet=getSheetObject(workbook, sheetName);
		initColHeader();
	}

	public DataTable_NonStatic(String workbookPath,int sheetIndex) {
		this.workbookPath=workbookPath;
		this.workbook=getWorkbookObject(workbookPath);
		this.sheet=getSheetObject(workbook, sheetIndex);
		initColHeader();
	}


	private void initColHeader() {
		colHeader = new ArrayList<String>();
		Row row = sheet.getRow(0);
		if(row!=null){
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Cell cell=row.getCell(j);
				if(cell!=null){
					colHeader.add(getCellValue(cell));
				}else{
					colHeader.add("");
				}
			}
		}
	}

	private int getColHeaderNum(String colName) {
		for (int j = 0; j < colHeader.size(); j++) {
			if(colHeader.get(j).equalsIgnoreCase(colName)){
				return j;
			}
		}
		return -1;
	}


	public  void killExcelProcess() {
		try{
			//Runtime.getRuntime().exec("cmd /c taskkill /f /im "+Constant.chromeDriverFileName_Win);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private Workbook getWorkbookObject(String workBookPath) {
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

	private Sheet getSheetObject(Workbook workBookObj,String sheetName) {
		Sheet sheetObj=null;
		try{
			sheetObj = workBookObj.getSheet(sheetName);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sheetObj;
	}	

	private  Sheet getSheetObject(Workbook workBookObj,int sheetIndex) {
		Sheet sheetObj=null;
		try{
			sheetObj = workBookObj.getSheetAt(sheetIndex);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sheetObj;
	}	

	public int getRowCount() {
		int rowCount=0;
		try{
			rowCount=sheet.getPhysicalNumberOfRows();
		}catch(Exception e){
			e.printStackTrace();
		}
		return rowCount;
	}

	public String getValueCoordinates(int rowNum,int colNum){
		String val="";
		Row row=null;
		Cell cell=null;
		try{
			row = sheet.getRow(rowNum);
			if(row!=null){
				cell=row.getCell(colNum);
				if(cell!=null){
					val=getCellValue(cell);
				}
			}	

		}catch(Exception e){
			e.printStackTrace();
		}
		return val;
	}

	public void setValueCoordinates(int rowNum,int colNum,String data){
		File file = null;
		FileOutputStream outputStream=null;
		Row row=null;
		Cell cell=null;
		try{
			row = sheet.getRow(rowNum);
			if (row == null) {
				sheet.createRow(rowNum);
				row=sheet.getRow(rowNum);
			}
			cell=row.getCell(colNum);
			if (cell == null) {
				cell = row.createCell(colNum);
				cell.setCellValue(data);
			} else {
				cell.setCellValue(data);
			}
			file = new File(workbookPath);
			outputStream = new FileOutputStream(file);
			workbook.write(outputStream);
			outputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}

	}	

	public void setValue(int rowNum,String colName,String data){
		File file = null;
		FileOutputStream outputStream=null;
		Row row=null;
		Cell cell=null;
		try{
			int colNum=getColHeaderNum(colName);
			row=sheet.getRow(rowNum);
			if (row == null) {
				sheet.createRow(rowNum);
				row=sheet.getRow(rowNum);
			}

			cell=row.getCell(colNum);
			if (cell == null) {
				cell = sheet.getRow(rowNum).createCell(colNum);
				cell.setCellValue(data);
			} else {
				cell.setCellValue(data);
			}
			file = new File(workbookPath);
			outputStream = new FileOutputStream(file);
			workbook.write(outputStream);
			outputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public String getValue(int rowNum,String colName) {
		String val="";
		Row row=null;
		Cell cell=null;
		try{
			row = sheet.getRow(rowNum);
			if(row!=null){
				int colNum=getColHeaderNum(colName);
				cell=row.getCell(colNum);
				if(cell!=null){
					val=getCellValue(cell);
				}
			}	

		}catch(Exception e){
			e.printStackTrace();
		}
		return val;
	}


	private String getCellValue(Cell cell){
		String val="";
		if(cell!=null){
			DataFormatter objDefaultFormat = new DataFormatter();
			FormulaEvaluator objFormulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
			objFormulaEvaluator.evaluate(cell); // This will evaluate the cell, And any type of cell will return string value
			val = objDefaultFormat.formatCellValue(cell,objFormulaEvaluator);
		}
		return val.trim();
	}

}
