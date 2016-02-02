package de.predbo.ksh;

public class KshEntry {
	
	private int _familyNumber;
	private float _price;
	
	public int getFamilyNumber() {
		return _familyNumber;
	}
	
	public void setFamilyNumber(int number) {
		_familyNumber = number;
	}
	
	public float getPrice() {
		return _price;
	}
	
	public void setPrice(float price) {
		_price = price;
	}
	
	@Override
	public String toString() {
		return _familyNumber + " : " + _price;
	}
	
}
