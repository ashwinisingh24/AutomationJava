package common.seleniumExceptionHandling;

import common.configData_Util.Constant;
import common.driverManager.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

/**
 * Use this class to work with standard Tables in any web page, <br>
 * <table><tbody> 
 * <tr> <th>Heading1</th> <th>Heading2</th> </tr>
 * <tr> <td>Data1.1</td> <td>Data1.2</td> </tr>
 * <tr> <td>Data2.1</td> <td>Data2.2</td> </tr>
 * </tbody> </table>
 */
public class WebTable extends WebUtils {
	private By locator;
	public WebTable(By locator) {
		this.locator=locator;
	}


	private WebElement getWebElement() {
		WebElement elem=null;
		try{
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, Constant.wait);
			elem=wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator)));
		}catch (Exception e) {
			new CustomExceptionHandler(e);
		}

		return elem;
	}

	/**
	 * use this method to get the Row count of a "table"
	 * 
	 * @return int rowCount the count of tr, and in case of exception will
	 *         return -1
	 */
	public int getRowCount() {

		int rowCount = -1;
		try {
			List<WebElement> listTR = getWebElement().findElements(By.tagName("tr"));
			rowCount = listTR.size();
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return rowCount;
	}

	/**
	 * use this method to get the Column count in a Row of a "table"
	 * 
	 * @param rowNum
	 *            The Row number, from which the column count is need to be
	 *            fetched
	 * @return int colCount the count of td inside tr, and in case of exception
	 *         will return -1
	 */
	public int getColCount(int rowNum) {

		int colCount = -1;
		try {
			List<WebElement> listTR = getWebElement().findElements(By.tagName("tr"));
			// List<WebElement>
			// listTD_TH=listTR.get(rowNum-1).findElements(By.xpath("th|td"));
			List<WebElement> listTD_TH = listTR.get(rowNum - 1).findElements(By.cssSelector("th,td"));
			colCount = listTD_TH.size();
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return colCount;
	}

	/**
	 * use this method to get the String innerText of the cell pointed by rowNum
	 * and colNum
	 * 
	 * @param rowNum
	 *            The Row number of cell
	 * @param rowNum
	 *            The Column number of cell
	 * @return The innerText displayed in the cell, and in case of exception
	 *         will return null
	 */
	public String getCellText(int rowNum, int colNum) {

		String cellTxt = null;
		try {
			List<WebElement> listTR = getWebElement().findElements(By.tagName("tr"));
			// List<WebElement>
			// listTD_TH=listTR.get(rowNum-1).findElements(By.xpath("th|td"));
			List<WebElement> listTD_TH = listTR.get(rowNum - 1).findElements(By.cssSelector("th,td"));
			WebElement cellObj = listTD_TH.get(colNum - 1);
			javaScriptScrollInto_BOTTOM_ViewAndHighlight(cellObj);
			cellTxt = cellObj.getText().replace("\r\n", " ").replace("\n", " ");
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return cellTxt;
	}

	/**
	 * use this method to get the List of WebElements matching the passed
	 * Tag(for e.g. "img","a" etc) present in the cell(pointed by passed row and
	 * col num)
	 * 
	 * @param rowNum
	 *            The Row number of cell
	 * @param rowNum
	 *            The Column number of cell
	 * @param tag
	 *            The tag of desired webelement, it could be any valid html tag,
	 *            common examples are "a","img".
	 * @return The matching tags inside the passed cell, it will skip all
	 *         hierarchy and fetch the desired objects list, and in case of
	 *         exception will return null
	 */
	public List<WebElement> getChildObjects(int rowNum, int colNum, String tag) {

		List<WebElement> childObjects = null;
		try {
			List<WebElement> listTR = getWebElement().findElements(By.tagName("tr"));
			// List<WebElement>
			// listTD_TH=listTR.get(rowNum-1).findElements(By.xpath("th|td"));
			List<WebElement> listTD_TH = listTR.get(rowNum - 1).findElements(By.cssSelector("th,td"));
			childObjects = listTD_TH.get(colNum - 1).findElements(By.tagName(tag));
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return childObjects;
	}

	/**
	 * use this method to get the single WebElement matching the passed Tag(for
	 * e.g. "img","a" etc) present in the cell(pointed by passed row and col
	 * num) based on passed index
	 * 
	 * @param rowNum
	 *            The Row number of cell
	 * @param rowNum
	 *            The Column number of cell
	 * @param tag
	 *            The tag of desired webelement, it could be any valid html tag,
	 *            common examples are "a","img".
	 * @param index
	 *            The index of desired webelement from the list, index starts
	 *            with 0
	 * @return The element which is present on the passed index, matching the
	 *         tag in a cell pointed by passed row and col nums, and in case of
	 *         exception will return null
	 */
	public WebElement getChildObject(int rowNum, int colNum, String tag, int index) {

		WebElement childObject = null;
		try {
			List<WebElement> listTR = getWebElement().findElements(By.tagName("tr"));
			// List<WebElement>
			// listTD_TH=listTR.get(rowNum-1).findElements(By.xpath("th|td"));
			List<WebElement> listTD_TH = listTR.get(rowNum - 1).findElements(By.cssSelector("th,td"));
			childObject = listTD_TH.get(colNum - 1).findElements(By.tagName(tag)).get(index);
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return childObject;
	}

	/**
	 * use this method to get the count of WebElements matching the passed
	 * Tag(for e.g. "img","a" etc) present in the cell(pointed by passed row and
	 * col num)
	 * 
	 * @param rowNum
	 *            The Row number of cell
	 * @param rowNum
	 *            The Column number of cell
	 * @param tag
	 *            The tag of desired webelement, it could be any valid html tag,
	 *            common examples are "a","img".
	 * @return The count of webelement, matching the tag in a cell pointed by
	 *         passed row and col nums, and in case of exception will return -1
	 */
	public int getChildObjectsCount(int rowNum, int colNum, String tag) {

		int childObjectsCount = -1;
		try {
			List<WebElement> listTR = getWebElement().findElements(By.tagName("tr"));
			// List<WebElement>
			// listTD_TH=listTR.get(rowNum-1).findElements(By.xpath("th|td"));
			List<WebElement> listTD_TH = listTR.get(rowNum - 1).findElements(By.cssSelector("th,td"));
			if(colNum<=getColCount(rowNum)){
				String innerHTML=listTD_TH.get(colNum - 1).getAttribute("innerHTML");
				if(innerHTML.contains(tag)){
					childObjectsCount = listTD_TH.get(colNum - 1).findElements(By.tagName(tag)).size();	
				}
			}
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return childObjectsCount;
	}

	/**
	 * use this method to get the Row number which has the passed text value
	 * 
	 * @param visibleTxt
	 *            The visible text, for which the row count is needed to be
	 *            found
	 * @return The first row number that has the text which matches the passed
	 *         parameter value, and in case of exception will return -1
	 * 
	 */
	public int getRowWithCellText(String visibleTxt) {
		int rowNum = -1;
		String temp="";
		try {
			List<WebElement> listTR = getWebElement().findElements(By.tagName("tr"));
			for (int row = 0; row < listTR.size(); row++) {
				temp = listTR.get(row).getText();
				if (temp.toLowerCase().contains(visibleTxt.toLowerCase())) {
					rowNum = row+1;
					break;
				}
			}
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return rowNum;
	}


	/**
	 * use this method to get the Row number which has the passed text value
	 * 
	 * @param startRow
	 *            the begin row count, inclusive.
	 * @param visibleTxt
	 *            The visible text, for which the row count is needed to be
	 *            found
	 * @return The first row number that has the text which matches the passed
	 *         parameter value, and in case of exception will return -1
	 * 
	 */
	public int getRowWithCellText(int startRow,String visibleTxt) {
		int rowNum = -1;
		String temp="";
		try {
			List<WebElement> listTR = getWebElement().findElements(By.tagName("tr"));
			for (int row = startRow-1; row < listTR.size(); row++) {
				temp = listTR.get(row).getText();
				if (temp.toLowerCase().contains(visibleTxt.toLowerCase())) {
					rowNum = row+1;
					break;
				}
			}
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return rowNum;
	}

	/** Call getColumnNumber method after running this method
	 * @headerRow Starts with 1
	 * */
	private List<String> headerColumnList;
	public WebTable initHeaderColumnList(int headerRow) {
		try{
			headerColumnList= new ArrayList<String>();
			List<WebElement> listTR = getWebElement().findElements(By.tagName("tr"));
			List<WebElement> thList=listTR.get(headerRow-1).findElements(By.cssSelector("th,td"));
			for (WebElement temp : thList) {
				headerColumnList.add(temp.getText().replace("\r\n", " ").replace("\n", " "));
			}	

		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return this;
	}

	/** Call initHeaderColumnList method prior to calling this method
	 * @return -1 if column name is not found
	 * */
	public int getColumnNumber(String colName) {
		int foundColNum=-1;
		try{
			if(headerColumnList!=null){
				for (int colNum = 0; colNum < headerColumnList.size(); colNum++) {
					if(headerColumnList.get(colNum).equalsIgnoreCase(colName)){
						foundColNum=colNum+1;
						break;
					}
				}
			}
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return foundColNum;
	}
}
