/* Playtomic Java API
-----------------------------------------------------------------------
 Documentation is available at: 
 	https://playtomic.com/api/java

 Support is available at:
 	https://playtomic.com/community 
 	https://playtomic.com/issues
 	https://playtomic.com/support has more options if you're a premium user

 Github repositories:
 	https://github.com/playtomic

You may modify this SDK if you wish but be kind to our servers.  Be
careful about modifying the analytics stuff as it may give you 
borked reports.

Pull requests are welcome if you spot a bug or know a more efficient
way to implement something.

Copyright (c) 2011 Playtomic Inc.  Playtomic APIs and SDKs are licensed 
under the MIT license.  Certain portions may come from 3rd parties and 
carry their own licensing terms and are referenced where applicable.
*/

package com.playtomic.api;

import java.util.LinkedHashMap;
import java.util.List;

public class PlaytomicLogRequest {

    private static final String PLAYTOMIC_QUEUE_SIZE = "playtomic.queue.size";
    private static final String PLAYTOMIC_QUEUE_BYTES = "playtomic.queue.bytes";
    private static final String PLAYTOMIC_QUEUE_ITEM = "playtomic.queue.item_";
    private static final String PLAYTOMIC_QUEUE_READY = "playtomic.queue.ready";

    private String mData = "";
    private String mTrackUrl = "";
    private String mFullUrl = "";
    
    public String getData() {
        return mData;
    }
    
    public void setData(String data) {
        this.mData = data;
    }
    
    public String getTrackUrl() {
        return mTrackUrl;
    }
    
    public void setTrackUrl(String trackUrl) {
        this.mTrackUrl = trackUrl;
    }

    public PlaytomicLogRequest(String url) {
        mTrackUrl = url;
        mData = "";
    }

    public void queueEvent(String event) {
        if(mData.length() == 0) {
            mData = event;
        }
        else {
            mData += "~";
            mData += event;
        }
    }

    public void massQueue(List<String>eventqueue) {
        while(eventqueue.size() > 0) {
            String event = eventqueue.get(0);
            eventqueue.remove(0);
            
            if(mData.length() == 0) {
                mData = event;
            } 
            else {
                mData += "~";
                mData += event;
                
                if(mData.length() > 300) {
                    send();                
                    PlaytomicLogRequest request = new PlaytomicLogRequest(mTrackUrl);
                    request.massQueue(eventqueue);
                    return;
                }
            } 
        }        
        send();
    }

    public void send() {
        mFullUrl = mTrackUrl + mData;
        PlaytomicHttpRequest request = new PlaytomicHttpRequest();
        request.setHttpRequestListener(new PlaytomicHttpRequestListener() {
            public void onRequestFinished(PlaytomicHttpResponse playtomicHttpResponse) {
                if (playtomicHttpResponse.getSuccess()) {
                    requestFinished();
                }            
                else {
                    requestFailed();
                }
            }
        });
        request.execute(mFullUrl);
    }

    private void requestFinished() {
        // try to send data we have failed to send in a previous call to send
        // 
    		int queueSize = 0;
    		
    		try {
	        PlaytomicOfflineData settings = new PlaytomicOfflineData();
	
	        queueSize = settings.getInt(PLAYTOMIC_QUEUE_SIZE, 0);
	        
	        if (queueSize > 0) {
	            // we send only one message by call
	            //
	            Boolean ready = settings.getBoolean(PLAYTOMIC_QUEUE_READY, false);
	            if (ready) {     
	                settings.putBoolean(PLAYTOMIC_QUEUE_READY, false);                
	                // this is managed as FILO list
	                //
	                String key = PLAYTOMIC_QUEUE_ITEM + queueSize;
	                String savedData = settings.getString(key, "");
	                queueSize--;                
	                settings.putInt(PLAYTOMIC_QUEUE_SIZE, queueSize);   
	                settings.remove(key);
	                
	                PlaytomicLogRequest request = new PlaytomicLogRequest(mTrackUrl);
	                request.queueEvent(savedData);
	                request.send();
	            }
	            else {
	            		settings.putBoolean(PLAYTOMIC_QUEUE_READY, true);     
	            }
	        }
    		}
    		catch (Exception ex) {
    			
    		}
        if (mRequestListener != null) {
            LinkedHashMap<String, String> log = new LinkedHashMap<String, String>();
            log.put("Url", mFullUrl);
            log.put("Result", "true");
            log.put("Queue size", String.valueOf(queueSize));
            mRequestListener.onRequestFinished(new PlaytomicResponse<String>(true, Playtomic.SUCCESS, log));
        }
    }

    private void requestFailed() {
        // save data to send later
        //
        // this is managed as FILO list
        //   
    		int queueSize = 0;
    		int queueBytes = 0;
    	 
    		try {
	        PlaytomicOfflineData settings = new PlaytomicOfflineData();
	
	        queueSize = settings.getInt(PLAYTOMIC_QUEUE_SIZE, 0);
	        queueBytes = settings.getInt(PLAYTOMIC_QUEUE_BYTES, 0);
	        
	        if (queueBytes < Playtomic.OfflineQueueMaxSize()) {        
	            queueSize++;
	            settings.putInt(PLAYTOMIC_QUEUE_SIZE, queueSize);
	            String key = PLAYTOMIC_QUEUE_ITEM + queueSize;
	            String dataToSave = mData;
	            if (!dataToSave.contains("&date=")) {
	                long seconds = System.currentTimeMillis() / 1000; // seconds since 1-1-1970 UTC
	                dataToSave += "&date=" + seconds;
	            }
	            settings.putString(key, dataToSave);
	            queueBytes += dataToSave.length() * 2;
	            settings.putInt(PLAYTOMIC_QUEUE_BYTES, queueBytes);
	            settings.putBoolean(PLAYTOMIC_QUEUE_READY, true);
	        }
    		}
    		catch (Exception ex) {
    			
    		}
        if (mRequestListener != null) {
            LinkedHashMap<String, String> log = new LinkedHashMap<String, String>();
            log.put("Url", mFullUrl);
            log.put("Result", "false");
            log.put("Queue size", String.valueOf(queueSize));
            log.put("Queue bytes", String.valueOf(queueBytes));
            mRequestListener.onRequestFinished(new PlaytomicResponse<String>(false, Playtomic.ERROR, log));
        }
    }

    private PlaytomicRequestListener<String> mRequestListener;

    public void setRequestListener(PlaytomicRequestListener<String> requestListener) {
        mRequestListener = requestListener;
    }
}
