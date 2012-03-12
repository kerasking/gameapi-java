package com.playtomic.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import com.playtomic.api.Playtomic;
import com.playtomic.api.PlaytomicData;
import com.playtomic.api.PlaytomicGameVars;
import com.playtomic.api.PlaytomicGeoIP;
import com.playtomic.api.PlaytomicLeaderboards;
import com.playtomic.api.PlaytomicLevel;
import com.playtomic.api.PlaytomicPlayerLevels;
import com.playtomic.api.PlaytomicPrivateLeaderboard;
import com.playtomic.api.PlaytomicRequestListener;
import com.playtomic.api.PlaytomicResponse;
import com.playtomic.api.PlaytomicScore;

public class Test {

	private String mLevelId = "";
	private String mBitly = "";
	
	public static void main(String[] arg) {
		
		try {
			Playtomic.initInstance(/*swfid*/, /*guid*/, /*api key*/);
			Playtomic.Log().view();
			Playtomic.setOfflineQueueMaxSize(512);
			System.out.println("Playtomic: Playtomic API has started");
		}
		catch (Exception ex) {
			
		}
		
		Test test = new Test();
		
		test.logPlay();
		test.logCustomMetric();
		test.logCustomMetricNg();
		test.logCustomMetricU();
		test.logLevelAverageMetric();
		test.logLevelAverageMetricU();
		test.logLevelCounterMetric();
		test.logLevelCounterMetricU();
		test.logLevelRangedMetric();
		test.logLevelRangedMetricU();
		test.loadGameVars();
		test.geoip();
		test.leaderboardSave();
		test.levelSave();
		test.privateLeaderboardSave();
		test.loadPlays();
		test.loadPlaytime();
		test.loadCounters();
		test.loadCustomData();
		test.loadViews();

		test.setLogListener();
		
	}
	
	private void setLogListener() {
		Playtomic.Log().setRequestListener(new PlaytomicRequestListener<String>() {
        	public void onRequestFinished(PlaytomicResponse<String> playtomicResponse) {
        		    requestLogFinished(playtomicResponse.getMap());
			}
		});
	}
	
	private void logPlay() {
		log("\nLog Play");
		Playtomic.Log().play();
		Playtomic.Log().forceSend();
	}

	private void logCustomMetric() {
		log("\nLog Custom Metric");
	    Playtomic.Log().customMetric("custom", "group", false);
	    Playtomic.Log().forceSend();
	}

	private void logCustomMetricNg() {
		log("\nLog Custom Metric Ng");
	    Playtomic.Log().customMetric("ungroupedcustom", null, false);
	    Playtomic.Log().forceSend();
	}

	private void logCustomMetricU() {
		log("\nLog Custom Metric U");
	    Playtomic.Log().customMetric("uniquecustom", "group", true);
	    Playtomic.Log().forceSend();
	}

	private void logLevelAverageMetric() {
		log("\nLog Average Metric");
	    Playtomic.Log().levelAverageMetric("average", "name", 100, false);
	    Playtomic.Log().levelAverageMetric("average", 1, 100, false);
	    Playtomic.Log().forceSend(); 
	}

	private void logLevelAverageMetricU() {
		log("\nLog Average Metric U");
	    Playtomic.Log().levelAverageMetric("average", "name", 100, true);
	    Playtomic.Log().levelAverageMetric("average", 1, 100, true);
	    Playtomic.Log().forceSend();
	}

	private void logLevelCounterMetric() {
		log("\nLog Counter Metric");
	    Playtomic.Log().levelCounterMetric("counter", "name", false);
	    Playtomic.Log().levelCounterMetric("counter", 1, false);
	    Playtomic.Log().forceSend(); 
	}

	private void logLevelCounterMetricU() {
		log("\nLog Counter Metric U");
	    Playtomic.Log().levelCounterMetric("counter", "name", true);
	    Playtomic.Log().levelCounterMetric("counter", 1, true);
	    Playtomic.Log().forceSend();
	}

	private void logLevelRangedMetric() {
		log("\nLog Ranged Metric");
	    Playtomic.Log().levelRangedMetric("ranged", "name", 10, false);
	    Playtomic.Log().levelRangedMetric("ranged", 1, 20, false);
	    Playtomic.Log().forceSend(); 
	}

	private void logLevelRangedMetricU() {
		log("\nLog Ranged Metric U");
	    Playtomic.Log().levelRangedMetric("ranged", "name", 30, true);
	    Playtomic.Log().levelRangedMetric("ranged", 1, 40, true);
	    Playtomic.Log().forceSend();
	}

	private void requestLogFinished(LinkedHashMap<String, String> data) {
		log("Log {");
		for (Map.Entry<String, String> entry : data.entrySet()) {
			log("Msg: Name=\"" + entry.getKey() + "\" Value=\"" + entry.getValue() + "\"");
		}
		log("}");
	}
		
	private void loadGameVars() {
		log("\nLoad Game Vars");

		PlaytomicGameVars vars = new PlaytomicGameVars();
        vars.setRequestListener(new PlaytomicRequestListener<String>() {
        	public void onRequestFinished(PlaytomicResponse<String> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestVarsFinished(playtomicResponse.getMap());
				}			
				else {
					requestVarsFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
	    vars.load();
	}
	
	private void requestVarsFinished(LinkedHashMap<String, String> data) {
		log("Vars {");
		for (Map.Entry<String, String> entry : data.entrySet()) {
			log("Var: Name=\"" + entry.getKey() + "\" Value=\"" + entry.getValue() + "\"");
		}
		log("}");
	}
	
	private void requestVarsFailed(int errorCode, String message) {
		log("GameVars failed to load because of errorcode #" + errorCode + " - Message:" + message);
	}

	private void geoip() {
		log("\nGeoIP");
		PlaytomicGeoIP geoIP = new PlaytomicGeoIP();
        geoIP.setRequestListener(new PlaytomicRequestListener<String>() {
        	public void onRequestFinished(PlaytomicResponse<String> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestGeoIPFinished(playtomicResponse.getMap());
				}			
				else {
					requestGeoIPFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
	    geoIP.load();
	}

	private void requestGeoIPFinished(LinkedHashMap<String, String> data) {
		log("GeoIP {");
		for (Map.Entry<String, String> entry : data.entrySet()) {
			log("GeoIP: Name=\"" + entry.getKey() + "\" Value=\"" + entry.getValue() + "\"");
		}
		log("}");
	}
	
	private void requestGeoIPFailed(int errorCode, String message) {
		log("GeoIP failed to load because of errorcode #" + errorCode + " - Message:" + message);
	}
	
	private void leaderboardSave() {
		log("\nLeaderboard Save");
		PlaytomicLeaderboards leaderboards = new PlaytomicLeaderboards();
		leaderboards.setRequestListener(new PlaytomicRequestListener<PlaytomicScore>() {
        	public void onRequestFinished(PlaytomicResponse<PlaytomicScore> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestLeaderBoardSaveFinished();
				}			
				else {
					requestLeaderBoardSaveFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
		PlaytomicScore score = new PlaytomicScore("Ben", 2000000);
		LinkedHashMap<String, String> customData =  score.getCustomData();
		customData.put("cd1", "value1");
		customData.put("cd2", "value2");
		customData.put("cd3", "value3");
		leaderboards.save("High scores", score, true, true);
	}

	private void requestLeaderBoardSaveFinished() {
		log("Save score success");
		leaderBoardList();
		leaderboardSaveAndList();
	}
	
	private void requestLeaderBoardSaveFailed(int errorCode, String message) {
		log("Leaderboard save failed to save because of errorcode #" + errorCode + " - Message:" + message);
	}

	private void leaderBoardList() {
		log("\nLeaderboard List");
		PlaytomicLeaderboards leaderboards = new PlaytomicLeaderboards();
		leaderboards.setRequestListener(new PlaytomicRequestListener<PlaytomicScore>() {
        	public void onRequestFinished(PlaytomicResponse<PlaytomicScore> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestLeaderBoardListFinished(playtomicResponse.getData());
				}			
				else {
					requestLeaderBoardListFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
		leaderboards.list("High scores", true, "alltime", 1, 20, null);
	}

	private void requestLeaderBoardListFinished(ArrayList<PlaytomicScore> data) {
		log("Leaderboard {");
		Iterator<PlaytomicScore> itr = data.iterator();
		while (itr.hasNext()) {
			PlaytomicScore score = itr.next();
			log("----------------------------------\nScore:\nName=\"" + score.getName() + "\"");
			log("Points=\"" + score.getPoints() + "\"");
			log("Date=\"" + score.getDate() + "\"");
			log("Relative Date=\"" + score.getRelativeDate() + "\"");
			log("Rank=\"" + score.getRank() + "\"");
			log("Custom Data {");
			for (Map.Entry<String, String> entry : score.getCustomData().entrySet()) {
				log("Var: Name=\"" + entry.getKey() + "\" Value=\"" + entry.getValue() + "\"");
			}
			log("}");
		}
		log("}");
	}

	private void requestLeaderBoardListFailed(int errorCode, String message) {
		log("Leaderboard list failed to list because of errorcode #" + errorCode + " - Message:" + message);
	}

	private void leaderboardSaveAndList() {
		log("\nLeaderboard Save and List");
		PlaytomicLeaderboards leaderboards = new PlaytomicLeaderboards();
		leaderboards.setRequestListener(new PlaytomicRequestListener<PlaytomicScore>() {
        	public void onRequestFinished(PlaytomicResponse<PlaytomicScore> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestLeaderBoardListFinished(playtomicResponse.getData());
				}			
				else {
					requestLeaderBoardSaveFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
		PlaytomicScore score = new PlaytomicScore("Ben", 2000000000);
		LinkedHashMap<String, String> customData =  score.getCustomData();
		customData.put("cd1", "value1");
		customData.put("cd2", "value2");
		customData.put("cd3", "value3");
		leaderboards.saveAndList("High scores", score, true, true, "alltime", 20, null, true, null);
	}

    private void privateLeaderboardSave() {
        log("\nPrivate Leaderboard Create");
        PlaytomicLeaderboards leaderboards = new PlaytomicLeaderboards();
        leaderboards.setRequestListenerPrivateLeaderboard(new PlaytomicRequestListener<PlaytomicPrivateLeaderboard>() {
            public void onRequestFinished(PlaytomicResponse<PlaytomicPrivateLeaderboard> playtomicResponse) {
                if (playtomicResponse.getSuccess()) {
                    requestPrivateLeaderBoardSaveFinished(playtomicResponse.getObject());
                }           
                else {
                    requestPrivateLeaderBoardSaveFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
                }
            }
        });
        leaderboards.createPrivateLeaderboard("Java Test", true);
    }
    
    private void requestPrivateLeaderBoardSaveFinished(PlaytomicPrivateLeaderboard leaderboard) {
        mBitly = leaderboard.getBitly();
        log("Private Leaderboard {");
        log("name = " + leaderboard.getName());
        log("tableId = " + leaderboard.getTableId());
        log("bitly = " + leaderboard.getBitly());
        log("permalink = " + leaderboard.getPermalink());
        log("highest = " + (leaderboard.getHighest() ? "yes" : "no"));
        log("realName = " + leaderboard.getRealName());
        log("}");
		privateLeaderboardLoad();
    }
    
    private void requestPrivateLeaderBoardSaveFailed(int errorCode, String message) {
        log("Leaderboard create failed because of errorcode #" + errorCode + " - Message:" + message);
    }
    
    private void privateLeaderboardLoad() {
        if (mBitly.length() == 0) {
            log("You need to save a private leaderboar before loaded it.");
        }
        else {
            log("\nPrivate Leaderboard Load");
            PlaytomicLeaderboards leaderboards = new PlaytomicLeaderboards();
            leaderboards.setRequestListenerPrivateLeaderboard(new PlaytomicRequestListener<PlaytomicPrivateLeaderboard>() {
                public void onRequestFinished(PlaytomicResponse<PlaytomicPrivateLeaderboard> playtomicResponse) {
                    if (playtomicResponse.getSuccess()) {
                        requestPrivateLeaderBoardLoadFinished(playtomicResponse.getObject());
                    }           
                    else {
                        requestPrivateLeaderBoardLoadFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
                    }
                }
            });
            leaderboards.loadPrivateLeaderboard(mBitly);
        }
    }

    private void requestPrivateLeaderBoardLoadFinished(PlaytomicPrivateLeaderboard leaderboard) {
        log("Private Leaderboard {");
        log("name = " + leaderboard.getName());
        log("tableId = " + leaderboard.getTableId());
        log("bitly = " + leaderboard.getBitly());
        log("permalink = " + leaderboard.getPermalink());
        log("highest = " + (leaderboard.getHighest() ? "yes" : "no"));
        log("realName = " + leaderboard.getRealName());
        log("}");
    }
    
    private void requestPrivateLeaderBoardLoadFailed(int errorCode, String message) {
        log("Leaderboard load failed because of errorcode #" + errorCode + " - Message:" + message);
    }
    
	private void levelSave() {
		log("\nLevel Save");
		Random generator = new Random();
		int r = generator.nextInt();
		PlaytomicLevel level = new PlaytomicLevel("level name : " + Math.abs(r % 100), "ben4", "0", 
				"r=-152&i0=13,440,140&i1=24,440,140&i2=25,440,140&i3=37,440,140,ie,37,450,150");
		PlaytomicPlayerLevels playerLevels = new PlaytomicPlayerLevels();
		playerLevels.setRequestListener(new PlaytomicRequestListener<PlaytomicLevel>() {
        	public void onRequestFinished(PlaytomicResponse<PlaytomicLevel> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestPlayerLevelsSaveFinished(playtomicResponse.getData());
				}			
				else {
					requestPlayerLevelsSaveFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
		playerLevels.save(level);
	}

	private void requestPlayerLevelsSaveFinished(ArrayList<PlaytomicLevel> data) {
		log("PlayerLevels Save");
		Iterator<PlaytomicLevel> itr = data.iterator();
		while (itr.hasNext()) {
			PlaytomicLevel level = itr.next();
			mLevelId = level.getLevelId();
			log("Level:\nLevel Id=\"" + mLevelId + "\"");
		}
		levelLoad();
		levelList();
		levelRate();
	}

	private void requestPlayerLevelsSaveFailed(int errorCode, String message) {
		log("PlayerLevels Save failed to save because of errorcode #" + errorCode + " - Message:" + message);
	}

	private void levelList() {
		log("\nLevel List");
		PlaytomicPlayerLevels playerLevels = new PlaytomicPlayerLevels();
		playerLevels.setRequestListener(new PlaytomicRequestListener<PlaytomicLevel>() {
        	public void onRequestFinished(PlaytomicResponse<PlaytomicLevel> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestPlayerLevelsListFinished(playtomicResponse.getData());
				}			
				else {
					requestPlayerLevelsListFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
		playerLevels.list("popular", 1, 10, false, false, null);
	}

	private void requestPlayerLevelsListFinished(ArrayList<PlaytomicLevel> data) {
		log("PlayerLevels {");
		showLevels(data);
	}
	
	private void requestPlayerLevelsListFailed(int errorCode, String message) {
		log("PlayerLevels List failed to list because of errorcode #" + errorCode + " - Message:" + message);
	}

	private void levelLoad() {
		log("\nLevel Load");
		if (mLevelId.length() == 0) {
			log("Before load a level you must save one. Use the 'Level Save' button.");
		}
		else {
			PlaytomicPlayerLevels playerLevels = new PlaytomicPlayerLevels();
			playerLevels.setRequestListener(new PlaytomicRequestListener<PlaytomicLevel>() {
	        	public void onRequestFinished(PlaytomicResponse<PlaytomicLevel> playtomicResponse) {
					if (playtomicResponse.getSuccess()) {
						requestPlayerLevelsLoadFinished(playtomicResponse.getData());
					}			
					else {
						requestPlayerLevelsLoadFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
					}
				}
			});
			playerLevels.load(mLevelId);
		}
	}

	private void requestPlayerLevelsLoadFinished(ArrayList<PlaytomicLevel> data) {
		log("PlayerLevels {");
		showLevels(data);
	}
	
	private void requestPlayerLevelsLoadFailed(int errorCode, String message) {
		log("PlayerLevels Load failed to load because of errorcode #" + errorCode + " - Message:" + message);
	}
	
	private void levelRate() {
		log("\nLevel Rate");
		if (mLevelId.length() == 0) {
			log("Before rate a level you must save one. Use the 'Level Save' button.");
		}
		else {
			PlaytomicPlayerLevels playerLevels = new PlaytomicPlayerLevels(); 
			playerLevels.startLevel(mLevelId);
			playerLevels.startLevel(mLevelId);
			playerLevels.startLevel(mLevelId);
			playerLevels.retryLevel(mLevelId);
			playerLevels.winLevel(mLevelId);
			playerLevels.quitLevel(mLevelId);
			playerLevels.flagLevel(mLevelId);
			Playtomic.Log().forceSend();
			
			playerLevels.setRequestListener(new PlaytomicRequestListener<PlaytomicLevel>() {
	        	public void onRequestFinished(PlaytomicResponse<PlaytomicLevel> playtomicResponse) {
					if (playtomicResponse.getSuccess()) {
						requestPlayerLevelsRateFinished();
					}			
					else {
						requestPlayerLevelsRateFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
					}
				}
			});
			playerLevels.rate(mLevelId, 8);
		}
	}

	private void requestPlayerLevelsRateFinished() {
		log("Rate level success");
	}

	private void requestPlayerLevelsRateFailed(int errorCode, String message) {
		log("PlayerLevels Rate failed to rate because of errorcode #" + errorCode + " - Message:" + message);
	}
	
	private void showLevels(ArrayList<PlaytomicLevel> data) {		
		Iterator<PlaytomicLevel> itr = data.iterator();
		while (itr.hasNext()) {
			PlaytomicLevel level = itr.next();
			log("----------------------------------\nLevel:\nName=\"" + level.getName() + "\"");
			log("Level Id=\"" + level.getLevelId() + "\"");
			log("Player Name=\"" + level.getPlayerName() + "\"");
			log("Player Source=\"" + level.getPlayerSource() + "\"");
			log("Date=\"" + level.getDate() + "\"");
			log("Relative Date=\"" + level.getRelativeDate() + "\"");
			log("Data=\"" + level.getData() + "\"");
			log("Votes=\"" + level.getVotes() + "\"");
			log("Plays=\"" + level.getPlays() + "\"");
			log("Rating=\"" + level.getRating() + "\"");
			log("Score=\"" + level.getScore() + "\"");
			log("Custom Data {");
			for (Map.Entry<String, String> entry : level.getCustomData().entrySet()) {
				log("Var: Name=\"" + entry.getKey() + "\" Value=\"" + entry.getValue() + "\"");
			}
			log("}");
		}
		log("}");
	}

	private void loadCounters() {
		log("\nLoad Counters");
		PlaytomicData counters = new PlaytomicData();
		counters.setRequestListener(new PlaytomicRequestListener<String>() {
        	public void onRequestFinished(PlaytomicResponse<String> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestCountersFinished(playtomicResponse.getMap());
				}			
				else {
					requestCountersFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
		counters.levelCounterMetric("hi", "level 1");

		log("\nLoad Average");
		PlaytomicData average = new PlaytomicData();
		average.setRequestListener(new PlaytomicRequestListener<String>() {
        	public void onRequestFinished(PlaytomicResponse<String> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestAverageFinished(playtomicResponse.getMap());
				}			
				else {
					requestAverageFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
		average.levelCounterMetric("hi", "level 1", 1, 2010);

		log("\nLoad Range");
		PlaytomicData range = new PlaytomicData();
		range.setRequestListener(new PlaytomicRequestListener<String>() {
        	public void onRequestFinished(PlaytomicResponse<String> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestRangeFinished(playtomicResponse.getMap());
				}			
				else {
					requestRangeFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
		range.levelCounterMetric("hi", "level 1", 1, 1, 2010);
		
	}

	private void requestCountersFinished(LinkedHashMap<String, String> data) {
		log("\nCounters {");
		for (Map.Entry<String, String> entry : data.entrySet()) {
			log("Counters: Name=\"" + entry.getKey() + "\" Value=\"" + entry.getValue() + "\"");
		}
		log("}");
	}
	
	private void requestCountersFailed(int errorCode, String message) {
		log("\nCounters data failed to load because of errorcode #" + errorCode + " - Message:" + message);
	}

	private void requestAverageFinished(LinkedHashMap<String, String> data) {
		log("\nAverage {");
		for (Map.Entry<String, String> entry : data.entrySet()) {
			log("Average: Name=\"" + entry.getKey() + "\" Value=\"" + entry.getValue() + "\"");
		}
		log("}");
	}
	
	private void requestAverageFailed(int errorCode, String message) {
		log("\nAverage data failed to load because of errorcode #" + errorCode + " - Message:" + message);
	}
	
	private void requestRangeFinished(LinkedHashMap<String, String> data) {
		log("\nRange {");
		for (Map.Entry<String, String> entry : data.entrySet()) {
			log("Range: Name=\"" + entry.getKey() + "\" Value=\"" + entry.getValue() + "\"");
		}
		log("}");
	}
	
	private void requestRangeFailed(int errorCode, String message) {
		log("\nRange data failed to load because of errorcode #" + errorCode + " - Message:" + message);
	}

	private void loadCustomData() {
		log("\nLoad Cutom Data");
		PlaytomicData customData = new PlaytomicData();
		customData.setRequestListener(new PlaytomicRequestListener<String>() {
        	public void onRequestFinished(PlaytomicResponse<String> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestCustomDataFinished(playtomicResponse.getMap());
				}			
				else {
					requestCustomDataFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
		customData.customMetric("hi");
	}

	private void requestCustomDataFinished(LinkedHashMap<String, String> data) {
		log("Custom data {");
		for (Map.Entry<String, String> entry : data.entrySet()) {
			log("Custom data: Name=\"" + entry.getKey() + "\" Value=\"" + entry.getValue() + "\"");
		}
		log("}");
	}
	
	private void requestCustomDataFailed(int errorCode, String message) {
		log("Custom data failed to load because of errorcode #" + errorCode + " - Message:" + message);
	}

	private void loadPlays() {
		log("\nLoad Plays");
		PlaytomicData plays = new PlaytomicData();
		plays.setRequestListener(new PlaytomicRequestListener<String>() {
        	public void onRequestFinished(PlaytomicResponse<String> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestPlaysFinished(playtomicResponse.getMap());
				}			
				else {
					requestPlaysFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
		plays.plays();
	}

	private void requestPlaysFinished(LinkedHashMap<String, String> data) {
		log("Plays {");
		for (Map.Entry<String, String> entry : data.entrySet()) {
			log("Plays: Name=\"" + entry.getKey() + "\" Value=\"" + entry.getValue() + "\"");
		}
		log("}");
	}
	
	private void requestPlaysFailed(int errorCode, String message) {
		log("Plays failed to load because of errorcode #" + errorCode + " - Message:" + message);
	}

	private void loadPlaytime() {
		log("\nLoad Playtime");
		PlaytomicData playTime = new PlaytomicData();
		playTime.setRequestListener(new PlaytomicRequestListener<String>() {
        	public void onRequestFinished(PlaytomicResponse<String> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestPlaytimeFinished(playtomicResponse.getMap());
				}			
				else {
					requestPlaytimeFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
		playTime.playtime();
	}

	private void requestPlaytimeFinished(LinkedHashMap<String, String> data) {
		log("Playtime {");
		for (Map.Entry<String, String> entry : data.entrySet()) {
			log("Playtime: Name=\"" + entry.getKey() + "\" Value=\"" + entry.getValue() + "\"");
		}
		log("}");
	}
	
	private void requestPlaytimeFailed(int errorCode, String message) {
		log("Playtime failed to load because of errorcode #" + errorCode + " - Message:" + message);
	}

	private void loadViews() {
		log("\nLoad Views");
		PlaytomicData views = new PlaytomicData();
		views.setRequestListener(new PlaytomicRequestListener<String>() {
        	public void onRequestFinished(PlaytomicResponse<String> playtomicResponse) {
				if (playtomicResponse.getSuccess()) {
					requestViewsFinished(playtomicResponse.getMap());
				}			
				else {
					requestViewsFailed(playtomicResponse.getErrorCode(), playtomicResponse.getErrorMessage());
				}
			}
		});
		views.views();
	}

	private void requestViewsFinished(LinkedHashMap<String, String> data) {
		log("Views {");
		for (Map.Entry<String, String> entry : data.entrySet()) {
			log("View: Name=\"" + entry.getKey() + "\" Value=\"" + entry.getValue() + "\"");
		}
		log("}");
	}
	
	private void requestViewsFailed(int errorCode, String message) {
		log("Views failed to load because of errorcode #" + errorCode + " - Message:" + message);
	}
	
	private void log(String message) {
		System.out.println("Playtomic: " + message);
	}

}
