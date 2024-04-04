package nuparu.sevendaystomine.world.gen.city;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.util.EnumFacing;

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

	public Street getByyFacing(EnumFacing facing, Street exclude) {
		for (Iterator<Street> iterator = streets.iterator(); iterator.hasNext();) {
			Street street = (Street) iterator.next();
			if(exclude != null && exclude == street)continue;
			if (street.facing == facing)
				return street;
		}
		return null;
	}
	public boolean isStreetInDirection(EnumFacing facing, Street exclude) {
		for (Iterator<Street> iterator = streets.iterator(); iterator.hasNext();) {
			Street street = (Street) iterator.next();
			if(exclude != null && exclude == street)continue;
			if (street.facing == facing)
				return true;
		}
		return false;
	}

}
