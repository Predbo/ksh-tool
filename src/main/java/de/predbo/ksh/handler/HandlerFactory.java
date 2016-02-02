package de.predbo.ksh.handler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

public interface HandlerFactory extends Handler<RoutingContext> {

	static WebsocketHandler createWebsocketHandler(Vertx vertx, KshRegistryHandler kshRegistryHandler) {
		return new WebsocketHandler(vertx, kshRegistryHandler);
	}

	static KshRegistryHandler createKshRegistryHandler() {
		return new KshRegistryHandler();
	}
	
	
}