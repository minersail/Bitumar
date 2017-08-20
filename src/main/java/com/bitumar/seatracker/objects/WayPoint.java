package com.bitumar.seatracker.objects;

public abstract class WayPoint
{
	String name;
	float latitude;
	float longitude;
	
	protected WayPoint(String n, float lat, float lon)
	{
		name = n;
		latitude = lat;
		longitude = lon;
	}
	
	public float getShipProximity(Vessel vessel)
	{
		int EARTH_RADIUS = 6371000;

		// Haversine forumla
		double vesselLatRad = vessel.getLat() * Math.PI / 180;
		double waypntLatRad = latitude * Math.PI / 180;

		double latDiffRad = (latitude - vessel.getLat()) * Math.PI / 180;
		double longDiffRad = (longitude - vessel.getLong()) * Math.PI / 180;

		double a = Math.sin(latDiffRad / 2) * Math.sin(latDiffRad / 2) +
			Math.cos(vesselLatRad) * Math.cos(waypntLatRad) *
			Math.sin(longDiffRad / 2) * Math.sin(longDiffRad / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return (float)c * EARTH_RADIUS;
	}
	
	@Override
	public String toString()
	{
		return "Waypoint " + name + " at (" + latitude + ", " + longitude + ")";
	}
	
	public String getName()
	{
		return name;
	}		
}