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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlaytomicLeaderboards {

    private PlaytomicRequestListener<PlaytomicScore> mRequestListener;
    private PlaytomicRequestListener<PlaytomicPrivateLeaderboard> mRequestListenerPrivateLeaderboard;

    public void setRequestListener(PlaytomicRequestListener<PlaytomicScore> requestListener) {
        mRequestListener = requestListener;
    }

    public void setRequestListenerPrivateLeaderboard(PlaytomicRequestListener<PlaytomicPrivateLeaderboard> requestListener) {
        mRequestListenerPrivateLeaderboard = requestListener;
    }
    
    private static String SECTION;
    private static String CREATEPRIVATELEADERBOARD;
    private static String LOADPRIVATELEADERBOARD;
    private static String SAVEANDLIST;
    private static String SAVE;
    private static String LIST;
    
    static void Initialise(String apiKey) throws Exception {
        SECTION = PlaytomicEncrypt.md5("leaderboards-" + apiKey);
        CREATEPRIVATELEADERBOARD = PlaytomicEncrypt.md5("leaderboards-createprivateleaderboard-" + apiKey);
        LOADPRIVATELEADERBOARD = PlaytomicEncrypt.md5("leaderboards-loadprivateleaderboard-" + apiKey);
        SAVEANDLIST = PlaytomicEncrypt.md5("leaderboards-saveandlist-" + apiKey);
        SAVE = PlaytomicEncrypt.md5("leaderboards-save-" + apiKey);
        LIST = PlaytomicEncrypt.md5("leaderboards-list-" + apiKey);
    }
    
    // asynchronous calls
    //
    public void save(String table, PlaytomicScore score, Boolean highest, Boolean allowduplicates) {
        PlaytomicHttpRequest request = new PlaytomicHttpRequest();
        request.setHttpRequestListener(new PlaytomicHttpRequestListener() {
            public void onRequestFinished(PlaytomicHttpResponse playtomicHttpResponse) {
                if (playtomicHttpResponse.getSuccess()) {
                    requestSaveFinished(playtomicHttpResponse.getData());
                }            
                else {
                    requestSaveFailed();
                }
            }
        });
        
        try {
            LinkedHashMap<String, String> postData = new LinkedHashMap<String, String>(); 
                
            postData.put("url", Playtomic.SourceUrl());
            postData.put("table", table);
            postData.put("highest", (highest ? "y" : "n"));
            postData.put("name", score.getName());
            postData.put("points", String.valueOf(score.getPoints()));
            postData.put("allowduplicates", (allowduplicates ? "y" : "n"));
            postData.put("auth", PlaytomicEncrypt.md5(
                                            Playtomic.SourceUrl() 
                                            + score.getPoints()));
            
            postData.put("fb", score.getFBUserId().length() == 0 ? "n" : "y");
            postData.put("fbuserid", score.getFBUserId());

            LinkedHashMap<String, String> map = score.getCustomData();
            postData.put("customfields", String.valueOf(map.size()));
            
            int fieldNumber = 0;
            
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String ckey = "ckey" + fieldNumber;
                String cdata = "cdata" + fieldNumber;
                postData.put(ckey, entry.getKey());
                postData.put(cdata, entry.getValue());
                fieldNumber++;
            }
            
            PlaytomicHttpRequestUrl url = PlaytomicHttpRequest.prepare(SECTION, SAVE, postData);
            request.addPostData("data", url.getData());
            request.execute(url.getUrl());
        }
        catch (Exception ex) {
            requestSaveFailed(ex.getMessage());
        }
    }

    private void requestSaveFinished(String response) {
        if (mRequestListener == null) {
            return;
        }
        
        try {        
            // we got a response of some kind
            //
            JSONObject json = new JSONObject(response);
            int status = json.getInt("Status");
            
            int errorCode = json.getInt("ErrorCode");
            
            // failed on the server side
            if (status == 1) {
                mRequestListener.onRequestFinished(new PlaytomicResponse<PlaytomicScore>(true, errorCode));
            }
            else {
                mRequestListener.onRequestFinished(
                        new PlaytomicResponse<PlaytomicScore>(errorCode, "Connectivity error. Server side. Response: " + response));
            }
        }
        catch (Exception ex) {
            mRequestListener.onRequestFinished(new PlaytomicResponse<PlaytomicScore>(Playtomic.ERROR, ex.getMessage()));
        }
    }

    private void requestSaveFailed() {
        requestSaveFailed("");
    }
    
    private void requestSaveFailed(String message) {
        // failed on the client / connectivity side
        //
        if (mRequestListener == null) {
            return;
        }
        mRequestListener.onRequestFinished(
                new PlaytomicResponse<PlaytomicScore>(Playtomic.ERROR, "Connectivity error. Client side. " + message));
    }

    public void list(
            String table,
            Boolean highest,
            String mode,
            int page,
            int perPage,
            LinkedHashMap<String, String> customFilter) {

        int numFilters = customFilter == null ? 0 : customFilter.size();

        PlaytomicHttpRequest request = new PlaytomicHttpRequest();
        request.setHttpRequestListener(new PlaytomicHttpRequestListener() {
            public void onRequestFinished(PlaytomicHttpResponse playtomicHttpResponse) {
                if (playtomicHttpResponse.getSuccess()) {
                    requestListFinished(playtomicHttpResponse.getData());
                }            
                else {
                    requestListFailed();
                }
            }
        });
        
        try {
            LinkedHashMap<String, String> postData = new LinkedHashMap<String, String>();
            
            postData.put("url", "global");
            postData.put("table", table);
            postData.put("highest", (highest ? "y" : "n"));
            postData.put("mode", mode);
            postData.put("page", String.valueOf(page));
            postData.put("perpage", String.valueOf(perPage));
            postData.put("filters", String.valueOf(numFilters));
        
            if (numFilters > 0) {
                int fieldNumber = 0;
                
                for (Map.Entry<String, String> entry : customFilter.entrySet()) {
                    String ckey = "ckey" + fieldNumber;
                    String cdata = "cdata" + fieldNumber;
                    postData.put(ckey, entry.getKey());
                    postData.put(cdata, entry.getValue());
                    fieldNumber++;
                }
            }           
        
            PlaytomicHttpRequestUrl url = PlaytomicHttpRequest.prepare(SECTION, LIST, postData);
            request.addPostData("data", url.getData());
            request.execute(url.getUrl());
        }
        catch (Exception ex) {
            requestSaveFailed(ex.getMessage());
        }
    }

    private void requestListFinished(String response) {
        if (mRequestListener == null) {
            return;
        }
        
        try {        
            // we got a response of some kind
            //
            JSONObject json = new JSONObject(response);
            int status = json.getInt("Status");
            
            // failed on the server side
            if (status != 1) {
                int errorCode = json.getInt("ErrorCode");
                mRequestListener.onRequestFinished(
                        new PlaytomicResponse<PlaytomicScore>(errorCode, "Connectivity error. Server side. Response: " + response));
                return;
            }
            
            JSONObject data = json.getJSONObject("Data");
            JSONArray scores = data.getJSONArray("Scores");
            int numScores = data.getInt("NumScores");
            ArrayList<PlaytomicScore> md = new ArrayList<PlaytomicScore>();
    
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            
            for (int i = 0; i < scores.length(); ++i) {
                JSONObject score = scores.getJSONObject(i);
                
                String userName = score.getString("Name");
                int points = score.getInt("Points");
                String relativeDate = score.getString("RDate");                
                Date date = formatter.parse(score.getString("SDate"));
                long rank = score.getLong("Rank");
                
                LinkedHashMap<String, String>customData = new LinkedHashMap<String, String>();
                JSONObject returnedCustomData = score.getJSONObject("CustomData");
                Iterator<?> itr = returnedCustomData.keys();
                while (itr.hasNext()) {
                        String key = (String)itr.next();
                        String cvalue = URLDecoder.decode(returnedCustomData.getString(key), "UTF-8");
                        customData.put(key, cvalue);
                }
                
                md.add(new PlaytomicScore(userName, points, date, relativeDate, customData, rank));
            }
            
            PlaytomicResponse<PlaytomicScore> playtomicResponse 
                = new PlaytomicResponse<PlaytomicScore>(true, Playtomic.SUCCESS, md, numScores);
            
            mRequestListener.onRequestFinished(playtomicResponse);
        }
        catch (Exception ex) {
            mRequestListener.onRequestFinished(new PlaytomicResponse<PlaytomicScore>(Playtomic.ERROR, ex.getMessage()));
        }
    }

    private void requestListFailed() {
        // failed on the client / connectivity side
        //
        if (mRequestListener == null) {
            return;
        }
        mRequestListener.onRequestFinished(
                new PlaytomicResponse<PlaytomicScore>(Playtomic.ERROR, "Connectivity error. Client side."));
    }

    public void saveAndList(
            String table,
            PlaytomicScore score,
            Boolean highest,
            Boolean allowduplicates,
            String mode,
            int perPage,
            LinkedHashMap<String, String> customFilter,
            Boolean global,
            ArrayList<String>friendList) {


        PlaytomicHttpRequest request = new PlaytomicHttpRequest();
        request.setHttpRequestListener(new PlaytomicHttpRequestListener() {
            public void onRequestFinished(PlaytomicHttpResponse playtomicHttpResponse) {
                if (playtomicHttpResponse.getSuccess()) {
                    requestListFinished(playtomicHttpResponse.getData());
                }            
                else {
                    requestSaveFailed();
                }
            }
        });
        
        try {
            LinkedHashMap<String, String> postData = new LinkedHashMap<String, String>();
        
            // common fields
            postData.put("table", table);
            postData.put("highest", (highest ? "y" : "n"));
            
            // save fields
            postData.put("url", "global");                    
            postData.put("name", score.getName());
            postData.put("points", String.valueOf(score.getPoints()));
            postData.put("allowduplicates", (allowduplicates ? "y" : "n"));
            postData.put("auth", PlaytomicEncrypt.md5(
                                            Playtomic.SourceUrl() 
                                            + score.getPoints()));
            
            LinkedHashMap<String, String> map = score.getCustomData();
            postData.put("customfields", String.valueOf(map.size()));
            
            int fieldNumber = 0;
            
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String ckey = "ckey" + fieldNumber;
                String cdata = "cdata" + fieldNumber;
                postData.put(ckey, entry.getKey());
                postData.put(cdata, entry.getValue());
                fieldNumber++;
            }
            
            postData.put("numfields", String.valueOf(fieldNumber));
            
            // list fields
            postData.put("global", global ? "y" : "n");
            postData.put("mode", mode);
            postData.put("perpage", String.valueOf(perPage));
            
            int numFilters = customFilter == null ? 0 : customFilter.size();
            
            if (numFilters > 0) {
                
                fieldNumber = 0;
                
                for (Map.Entry<String, String> entry : customFilter.entrySet()) {
                    String ckey = "lkey" + fieldNumber;
                    String cdata = "ldata" + fieldNumber;
                    postData.put(ckey, entry.getKey());
                    postData.put(cdata, entry.getValue());
                    fieldNumber++;
                }
            }          
            
            postData.put("numfilters", String.valueOf(numFilters));
            
            postData.put("fb", score.getFBUserId().length() == 0 ? "n" : "y");
            postData.put("fbuserid", score.getFBUserId());
            
            if (friendList != null) {
                postData.put("friendlist", getFriendList(friendList));
            }
                
            PlaytomicHttpRequestUrl url = PlaytomicHttpRequest.prepare(SECTION, SAVEANDLIST, postData);
            request.addPostData("data", url.getData());
            request.execute(url.getUrl());
        }
        catch (Exception ex) {
                requestSaveFailed(ex.getMessage());
        }
    }
    
    private String getFriendList(ArrayList<String> friendList) {
        if (friendList.size() > 0) {
            StringBuilder friends = new StringBuilder();
            for (int i = 0; i < friendList.size()-1; i++) {
                friends.append(friendList.get(i));
                friends.append(",");
            }
            friends.append(friendList.get(friendList.size()-1));
            return friends.toString();
        }
        else {
            return "";
        }
    }
    
    // private leaderboards
    //
    public void createPrivateLeaderboard(String table, Boolean highest) {
        PlaytomicHttpRequest request = new PlaytomicHttpRequest();
        request.setHttpRequestListener(new PlaytomicHttpRequestListener() {
            public void onRequestFinished(PlaytomicHttpResponse playtomicHttpResponse) {
                if (playtomicHttpResponse.getSuccess()) {
                    requestCreatePrivateFinished(playtomicHttpResponse.getData());
                }           
                else {
                    requestCreatePrivateFailed();
                }
            }
        });
        
        try {
            LinkedHashMap<String, String> postData = new LinkedHashMap<String, String>();
            postData.put("table", table);
            postData.put("highest", highest ? "y" : "n");
            postData.put("permalink", "http://java.com/");
                
            PlaytomicHttpRequestUrl url = PlaytomicHttpRequest.prepare(SECTION, CREATEPRIVATELEADERBOARD, postData);
            request.addPostData("data", url.getData());
            request.execute(url.getUrl());
        }
        catch (Exception ex) {
            requestCreatePrivateFailed(ex.getMessage());
        }
    }
    
    private void requestCreatePrivateFinished(String response) {
        LoadPrivateLeaderboardComplete(response);
    }
    
    private void requestCreatePrivateFailed() {
        requestCreatePrivateFailed("");        
    }
    
    private void requestCreatePrivateFailed(String message) {
        // failed on the client / connectivity side
        //
        if (mRequestListenerPrivateLeaderboard == null) {
            return;
        }
        mRequestListenerPrivateLeaderboard.onRequestFinished(
                new PlaytomicResponse<PlaytomicPrivateLeaderboard>(Playtomic.ERROR, "Connectivity error. Client side."));        
    }
           
    public void loadPrivateLeaderboard(String bitly) {
        PlaytomicHttpRequest request = new PlaytomicHttpRequest();
        request.setHttpRequestListener(new PlaytomicHttpRequestListener() {
            public void onRequestFinished(PlaytomicHttpResponse playtomicHttpResponse) {
                if (playtomicHttpResponse.getSuccess()) {
                    LoadPrivateLeaderboardComplete(playtomicHttpResponse.getData());
                }           
                else {
                    requestLoadPrivateFailed();
                }
            }
        });
        
        try {
            LinkedHashMap<String, String> postData = new LinkedHashMap<String, String>();
            postData.put("bitly", bitly);
                
            PlaytomicHttpRequestUrl url = PlaytomicHttpRequest.prepare(SECTION, LOADPRIVATELEADERBOARD, postData);
            request.addPostData("data", url.getData());
            request.execute(url.getUrl());
        }
        catch (Exception ex) {
            requestLoadPrivateFailed(ex.getMessage());
        }
    }
    
    private void LoadPrivateLeaderboardComplete(String response) {
        if (mRequestListenerPrivateLeaderboard == null) {
            return;
        }
        
        try {       
            // we got a response of some kind
            //
            JSONObject json = new JSONObject(response);
            int status = json.getInt("Status");
            
            // failed on the server side
            if (status != 1) {
                int errorCode = json.getInt("ErrorCode");
                mRequestListenerPrivateLeaderboard.onRequestFinished(
                        new PlaytomicResponse<PlaytomicPrivateLeaderboard>(errorCode, "Connectivity error. Server side. Response: " + response));
                return;
            }
            
            JSONObject data = json.getJSONObject("Data");
            String tableId = data.getString("TableId");
            String name = data.getString("Name");
            String bitly = data.getString("Bitly");
            String permalink = data.getString("Permalink");
            Boolean highest = data.getBoolean("Highest");
            String realname = data.getString("RealName");
            
            PlaytomicPrivateLeaderboard leaderboard = new PlaytomicPrivateLeaderboard(tableId, name, bitly, permalink, highest, realname);
            
            PlaytomicResponse<PlaytomicPrivateLeaderboard> playtomicResponse 
                = new PlaytomicResponse<PlaytomicPrivateLeaderboard>(true, Playtomic.SUCCESS, leaderboard);
            
            mRequestListenerPrivateLeaderboard.onRequestFinished(playtomicResponse);
        }
        catch (Exception ex) {
            mRequestListenerPrivateLeaderboard.onRequestFinished(new PlaytomicResponse<PlaytomicPrivateLeaderboard>(Playtomic.ERROR, ex.getMessage()));
        }
    }    
    
    private void requestLoadPrivateFailed() {
        requestLoadPrivateFailed("");        
    }
    
    private void requestLoadPrivateFailed(String message) {
        // failed on the client / connectivity side
        //
        if (mRequestListenerPrivateLeaderboard == null) {
            return;
        }
        mRequestListenerPrivateLeaderboard.onRequestFinished(
                new PlaytomicResponse<PlaytomicPrivateLeaderboard>(Playtomic.ERROR, "Connectivity error. Client side."));        
    }
}
