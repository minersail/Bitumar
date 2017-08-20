package com.bitumar.seatracker.webscraper;

import com.bitumar.seatracker.objects.Journey;
import com.bitumar.seatracker.objects.Terminal;
import com.bitumar.seatracker.objects.Vessel;
import com.bitumar.seatracker.objects.WayPoint;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

public class ScrapeJob implements Job
{
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		SchedulerContext schedCont;
		try
		{
			schedCont = context.getScheduler().getContext();
		} catch (SchedulerException ex)
		{
			Logger.getLogger(ScrapeJob.class.getName()).log(Level.SEVERE, null, ex);
			return;
		}
		
		WebScraper scraper = (WebScraper)schedCont.get("scraper");
		ArrayList<Vessel> vessels = (ArrayList<Vessel>)schedCont.get("vessels");
		ArrayList<Journey> journeys = (ArrayList<Journey>)schedCont.get("journeys");
		
		ArrayList<ShipData> scrapeData = scraper.scrapeData();
		
		for (Vessel vessel : vessels)
		{
			for (ShipData data : scrapeData)
			{
				if (data.name.equals(vessel.getName()))
				{
					vessel.update(data);
					break;
				}
			}
		}
		
		//------------------------------------------------------------
		CSVPrinter csvFilePrinter = null;
				
		try 
		{						
			//initialize CSVPrinter object 
	        csvFilePrinter = new CSVPrinter(new FileWriter("src/main/resources/journey_log.csv", true), CSVFormat.DEFAULT);
	        
	        //Create CSV file header if empty
			if (Files.size(Paths.get("src/main/resources/journey_log.csv")) == 0)
				csvFilePrinter.printRecord("Time", "Vessel", "Waypoints", "Time Parked");
			
			for (Journey journey : journeys) 
			{
				String time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(context.getFireTime());
				String vessel = journey.getVessel().getName();
				final ArrayList<String> waypoints = new ArrayList<>();
				journey.getWaypoints().forEach((waypoint) -> {
					waypoints.add(waypoint.getName());
				});
				String parkTime = Integer.toString(journey.getParkedTime());
	            csvFilePrinter.printRecord(time, vessel, String.join(", ", waypoints), parkTime);
			}

			System.out.println("CSV file was created successfully !!!");
			
		} 
		catch (Exception e) 
		{
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace(System.out);
		}
		finally 
		{
			try 
			{
				csvFilePrinter.close();
			} 
			catch (IOException e) 
			{
				System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace(System.out);
			}
		}		
		
		//------------------------------------------------------------
		csvFilePrinter = null;
				
		try 
		{						
			//initialize CSVPrinter object 
	        csvFilePrinter = new CSVPrinter(new FileWriter("src/main/resources/vessel_log.csv", true), CSVFormat.DEFAULT);
	        
	        //Create CSV file header if empty
			if (Files.size(Paths.get("src/main/resources/vessel_log.csv")) == 0)
				csvFilePrinter.printRecord("Time", "Name", "Latitude", "Longitude", "Speed");
			
			for (Vessel vessel : vessels) 
			{
				String time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(context.getFireTime());
				String name = vessel.getName();
				String lat = Float.toString(vessel.getLat());
				String lon = Float.toString(vessel.getLong());
				String speed = Float.toString(vessel.getSpeed());
	            csvFilePrinter.printRecord(time, name, lat, lon, speed);
			}

			System.out.println("CSV file was created successfully !!!");
			
		} 
		catch (Exception e) 
		{
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace(System.out);
		}
		finally 
		{
			try 
			{
				csvFilePrinter.close();
			} 
			catch (IOException e) 
			{
				System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace(System.out);
			}
		}
		
		//------------------------------------------------------------
		csvFilePrinter = null;
				
		try 
		{						
			//initialize CSVPrinter object 
	        csvFilePrinter = new CSVPrinter(new FileWriter("src/main/resources/terminal_log.csv", true), CSVFormat.DEFAULT);
	        
	        //Create CSV file header if empty
			if (Files.size(Paths.get("src/main/resources/terminal_log.csv")) == 0)
				csvFilePrinter.printRecord("Time", "Name", "Product");
			
			for (WayPoint waypoint : journeys.get(0).getWayPointManager().getWayPoints())
			{
				if (waypoint instanceof Terminal)
				{
					Terminal terminal = (Terminal)waypoint;
				
					String time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(context.getFireTime());
					String name = waypoint.getName();
					String product = Float.toString(terminal.getProduct());
					
					csvFilePrinter.printRecord(time, name, product);
				}
			}

			System.out.println("CSV file was created successfully !!!");
			
		} 
		catch (Exception e) 
		{
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace(System.out);
		}
		finally 
		{
			try 
			{
				csvFilePrinter.close();
			} 
			catch (IOException e) 
			{
				System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace(System.out);
			}
		}
	}
}
