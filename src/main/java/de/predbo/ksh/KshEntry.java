package de.predbo.ksh;

public class KshEntry {
	
	private long _sheetNumber;
	private long _currentNumber;
	private long _familyNumber;
	private double _price;
	
	
	public long getCurrentNumber() {
		return _currentNumber;
	}

	public void setCurrentNumber(long currentNumber) {
		_currentNumber = currentNumber;
	}

	public long getFamilyNumber() {
		return _familyNumber;
	}
	
	public void setFamilyNumber(long number) {
		_familyNumber = number;
	}
	
	public double getPrice() {
		return _price;
	}
	
	public void setPrice(double price) {
		_price = price;
	}
	
	@Override
	public String toString() {
		return _sheetNumber+"-"+_currentNumber + " " + _familyNumber + " : " + _price;
	}

	public long getSheetNumber() {
		return _sheetNumber;
	}

	public void setSheetNumber(long sheetNumber) {
		_sheetNumber = sheetNumber;
	}
	
}
