package com.capgemini.scripts;

import java.util.List;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
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
		int intMakepay=0;
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
		try{
			driverWait.until(ExpectedConditions.elementToBeClickable(By.id("com.experitest.ExperiBank:id/makePaymentButton")));
		}catch(Exception e){
			intMakepay=1;
		}
		if(intMakepay==1){
			reporter.log(LogStatus.FAIL, "Make payment screen not displayed. Login Failed!!");
			reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));
		}
		else{
			reporter.log(LogStatus.PASS, "Make payment screen displayed. Login Successfull!!");
			reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));
			driver.findElement(By.id("com.experitest.ExperiBank:id/makePaymentButton")).click();
			MakePayment(intCounter);
		}
		
	}
	
	//Function to make payment...................
	public void MakePayment(int intCounter){
		int intPay=0, intLogin=0;
		WebDriverWait driverWait = new WebDriverWait(driver,30);
		try{
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.experitest.ExperiBank:id/sendPaymentButton")));
		}catch(Exception e){
			intPay=1;
		}
		if(intPay==1){
			reporter.log(LogStatus.FAIL, "Payment Transaction screen not displayed");
			reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));
		}
		else{
			reporter.log(LogStatus.PASS, "Payment Transaction screen displayed");
			reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));
			String strPhone = read.getValue(strTestName, "PhoneNumber", intCounter);
			String strName = read.getValue(strTestName, "Name", intCounter);
			String strCountry = read.getValue(strTestName, "Country", intCounter);
			driver.findElement(By.id("com.experitest.ExperiBank:id/phoneTextField")).sendKeys(strPhone);
			driver.findElement(By.id("com.experitest.ExperiBank:id/nameTextField")).sendKeys(strName);
			try{
	        	androidDriver.hideKeyboard();
	        }catch(Exception e)
	        {
	        	System.out.println("Keyboad not visible");
	        }
			driver.findElement(By.id("com.experitest.ExperiBank:id/countryTextField")).sendKeys(strCountry);
			try{
	        	androidDriver.hideKeyboard();
	        }catch(Exception e)
	        {
	        	System.out.println("Keyboad not visible");
	        }
			WebElement ele = driver.findElementById("com.experitest.ExperiBank:id/amount");
			Point location = ele.getLocation();
	        Dimension size = ele.getSize();
			int x = location.getX();
	        int y = location.getY() + (size.getHeight() / 2);
	        //To swipe to amount 5 as seekbar length is (440/100*5)+1
	        androidDriver.swipe(x, y, x+23, y, 1000);
	        //To add the report and screenshot after sliding the seekbar
	       
	        driverWait.until(ExpectedConditions.elementToBeClickable(By.id("com.experitest.ExperiBank:id/sendPaymentButton")));
	        reporter.log(LogStatus.INFO, "Slider has been Swiped to specific value");
			reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));
			driver.findElement(By.id("com.experitest.ExperiBank:id/sendPaymentButton")).click();
			driverWait.until(ExpectedConditions.elementToBeClickable(By.name("Are you sure you want to send payment?")));
			driver.findElementById("android:id/button1").click();
			driverWait.until(ExpectedConditions.elementToBeClickable(By.id("com.experitest.ExperiBank:id/logoutButton")));
			driver.findElement(By.id("com.experitest.ExperiBank:id/logoutButton")).click();
			try{
				driverWait.until(ExpectedConditions.elementToBeClickable(By.id("com.experitest.ExperiBank:id/loginButton")));
			}catch(Exception e){
				intLogin=1;
			}
			if(intLogin==1){
				reporter.log(LogStatus.FAIL, "Login Screen not displayed");
				reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));
			}
			else{
				reporter.log(LogStatus.PASS, "Login Screen displayed");
				reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));

			}
		}
	}
	
	//next function
}
