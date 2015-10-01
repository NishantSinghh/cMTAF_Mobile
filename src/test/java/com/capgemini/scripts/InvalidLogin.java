package com.capgemini.scripts;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.capgemini.pages.DemoautLogin;
import com.capgemini.pages.EriBank;
import com.capgemini.utilities.CreateDriver;
import com.capgemini.utilities.ReadFile;
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class InvalidLogin {
	RemoteWebDriver driver;
	AndroidDriver androidDriver;
	IOSDriver iosDriver;
	CreateDriver create = new CreateDriver();
	DemoautLogin objLogin;
	Utilities util;
	ExtentReports report;
	ExtentTest reporter;
	ReadFile read = new ReadFile();
	String strTestName = this.getClass().getSimpleName();
	String strBrowser = null;
	//.....Executing test from the testng.xml for parallel run.....................................................
	public void executeTestCase(ExtentReports report, String host, String browser, String bootstrap, String udid){
		this.report = report;
		System.out.println("Test Case Name: "+strTestName);
		int intCounter=1;
		strBrowser = browser;
		int intRow = read.Rowcount(strTestName);
		System.out.println("Row Count " + intRow);
		for(int i=1;i<= intRow;i++){
			if (browser.equalsIgnoreCase("Android"))
			{
				androidDriver = create.setAndroid(strTestName,host,bootstrap,udid);
				driver = androidDriver;
				
			}else if (browser.equalsIgnoreCase("iOS"))
			{
				iosDriver = create.setIOS(strTestName,host);
				driver = iosDriver;
				
			}
			else{
			driver = new CreateDriver().setBrowser(host, browser);
			}
			initRun(i);
			executeTest(i);
			tear();
		}
		LogStatus status = reporter.getRunStatus();
		read.updateResult(strTestName,status);
		
	}
	//mandatory function with intCounter argument
	public void initRun(int intCounter){
		objLogin = new DemoautLogin(driver);
		String strBrowserName = driver.getCapabilities().getBrowserName();
		String strBrowserVersion = driver.getCapabilities().getVersion();
		report.addSystemInfo("Browser", strBrowserName);
		report.addSystemInfo("Browser Version", strBrowserVersion);
		util=new Utilities(driver);
		if(intCounter>1){
			reporter=report.startTest(strTestName,"Iteration : "+intCounter);
		}
		else{
			reporter=report.startTest(strTestName);
		}
		
	}
	
	//Mandatory function 
	public void tear(){
		report.endTest(reporter);
		report.flush();
		if(strBrowser.equalsIgnoreCase("Android")){
			androidDriver.quit();
			driver.quit();
		}else if(strBrowser.equalsIgnoreCase("iOS")){
			iosDriver.quit();
			driver.quit();
		}
		else{
			driver.close();
		}
	}
	
	//All the test function should have intCounter as argument
	public void executeTest(int intCounter){
		driver.get("http://newtours.demoaut.com/");
		reporter.log(LogStatus.INFO, "Navigated to the URL");
		util.waitClick(objLogin.LogUserName, 60);
		String strUserName = read.getValue(strTestName, "UserName", intCounter);
		String strPassword = read.getValue(strTestName, "Password", intCounter);
		if(!strUserName.equalsIgnoreCase("Nishant"))
		{
			reporter.log(LogStatus.FAIL, "Wrong UserName");
		}
		System.out.println(strUserName);
		objLogin.setUserName(strUserName);
		reporter.log(LogStatus.PASS, "UserName " + strUserName + " Entered!!");
		reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));
		objLogin.setPassword(strPassword);
		objLogin.clkLogin();
		
	}
	//next function
}
