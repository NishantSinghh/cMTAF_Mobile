package com.capgemini.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EriBank {
	RemoteWebDriver driver;
	@FindBy(id="com.experitest.ExperiBank:id/usernameTextField")
	public WebElement txtUserName;
	@FindBy(id="com.experitest.ExperiBank:id/passwordTextField")
	public WebElement txtPassword;
	@FindBy(id="com.experitest.ExperiBank:id/loginButton")
	public WebElement btnLogSignin;
	public EriBank(RemoteWebDriver driver){
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	public void setUserName(String strUserName){
		txtUserName.sendKeys(strUserName);
	}
	public void setPassword(String strPassword){
		txtPassword.sendKeys(strPassword);
	}
}
