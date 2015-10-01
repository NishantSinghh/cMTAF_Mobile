package com.capgemini.utilities;
//******************************************************************************************************************//
//Created By		: Nishant Singh
//Created Date      : 18-Sep-2015
//Changes done on   :
//Changes done by   :
//Reviewed by       :
//******************************************************************************************************************//

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class CreateDriver {
	RemoteWebDriver driver;
	AndroidDriver androidDriver;
	IOSDriver iosDriver;
	ReadFile read = new ReadFile();
	String winAppium_Node = ReadFile.winAppNode;
	String winAppium_Js = ReadFile.winAppjs;
	String osxAppium_Node = ReadFile.osxAppNode;
	String osxAppium_Js = ReadFile.osxAppjs;
	DesiredCapabilities cap  = new DesiredCapabilities();
	String strAbsPath = new File("").getAbsolutePath();
	String strChromeServer = strAbsPath + "\\src\\test\\resources\\chromedriver.exe";
	String strIEServer = strAbsPath + "\\src\\test\\resources\\IEDriverServer.exe";
	public static String strBrowser=null;
	public void browserName(String strBrowser){
		this.strBrowser = strBrowser;
	}
	
	
	//..............To set the browser for parallel Execution...............................................................................................................//
	
	public RemoteWebDriver setBrowser(String host, String browser) {
		String strBrowserType = null;

		strBrowserType = browser;

		try {
			if (strBrowserType.equalsIgnoreCase("IE")
					|| strBrowserType.equalsIgnoreCase("Internet Explorer")) {

				System.setProperty("webdriver.ie.driver",
						strIEServer);

				DesiredCapabilities cap = DesiredCapabilities
						.internetExplorer();

				cap.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS,
						true);
				cap.setCapability(InternetExplorerDriver.IE_SWITCHES, true);
				cap.setCapability(
						InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				cap.setCapability(
						InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR, true);
				cap.setCapability(
						InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP,
						true);

				if (host.isEmpty()) {
					return new InternetExplorerDriver(cap);
				} else {
					return new RemoteWebDriver(new URL(host), cap);
				}

			} else if (strBrowserType.equalsIgnoreCase("FF")
					|| strBrowserType.equalsIgnoreCase("Firefox")) {

				DesiredCapabilities cap = DesiredCapabilities.firefox();
				if (host.isEmpty()) {
					return new FirefoxDriver(cap);
				} else {
					return new RemoteWebDriver(new URL(host), cap);
				}
			} else if (strBrowserType.equalsIgnoreCase("Safari")) {
				/*DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setBrowserName("safari");*/
				DesiredCapabilities cap = DesiredCapabilities.safari();
				/* CommandExecutor executor = new SeleneseCommandExecutor(new
				 URL("http://localhost:4444/"), new URL(strAppURL),
				 capabilities);*/
				 //safariDriver = new RemoteWebDriver(executor, capabilities);
				if (host.isEmpty()) {
					return new SafariDriver(cap);
				} else {
					return new RemoteWebDriver(new URL(host), cap);
				}
				
				
			} else if (strBrowserType.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver",
						strChromeServer);
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				if (host.isEmpty()) {
					return new ChromeDriver(cap);
				} else {
					return new RemoteWebDriver(new URL(host), cap);
				}

			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	//......................................................................................................................................................................//
	
	
	
	//..............To set Android Driver for parallel execution......................................................................................................................................//
	public AndroidDriver setAndroid(String testname, String host, String boot, String udid){
		String platFormName = read.getValue(testname, "PlatformName", 1);
		System.out.println(platFormName);
		String browser = read.getValue(testname, "Browser", 1);
		System.out.println(browser);
		String deviceName = read.getValue(testname, "DeviceName", 1);
		System.out.println(deviceName+"\t"+testname);
		String appPath = read.getValue(testname, "AppPath", 1);
		System.out.println(appPath+"\t"+testname);
		String UDID = read.getValue(testname, "UDID", 1);
		System.out.println(UDID+"\t"+testname);
		String bundleID = read.getValue(testname, "BundleID", 1);
		System.out.println(bundleID+"\t"+testname);
		String packageName = read.getValue(testname, "PackageName", 1);
		System.out.println(packageName+"\t"+testname);
		String packageActivity = read.getValue(testname, "PackageActivity", 1);
		System.out.println(packageActivity+"\t"+testname);
		String platformversion = read.getValue(testname, "Platformversion", 1);
		System.out.println(platformversion+"\t"+testname);
		
		cap.setCapability("platformName", platFormName);
		cap.setCapability("deviceName",deviceName);
		
		if (browser.equalsIgnoreCase("native")){
			// Setting capabilities for Native App 
			//File appDir = new File(appDirectory);
			//File app = new File(appDir, appName);
			//cap.setCapability("app", app.getAbsolutePath()); //app.getAbsolutePath()
			cap.setCapability(CapabilityType.VERSION, platformversion);
			cap.setCapability("appPackage",packageName);
			cap.setCapability("appActivity",packageActivity);
			if(!udid.isEmpty()){
				cap.setCapability("udid",udid);
			}
		}else if(browser.equalsIgnoreCase("browser")||browser.equalsIgnoreCase("Chromium")) { 
			//Setting up capabilities for Browser in Android
			cap.setCapability("browserName", browser);  
			cap.setCapability("automationName", "Appium");
			cap.setCapability("app", "browser");
			cap.setCapability("platformVersion", platformversion);
		}else if(browser.equalsIgnoreCase("Safari")){
			cap.setCapability("browserName", browser);  
//			cap.setCapability("defaultDevice", "true");
			cap.setCapability("platformVersion", platformversion);
		}
		else {
			System.out.println("Error in CAFE Config");					
		}
		System.out.println(cap.toString());
		try {
			androidDriver = new AndroidDriver(new URL(host), cap);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return androidDriver;
		}
	
	//....................................................................................................................................................................................//
	

	//..............To set Android Driver for parallel execution......................................................................................................................................//
	public IOSDriver setIOS(String testname, String host){
		String platFormName = read.getValue(testname, "PlatformName", 1);
		System.out.println(platFormName);
		String browser = read.getValue(testname, "Browser", 1);
		System.out.println(browser);
		String deviceName = read.getValue(testname, "DeviceName", 1);
		System.out.println(deviceName+"\t"+testname);
		String appPath = read.getValue(testname, "AppPath", 1);
		System.out.println(appPath+"\t"+testname);
		String UDID = read.getValue(testname, "UDID", 1);
		System.out.println(UDID+"\t"+testname);
		String bundleID = read.getValue(testname, "BundleID", 1);
		System.out.println(bundleID+"\t"+testname);
		String packageName = read.getValue(testname, "PackageName", 1);
		System.out.println(packageName+"\t"+testname);
		String packageActivity = read.getValue(testname, "PackageActivity", 1);
		System.out.println(packageActivity+"\t"+testname);
		String platformversion = read.getValue(testname, "Platformversion", 1);
		System.out.println(platformversion+"\t"+testname);
		
		cap.setCapability("platformName", platFormName);
		cap.setCapability("deviceName",deviceName);
		
		if (browser.equalsIgnoreCase("native")){
			// Setting capabilities for Native App 
			//File appDir = new File(appDirectory);
			//File app = new File(appDir, appName);
			//cap.setCapability("app", app.getAbsolutePath()); //app.getAbsolutePath()
			cap.setCapability("platformVersion", platformversion);
			if(UDID != ""){
				cap.setCapability("bundleId", bundleID);
				cap.setCapability("udid", UDID);
			}
			else{
				cap.setCapability("app",appPath);
			}
			cap.setCapability("screenshotWaitTimeout",2);
		}else if(browser.equalsIgnoreCase("browser")||browser.equalsIgnoreCase("Chromium")) { 
			//Setting up capabilities for Browser in Android
			cap.setCapability("browserName", browser);  
			cap.setCapability("automationName", "Appium");
			cap.setCapability("app", "browser");
			cap.setCapability("platformVersion", platformversion);
		}else if(browser.equalsIgnoreCase("Safari")){
			cap.setCapability("browserName", browser);  
//				cap.setCapability("defaultDevice", "true");
			cap.setCapability("platformVersion", platformversion);
		}
		else {
			System.out.println("Error in CAFE Config");					
		}
		System.out.println(cap.toString());
		try {
			iosDriver = new IOSDriver(new URL(host), cap);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return iosDriver;
		}
	
	//....................................................................................................................................................................................//
	

		
	
	
	
	//.............................Start Multiple Appium Server instance.................................................................................................................................//
	
	public void startappiumMulti(String host, String boot) {
	try{	
		String os_name = null;
		os_name = System.getProperty("os.name");
		System.out.println(os_name);
		CommandLine command = null;
		
		if(os_name.contains("Windows")){
			// Starting Appium on Windows-----------------------------------------------------------
						System.out.print("Attempting to start appium server..");
						command = new CommandLine("cmd");
						command.addArgument("/c");
						command.addArgument(winAppium_Node);
						command.addArgument(winAppium_Js);
						command.addArgument("--address");
						command.addArgument("127.0.0.1");
						command.addArgument("--port");
						command.addArgument(host);
						command.addArgument("--bootstrap-port");
						command.addArgument(boot);
						command.addArgument("--command-timeout");
						command.addArgument("180");
						command.addArgument("--chromedriver-port");
						command.addArgument("9157");
						command.addArgument("--no-reset");
						command.addArgument("--log");
						command.addArgument("D:\\log\\appiumLogs.txt");  
						
						
			/*			System.out.print("Attempting to start appium server ..");
						String[] command1 = {"cmd.exe", "/C", "Start", strStartServerPath};
						Runtime.getRuntime().exec(command1);		*/
			//-----------------------------------------------------------------------
			
		} else if (os_name.contains("Mac")){
			
//			Runtime.getRuntime().exec("/bin/bash export ANDROID_HOME=/Users/admincg/MTCoE/android/sdk");
			
						command = new CommandLine(osxAppium_Node);
						command.addArgument(osxAppium_Js,false);
						command.addArgument("--address",false);
						command.addArgument("127.0.0.1",false);
						command.addArgument("--port",false);
						command.addArgument("4723",false);
						command.addArgument("--full-reset",false);
						command.addArgument("--automation-name",false);
						command.addArgument("Appium",false);
//						command.addArgument("--no-reset",false);
//						command.addArgument("--platform-name",false);
//						command.addArgument("iOS",false);
//						command.addArgument("--browser-name",false);
//						command.addArgument("Safari",false);
												
		}
		
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
	

			executor.execute(command, resultHandler);
			Thread.sleep(10000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e1) {
		// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.print("Appium Started..");
	
		// TODO Auto-generated method stub
		
		
	}
	
	
	
	
	
	//...................................................................................................................................................................................................//
	
	
	
	
	//...............................To Stop the Android Driver..................................................................................................................................//
	
	public void StopAppium()
	{
		try{
			CommandLine command = new CommandLine("cmd");
			command.addArgument("/c");
			command.addArgument("taskkill");
			command.addArgument("/F");
			command.addArgument("/IM");
			command.addArgument("node.exe");
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			DefaultExecutor executor = new DefaultExecutor();
			executor.setExitValue(1);
			executor.execute(command, resultHandler);
			Thread.sleep(4000);
			System.out.println("StopAppiumExecuted");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	//...........................................................................................................................................................................................//

	
	//...............................To Stop the iOS Driver..................................................................................................................................//
	
	public void StopiOSAppium()
	{
		try{
	        CommandLine command = new CommandLine("killall");
	        command.addArgument("node", false);
	        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
	        DefaultExecutor executor = new DefaultExecutor();
	        executor.setExitValue(1);
	        executor.execute(command, resultHandler);
	        Thread.sleep(4000);
	        System.out.println("StopAppiumExecuted");
	    }catch(Exception e){
	        System.out.println(e);
	    }
		
	}
	
	//...........................................................................................................................................................................................//



}

