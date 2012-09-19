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

import java.util.Date;
import java.util.LinkedHashMap;

public class PlaytomicLevel {

    private String mLevelId = "";
    private String mPlayerId = "";
    private String mPlayerName = "";
    private String mPlayerSource = "";
    private String mName = "";
    private String mData = "";
    @SuppressWarnings("unused")
	private String mThumb = "";
    private int mVotes = 0;
    private int mPlays = 0;
    private double mRating = 0;
    private int mScore = 0;
    private Date mDate = null;
    private String mRelativeDate = "";
    private LinkedHashMap<String, String> mCustomData = new LinkedHashMap<String, String>();

    public PlaytomicLevel(String levelId) {
        mLevelId = levelId;
    }
    
    public PlaytomicLevel(String name, String playerName, String playerId, String data) {
        mName = name;
        mPlayerName = playerName;
        mPlayerId = playerId;
        mData = data;
    }

    public PlaytomicLevel(
            String name,
            String playerName,
            String playerId,
            String playerSource,
            String data,
            String thumb,
            int votes,
            int plays,
            double rating,
            int score,
            Date date,
            String relativeDate,
            LinkedHashMap<String, String> customData,
            String levelId) {
        mName = name;
        mPlayerName = playerName;
        mPlayerId = playerId;
        mPlayerSource = playerSource;
        mData = data;
        mThumb = thumb;
        mVotes = votes;
        mPlays = plays;
        mRating = rating;
        mScore = score;
        mDate = date;
        mRelativeDate = relativeDate;
        mCustomData = customData;
        mLevelId = levelId;
    }

    public String getLevelId() {
        return mLevelId;
    }

    public void setLevelId(String levelId) {
        this.mLevelId = levelId;
    }

    public String getPlayerId() {
        return mPlayerId;
    }

    public void setPlayerId(String playerId) {
        this.mPlayerId = playerId;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public void setPlayerName(String playerName) {
        this.mPlayerName = playerName;
    }

    public String getPlayerSource() {
        return mPlayerSource;
    }

    public void setPlayerSource(String playerSource) {
        this.mPlayerSource = playerSource;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        this.mData = data;
    }

    public int getVotes() {
        return mVotes;
    }

    public void setVotes(int votes) {
        this.mVotes = votes;
    }

    public int getPlays() {
        return mPlays;
    }

    public void setPlays(int plays) {
        this.mPlays = plays;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        this.mRating = rating;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        this.mScore = score;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public String getRelativeDate() {
        return mRelativeDate;
    }

    public void setRelativeDate(String relativeDate) {
        this.mRelativeDate = relativeDate;
    }

    public LinkedHashMap<String, String> getCustomData() {
        return mCustomData;
    }

    public void setCustomData(LinkedHashMap<String, String> customData) {
        this.mCustomData = customData;
    }
    
    public String getThumbnailURL() {
        return "http://g" + Playtomic.GameGuid() + ".api.playtomic.com/playerlevels/load.aspx?swfid="
                + Playtomic.GameId() + "&levelid=" + mLevelId;
    }

    public Object getThumbnail() {
        return null;
    }

    public String getCustomValue(String key) {
        return mCustomData.get(key);
    }

    public void addCustomValue(String key, String value) {
        mCustomData.put(key, value);
    }
}