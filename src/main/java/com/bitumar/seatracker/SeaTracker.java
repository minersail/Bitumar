package com.bitumar.seatracker;

import com.bitumar.seatracker.objects.Journey;
import com.bitumar.seatracker.objects.Vessel;
import com.bitumar.seatracker.objects.WayPointManager;
import com.bitumar.seatracker.webscraper.ScrapeJob;
import com.bitumar.seatracker.webscraper.WebScraper;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class SeaTracker
{
	public static void main(String[] arg) throws IOException, SchedulerException, ParseException
	{		
		WebScraper scraper = new WebScraper();
		WayPointManager wpManager = new WayPointManager();
		
		ArrayList<Vessel> vessels = new ArrayList<>();
		try (CSVParser parser = new CSVParser(new FileReader("src/main/resources/shipsizes.csv"), CSVFormat.DEFAULT.withHeader()))
		{
			for (CSVRecord record : parser)
			{
				Vessel vessel = new Vessel(record.get("Name"), record.get("Gross tonnage").equals("-") ? 0 : Integer.parseInt(record.get("Gross tonnage")));
				vessels.add(vessel);
			}
		}
		
		ArrayList<Journey> journeys = new ArrayList<>();
		for (Vessel vessel : vessels)
		{
			Journey journey = new Journey(vessel, wpManager);
			vessel.addObserver(journey);
			journeys.add(journey);
		}
		
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.getContext().put("scraper", scraper);
		sched.getContext().put("vessels", vessels);
		sched.getContext().put("journeys", journeys);

		JobDetail job = newJob(ScrapeJob.class)
			.withIdentity("job1", "group1")	
			.build();

		CronTrigger trigger = newTrigger()
			.withIdentity("trigger1", "group1")
			.withSchedule(CronScheduleBuilder.cronSchedule(new CronExpression("0 17 * * * ?")))
			.build();

		sched.start();
		sched.scheduleJob(job, trigger);
	}
}