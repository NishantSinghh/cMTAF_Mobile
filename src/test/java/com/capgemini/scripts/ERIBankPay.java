package com.capgemini.scripts;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.capgemini.pages.EriBank;
import com.capgemini.utilities.CreateDriver;
import com.capgemini.utilities.ReadFile;
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ERIBankPay {
	RemoteWebDriver driver;
	AndroidDriver androidDriver;
	IOSDriver iosDriver;
	CreateDriver create = new CreateDriver();
	EriBank bank;
	Utilities util;
	ExtentReports report;
	ExtentTest reporter;
	ReadFile read = new ReadFile();
	String strTestName = this.getClass().getSimpleName();
	//String strBrowser = CreateDriver.strBrowser;
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
		bank = new EriBank(driver);
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
		WebDriverWait driverWait = new WebDriverWait(driver,30);
		driverWait.until(ExpectedConditions.elementToBeClickable(bank.txtUserName));
		String strUserName = read.getValue(strTestName, "Username", intCounter);
		String strPassword =read.getValue(strTestName, "Password", intCounter);
		bank.txtUserName.sendKeys(strUserName);
		reporter.log(LogStatus.INFO, strUserName + "Entered!!");
		bank.setPassword(strPassword);
		reporter.log(LogStatus.INFO, strPassword + "Entered!!");
		reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));
		bank.btnLogSignin.click();
		System.out.println("button Clicked");
		
	}
	//next function
}
