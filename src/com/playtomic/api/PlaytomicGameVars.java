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

import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class PlaytomicGameVars {
    
    private PlaytomicRequestListener<String> mRequestListener;

    public void setRequestListener(PlaytomicRequestListener<String> requestListener) {
        mRequestListener = requestListener;
    }
    
    private static String SECTION;
    private static String LOAD;
    private static String LOADSINGLE;
    
    static void Initialise(String apiKey) throws Exception {
        SECTION = PlaytomicEncrypt.md5("gamevars-" + apiKey);
        LOAD = PlaytomicEncrypt.md5("gamevars-load-" + apiKey);
        LOADSINGLE = PlaytomicEncrypt.md5("gamevars-loadsingle-" + apiKey);
    }
    
    public void load() {
        PlaytomicHttpRequest request = new PlaytomicHttpRequest();
        request.setHttpRequestListener(new PlaytomicHttpRequestListener() {
            public void onRequestFinished(PlaytomicHttpResponse playtomicHttpResponse) {
                if (playtomicHttpResponse.getSuccess()) {
                    requestFinished(playtomicHttpResponse.getData());
                }            
                else {
                    requestFailed();
                }
            }
        });
        try {
            PlaytomicHttpRequestUrl url = PlaytomicHttpRequest.prepare(SECTION, LOAD, null);
            request.addPostData("data", url.getData());
            request.execute(url.getUrl());
        }
        catch (Exception ex) {
            requestFailed(ex.getMessage());
        }
    }
    
    public void loadSingle(String name)  {
        PlaytomicHttpRequest request = new PlaytomicHttpRequest();
        request.setHttpRequestListener(new PlaytomicHttpRequestListener() {
            public void onRequestFinished(PlaytomicHttpResponse playtomicHttpResponse) {
                if (playtomicHttpResponse.getSuccess()) {
                    requestFinished(playtomicHttpResponse.getData());
                }            
                else {
                    requestFailed();
                }
            }
        });
        try {
            LinkedHashMap<String, String> postData = new LinkedHashMap<String, String>(); 
            
            postData.put("name", name);
            
            PlaytomicHttpRequestUrl url = PlaytomicHttpRequest.prepare(SECTION, LOADSINGLE, postData);
            request.addPostData("data", url.getData());
            request.execute(url.getUrl());
        }
        catch (Exception ex) {
            requestFailed(ex.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
	private void requestFinished(String response) {
        if (mRequestListener == null) {
            return;
        }
        
        try {        
            // we got a response of some kind
            //
            JSONObject json = new JSONObject(response);
            int status = json.getInt("Status");
            
            // failed on the server side
            if(status != 1)
            {
                int errorCode = json.getInt("ErrorCode");
                mRequestListener.onRequestFinished(
                        new PlaytomicResponse<String>(errorCode, "Connectivity error. Server side"));
                return;
            }
            
            JSONObject variables = json.getJSONObject("Data");
            LinkedHashMap<String, String> md = new LinkedHashMap<String, String>();
            Iterator<?> itr = variables.keys();
            while (itr.hasNext()) {
                String name = (String)itr.next();
                String value = variables.getString(name);
                value = URLDecoder.decode(value);
                value = value.replace("'", " ");
                md.put(name, value);
            }
            
            PlaytomicResponse<String> playtomicResponse = new PlaytomicResponse<String>(true, Playtomic.SUCCESS, md);
            
            mRequestListener.onRequestFinished(playtomicResponse);
        }
        catch (Exception ex) {
            mRequestListener.onRequestFinished(new PlaytomicResponse<String>(Playtomic.ERROR, ex.getMessage()));
        }
    }

    private void requestFailed() {
        requestFailed("");
    }
    
    private void requestFailed(String message) {
        // failed on the client / connectivity side
        //
        if (mRequestListener == null) {
            return;
        }
        mRequestListener.onRequestFinished(
                new PlaytomicResponse<String>(Playtomic.ERROR, "Connectivity error. Client side. " + message));
    }    
}
