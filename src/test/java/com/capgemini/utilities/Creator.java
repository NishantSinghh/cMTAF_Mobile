package com.capgemini.utilities;
//******************************************************************************************************************//
//Created By		: Nishant Singh
//Created Date      : 18-Sep-2015
//Changes done on   :
//Changes done by   :
//Reviewed by       :
//******************************************************************************************************************//

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.testng.annotations.BeforeSuite;

public class Creator {
	
	static String strAbsolutepath = new File("").getAbsolutePath();
	public static int month;
	public static int day;
	public static int year;
	public static String[] monthName = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd-hh.mm.ss.SSS";
	
	public static String now() {
		//Calendar cal = Calendar.getInstance();
		//month = cal.get(Calendar.MONTH) + 1;
		//day = cal.get(Calendar.DAY_OF_MONTH);
		//year = cal.get(Calendar.YEAR);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		Random rand = new Random();
		int num = rand.nextInt(1000);
		Date dt = new Date();
		return sdf.format(dt)+num;
	}
	public String getPath(){
		
		String strPath=null;
		String sPathTillMonth;
		String sPathTillDate;
		String sPathTillUserName;
		String strOSName = System.getProperty("os.name");

		Calendar cal = Calendar.getInstance();
		int iMonth = cal.get(Calendar.MONTH);
		String sMonthName = monthName[iMonth];
		String userName = System.getProperty("user.name");
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String sDate = sdf.format(cal.getTime());
		sPathTillUserName = strAbsolutepath + "/Results/" + userName;
		sPathTillMonth = sPathTillUserName + "/" + sMonthName;
		sPathTillDate = sPathTillMonth + "/" + sDate;

		try {
			
			//strComponent = "BMC REMEDY";
			String time = now();
			File oFilePathTillUserName = new File(sPathTillUserName);
			if (!oFilePathTillUserName.exists()) {
				oFilePathTillUserName.mkdir();
			}
			File osPathTillMonth = new File(sPathTillMonth);
			if (!osPathTillMonth.exists()) {
				osPathTillMonth.mkdir();
			}
			File osPathTilldate = new File(sPathTillDate);
			if (!osPathTilldate.exists()) {
				osPathTilldate.mkdir();
			}
			
	
			strPath = osPathTilldate + "\\" + "Report_" + time
					+ Math.random() + ".html";
		}catch(Exception e){
			e.printStackTrace();
		}
		return strPath;
	}
}
