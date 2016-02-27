package de.predbo.ksh;

import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class KshConfig {
	
	private static final Logger _logger = LoggerFactory.getLogger(KshConfig.class);

	private int _maxSheetNumber = 99999;
	private int _maxCurrentNumber = 20;
	private int _maxFamilyNumber = 77777;
	private int _maxPrice = 66666;
	
	public KshConfig(JsonObject config) {
		try {
			_maxSheetNumber = config.getInteger("max.sheet.number");
			_maxCurrentNumber = config.getInteger("max.current.number");
			_maxFamilyNumber = config.getInteger("max.familiy.number");
			_maxPrice = config.getInteger("max.pricer");
		} catch (Exception e) {
			_logger.info("No config file available, will use default values");
		}
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
