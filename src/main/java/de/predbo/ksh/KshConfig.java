package de.predbo.ksh;

import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class KshConfig {
	
	private static final Logger _logger = LoggerFactory.getLogger(KshConfig.class);

	private int _maxSheetNumber;
	private int _maxCurrentNumber;
	private int _maxFamilyNumber;
	private int _maxPrice;
	
	public KshConfig(JsonObject config) {
		if (config == null) {
			config = new JsonObject();
			_logger.info("Keine Konfiguration übergeben, folgende Standard Werte werden verwendet:");
		} else {
			_logger.info("Folgende Konfiguration wurde übegeben:");
		}
		_maxSheetNumber = config.getInteger("max.sheet.number", 300);
		_maxCurrentNumber = config.getInteger("max.current.number", 40);
		_maxFamilyNumber = config.getInteger("max.familiy.number", 120);
		_maxPrice = config.getInteger("max.price", 10000);
		print();
	}
	
	public void print()  {
		_logger.info("Maximale Blattnummer = " + _maxSheetNumber);
		_logger.info("Maximale laufende Nummer pro Blatt = " + _maxCurrentNumber);
		_logger.info("Maximale Familiennummer = " + _maxFamilyNumber);
		_logger.info("Maximaler Preis = " + _maxPrice);
	}
	
	public int getMaxSheetNumber() {
		return _maxSheetNumber;
	}
	public void setMaxSheetNumber(int maxSheetNumber) {
		_maxSheetNumber = maxSheetNumber;
	}
	public int getMaxCurrentNumber() {
		return _maxCurrentNumber;
	}
	public void setMaxCurrentNumber(int maxCurrentNumber) {
		_maxCurrentNumber = maxCurrentNumber;
	}
	public int getMaxFamilyNumber() {
		return _maxFamilyNumber;
	}
	public void setMaxFamilyNumber(int maxFamilyNumber) {
		_maxFamilyNumber = maxFamilyNumber;
	}
	public int getMaxprice() {
		return _maxPrice;
	}
	public void setMaxprice(int maxprice) {
		_maxPrice = maxprice;
	}
	
	
}
