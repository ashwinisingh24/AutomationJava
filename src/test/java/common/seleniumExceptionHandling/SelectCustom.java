package common.seleniumExceptionHandling;

import common.configData_Util.Constant;
import common.customReporting.CustomReporter;
import common.customReporting.SnapshotManager;
import common.driverManager.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Quotes;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

class SelectCustom extends WebUtils{
	/**
	 * Clear all selected entries. This is only valid when the SELECT supports multiple selections.
	 * 
	 * @param element
	 *            The By/WebElement Object
	 *
	 * @throws UnsupportedOperationException If the SELECT does not support multiple selections
	 *
	 */
	public void deselectAll(Object element) {
		try {
			WebElement elem = new SeleniumMethods().getWebElement(element);

			Select sel = new Select(elem);
			if(sel.isMultiple()){
				if (sel.getOptions().size() > 0) {
					sel.deselectAll();
					SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
				} else {
					CustomReporter.report(Constant.FAIL, "Dropdown does not have any options");
				}
			}else{
				CustomReporter.report(Constant.FAIL, "Passed dropdown is not a MultiSelect");
			}
		} catch (Exception e) {

			new CustomExceptionHandler(e);
		}
	}
	
	/**
	 * Deselect the option at the given index. This is done by examining the "index" attribute of an
	 * element, and not merely by counting.
	 *
	 * @param element
	 *            The By/WebElement Object
	 * @param index The option at this index will be deselected
	 * @throws NoSuchElementException If no matching option elements are found
	 * @throws UnsupportedOperationException If the SELECT does not support multiple selections
	 * 
	 */
	public void deselectByIndex(Object element, int index) {
		try {
			WebElement elem = getWebElement(element);

			Select sel = new Select(elem);

			if (sel.getOptions().size() > 0) {
				sel.deselectByIndex(index);
				SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have any options");
			}

		} catch (Exception e) {

			new CustomExceptionHandler(e);
		}
	}


	/**
	 * Deselect all options that have a value matching the argument. That is, when given "foo" this
	 * would deselect an option like:
	 * 
	 * &lt;option value="foo"&gt;Bar&lt;/option&gt;
	 *
	 * @param element
	 *            The By/WebElement Object
	 *
	 *
	 * @param value The value to match against
	 * @throws NoSuchElementException If no matching option elements are found
	 * @throws UnsupportedOperationException If the SELECT does not support multiple selections
	 * 
	 */
	public void deselectByValue(Object element, String value) {
		try {
			WebElement elem = getWebElement(element);

			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				sel.deselectByValue(value);
				SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have any options");
			}
		} catch (Exception e) {

			new CustomExceptionHandler(e);
		}
	}

	/**
	 * Deselect all options that display text matching the argument. That is, when given "Bar" this
	 * would deselect an option like:
	 *
	 * &lt;option value="foo"&gt;Bar&lt;/option&gt;
	 *
	 * @param element
	 *            The By/WebElement Object
	 *
	 *
	 * @param text The visible text to match against
	 * @throws NoSuchElementException If no matching option elements are found
	 * @throws UnsupportedOperationException If the SELECT does not support multiple selections
	 * 
	 */
	public void deselectByVisibleText(Object element, String text) {
		try {
			WebElement elem = getWebElement(element);

			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				sel.deselectByVisibleText(text);
				SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have any options");
			}
		} catch (Exception e) {

			new CustomExceptionHandler(e);
		}
	}


	/**
	 * @param element
	 *            The By/WebElement Object
	 * @return Whether this select element support selecting multiple options at the same time? This
	 *         is done by checking the value of the "multiple" attribute.
	 * @throws NoSuchElementException
	 *             If no matching option elements are found
	 */
	public boolean isMultiple(Object element) {
		boolean bool=false;
		try {
			WebElement elem = getWebElement(element);
			Select sel = new Select(elem);
			bool=sel.isMultiple();
			SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return bool;
	}

	/**
	 * Select all options that have a value matching the argument. That is, when
	 * given "foo" this would select an option like:
	 *
	 * &lt;option value="foo"&gt;Bar&lt;/option&gt;
	 *
	 * @param element
	 *            The By/WebElement Object
	 * @param value
	 *            The value to match against
	 * @throws NoSuchElementException
	 *             If no matching option elements are found
	 */
	public void selectByValue(Object element, String value) {
		try {
			WebElement elem = getWebElement(element);
			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				sel.selectByValue(value);
				SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have options to select");
			}
		} catch (Exception e) {

			new CustomExceptionHandler(e);
		}
	}

	/**
	 * Deselect all options that display text containing the argument. That is, when given "Bar" this
	 * would deselect all options like:
	 *
	 * &lt;option value="foo"&gt;1. Bar&lt;/option&gt;
	 * 
	 * and in case of multiselect dropdown this would select all options that has text partially matching with the argument like:
	 * 
	 * &lt;option value="foo"&gt;1. BarOne&lt;/option&gt;
	 * &lt;option value="foo"&gt;2. BarTwo&lt;/option&gt;
	 * 
	 * @param element
	 *            The By/WebElement Object
	 *
	 * @param text The visible text to match against
	 * @throws NoSuchElementException If no matching option elements are found
	 * @throws UnsupportedOperationException If the SELECT does not support multiple selections
	 * 
	 */
	public void deselectByPartialVisibleText(Object element, String text) {
		try {
			WebElement elem = getWebElement(element);

			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				if (!sel.isMultiple()) {
					throw new UnsupportedOperationException(
							"You may only deselect options of a multi-select");
				}

				List<WebElement> options = elem.findElements(By.xpath(
						".//option[contains(normalize-space(.) , " + Quotes.escape(text) + ")]"));

				boolean matched = false;
				for (WebElement option : options) {
					setSelected(option, false);
					matched = true;
				}

				if (!matched) {
					throw new NoSuchElementException("Cannot locate element with text: " + text);
				}
				SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have any options");
			}

		}catch (Exception e) {

			new CustomExceptionHandler(e);
		}
	}

	public void selectByPartialVisibleText_DoubleClick(WebElement element, String text) {
		try {
			WebElement elem = getWebElement(element);
			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				// try to find the option via XPATH ...
				List<WebElement> options =
						elem.findElements(By.xpath(".//option[contains(normalize-space(.) , " + Quotes.escape(text) + ")]"));

				boolean matched = false;
				for (WebElement option : options) {
					setSelected_DoubleClick(option, true);
					if (!sel.isMultiple()) {
						return;
					}
					matched = true;
				}

				if (options.isEmpty() && text.contains(" ")) {
					String subStringWithoutSpace = getLongestSubstringWithoutSpace(text);
					List<WebElement> candidates;
					if ("".equals(subStringWithoutSpace)) {
						// hmm, text is either empty or contains only spaces - get all options ...
						candidates = elem.findElements(By.tagName("option"));
					} else {
						// get candidates via XPATH ...
						candidates =
								elem.findElements(By.xpath(".//option[contains(., " +
										Quotes.escape(subStringWithoutSpace) + ")]"));
					}
					for (WebElement option : candidates) {
						if (text.equals(option.getText())) {
							setSelected_DoubleClick(option, true);
							if (!sel.isMultiple()) {
								return;
							}
							matched = true;
						}
					}
					SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
				}

				if (!matched) {
					throw new NoSuchElementException("Cannot locate element with text: " + text);
				}
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have options to select");
			}
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		
	}


	/**
	 * Select all options that display text containing the argument. That is, when
	 * given "Bar" this would select all options like:
	 *
	 * &lt;option value="foo"&gt;1. Bar&lt;/option&gt;
	 * 
	 * and in case of multiselect dropdown this would select all options that has text partially matching with the argument like:
	 * 
	 * &lt;option value="foo"&gt;1. BarOne&lt;/option&gt;
	 * &lt;option value="foo"&gt;2. BarTwo&lt;/option&gt;
	 * 
	 * Will take snapshot
	 * 
	 * @param element
	 *            The By/WebElement Object
	 * @param text
	 *            The visible text to match against
	 * @throws NoSuchElementException
	 *             If no matching option elements are found
	 */
	public void selectByPartialVisibleText(Object element, String text) {
		try {
			WebElement elem = getWebElement(element);
			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				// try to find the option via XPATH ...
				List<WebElement> options =
						elem.findElements(By.xpath(".//option[contains(normalize-space(.) , " + Quotes.escape(text) + ")]"));

				boolean matched = false;
				for (WebElement option : options) {
					setSelected(option, true);
					if (!sel.isMultiple()) {
						return;
					}
					matched = true;
				}

				if (options.isEmpty() && text.contains(" ")) {
					String subStringWithoutSpace = getLongestSubstringWithoutSpace(text);
					List<WebElement> candidates;
					if ("".equals(subStringWithoutSpace)) {
						// hmm, text is either empty or contains only spaces - get all options ...
						candidates = elem.findElements(By.tagName("option"));
					} else {
						// get candidates via XPATH ...
						candidates =
								elem.findElements(By.xpath(".//option[contains(., " +
										Quotes.escape(subStringWithoutSpace) + ")]"));
					}
					for (WebElement option : candidates) {
						if (text.equals(option.getText())) {
							setSelected(option, true);
							if (!sel.isMultiple()) {
								return;
							}
							matched = true;
						}
					}
					SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
				}

				if (!matched) {
					throw new NoSuchElementException("Cannot locate element with text: " + text);
				}
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have options to select");
			}
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
	}
	private String getLongestSubstringWithoutSpace(String s) {
		String result = "";
		StringTokenizer st = new StringTokenizer(s, " ");
		while (st.hasMoreTokens()) {
			String t = st.nextToken();
			if (t.length() > result.length()) {
				result = t;
			}
		}
		return result;
	}

	/**
	 * Select or deselect specified option
	 *
	 * @param option
	 *          The option which state needs to be changed
	 * @param select
	 *          Indicates whether the option needs to be selected (true) or
	 *          deselected (false)
	 */
	private void setSelected(WebElement option, boolean select) {
		boolean isSelected=option.isSelected();
		if ((!isSelected && select) || (isSelected && !select)) {
			option.click();
		}
	}

	private void setSelected_DoubleClick(WebElement option, boolean select) {
		boolean isSelected=option.isSelected();
		if ((!isSelected && select) || (isSelected && !select)) {
			Actions act=new Actions(DriverFactory.getDriver());
			act.moveToElement(option).doubleClick().build().perform();
		}
	}

	
	/**
	 * Select all options that display text matching the argument. That is, when
	 * given "Bar" this would select an option like:
	 *
	 * &lt;option value="foo"&gt;Bar&lt;/option&gt;
	 * 
	 * @param element
	 *            The By/WebElement Object
	 * @param text
	 *            The visible text to match against
	 * @throws NoSuchElementException
	 *             If no matching option elements are found
	 */
	public void selectByVisibleText(Object element, String text) {
		try {
			WebElement elem = getWebElement(element);
			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				sel.selectByVisibleText(text);
				SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have options to select");
			}
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
	}

	public void selectByVisibleText_DoubleClick(WebElement element, String text) {
		try {
			WebElement elem = getWebElement(element);
			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				// try to find the option via XPATH ...
				List<WebElement> options =
						elem.findElements(By.xpath(".//option[normalize-space(.) = " + Quotes.escape(text) + "]"));

				boolean matched = false;
				for (WebElement option : options) {
					setSelected_DoubleClick(option, true);
					if (!sel.isMultiple()) {
						return;
					}
					matched = true;
				}

				if (options.isEmpty() && text.contains(" ")) {
					String subStringWithoutSpace = getLongestSubstringWithoutSpace(text);
					List<WebElement> candidates;
					if ("".equals(subStringWithoutSpace)) {
						// hmm, text is either empty or contains only spaces - get all options ...
						candidates = elem.findElements(By.tagName("option"));
					} else {
						// get candidates via XPATH ...
						candidates =
								elem.findElements(By.xpath(".//option[.= " +
										Quotes.escape(subStringWithoutSpace) + "]"));
					}
					for (WebElement option : candidates) {
						if (text.equals(option.getText())) {
							setSelected_DoubleClick(option, true);
							if (!sel.isMultiple()) {
								return;
							}
							matched = true;
						}
					}
					SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
				}

				if (!matched) {
					throw new NoSuchElementException("Cannot locate element with text: " + text);
				}
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have options to select");
			}
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		
	}

	
	public void selectByVisibleText(Object element, String text, boolean runReport) {
		String oldVal="BLANK";
		boolean flag = false;
		try {
			WebElement elem = getWebElement(element);
			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				oldVal=sel.getFirstSelectedOption().getText();
				sel.selectByVisibleText(text);
				SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
				flag = true;
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have options to select");
			}
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		} finally {
			if (runReport) {
				if (flag) {
					CustomReporter.report(Constant.PASS, "'" + text + "' successfully selected in dropdown replacing '"+ oldVal +"'");
				} else {
					CustomReporter.report(Constant.FAIL, "'" + text + "' is NOT selected from dropdown");
				}
			}
		}
	}
	
	/**
	 * Will return WebElement List<> of all options of passed list object
	 * 
	 * @param element
	 *            The By/WebElement Object
	 * @return All options belonging to this select tag
	 */
	public List<WebElement> getOptions(Object element) {
		List<WebElement> listObj = null;
		try {
			WebElement elem = getWebElement(element);

			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				listObj = sel.getOptions();
				SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have options");
			}
		} catch (Exception e) {

			new CustomExceptionHandler(e);
		}
		return listObj;
	}

	/**
	 * Will return Visible Text (String List<>) of all options of passed select object
	 * 
	 * @param element
	 *            The By/WebElement Object
	 * @return All options belonging to this select tag
	 */
	public List<String> getAllOptionsVisibleText(Object element) {
		List<String> listStr = new ArrayList<String>();
		try {
			WebElement elem = getWebElement(element);

			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				List<WebElement> listObj = sel.getOptions();
				for (WebElement temp : listObj) {
					listStr.add(temp.getText().trim());
				}
				SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have options");
			}
		} catch (Exception e) {
			new CustomExceptionHandler(e);
		}
		return listStr;
	}
	
	/**
	 * Will return WebElement List<> of all options which are selected
	 * 
	 * @param element
	 *            The By/WebElement Object
	 * @return All selected options belonging to this select tag
	 */
	public List<WebElement> getAllSelectedOptions(Object element) {
		List<WebElement> listObj = null;
		try {
			WebElement elem = getWebElement(element);
			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				listObj = sel.getAllSelectedOptions();
				SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have options");
			}
		} catch (Exception e) {

			new CustomExceptionHandler(e);
		}
		return listObj;
	}

	/**
	 * Select the option at the given index. This is done by examining the
	 * "index" attribute of an element, and not merely by counting.
	 *
	 * @param element
	 *            The By/WebElement Object
	 * @param index
	 *            The option at this index will be selected
	 * @throws NoSuchElementException
	 *             If no matching option elements are found
	 */
	public void selectByIndex(Object element, int index) {
		try {
			WebElement elem = getWebElement(element);

			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				sel.selectByIndex(index);
				SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have options to select");
			}
		} catch (Exception e) {

			new CustomExceptionHandler(e);
		}
	}

	

	/**
	 * Clear all selected entries. Then select all Entries. This is only valid when the SELECT supports multiple selections.
	 * 
	 * @param element
	 *            The By/WebElement Object
	 *
	 * @throws UnsupportedOperationException If the SELECT does not support multiple selections
	 *
	 */
	public void selectAll(Object element) {
		try {
			WebElement elem = getWebElement(element);

			Select sel = new Select(elem);
			if(sel.isMultiple()){
				List<WebElement> options=sel.getOptions();
				if (options.size() > 0) {
					sel.deselectAll();
					for (int index = 0; index <options.size(); index++) {
						sel.selectByIndex(index);
					}
					SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
				} else {
					CustomReporter.report(Constant.FAIL, "Dropdown does not have any options");
				}
			}else{
				CustomReporter.report(Constant.FAIL, "Passed dropdown is not a MultiSelect");
			}
		} catch (Exception e) {

			new CustomExceptionHandler(e);
		}
	}

	/**
	 * Will return the first WebElement object from the List
	 * 
	 * @param element
	 *            The By/WebElement Object
	 * @return The first selected option in this select tag (or the currently
	 *         selected option in a normal select)
	 * @throws NoSuchElementException
	 *             If no option is selected
	 */
	public WebElement getFirstSelectedOption(Object element) {
		WebElement elem = null;
		try {
			elem = getWebElement(element);
			Select sel = new Select(elem);
			if (sel.getOptions().size() > 0) {
				elem = sel.getFirstSelectedOption();
				SnapshotManager.takeSnapShot(new Object(){}.getClass().getEnclosingMethod().getName());
			} else {
				CustomReporter.report(Constant.FAIL, "Dropdown does not have options");
			}
		} catch (Exception e) {

			new CustomExceptionHandler(e);
		}
		return elem;
	}

}
