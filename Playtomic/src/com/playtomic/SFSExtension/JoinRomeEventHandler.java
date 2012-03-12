package com.playtomic.SFSExtension;

import com.playtomic.api.Playtomic;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class JoinRomeEventHandler  extends BaseServerEventHandler {

	public void handleServerEvent(ISFSEvent arg0) throws SFSException {
		log("\nJoin Room");
		String roomName = "SYSTEM";         
		Playtomic.Log().customMetric(roomName, "Rooms", false);
		Playtomic.Log().forceSend();
	}
	
	private void log(String message) {
		trace("Playtomic: " + message);
	}
}
