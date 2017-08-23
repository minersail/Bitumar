package com.bitumar.seatracker.webscraper;

import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebScraper
{
	public WebScraper()
	{
		System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
	}
	
	public ArrayList<ShipData> scrapeData()
	{
		ArrayList<ShipData> data = new ArrayList<>();
		
		WebDriver driver = new ChromeDriver();
		driver.get("http://www.marinetraffic.com");
		driver.findElement(By.id("user-logggin")).click();
		driver.findElement(By.id("email")).sendKeys("jkwoo1234@gmail.com");
		driver.findElement(By.id("password")).sendKeys("Port8080");
		driver.findElement(By.cssSelector("button.vertical-offset-10")).click();
		
		WebElement element1 = (new WebDriverWait(driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[href='/en/ais/index/ships/all/_:35575cc9087fd216f2873d74b5ae8426']")));	
		
		driver.get("http://www.marinetraffic.com/en/ais/index/fleet/all/_:35575cc9087fd216f2873d74b5ae8426/per_page:20");
		
		ArrayList<String> shipList = new ArrayList<>();
				
		while (true)
		{
			driver.findElements(By.cssSelector("table tr:nth-child(n+2)")).forEach((WebElement t) ->
			{
				shipList.add(t.findElement(By.cssSelector("td:nth-child(4) a")).getAttribute("href"));
			});
			
			if (driver.findElements(By.cssSelector("a[rel='next']")).isEmpty())
			{
				break;
			}
			else
			{
				driver.findElement(By.cssSelector("a[rel='next']")).click();
				new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".page_select")));
			}
		}
		
		shipList.forEach((String url) -> 
		{
			driver.get(url);
			ShipData shipdata = new ShipData();
			shipdata.name = driver.findElement(By.cssSelector("h1")).getText();
			shipdata.latitude = Float.parseFloat(driver.findElement(By.cssSelector("a.details_data_link")).getText().split("/")[0].replace("°", ""));
			shipdata.longitude = Float.parseFloat(driver.findElement(By.cssSelector("a.details_data_link")).getText().split("/")[1].replace("°", ""));
			shipdata.speed = Float.parseFloat(driver.findElement(By.cssSelector("#tabs-last-pos div.table-cell.cell-full div:nth-child(6) strong")).getText().split("/")[0].replace("kn", ""));
			shipdata.IMO = driver.findElement(By.cssSelector("div[style='border-right: 1px dotted #000'] div:nth-child(1) b")).getText();
			data.add(shipdata);
		});
		
		driver.quit();
		
		return data;
	}	
}
