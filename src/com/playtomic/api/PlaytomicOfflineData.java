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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PlaytomicOfflineData {

	public int getInt(String key, int defaultValue) throws FileNotFoundException, IOException, ClassNotFoundException {
		String value = readFromFile(key);
		if (value == null)
			return defaultValue;
		
		int rtn;
		
		try {
			rtn = Integer.parseInt(value);
		}
		catch (Exception ex) {
			rtn = defaultValue;
		}
		return rtn;
	}

	public boolean getBoolean(String key, boolean defaultValue) throws FileNotFoundException, IOException, ClassNotFoundException {
		String value = readFromFile(key);
		if (value == null)
			return defaultValue;

		if (value.equals("true"))
			return true;
		if (value.equals("false"))
			return false;
		return defaultValue;
	}
	
	public String getString(String key, String defaultValue) throws FileNotFoundException, IOException, ClassNotFoundException {
		String value = readFromFile(key);
		if (value == null)
			return defaultValue;
		else
			return value;
	}
	
	public void putInt(String key, int value) throws FileNotFoundException, IOException {
		saveToFile(key, String.valueOf(value));
	}
	
	public void putString(String key, String value) throws FileNotFoundException, IOException {
		saveToFile(key, value);
	}
	
	public void putBoolean(String key, boolean value) throws FileNotFoundException, IOException {
		saveToFile(key, value ? "true" : "false");
	}
	
	public void remove(String key) throws FileNotFoundException, IOException {
		File file = new File(key);
		if (file.exists())
			file.delete();
	}
	
	private void saveToFile(String file, String value) throws FileNotFoundException, IOException {
		FileOutputStream saveFile = new FileOutputStream(file);
		ObjectOutputStream save = new ObjectOutputStream(saveFile);
		save.writeObject(value);
		save.close();
	}
	
	private String readFromFile(String file) throws IOException, ClassNotFoundException {
		if (!fileExists(file)) {
			return null;
		}
		else {
			FileInputStream inputFile = new FileInputStream(file);
			ObjectInputStream input = new ObjectInputStream(inputFile);
			String ret = (String) input.readObject();
			input.close();
			return ret;
		}
	}
	
	private boolean fileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();		
	}
}
