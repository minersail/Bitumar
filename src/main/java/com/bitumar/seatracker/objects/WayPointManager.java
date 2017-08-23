package com.bitumar.seatracker.objects;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class WayPointManager
{
	private ArrayList<WayPoint> waypoints;
	
	public WayPointManager()
	{
		waypoints = new ArrayList<>();
		try (CSVParser parser = new CSVParser(new FileReader("resources/terminalrefinerylist.csv"), CSVFormat.DEFAULT.withHeader()))
		{
			for (CSVRecord record : parser)
			{
				if (record.get("Type").equals("Terminal"))
					waypoints.add(new Terminal(record.get("Name"), Float.parseFloat(record.get("Latitude")), Float.parseFloat(record.get("Longitude"))));
				else if (record.get("Type").equals("Refinery"))
					waypoints.add(new Refinery(record.get("Name"), Float.parseFloat(record.get("Latitude")), Float.parseFloat(record.get("Longitude"))));
			}
		} catch (IOException ex)
		{
			Logger.getLogger(WayPointManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public ArrayList<WayPoint> getWayPoints()
	{
		return waypoints;
	}
}
