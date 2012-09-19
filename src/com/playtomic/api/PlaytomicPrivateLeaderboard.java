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

public class PlaytomicPrivateLeaderboard {
    private String mTableId;
    private String mName;
    private String mBitly;
    private String mPermalink;
    private Boolean mHighest;
    private String mRealName;
    
    public PlaytomicPrivateLeaderboard(String tableid, String name, String bitly, String permalink, Boolean highest, String realname) {
        mTableId = tableid;
        mName = name;
        mBitly = bitly;
        mPermalink = permalink;
        mHighest = highest;
        mRealName = realname;
    }
    
    public String toString() {
        return "Playtomic.PrivateLeaderboard:" + 
                "\nTableId: "+ mTableId + 
                "\nName: " + mName + 
                "\nBitly: " + mBitly + 
                "\nPermalink: " + mPermalink + 
                "\nHighest: " + mHighest + 
                "\nRealName: " + mRealName;
    }

    public String getTableId() {
        return mTableId;
    }

    public void setTableId(String tableId) {
        this.mTableId = tableId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getBitly() {
        return mBitly;
    }

    public void setBitly(String bitly) {
        this.mBitly = bitly;
    }

    public String getPermalink() {
        return mPermalink;
    }

    public void setPermalink(String permalink) {
        this.mPermalink = permalink;
    }

    public Boolean getHighest() {
        return mHighest;
    }

    public void setHighest(Boolean highest) {
        this.mHighest = highest;
    }

    public String getRealName() {
        return mRealName;
    }

    public void setRealName(String realName) {
        this.mRealName = realName;
    }
}
