package de.predbo.ksh;

import java.io.IOException;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import de.predbo.ksh.handler.HandlerFactory;
import de.predbo.ksh.handler.KshRegistryHandler;

public class MainVerticle extends AbstractVerticle {
	
	@Override
	public void start() throws IOException {
		
		KshConfig kshConfig = new KshConfig(config());
		
		KshRegistryHandler kshRegistryHandler = new KshRegistryHandler(kshConfig);

		Router router = Router.router(vertx);
		
		router.route("/eventbus*").handler(HandlerFactory.createWebsocketHandler(vertx, kshRegistryHandler));
		
		router.get("/kshentries").handler(kshRegistryHandler::getAllEntries);
		
		router.route("/*").handler(StaticHandler.create().setCachingEnabled(false));
		
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}
}