package de.predbo.ksh.handler;

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
import de.predbo.ksh.KshConfig;

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
		
		eventBus.consumer("web.client2server.get.maxCurrentNumber", message -> {
			message.reply(_kshRegistryHandler.getKshConfig().getMaxCurrentNumber());
		});
		
		eventBus.consumer("web.client2server.publish.test", message -> {
			_logger.info("message to publish: " + message.body());
			eventBus.publish("web.server2client.publish.test", message.body());
		});
	}

	private void sendErrorResponse(Message<Object> message, Throwable t) {
		_logger.error(t.getMessage());
		KshConfig kshConfig = _kshRegistryHandler.getKshConfig();
		
		if (t.getMessage().contains(kshConfig.getMaxSheetNumber() + "")) {
			message.reply("ERROR: Blattnummer muss zwischen 1 und " + kshConfig.getMaxSheetNumber() + " liegen!");
		} else if(t.getMessage().contains(kshConfig.getMaxCurrentNumber() + "")) {
			message.reply("ERROR: Laufende Nummer muss zwischen 1 und " + kshConfig.getMaxCurrentNumber() + " liegen!");
		} else if (t.getMessage().contains(kshConfig.getMaxFamilyNumber() + "")) {
			message.reply("ERROR: Familiennummer muss zwischen 1 und " + kshConfig.getMaxFamilyNumber() + " liegen!");
		} else if (t.getMessage().contains(kshConfig.getMaxprice() + "")) {
			message.reply("ERROR: Preis muss zwischen 0,50 und " + kshConfig.getMaxprice() + ",00 Euro liegen!");
		} else if (t.getMessage().contains("Failed to decode:Can not construct instance of")){
			message.reply("ERROR: Bitte Werte überprüfen, nur Zahlen sind erlaubt");
		} else {
			message.reply("ERROR: Unerwarteter Fehler!");
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
