package com.bitumar.seatracker;

import com.bitumar.seatracker.objects.Vessel;
import com.bitumar.seatracker.webscraper.ShipData;
import com.bitumar.seatracker.webscraper.WebScraper;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class SeaTracker
{
	public static void main(String[] arg) throws IOException
	{
		WebScraper scraper = new WebScraper();
		//ArrayList<ShipData> data = scraper.scrapeData();
		
		//System.out.println(data);
		
		// Matt- Use src/main/resources/shipsizes.csv to create an array of ships
		//		 Write function to take in the data ArrayList (provided above) and update all ships in array
		// Ben- Use src/main/resources/terminalrefinerylist.csv to create an array of waypoints
		//		Make sure that the WayPoint getShipProximity() forumla works as intended
		// Jake- Implement observer pattern on terminal
		
		ArrayList<Vessel> vessels = new ArrayList<>();
		try (CSVParser parser = new CSVParser(new FileReader("src/main/resources/shipsizes.csv"), CSVFormat.DEFAULT.withHeader()))
		{
			for (CSVRecord record : parser)
			{
				Vessel vessel = new Vessel(record.get("Name"), record.get("Gross tonnage").equals("-") ? 0 : Integer.parseInt(record.get("Gross tonnage")));
				vessels.add(vessel);
			}
		}
		System.out.println(vessels);
	}
}