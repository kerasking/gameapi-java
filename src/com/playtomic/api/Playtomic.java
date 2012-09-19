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

public class Playtomic {

    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int ERROR_INVALID_RATING_VALUE = 401;
    public static final String PLAYTOMIC_CACHE = "playtomic.cache";

    // actually the max size is 1048576.
    // the 1048577 value allow us to do
    // if (queueSize < PLAYTOMIC_QUEUE_MAX_SIZE) {...}
    //
    public static final int PLAYTOMIC_QUEUE_MAX_BYTES = 1048577;     
    
    private static Playtomic sInstance;

    private int mGameId = 0;
    private String mGameGuid = "";
    private String mSourceUrl = "";
    private String mBaseUrl = "";
    private PlaytomicLog mLog = null;
    private int mOfflineQueueMaxSize;
    
    public static PlaytomicLog Log;
    public static String GameGuid;
    public static int GameId;
    public static String ApiKey;

    /**
    * Sets the API to use SSL-only for all communication
    */
    public static void setSSL()
    {
        // You are now using SSL for your api requests. This feature is for premium users only, if your account is not premium the data you send will be ignored.
        sInstance.setSSL(true);
    }
    
    public void setSSL(boolean value)
    {
        PlaytomicHttpRequest.setSSL(value) ;
        mLog.setSSL(value);
    }

    public static void initInstance(int gameId, String gameGuid, String apiKey) throws Exception {
        if (sInstance == null) {
            sInstance = new Playtomic(gameId, gameGuid, apiKey);
        }
    }
    
    protected Playtomic(int gameId, String gameGuid, String apiKey) throws Exception {

        sInstance = this;
        
        
        String model = "JAVA_SERVER";
        String system = "JAVA_SERVER";
        String version = "JAVA_SERVER";

        model = model.replace(" ", "+");
        system = system.replace(" ", "+");
        version = version.replace(" ", "+");

        mGameId = gameId;
        mGameGuid = gameGuid;
        mSourceUrl = "http://java.com/" + model + "/" + system + "/"    + version;
        mBaseUrl = "java.com";
        
        PlaytomicHttpRequest.Initialise();
        PlaytomicData.Initialise(apiKey);        
        PlaytomicLeaderboards.Initialise(apiKey);
        PlaytomicGameVars.Initialise(apiKey);
        PlaytomicGeoIP.Initialise(apiKey);
        PlaytomicPlayerLevels.Initialise(apiKey);

        mLog = new PlaytomicLog(gameId, gameGuid);
        
        GameGuid = gameGuid;
        GameId = gameId;
        ApiKey = apiKey;
        Log = mLog;
    }

    protected int getGameId() {
        return mGameId;
    }
    
    protected String getGameGuid() {
        return mGameGuid;
    }
    
    protected int getOfflineQueueMaxSize() {
        return mOfflineQueueMaxSize;
    }
    
    protected void setOfflineQueueMaximumSize(int size) {
        mOfflineQueueMaxSize = size;
    }
    
    protected String getBaseUrl() {
        return mBaseUrl;
    }

    protected String getSourceUrl() {
        return mSourceUrl;
    }

    protected PlaytomicLog getLog() {
        return mLog;
    }
    
    public static int GameId() {
        return sInstance.getGameId();
    } 
    
    public static String GameGuid() {
        return sInstance.getGameGuid();
    }
    
    public static int OfflineQueueMaxSize() {
        return sInstance.getOfflineQueueMaxSize();
    }

    public static void setOfflineQueueMaxSize(int kbSize) {
        // we save the size in bytes
        //
        if (kbSize < 0) {
            kbSize = 0;
        }
        kbSize = kbSize * 1024;
        
        if (kbSize > PLAYTOMIC_QUEUE_MAX_BYTES) {
            sInstance.setOfflineQueueMaximumSize(PLAYTOMIC_QUEUE_MAX_BYTES);
        }
        else {
            sInstance.setOfflineQueueMaximumSize(kbSize);
        }
    }
    
    public static String BaseUrl() {
        return sInstance.getBaseUrl();
    }

    public static String SourceUrl() {
        return sInstance.getSourceUrl();
    }
    
    public static PlaytomicLog Log() {        
        return sInstance.getLog();
    }    
}
