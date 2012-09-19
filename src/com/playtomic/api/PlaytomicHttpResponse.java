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

public class PlaytomicHttpResponse {

	private Boolean mSuccess;
	private int mErrorCode = Playtomic.ERROR;
	private String mData;
	
	public Boolean getSuccess() {
		return mSuccess;
	}

	public int getErrorCode() {
		return mErrorCode;
	}

	public String getData() {
		return mData;
	}
	
	public PlaytomicHttpResponse(Boolean success, int errorCode, String data) {
		mSuccess = success;
		mErrorCode = errorCode;
		mData = data;
	}

	public PlaytomicHttpResponse(int errorCode, String message) {
		mSuccess = false;
		mErrorCode = errorCode;
		mData = message;
	}
}
