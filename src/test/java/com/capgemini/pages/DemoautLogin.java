package com.capgemini.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DemoautLogin {
	RemoteWebDriver driver;
	@FindBy(name="userName")
	public WebElement LogUserName;
	@FindBy(name="password")
	public WebElement LogPassword;
	@FindBy(name="login")
	public WebElement LogSignin;
	public DemoautLogin(RemoteWebDriver driver){
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	public void setUserName(String strUserName){
		LogUserName.sendKeys(strUserName);
	}
	public void setPassword(String strPassword){
		LogPassword.sendKeys(strPassword);
	}
	public void clkLogin(){
		LogPassword.click();
	}
}
