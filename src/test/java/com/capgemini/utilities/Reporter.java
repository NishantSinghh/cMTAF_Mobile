package com.capgemini.utilities;
//******************************************************************************************************************//
//Created By		: Nishant Singh
//Created Date      : 18-Sep-2015
//Changes done on   :
//Changes done by   :
//Reviewed by       :
//******************************************************************************************************************//

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.capgemini.executor.Executioner;
import com.relevantcodes.extentreports.ExtentReports;

public class Reporter {
	static String strRelPath = new File("").getAbsolutePath();
	static String strPath ;
	static Creator dir = new Creator();
	ReadFile read = new ReadFile();
	static String strReportTitle= ReadFile.reportTitle;
	static String strReportName= ReadFile.reportName;
	static String strReportHeadline= ReadFile.reportHeadline;
	public static ExtentReports Instance()
    {
		strPath = dir.getPath();
		//String browser = driver.getCapabilities().getBrowserName();
		//String version = driver.getCapabilities().getVersion();
		ExtentReports extent;
		String Path = strPath;
		System.out.println(Path);
		extent = new ExtentReports(Path, false);
		extent.config()
		           .documentTitle(strReportTitle)
		           .reportName(strReportName).reportHeadline(strReportHeadline);
		//extent.addSystemInfo("Browser Version",version);
		//extent.addSystemInfo("Browser",browser);
		
		return extent;
	}
	public static String CaptureScreen(RemoteWebDriver driver)
	{	
		SimpleDateFormat sd = new SimpleDateFormat("ddMMyyHHmmssSSS");
		Date now = new Date();
		String strName = sd.format(now);
		String strScrPath = strRelPath+"/Results/Screenshot/"+strName;
		 TakesScreenshot oScn = (TakesScreenshot) driver;
		 File oScnShot = oScn.getScreenshotAs(OutputType.FILE);
		File oDest = new File(strScrPath+".jpg");
		try {
		   FileUtils.copyFile(oScnShot, oDest);
		} catch (IOException e) {System.out.println(e.getMessage());}
		return strScrPath+".jpg";
	}
	
}
