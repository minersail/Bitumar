package com.bitumar.seatracker.objects;

public class Vessel
{
	private String name;
	private float latitude;
	private float longitude;
	private float cargo;
	private float cargoMax;
	private float speed;

	public Vessel(String shipName, float maxCargo)
	{
		name = shipName;
		latitude = 0;
		longitude = 0;
		cargo = 0;
		cargoMax = maxCargo;
		speed = 0;
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

	public void setName(String shipName)
	{
		name = shipName;
	}

	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return "Vessel " + name + " at location (" + latitude + ", " + longitude + ")";
	}
}
