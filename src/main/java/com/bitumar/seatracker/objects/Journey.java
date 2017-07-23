package com.bitumar.seatracker.objects;

import java.util.ArrayList;

public class Journey
{
	private final float PARK_DISTANCE = 100;
	
 	private ArrayList<WayPoint> waypoints;	//i'm only storing the terminals
  	private Vessel vessel;
  
	public Journey(Vessel v){
		vessel = v;
	}
	
  	public void checkWaypoint(){
    	if(vessel.getSpeed() < 2){		//if the ship is detected as having little to no movement 
			for(WayPoint w : waypoints){	//I'm not sure if we will have a list of waypoints to check
				if(w.getShipProximity(vessel) < PARK_DISTANCE){
					if(w instanceof Terminal){		//funtion getType should return a string or number or something to id it
						waypoints.add(w);
						break;
					}
					else if(w instanceof Refinery){
						this.complete();
						break;
					}
				}
			}			
		}
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
		
}
