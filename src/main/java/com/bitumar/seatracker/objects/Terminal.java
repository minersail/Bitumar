package com.bitumar.seatracker.objects;

public class Terminal extends WayPoint
{
	private	float amountProduct;

	public Terminal(String name, float lat, float lon)
	{
		super(name, lat, lon);
	}
	
	public void addProduct(float amount)
	{
		amountProduct += amount;
	}
	
	public float getProduct()
	{
		return amountProduct;
	}
}