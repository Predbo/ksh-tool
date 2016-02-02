package de.predbo.ksh;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import de.predbo.ksh.handler.HandlerFactory;
import de.predbo.ksh.handler.KshRegistryHandler;

public class MainVerticle extends AbstractVerticle {
	
	@Override
	public void start() {
		KshRegistryHandler kshRegistryHandler = new KshRegistryHandler();

		Router router = Router.router(vertx);
		
		router.route("/eventbus*").handler(HandlerFactory.createWebsocketHandler(vertx, kshRegistryHandler));
		
		router.get("/kshentries").handler(kshRegistryHandler::getAllEntries);
		
		router.route("/*").handler(StaticHandler.create().setCachingEnabled(false));
		
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}
}