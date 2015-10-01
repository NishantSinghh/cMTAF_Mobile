package com.capgemini.executor;
//******************************************************************************************************************//
//Created By		: Nishant Singh
//Created Date      : 18-Sep-2015
//Changes done on   :
//Changes done by   :
//Reviewed by       :
//******************************************************************************************************************//

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.capgemini.utilities.CreateDriver;
import com.capgemini.utilities.Creator;
import com.capgemini.utilities.ReadFile;
import com.capgemini.utilities.Reporter;
import com.capgemini.utilities.Utilities;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class Executioner {
	RemoteWebDriver driver;
	String Path;
	String TCName=null;
	String BrName = null;
	ExtentReports report;
	ExtentTest reporter;
	ReadFile read = new ReadFile();
	CreateDriver create = new CreateDriver();
	//Utilities util = new Utilities(driver);
	public static String ReportPath=null;
	@Test
	@Parameters({"host","bootstrap","browser","udid"})
	public void setup(String host, String bootstrap,String browser, String udid){
		report = Reporter.Instance();
		
		executeExcel(report, host, bootstrap,browser, udid);
	}
	public void executeExcel(ExtentReports report, String host, String bootstrap, String browser,String udid){
		
		String strCases = read.ReadMaster();
		String[] arrCases = strCases.split(",");
		int intTCNum = arrCases.length;
		if((browser.equalsIgnoreCase("Android"))||(browser.equalsIgnoreCase("iOS"))){
			if(browser.equalsIgnoreCase("Android")){
				create.StopAppium();
			}else{
				create.StopiOSAppium();
			}
			if(host.isEmpty()){
				host = "http://localhost:4723/wd/hub";
				create.startappiumMulti("4723","4724");
				
			}else{
				String[] arrhost = host.split(":");
				String Port = arrhost[2].substring(0, 4);
				System.out.println(Port);
				create.startappiumMulti(Port,bootstrap);
			}
			
			for(int i = 1; i<intTCNum;i++){
				String[] tempVar = arrCases[i].split("#");
				TCName = tempVar[0];
				BrName = tempVar[1];
				if(browser.isEmpty()){
					browser = BrName;
				}
		}
		
			
			try {
				Class<?> objClass = Class.forName("com.capgemini.scripts."+TCName);
				Object obj = objClass.newInstance();
				System.out.println("browser Steup");
				Method method = objClass.getMethod("executeTestCase", ExtentReports.class, String.class,String.class, String.class, String.class);
				System.out.println("Method");
				Object[] args = { report, host,browser, bootstrap, udid};
				method.invoke(obj, args);
				System.out.println(obj.getClass().getSimpleName());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.getCause().printStackTrace();;
			}
	
		}
	}
	
	@AfterSuite
	@Parameters("browser")
	public void tear(String browser){
		report.close();
		if(browser.equalsIgnoreCase("Android")){
			create.StopAppium();
		}else if(browser.equalsIgnoreCase("iOS")){
			create.StopiOSAppium();
		}
		
	}
}
