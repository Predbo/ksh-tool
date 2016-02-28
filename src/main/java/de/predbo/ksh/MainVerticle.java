package de.predbo.ksh;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.io.IOException;

public class MainVerticle extends AbstractVerticle {
	
	private static final Logger _logger = LoggerFactory.getLogger(MainVerticle.class);
	
	@Override
	public void start() throws IOException {
		_logger.info("Starte KSH Auswertungstool");

		KshConfig kshConfig = new KshConfig(config());
		CsvManager csvManager = new CsvManager(kshConfig);
		KshRestApiHandler restApiHandler = new KshRestApiHandler(csvManager, kshConfig);

		Router router = Router.router(vertx);

		router.get("/data/entries").handler(restApiHandler::getAllEntries);
		router.delete("/data/entry/:id").handler(restApiHandler::deleteEntry);
		router.get("/config/max-current-number").handler(restApiHandler::getMaxCurrentNumber);
		router.route().handler(BodyHandler.create());
		router.post("/data/entry").handler(restApiHandler::addEntry);
		
		router.route("/*").handler(StaticHandler.create().setCachingEnabled(false));
		
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
		
		_logger.info("KSH Auswertungstool erfolgreich gestartet!");
	}
}