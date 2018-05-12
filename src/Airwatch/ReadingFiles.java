package Airwatch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReadingFiles 
{
	private static final String dir = System.getProperty("user.dir");

	private static final String req_files = dir+"\\req\\";
	private static final String file_req = req_files+"Airwatch_req.txt";
	private static final String file_paths = req_files+"Airwatch_parameters.txt";

	public static String fInput = null;
	public static List<String> inputs;
	public static WebDriverWait wait;
	
	WebElement element;
	
	public List<String> copyFile(Logger logger) 
	{
		inputs = new ArrayList<String>();
		String paths = null;
		String req = null;
		try 
		{
			//Robot robot = new Robot();
			try 
			{
				req = FileUtils.readFileToString(new File(file_req), "UTF-8");
			} 
			catch (IOException e1) 
			{
				logger.error("An Exception has occured while reading the contents from Airwatch_req.txt file --> \n"+e1.getMessage());
				System.exit(0);
			}
			String input_data[] = req.split("\n");
			
			inputs.add(input_data[0]);//zpl		    0
			inputs.add(input_data[1]);//group name  1
			
			createParametersFile(inputs.get(0), logger);

			try 
			{
				paths = FileUtils.readFileToString(new File(file_paths), "UTF-8");
			} 
			catch (IOException e1) 
			{
				logger.error("An Exception has occured while reading the contents from Airwatch_parameter.txt file --> \n"+e1.getMessage());
				System.exit(0);
			}
			String parameter[] = paths.split("\n");

			inputs.add(parameter[0]);//temp	path to download    2
			inputs.add(parameter[1]);//remove software folder   3
			inputs.add(parameter[3]);//filename		            4
			inputs.add(parameter[4]);//productname	            5

		}
		catch (Exception e) 
		{
			logger.error("An Exception has occured i --> "+e.toString());
			System.exit(0);
		}
		return inputs;
				
	}


	
	public static void createParametersFile(String zpl, Logger logger) 
	{
		try
		{
			DateFormat dateFormat2 = new SimpleDateFormat("MMMddyyHm");
			Date now = new Date();
			String fp_name = dateFormat2.format(now).toUpperCase();

			File file = new File(file_paths);

			FileWriter writer = new FileWriter(file);
			file.createNewFile();

			writer.write("/JetDisk/Newpackage/temp\n");//download path
			writer.write("/JetDisk/Newpackage/Software\n");//remove folder
			writer.write(dir+"\\temp\n");
			writer.write("OFFSHORE-F_"+fp_name+"_"+zpl+"\n");
			writer.write("OFFSHORE-P_"+fp_name+"_"+zpl);

			writer.flush();
			writer.close();
		}
		catch(Exception e)
		{
			logger.error("An Exception has occured while creating and writing the data into Airwatch_parameters text file --> \n"+e.toString());
			System.exit(0);
		}
	}

	
	public String fileInput(String f)
	{
		
		if(f.contains("Fr_zpl")){ fInput = inputs.get(0); }
		else if(f.contains("Fr_group")){ fInput = inputs.get(1); }
		
		
		else if(f.contains("Fp_temp")){ fInput = inputs.get(2); }
		else if(f.contains("Fp_remove")){ fInput = inputs.get(3); }
		else if(f.contains("Fp_filename")){ fInput = inputs.get(4); }
		else if(f.contains("Fp_productname")){ fInput = inputs.get(5); }
		
		else
		{
			System.out.println("Invalid input in file/excel");
		}
		return fInput;
	}

	
	public void autoit(WebDriver driver,String name, int time)
	{
		try 
		{
			Runtime.getRuntime().exec(req_files+name+".exe");
			try 
			{
				Thread.sleep(time);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		} 
		catch (IOException e) 
		{
			System.out.println("An Exception has occured while trying to run the autoit exe file --> "+e.getMessage());
			driver.quit();
			System.exit(0);
		}
	}


	public void eWait(WebDriver driver,String conditions, String selector, int time) 
	{
		wait = new WebDriverWait(driver, time);
		
		if(conditions.equals("vElement"))
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(selector)));
		}
		else if(conditions.equals("ivElement"))
		{
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(selector)));
		}
		else
		{
			System.out.println("Invalid wait condition");
		}
	}
	
	
	public void captureScreenshot(WebDriver driver, String name, String time,Logger logger)
	{
		try 
		{   
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); 
			FileUtils.copyFile(src, new File(dir+"\\screenshots\\"+time+"/"+name+".png"));                 
		} 
		catch (IOException e)
		{
			logger.error("There is some problem in Screen Capturing --> \n"+e.toString());
			driver.quit();
			System.exit(0);
		}
		System.out.println("*****Screen capture Done******");
	}

}
