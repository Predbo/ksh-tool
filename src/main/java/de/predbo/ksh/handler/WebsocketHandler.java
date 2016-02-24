package de.predbo.ksh.handler;

import org.supercsv.cellprocessor.constraint.DMinMax;
import org.supercsv.cellprocessor.constraint.LMinMax;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class WebsocketHandler implements Handler<RoutingContext> {

	private static final Logger _logger = LoggerFactory.getLogger(WebsocketHandler.class);
	
	private final SockJSHandler _sockJSHandler;

	private KshRegistryHandler _kshRegistryHandler;

	public WebsocketHandler(Vertx vertx, KshRegistryHandler kshRegistryHandler) {
		_kshRegistryHandler = kshRegistryHandler;
		_sockJSHandler = createSockJsBridge(vertx);
		
		EventBus eventBus = vertx.eventBus();
		
		eventBus.consumer("web.client2server.add.entry", message -> {
			_logger.info("add Entry: " + message.body());
			try {
				_kshRegistryHandler.registryNewEntry(message.body().toString());
				message.reply(message.body());
			} catch (Throwable t) {
				sendErrorResponse(message, t);
			}
		});
		
		eventBus.consumer("web.client2server.remove.entry", message -> {
			_logger.info("remove Entry: " + message.body());
			try {
				_kshRegistryHandler.removeEntry(message.body().toString());
				message.reply("SUCCESS");
			} catch (Throwable t) {
				sendErrorResponse(message, t);
			}
		});
		
		
		
		
		eventBus.consumer("web.client2server.publish.test", message -> {
			_logger.info("message to publish: " + message.body());
			eventBus.publish("web.server2client.publish.test", message.body());
		});
	}

	private void sendErrorResponse(Message<Object> message, Throwable t) {
		if (t.getMessage().equals("0 does not lie between the min (1) and max (60) values (inclusive)")) {
			message.reply("ERROR: 'Laufende Nummer' darf nicht leer sein, und muss zwischen 1 und 60 liegen!");
		} else if(t.getMessage().equals("0 does not lie between the min (1) and max (399) values (inclusive)")){
			message.reply("ERROR: 'Blatt Nummer' darf nicht leer sein, und muss zwischen 1 und 300 liegen!");
		} else if (t.getMessage().equals("0 does not lie between the min (1) and max (200) values (inclusive)")){
			message.reply("ERROR: 'Familien Nummer' darf nicht leer sein, und muss zwischen 1 und 200 liegen!");
		} else if (t.getMessage().equals("0,000000 does not lie between the min (0,500000) and max (1000,000000) values (inclusive)")){
			message.reply("ERROR: 'Preis' darf nicht leer sein, und muss zwischen 0,50 und 1000 Euro liegen!");
		}
	}
	
	@Override
	public void handle(RoutingContext event) {
		_sockJSHandler.handle(event);
	}

	
	
	
	
	private SockJSHandler createSockJsBridge(Vertx vertx) {
		SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        BridgeOptions options = new BridgeOptions()
        .addOutboundPermitted(new PermittedOptions().setAddressRegex("web.server2client.*"))
        .addInboundPermitted(new PermittedOptions().setAddressRegex("web.client2server.*"));
		sockJSHandler.bridge(options,be -> {
			if (be.type() == BridgeEventType.SOCKET_CREATED) {
				_logger.info("A socket was created");
			}
//			if (be.type() == BridgeEventType.RECEIVE) {
//				_logger.info("Send message to client: " + be.rawMessage().toString());
//			}
//			if (be.type() == BridgeEventType.SEND) {
//				_logger.info("Received message from client: " + be.rawMessage().toString());
//			}
			be.complete(true);
		});
		
		return sockJSHandler;
	}
}
