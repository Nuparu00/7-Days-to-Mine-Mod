package com.nuparu.sevendaystomine.world.gen.city;

import java.util.ArrayList;

public class Crossing {

	private ArrayList<Street> streets = new ArrayList<Street>();
	
	private City city;
	
	public Crossing(City city) {
		this.setCity(city);
	}
	
	public void addStreet(Street street) {
		streets.add(street);
	}
	
    @SuppressWarnings("unchecked")
	public ArrayList<Street> getStreets() {
    	return (ArrayList<Street>) streets.clone();
    }
    
    public int getStreetsCont() {
    	return streets.size();
    }

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
}
