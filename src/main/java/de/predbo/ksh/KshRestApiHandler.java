package de.predbo.ksh;

import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class KshRestApiHandler {

	private static final Logger _logger = LoggerFactory.getLogger(KshRestApiHandler.class);
	
	private CsvManager _csvManager;
	private KshConfig _kshConfig;

	public KshRestApiHandler(CsvManager csvManager, KshConfig kshConfig) {
		_csvManager = csvManager;
		_kshConfig = kshConfig;
	}
	
	public void getAllEntries(RoutingContext routingContext) {
		try {
			List<KshEntry> allEntries = _csvManager.getCsvContentAsList();
			routingContext.response()
						  .putHeader("content-type", "application/json; charset=utf-8")
						  .end(Json.encodePrettily(allEntries));
		} catch (Exception e) {
			e.printStackTrace();
			routingContext.response().end();
		}
	}
	
	public void addEntry(RoutingContext routingContext) {
		try {
			KshEntry newEntry = Json.decodeValue(routingContext.getBodyAsString(), KshEntry.class);
			_csvManager.addEntryToCsvFile(newEntry);

			routingContext.response()
						  .setStatusCode(201)
			              .putHeader("content-type", "application/json; charset=utf-8")
			              .end(Json.encodePrettily(newEntry));
		} catch (Throwable t) {
			_logger.error("Der Datensatz: " + routingContext.getBodyAsString() + " konnte nicht gespeichert werden. Es ist folgender Fehler aufgretreten: ", t);
			sendErrorResponse(routingContext, t);
		}
	}
	
	public void deleteEntry(RoutingContext routingContext) {
		String idString = "undefined";
		try {
			idString = routingContext.request().getParam("id");
			_csvManager.removeEntryFromCsvFile(idString);
			routingContext.response().setStatusCode(204).end();
		} catch (Throwable t) {
			_logger.error("Der Datensatz: " + idString + " konnte nicht gelöscht werden. Es ist folgender Fehler aufgretreten: ", t);
			sendErrorResponse(routingContext, t);
		}
	}
	
	public void getMaxCurrentNumber(RoutingContext routingContext) {
		routingContext.response().end(_kshConfig.getMaxCurrentNumber()+"");
	}

		
		

	private void sendErrorResponse(RoutingContext routingContext, Throwable t) {
		String errorResponseMessage = "";
		if (t.getMessage().contains("Failed to decode:Can not construct instance of")){
			errorResponseMessage = "Bitte Werte überprüfen, nur Zahlen sind erlaubt";
		} else if (t.getMessage().contains(_kshConfig.getMaxSheetNumber() + "")) {
			errorResponseMessage = "Blattnummer muss zwischen 1 und " + _kshConfig.getMaxSheetNumber() + " liegen!";
		} else if(t.getMessage().contains(_kshConfig.getMaxCurrentNumber() + "")) {
			errorResponseMessage = "Laufende Nummer muss zwischen 1 und " + _kshConfig.getMaxCurrentNumber() + " liegen!";
		} else if (t.getMessage().contains(_kshConfig.getMaxFamilyNumber() + "")) {
			errorResponseMessage = "Familiennummer muss zwischen 1 und " + _kshConfig.getMaxFamilyNumber() + " liegen!";
		} else if (t.getMessage().contains(_kshConfig.getMaxprice() + "")) {
			errorResponseMessage = "Preis muss zwischen 0,50 und " + _kshConfig.getMaxprice() + ",00 Euro liegen!";
		} else {
			errorResponseMessage = "Unerwarteter Fehler!";
		}
		routingContext.response().setStatusCode(500).end(errorResponseMessage);
	}

}
