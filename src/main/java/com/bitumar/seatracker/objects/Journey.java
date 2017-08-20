package com.bitumar.seatracker.objects;

import java.util.ArrayList;

public class Journey implements ShipObserver
{
	private final float PARK_DISTANCE = 1000;
	private final int PARK_TIME = 240; // 4 hours
	
	private WayPointManager wpManager; // Provides access to global waypoints
 	private ArrayList<WayPoint> waypoints;	//i'm only storing the terminals (THIS IS A LIST OF JOURNEY'S PERSONAL WAYPOINTS, NOT GLOBAL WAYPOINTS)
  	private Vessel vessel;
	private int parkedTimer; 
  
	public Journey(Vessel v, WayPointManager manager){
		vessel = v;
		wpManager = manager;
		waypoints = new ArrayList<>();
	}
        
	@Override
	public void notify(Vessel ship){
		checkWaypoints();
	}
	
  	public void checkWaypoints(){
    	if(vessel.getSpeed() < 0.3){		//if the ship is detected as having little to no movement 
			for(WayPoint w : wpManager.getWayPoints()){
				if(w.getShipProximity(vessel) < PARK_DISTANCE)
				{
					parkedTimer += 15;
					
					if (parkedTimer >= PARK_TIME)
					{
						if(w instanceof Terminal){		//funtion getType should return a string or number or something to id it
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
	
  	public void complete(){
		float droppedOff = vessel.getCargo() / waypoints.size();	//amount dropped off at each terminal
		for(WayPoint w : waypoints) {		//adds that amount to each terminal
			if (w instanceof Terminal)
			{
				((Terminal)w).addProduct(droppedOff);
			}
		}
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
}
