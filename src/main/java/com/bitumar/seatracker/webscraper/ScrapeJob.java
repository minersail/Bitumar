package com.bitumar.seatracker.webscraper;

import com.bitumar.seatracker.objects.Journey;
import com.bitumar.seatracker.objects.LogWriter;
import com.bitumar.seatracker.objects.Terminal;
import com.bitumar.seatracker.objects.Vessel;
import com.bitumar.seatracker.objects.WayPoint;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
		
		LogWriter writer = new LogWriter();
		for (Journey journey : journeys) 
		{
			String time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(context.getFireTime());
			String vessel = journey.getVessel().getName();
			final ArrayList<String> waypoints = new ArrayList<>();
			journey.getWaypoints().forEach((waypoint) -> {
				waypoints.add(waypoint.getName());
			});
			String parkTime = Integer.toString(journey.getParkedTime());
			writer.addLine(time, vessel, String.join(", ", waypoints), parkTime);
		}
		writer.print("resources/journey_log.csv", "Time", "Vessel", "Waypoints", "Time Parked");
					
		for (Vessel vessel : vessels) 
		{
			String time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(context.getFireTime());
			String name = vessel.getName();
			String lat = Float.toString(vessel.getLat());
			String lon = Float.toString(vessel.getLong());
			String speed = Float.toString(vessel.getSpeed());
			writer.addLine(time, name, lat, lon, speed);
		}		
		writer.print("resources/vessel_log.csv", "Time", "Name", "Latitude", "Longitude", "Speed");
		
		for (WayPoint waypoint : journeys.get(0).getWayPointManager().getWayPoints())
		{
			if (waypoint instanceof Terminal)
			{
				Terminal terminal = (Terminal)waypoint;

				String time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(context.getFireTime());
				String name = waypoint.getName();
				String product = Float.toString(terminal.getProduct());

				writer.addLine(time, name, product);
			}
		}		
		writer.print("resources/terminal_log.csv", "Time", "Name", "Product");
	}
}
