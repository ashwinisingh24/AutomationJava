package common.customReporting;

import common.configData_Util.Constant;
import common.configData_Util.Util;
import common.driverManager.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SnapshotManager {
	static Map<Integer, String> snapshotFolderNamesMap = new HashMap<Integer, String>();

	public static void initialize() {
		File file=new File(Constant.getSnapShotsFolderPath());
		deleteDirectory(file);

	}

	public static void setUp(String name) {
		String tempName=snapshotFolderNamesMap.get((int) (long) (Thread.currentThread().getId()));
		if(tempName==null){
			snapshotFolderNamesMap.put((int) (long) (Thread.currentThread().getId()), name);
		}
	}

	public static synchronized String getName() {
		return snapshotFolderNamesMap.get((int) (long) (Thread.currentThread().getId()));
	}

	public static  boolean deleteDirectory(File dir) {
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
		if(dir.getName().equals(Constant.snapshotsFolderName)){
			//System.out.println("Skipping file or directory : " + dir.getName());
			bool=true;
		}else{
			//System.out.println("Removing file or directory : " + dir.getName());
			bool=dir.delete();
		}
		return bool;
	}

	public static String takeSnapShot(String extraIdentifierCanAlsoBeBlank)  {
		String url="";

		try{
			if(extraIdentifierCanAlsoBeBlank.contains("failure") || Constant.captureSnapshots){
				String timeStamp = Util.getTimeStamp();
				String snapShotFolder= Constant.getSnapShotsFolderPath();
				String subFolderName=getName();
				String fileName=timeStamp+"_"+extraIdentifierCanAlsoBeBlank+".jpg";
				String path=snapShotFolder+"/"+subFolderName+"/"+fileName;
				TakesScreenshot scrShot=(TakesScreenshot) DriverFactory.getDriver();
				File srcFile=scrShot.getScreenshotAs(OutputType.FILE);
				File destFile=new File(path);
				FileUtils.copyFile(srcFile, destFile);

				if(extraIdentifierCanAlsoBeBlank.contains("failure")){
					url=snapShotFolder+"/"+subFolderName+"/failure/"+fileName;
					destFile=new File(url);
					FileUtils.copyFile(srcFile, destFile);
					url="../"+ Constant.snapshotsFolderName+"/"+subFolderName+"/failure/"+fileName;
					url = url.replace("\\", "/");
				}
			}

		}catch(Exception e){

		}
		return url;
	}

	public static void tearDown() {
		snapshotFolderNamesMap.remove((int) (long) (Thread.currentThread().getId()));
	}

	/**
	 * Use this method to take the snapshot of passed web element object
	 * */
	public static String takeSnapShot(String extraIdentifierCanAlsoBeBlank,Object element)  {
		String url="";
		try{
			WebElement webElement=(WebElement) element;

			String timeStamp = Util.getTimeStamp();
			String snapShotFolder= Constant.getSnapShotsFolderPath();
			String subFolderName=getName();
			String fileName=timeStamp+"_"+extraIdentifierCanAlsoBeBlank+".jpg";
			String path=snapShotFolder+"/"+subFolderName+"/"+fileName;
			TakesScreenshot scrShot=(TakesScreenshot) DriverFactory.getDriver();
			File srcFile=scrShot.getScreenshotAs(OutputType.FILE);
			int w=webElement.getSize().getWidth();
			int h=webElement.getSize().getHeight();
			BufferedImage img=ImageIO.read(srcFile);
			/*int totH=img.getHeight();
			int totW=img.getWidth();*/
			int x=webElement.getLocation().getX();
			int y=webElement.getLocation().getY();
			BufferedImage dest=img.getSubimage(x, y, w, h);
			ImageIO.write(dest, "jpg", srcFile);
			File destFile=new File(path);
			FileUtils.copyFile(srcFile, destFile);
			url="../"+ Constant.snapshotsFolderName+"/"+subFolderName+"/"+fileName;
			url = url.replace("\\", "/");
		}catch(Exception e){
			//e.printStackTrace();
		}
		return url;
	}
}
