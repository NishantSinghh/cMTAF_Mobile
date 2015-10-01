package com.capgemini.utilities;
//******************************************************************************************************************//
//Created By		: Nishant Singh
//Created Date      : 18-Sep-2015
//Changes done on   :
//Changes done by   :
//Reviewed by       :
//******************************************************************************************************************//

import java.io.File;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Utilities {
	RemoteWebDriver driver;
	
	
	
	public Utilities(RemoteWebDriver driver){
		this.driver=driver;
	}
	
	//Explicitly wait for a webElement to be clickable
	public void waitClick(WebElement element, int time){
		WebDriverWait wwait = new WebDriverWait(driver,time);
		wwait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	//To enter text in a textbox with results
	public void enterText(WebElement element,String text, ExtentTest reporter){
		element.sendKeys(text);
		reporter.log(LogStatus.PASS, text + " Entered!!");
		reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));
	}
	
	//To click on a button/img/links with results
	public void btnClick(WebElement element, ExtentTest reporter){
		reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));
		element.click();
	}
	

	//To select a checkbox/radio with results
	public void selCheck(WebElement element, ExtentTest reporter){
		if(!element.isSelected()){
			element.click();
			reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));
		}
		else
		{
			reporter.log(LogStatus.INFO, "Check box is already checked!!");
		}	
	}
	
	//To select value from a dropdown with results
	public void selValue(WebElement element, String Value,ExtentTest reporter){
		Select selData = new Select(element);
		selData.selectByValue(Value);
		reporter.log(LogStatus.INFO, Value + " selected!!");
		reporter.log(LogStatus.INFO, reporter.addScreenCapture(Reporter.CaptureScreen(driver)));

	}
}
