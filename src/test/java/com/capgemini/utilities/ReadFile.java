package com.capgemini.utilities;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.relevantcodes.extentreports.LogStatus;

public class ReadFile {
	Properties pr = new Properties();
	String Path;
	public static String winAppNode = null;
	public static String winAppjs = null;
	public static String osxAppNode = null;
	public static String osxAppjs = null;
	public static String reportTitle = null;
	public static String reportName = null;
	public static String reportHeadline = null;
	public ReadFile(){
		try {
			pr.load(new FileInputStream("./src/test/resources/config.properties"));
			String Path = pr.getProperty("file_path");
			this.Path = Path;
			winAppNode = pr.getProperty("win_appium_node");
			winAppjs = pr.getProperty("win_appium_js");
			osxAppNode = pr.getProperty("osx_appium_node");
			osxAppjs = pr.getProperty("osx_appium_js");
			reportTitle = pr.getProperty("report_title");
			reportName = pr.getProperty("report_name");
			reportHeadline = pr.getProperty("report_headline");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String ReadMaster(){
		String strCases=null;
		File file = new File(Path);
		FileInputStream fi;
		Workbook wkMasterSheet = null;
		int colExecute=0;
		List<String> lstID = new ArrayList<String>();
		try {
			fi = new FileInputStream(file);
			wkMasterSheet =  new XSSFWorkbook(fi);
			Sheet shName = wkMasterSheet.getSheet("TC_Data");
			int ColCount = shName.getRow(0).getLastCellNum();
			int RowCount = shName.getLastRowNum();
			Row rowHeader = shName.getRow(0);
			for(int i=0; i<ColCount;i++)
			{
				
				String strHeader= rowHeader.getCell(i).getStringCellValue();
				if(strHeader.equalsIgnoreCase("Execute")){
					colExecute=i;
					break;
				}
				
			}
			for(int i=1;i<=RowCount;i++)
			{
				String strFlag = shName.getRow(i).getCell(colExecute).getStringCellValue();
				if(strFlag.equalsIgnoreCase("yes")){
					strCases = strCases + "," + shName.getRow(i).getCell(1).getStringCellValue() + "#"+shName.getRow(i).getCell(2).getStringCellValue();
					String strID= shName.getRow(i).getCell(1).getStringCellValue();
					lstID.add(strID);
				}
				
			}
			for(String s:lstID){
				System.out.println(s);
			}
			wkMasterSheet.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strCases;
	}
	
	//....Get the Cell Value using Sheet Name, Column Name, Iteration number......................................................................//
	public String getValue(String SheetName, String ColName, int intCounter){
		String strValue=null;
		try{
			File file = new File(Path);
			FileInputStream fi = new FileInputStream(file);
			Workbook wkMasterSheet = null;
			wkMasterSheet =  new XSSFWorkbook(fi);
			Sheet shName = wkMasterSheet.getSheet(SheetName);
			int ColCount = shName.getRow(0).getLastCellNum();
			Row rowHeader = shName.getRow(0);
			Row rowValue = shName.getRow(intCounter);
			for(int i=0; i<ColCount;i++)
			{
				
				String strHeader= rowHeader.getCell(i).getStringCellValue();
				if(strHeader.equalsIgnoreCase(ColName)){
					//System.out.println(i);
					strValue = rowValue.getCell(i,Row.CREATE_NULL_AS_BLANK).getStringCellValue();
					//System.out.println(strValue);
					break;
				}
				
			}
			wkMasterSheet.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		return strValue;
	}
	//.............................................................................................................................................//
	
	
	
	//....Get the row count of a Sheet......................................................................//
	public int Rowcount(String SheetName){
		int ColCount = 0;
		try{
			String strValue=null;
			File file = new File(Path);
			FileInputStream fi = new FileInputStream(file);
			Workbook wkMasterSheet = null;
			wkMasterSheet =  new XSSFWorkbook(fi);
			Sheet shName = wkMasterSheet.getSheet(SheetName);
			ColCount = shName.getLastRowNum();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		return ColCount;
	}
	//.............................................................................................................................................//
		
	
	
	//....Update the result in the MasterSheet......................................................................//
	public void updateResult(String strTestName, LogStatus status){
		File file = new File(Path);
		String strStatus = status.toString();
		try {
			
			
			FileInputStream fi = new FileInputStream(file);
			Workbook wkMasterSheet = null;
			wkMasterSheet =  new XSSFWorkbook(fi);
			CellStyle style = wkMasterSheet.createCellStyle();
			Font ft = wkMasterSheet.createFont();
			
			if(strStatus.toLowerCase().contains("fail")){
				strStatus = "FAIL";
				ft.setColor(IndexedColors.DARK_RED.getIndex());
				style.setFont(ft);
			}else{
				strStatus = "PASS";
				ft.setColor(IndexedColors.DARK_GREEN.getIndex());
				style.setFont(ft);
				
			}
			Sheet shName = wkMasterSheet.getSheet("TC_Data");
			int ColCount = shName.getRow(0).getLastCellNum();
			Row rowHeader = shName.getRow(0);
			int intTestColNum = 0, intResultColNum=0 ;
			int RowCount = shName.getLastRowNum();
			for(int i=0; i<ColCount;i++)
			{
				
				String strHeader= rowHeader.getCell(i).getStringCellValue();
				if(strHeader.equalsIgnoreCase("TestCase_Name")){
					intTestColNum =i;
				}
				if(strHeader.equalsIgnoreCase("Result")){
					intResultColNum =i;
				}
				
			}
			for(int i=1;i<=RowCount;i++)
			{
				String strFlag = shName.getRow(i).getCell(intTestColNum).getStringCellValue();
				if(strFlag.equalsIgnoreCase(strTestName)){
					Row row = shName.getRow(i);
					if(row==null){
						row = shName.createRow(i);
					}
					Cell cell = row.createCell(intResultColNum);
					cell.setCellType(cell.CELL_TYPE_STRING);
					cell.setCellValue(strStatus);
					cell.setCellStyle(style);
					break;
				}
				
			}
			fi.close();
			FileOutputStream fo = new FileOutputStream(file);
			wkMasterSheet.write(fo);
			fo.close();
			wkMasterSheet.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//.............................................................................................................................................//
		
}
