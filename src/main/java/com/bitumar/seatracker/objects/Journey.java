package com.bitumar.seatracker.objects;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Journey implements ShipObserver
{
	private final float PARK_DISTANCE = 1000;
	private final int PARK_TIME = 240; // 4 hours
	
	private Date lastDate;
	private LogWriter writer;
	private WayPointManager wpManager; // Provides access to global waypoints
 	private ArrayList<WayPoint> waypoints;	//i'm only storing the terminals (THIS IS A LIST OF JOURNEY'S PERSONAL WAYPOINTS, NOT GLOBAL WAYPOINTS)
  	private Vessel vessel;
	private int parkedTimer;
    private boolean endJourney;
  
	public Journey(Vessel v, WayPointManager manager) {
		vessel = v;
		wpManager = manager;
		waypoints = new ArrayList<>();
        endJourney = false;
		writer = new LogWriter();
		lastDate = new Date();
	}
        
	@Override
	public void notify(Vessel ship) {
		checkWaypoints();
	}
	
  	public void checkWaypoints() {
		long diff = new Date().getTime() - lastDate.getTime();
		long minutesSince = diff / (60 * 1000) % 60;
		lastDate = new Date();
		
		if(endJourney) {
			if(vessel.getSpeed() > 0.3) {
				endJourney = false;
			}
		return;
		}
		if(vessel.getSpeed() < 0.3){                            //if the ship is detected as having little to no movement 
			for(WayPoint w : wpManager.getWayPoints()){
				if(w.getShipProximity(vessel) < PARK_DISTANCE){
					parkedTimer += minutesSince;
					if (parkedTimer >= PARK_TIME){
						if(w instanceof Terminal){
							if (!waypoints.contains(w))
							{
								waypoints.add(w);
							}
						}
						else if(w instanceof Refinery){
							this.complete();
						}
					}
					return;
				}
			}			
		}
		parkedTimer = 0;
	}
	
  	public void complete() {
        endJourney = true;
		float droppedOff = vessel.getCargo() / waypoints.size();	//amount dropped off at each terminal
		for(WayPoint w : waypoints) {		//adds that amount to each terminal
			if (w instanceof Terminal)
			{
				((Terminal)w).addProduct(droppedOff);
			}
		}
        waypoints.clear();
		
		String time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date());
		writer.addLine(time, "Completed: " + this);
		writer.print("messages.csv", "Time", "Message");		
	}		
	
	public Vessel getVessel()
	{
		return vessel;
	}
	
	public ArrayList<WayPoint> getWaypoints()
	{
		return waypoints;
	}
	
	public int getParkedTime()
	{
		return parkedTimer;
	}
	
	public WayPointManager getWayPointManager()
	{
		return wpManager;
	}
	
	@Override
	public String toString()
	{
		return "Journey of ship " + vessel.getName();
	}
}
