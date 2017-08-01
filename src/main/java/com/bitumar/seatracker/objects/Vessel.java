package com.bitumar.seatracker.objects;

import com.bitumar.seatracker.webscraper.ShipData;
import java.util.ArrayList;

public class Vessel
{
	private float latitude;
	private float longitude;
	private float cargo;
	private float cargoMax;
	private float speed;
        
        private ArrayList<ShipObserver> shipObservers;

	public Vessel(String CSV)
	{
		// Unsure about implementation for now; Should parse CSV
		// Maybe id system to link to ships
	}
        
        public void update(ArrayList<ShipData> data){
            for(ShipObserver observer:shipObservers){
                observer.notify(this);
            }
        }

	public void setLat(float lat)
	{
		latitude = lat;
	}

	public float getLat()
	{
		return latitude;
	}

	public void setLong(float longt)
	{
		longitude = longt;
	}

	public float getLong()
	{
		return longitude;
	}

	public void addCargo(float amt)
	{
		if(cargo + amt >= cargoMax)
			cargo = cargoMax;
		else cargo += amt;
	}
	
	public float getCargo()
	{
		return cargo;
	}

	public void setSpeed(float spd)
	{
		speed = spd;
	}

	public float getSpeed()
	{
		return speed;
	}
}
