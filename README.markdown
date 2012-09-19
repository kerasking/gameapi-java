# Playtomic Java API

This file is the official Playtomic API for Java games.  

Playtomic is a real time analytics platform for casual games and services 
that go in casual games.  If you haven't used it before check it out:

- https://playtomic.com/

Documentation is available at:

- https://playtomic.com/api/java

Support is available at:
	
- https://playtomic.com/community
- https://playtomic.com/issues
- https://playtomic.com/support has more options if you're a premium user
	
	
You may modify this SDK if you wish but be kind to our servers.  Be
careful about modifying the analytics stuff as it may give you 
borked reports.

Pull requests are welcome if you spot a bug or know a more efficient
way to implement something.

### General notes
Don't use crazy characters in your metric, level, leaderboard table etc 
names.  A very good idea is to use the same naming conventions for variables:

- alphanumeric
- keep them short (50 chars is the max)

Play time, country and source information is handled automatically, you do not 
need to do anything to collect that data.

Begin by logging a view which initializes the API, and then log any metrics you 
want.  Play time, repeat visitors etc are handled automatically.

#### Logging views and plays
Call this at the very start of your game loading:

	Playtomic.initInstance(swfid, guid, apikey, this)
	
		swfid = from your API page in Playtomic
		guid = from your API page in Playtomic
		apikey = from your API page in Playtomic
		loaderinfo = your root.loaderInfo

This initializes the API, after which you can track the view and when the player actually starts playing (eg play button) by calling this 0 or more times in your game:

	Playtomic.Log().view();
	Playtomic.Log().play();
	
Log.Play can be called multiple times in a single session.

#### Custom metrics
Custom metrics can track any general events you want to follow, like 
tracking who views your credits screen or anything else.

Call this to log any custom data you want to track.

	Playtomic.Log().customMetric(name, group, unique)
	
		name = your metric name, eg "ViewedCredits"
		group = optional, specify the group name for sorting in reports
		unique = optional, only count unique occurrences 

#### Level metrics
Level metrics can identify problems players are having with your difficulty 
or anything else by logging information like how many people begin vs. finish
each level.  They can help you identify major problems in your player retention.

These methods log the 3 types of level metrics - counters, ranged-value 
and average-value metrics.

- Counter metrics count how many times an event occurs across levels
- Ranged-value metrics track multiple values across a single event across levels
- Average-value metrics track the average of something across levels

	Playtomic.Log().levelCounterMetric(name, level, unique)

		name = your metric name
		level = either a level number (int > 0) or a level name
		unique = optional, only count unique-per-view occurrences

	Playtomic.Log().levelRangedMetric(name, level, value, unique)

		name = your metric name
		level = either a level number (int > 0) or a level name
		value = the value you want to track
		unique = optional, only count unique-per-view occurrences 


	Playtomic.Log().levelAverageMetric(name, level, value, unique)
		name = your metric name
		level = either a level number (int > 0) or a level name
		value = the value you want to track
		unique = optional, only count unique-per-view occurrences 

#### Leaderboards, level sharing, data and geoip 
This stuff gets a little more complicated.  Please check the documentation 
for examples:
	
- https://playtomic.com/api/java
	
##### LICENSE & DISCLAIMER
Copyright (c) 2011 Playtomic Inc.  Playtomic APIs and SDKs are licensed 
under the MIT license.  Certain portions may come from 3rd parties and 
carry their own licensing terms and are referenced where applicable.