package com.playtomic.SFSExtension;

import com.playtomic.api.Playtomic;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class PlaytomicApiExtension extends SFSExtension 
{

	public void init()
	{
		try {
			Playtomic.initInstance(4748, "3893d4cf173a429d", "aba2af63c5d54d3f920103b277e696");
			Playtomic.Log().view();
			Playtomic.setOfflineQueueMaxSize(512);
			trace("Playtomic: Playtomic API has started");
		}
		catch (Exception ex) {
			
		}
		
		addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		addEventHandler(SFSEventType.USER_JOIN_ZONE, JoinZoneEventHandler.class);
		addEventHandler(SFSEventType.USER_JOIN_ROOM, JoinRomeEventHandler.class);
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
	}
}
