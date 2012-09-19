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

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PlaytomicResponse<T> {

    private Boolean mSuccess;
    private int mErrorCode = Playtomic.ERROR;
    private ArrayList<T> mData = null;
    private LinkedHashMap<String, T> mMap = null;
    private int mNumResults = 0;
    private String mErrorMessage = "";
    private T mObject;
    
    public Boolean getSuccess() {
        return mSuccess;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
    
    public ArrayList<T> getData() {
        return mData;
    }

    public LinkedHashMap<String, T> getMap() {
        return mMap;
    }

    public int getNumResults() {
        return mNumResults;
    }
    
    public T getObject() {
        return mObject;
    }
    
    public void setObject(T object) {
        mObject = object;
    }
    
    public PlaytomicResponse(Boolean success, int errorCode) {
        mSuccess = success;
        mErrorCode = errorCode;
    }
    
    public PlaytomicResponse(Boolean success, int errorCode, ArrayList<T> data, int numResults) {
        mSuccess = success;
        mErrorCode = errorCode;
        mData = data;
        mNumResults = numResults;
    }

    public PlaytomicResponse(Boolean success, int errorCode, LinkedHashMap<String, T> map) {
        mSuccess = success;
        mErrorCode = errorCode;
        mMap = map;
    }
    
    public PlaytomicResponse(Boolean success, int errorCode, T object) {
        mSuccess = success;
        mErrorCode = errorCode;
        mObject = object;
    }

    public PlaytomicResponse(int errorCode, String errorMessage) {
        mSuccess = false;
        mErrorCode = errorCode;
        mErrorMessage = errorMessage;
    }
}
