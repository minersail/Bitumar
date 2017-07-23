package com.bitumar.seatracker.webscraper;

public class ShipData
{
	public String name;
	public float latitude;
	public float longitude;
	public float speed;
	public String IMO;
	
	@Override
	public String toString()
	{
		return "Name: " + name + "\n" +
			"Latitude: " + latitude + "\n" + 
			"Longitude: " + longitude + "\n" + 
			"Speed: " + speed + "\n" + 
			"IMO: " + IMO + "\n";
	}
}