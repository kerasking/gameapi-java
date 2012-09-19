package com.playtomic.SFSExtension;

import com.playtomic.api.Playtomic;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class LoginEventHandler extends BaseServerEventHandler {
	@Override
    public void handleServerEvent(ISFSEvent event) throws SFSException
    {
	    Playtomic.Log().customMetric("Login", "General", false);
	    Playtomic.Log().forceSend();
	    trace("Playtomic: A custom metric (Login) has been sent");
    }
}
