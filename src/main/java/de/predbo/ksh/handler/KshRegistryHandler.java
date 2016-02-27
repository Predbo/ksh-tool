package de.predbo.ksh.handler;

import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;

import de.predbo.ksh.CsvManager;
import de.predbo.ksh.KshConfig;
import de.predbo.ksh.KshEntry;
import de.predbo.ksh.KshRegistry;

public class KshRegistryHandler {

	private static final Logger _logger = LoggerFactory.getLogger(KshRegistryHandler.class);
	
	private final KshRegistry _kshRegistry = new KshRegistry();
	private final CsvManager _csvManager;

	private KshConfig _kshConfig;
	
	public KshRegistryHandler(KshConfig kshConfig) throws IOException {
		_kshConfig = kshConfig;
		_csvManager = new CsvManager(_kshConfig);
	}
	
	public void getAllEntries(RoutingContext routingContext) {
		try {
			routingContext.response()
						  .putHeader("content-type", "application/json; charset=utf-8")
						  .end(Json.encodePrettily(_csvManager.getCsvContentAsList()));
		} catch (Exception e) {
			e.printStackTrace();
			routingContext.response().end();
		}
		

	}

	public void registryNewEntry(String jsonObjectAsString) throws IOException {
		KshEntry newEntry = Json.decodeValue(jsonObjectAsString, KshEntry.class);
		//TODO validation
		
		_csvManager.addEntryToCsvFile(newEntry);
		_kshRegistry.registerEntry(newEntry);
	}

	public void removeEntry(String entryId) throws Exception {
		_csvManager.removeEntryFromCsvFile(entryId);
	}

	public KshConfig getKshConfig() {
		return _kshConfig;
	}

	
	

}
