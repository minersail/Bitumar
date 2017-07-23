package com.bitumar.seatracker.objects;

class Terminal extends WayPoint
{
	private	float amountProduct;

	public Terminal(float lat, float lon)
	{
		super(lat, lon);
	}
	
	public void addProduct(float amount)
	{
		amountProduct += amount;
	}
}