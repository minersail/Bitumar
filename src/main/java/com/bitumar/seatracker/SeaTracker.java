package com.bitumar.seatracker;

import com.bitumar.seatracker.webscraper.ShipData;
import com.bitumar.seatracker.webscraper.WebScraper;
import java.util.ArrayList;

public class SeaTracker
{
	public static void main(String[] arg)
	{
		WebScraper scraper = new WebScraper();
		ArrayList<ShipData> data = scraper.scrapeData();
		
		System.out.println(data);
		
		// Matt- Use src/main/resources/shipsizes.csv to create an array of ships
		//		 Write function to take in the data ArrayList (provided above) and update all ships in array
		// Ben- Use src/main/resources/terminalrefinerylist.csv to create an array of waypoints
		//		Make sure that the WayPoint getShipProximity() forumla works as intended
		// Jake- Your thingy is complicated see me
	}
}