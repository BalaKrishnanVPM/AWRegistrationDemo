package Airwatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class AccessExcel 
{
	WebElement element = null;
	String req = null;

	ReadingFiles read = new ReadingFiles();
	AirwatchRun run = new AirwatchRun();
	
	
	public WebElement getElement(WebDriver driver,Map<String, String> objects,Logger logger) throws InterruptedException
	{
		String selectorType = objects.get("Selectortype");
		String selector = objects.get("Selector");
		String conditions = objects.get("Conditions");
		String keyboard = objects.get("Keyboard");
		String date = date();
		int time=0;
		if(objects.get("Time")!="")
		{
			time = Integer.parseInt(objects.get("Time"));
		}
		String autoitfile = objects.get("Autoit_filename");
		String wait = objects.get("Wait");
		try
		{
			if(selectorType.equals("xpath")&& conditions.isEmpty() && keyboard.isEmpty())
			{
				element = driver.findElement(By.xpath(selector));
			}
			else if(selectorType.equals("xpath")&& wait.contains("wait"))
			{
				read.eWait(driver, conditions, selector, time);
			}
			else if(selectorType.equals("xpath")&& keyboard.contains("k_control_a"))
			{
				driver.findElement(By.xpath(selector)).sendKeys(Keys.chord(Keys.CONTROL,"a"));
			}
			else if(selectorType.equals("xpath")&& keyboard.contains("k_backspace"))
			{
				Thread.sleep(2000);
				driver.findElement(By.xpath(selector)).sendKeys(Keys.BACK_SPACE);
			}
			else if(selectorType.equals("xpath")&& keyboard.contains("k_arrowdown"))
			{
				Thread.sleep(2000);
				driver.findElement(By.xpath(selector)).sendKeys(Keys.ARROW_DOWN);
			}
			else if(selectorType.equals("xpath")&& keyboard.contains("k_enter"))
			{
				Thread.sleep(1000);
				driver.findElement(By.xpath(selector)).sendKeys(Keys.ENTER);
			}
			else if(selectorType.equals("id"))
			{
				element = driver.findElement(By.id(selector));
			}
			else if(selector.equals("autoit_script")&& conditions.isEmpty())
			{
				read.autoit(driver, autoitfile, time);
			}
			else if(selectorType.equals("Screenshot"))
			{
				read.captureScreenshot(driver,selector,date,logger);
			}
			else if(selectorType.equals("xpath") && conditions.contains("TextFile"))
			{
				element = driver.findElement(By.xpath(selector));
			}
			else
			{
				System.out.println("invalid selector");
			}
		}
		catch(Exception e)
		{
			logger.error("An Exception has occured in getElement method --> "+e.toString());
			System.exit(0);
		}
		
		return element;
	}

	

	public void doAction(WebElement element,Map<String, String> objects,Logger logger) throws InterruptedException
	{
		String action = objects.get("Action");
		String input = objects.get("Input");
		String input1;
		try
		{
			if(action.equals("click"))
			{
				try 
				{
					element.click();
					Thread.sleep(2000);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			else if(action.equals("sendkeys"))
			{
				if(input.contains("Fr") || input.contains("Fp"))
				{
					input1 = read.fileInput(input);//function
					element.sendKeys(input1);
				}
				else
				{
					element.sendKeys(input);
				}
			}
			else if(action.equals("alert"))
			{
				element.click();
			}
			else if(action.isEmpty())
			{
			}
			else
			{
				System.out.println("invalid action");
			}
		}
		catch(Exception e)
		{
			logger.error("An Exception has occured in doAction method --> "+e.toString());
			System.exit(0);
		}
		
	}


	
	public static String date()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String nms_date = dateFormat.format(now);
		return nms_date;
	}
	
	

	@Override
	public String toString() 
	{
		return "accessExcel []";
	}

}

