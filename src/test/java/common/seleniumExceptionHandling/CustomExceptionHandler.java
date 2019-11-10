package common.seleniumExceptionHandling;

import common.configData_Util.Constant;
import common.customReporting.CustomReporter;

public class CustomExceptionHandler extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CustomExceptionHandler(Exception e) {
		//System.out.println(Thread.currentThread().getId()+" My custom Exception class is called");
		CustomReporter.report(Constant.FATAL,  e.getMessage());
		e.printStackTrace();
	}

}
