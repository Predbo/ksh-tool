package de.predbo.ksh.handler;

import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import de.predbo.ksh.KshEntry;
import de.predbo.ksh.KshRegistry;

public class KshRegistryHandler {

	private static final Logger _logger = LoggerFactory.getLogger(KshRegistryHandler.class);
	
	private final KshRegistry _kshRegistry = new KshRegistry();

	public KshRegistryHandler() {
		
	}
	
	public void getAllEntries(RoutingContext routingContext) {
		routingContext.response()
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(_kshRegistry.getEntries().values()));
	}

	public void registryNewEntry(String jsonObjectAsString) {
		KshEntry newEntry = Json.decodeValue(jsonObjectAsString, KshEntry.class);
		//TODO validation
		_kshRegistry.registerEntry(newEntry);
	}

	
	

}
