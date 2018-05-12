package Airwatch;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jxl.Sheet;
import jxl.Workbook;

public class ExcelRead 
{
	public ArrayList<Map<String,String>> datalist = new ArrayList<Map<String,String>>();
	public ArrayList<Map<String,String>> linklist = new ArrayList<Map<String,String>>();
	private Properties pro;
	

	public ArrayList<Map<String,String>> readDataSheet()
	{
		Map<String,String> datamap;
		
		try 
		{
			pro.load(new FileInputStream("./configuration/config.properties"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		String datafilepath=pro.getProperty("DataFilePath");
		String datasheetname = pro.getProperty("DataSheetName");
		
		Sheet datasheet = excelread(datafilepath,datasheetname);
		
		for(int row=1;row<datasheet.getRows();row++)
		{
			datamap = new HashMap<String, String>();
			for(int column=0;column<datasheet.getColumns();column++)
			{
				datamap.put(datasheet.getCell(column, 0).getContents(), datasheet.getCell(column, row).getContents());
				
			}
			datalist.add(datamap);
		}
		return datalist;
	}
	
	
	public ArrayList<Map<String,String>> linkDataSheet()
	{
		Map<String,String> linkmap ;
		
		try 
		{
			pro.load(new FileInputStream("./configuration/config.properties"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		String datafilepath=pro.getProperty("DataFilePath");
		String datasheetname = pro.getProperty("DataSheetName2");
		
		Sheet datasheet = excelread(datafilepath,datasheetname);
		
		for(int row=1;row<datasheet.getRows();row++)
		{
			linkmap = new HashMap<String, String>();
			for(int column=0;column<datasheet.getColumns();column++)
			{
				linkmap.put(datasheet.getCell(column, 0).getContents(), datasheet.getCell(column, row).getContents());
			}
			linklist.add(linkmap);
		}
		
		return linklist;
	}
	

	public static Sheet excelread(String datafilePath, String datasheetName) 
	{
		Workbook wb = null;
		try 
		{
			wb = Workbook.getWorkbook(new FileInputStream(datafilePath));
		} 
		catch (Exception e) 
		{
			System.out.println("Failed to load sheet - '"+datasheetName+"'");
		}
		Sheet sh = wb.getSheet(datasheetName);
		return sh;
	}
	

	@Override
	public String toString() 
	{
		return "Excelread[]";
	}
	
	
	public ExcelRead(Properties property) 
	{
		super();
		this.pro = property;
	}
	
}