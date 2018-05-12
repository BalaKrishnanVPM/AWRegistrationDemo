package Airwatch;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import Airwatch.AccessExcel;
import Airwatch.ExcelRead;


public class AirwatchRun 
{
	private static final String dir = System.getProperty("user.dir");
	private static final String req_files = dir+"\\req\\";
	
	static WebDriver driver;
	static Properties property = new Properties();

	public static void main(String[] args) 
	{
		Logger logger=Logger.getLogger("AirwatchRun");
		PropertyConfigurator.configure(req_files+"log4j.properties");
		
		try 
		{
			property.load(new FileInputStream("./configuration/config.properties"));
			System.out.println(req_files);
			runTest(logger);
		} 
		catch (IOException e) 
		{
			System.out.println("Failed to load the properties file");
		}
		catch (Exception e) 
		{
			driver.quit();
			System.out.println("Exception occured --> "+e.toString());
			e.printStackTrace();
		}

	}

	public static void runTest(Logger logger) throws InterruptedException 
	{
		WebDriver driver;
		ExcelRead elementAccess = new ExcelRead(property);
		AccessExcel accessexcel = new AccessExcel();
		ReadingFiles read = new ReadingFiles();
		read.copyFile(logger);
		
		WebElement element;
		Map<String, String> objects;

		elementAccess.readDataSheet();
		elementAccess.linkDataSheet();
		try
		{
			for (Map<String, String> data : elementAccess.linklist) 
			{
				driver = launchBrowser();
				driver.get(data.get("URL"));
			
				for (int size = 0; size < elementAccess.datalist.size(); size++) 
				{
					objects = elementAccess.datalist.get(size);
					
					element = accessexcel.getElement(driver, objects, logger);
					accessexcel.doAction(element, objects, logger);
				}
			}
		}
		catch(Exception e)
		{
			logger.error("An Exception has occured in runTest method --> "+e.toString());
			System.exit(0);
		}
		
	}

	public static WebDriver launchBrowser() 
	{
		System.setProperty("webdriver.chrome.driver", "D:\\Workspace_eclipse\\selinumjars\\chromedriver.exe");
		driver = new ChromeDriver();
		driver .manage().window().maximize();
		return driver;
	}

}


