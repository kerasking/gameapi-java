//
// PlaytomicHttpRequest.java
// Playtomic
//
// This file is part of the official Playtomic API for Java games.
// Playtomic is a real time analytics platform for casual games
// and services that go in casual games. If you haven't used it
// before check it out:
// http://playtomic.com/
//
// Created by ben at the above domain on 10/19/11.
// Copyright 2011 Playtomic LLC. All rights reserved.
//
// Documentation is available at:
// http://playtomic.com/api/java
//
// PLEASE NOTE:
// You may modify this SDK if you wish but be kind to our servers. Be
// careful about modifying the analytics stuff as it may give you
// borked reports.
//
// If you make any awesome improvements feel free to let us know!
//
// -------------------------------------------------------------------------
// THIS SOFTWARE IS PROVIDED BY PLAYTOMIC, LLC "AS IS" AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
// PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
// CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
// EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
// PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
// LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
// NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.playtomic.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

class PlaytomicHttpRequest implements Runnable {

    private PlaytomicHttpRequestListener mHttpRequestListener;
    private LinkedHashMap<String, String> mPostData = new LinkedHashMap<String, String>();

    private static String URLStub;
    private static String URLTail;
    private static String URL;

    private Thread mT;
    private String[] mUrls;
    
    class Result {
    		public boolean success = false;
    		public String result = "";
    		public Result (boolean success, String result) {
    			this.success = success;
    			this.result = result;
    		}
    }
    
    public static void Initialise() {
        URLStub = "http://g" + Playtomic.GameGuid() + ".api.playtomic.com/";
        URLTail = "swfid=" + Playtomic.GameId() + "&js=y";
        URL = URLStub + "v3/api.aspx?" + URLTail;
    }

    public void setHttpRequestListener(PlaytomicHttpRequestListener httpRequestListener) {
        mHttpRequestListener = httpRequestListener;
    }
    
    public void addPostData(String name, String value) {
        mPostData.put(name, value);        
    }

    public void addPostData(String name, int value) {
        mPostData.put(name, String.valueOf(value));
    }
    
    public void execute(String ... urls) throws HttpRequestExecutionException {
    		if (mT != null)
    			throw new HttpRequestExecutionException("The exec method can be called only one time for each instance of the class PlaytomicHttpRequest.");
    		
    		mUrls = urls;
    		mT = new Thread(this);
    		mT.start();
    }

    public void run() {
    		Result result = doInBackground();
    		notifyListener(new PlaytomicHttpResponse(result.success, result.success ? Playtomic.SUCCESS : Playtomic.ERROR, result.result));    		
    }
    
    private Result doInBackground() {
        try {
            URL url = new URL(mUrls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try {
                // Post data
                //            
                if (mUrls.length > 1 || mPostData.size() > 0) {

                    StringBuilder parameters = new StringBuilder(); 

                    if (mUrls.length > 1) {
                        for (int i = 1; i < mUrls.length; i++) {
                            parameters.append(mUrls[i]);
                        }
                    }
                    
                    Boolean addAmp = false;
                    for (Map.Entry<String, String> entry : mPostData.entrySet()) {
                        parameters.append(entry.getKey());
                        parameters.append("=");
                        parameters.append(URLUTF8Encoder.encode(entry.getValue()));
                        if (addAmp) {
                            parameters.append("&");
                        }
                        else {
                            addAmp = true;
                        }
                    }
                    
                    String urlParameters = parameters.toString(); 
                    
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", 
                       "application/x-www-form-urlencoded");
                            
                    connection.setRequestProperty("Content-Length", "" + 
                               Integer.toString(urlParameters.getBytes().length));
                    connection.setRequestProperty("Content-Language", "en-US");  
                            
                    connection.setUseCaches(false);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    
                    DataOutputStream wr = new DataOutputStream (
                              connection.getOutputStream ());
                    wr.writeBytes (urlParameters);
                    wr.flush ();
                    wr.close ();                  
                }
                
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    
                String decodedString;
                String response = "";
                
                while ((decodedString = in.readLine()) != null) {
                    response += decodedString;
                }
                return new Result(true, response);
            }
            catch(Exception ex) {
                return new Result(false, ex.toString() +  " - url: " + mUrls[0]);
            }
            finally {
                connection.disconnect();
            }
        }        
        catch (Exception ex) {
            return new Result(false, ex.toString() +  " - url: " + mUrls[0]);
        }
    }

    public static PlaytomicHttpRequestUrl prepare(String section, String action, LinkedHashMap<String, String> postdata) throws Exception {
        PlaytomicHttpRequestUrl url = new PlaytomicHttpRequestUrl();
        Random r = new Random();
        url.setUrl(URL + "&r=" + r.nextInt(10000000) + "Z");
        
        long seconds = System.currentTimeMillis() / 1000;
        String timestamp = String.valueOf(seconds);
        String nonce = PlaytomicEncrypt.md5(timestamp + Playtomic.SourceUrl() + Playtomic.GameGuid());
    
        ArrayList<String> pd = new ArrayList<String>();
        pd.add("nonce=" + nonce);
        pd.add("timestamp=" + timestamp);
        
        if(postdata != null) {
            for (Map.Entry<String, String> entry : postdata.entrySet()) {
                pd.add(entry.getKey() + "=" + escape(entry.getValue()));
            }
        }
        
        generateKey("section", section, pd);
        generateKey("action", action, pd);
        generateKey("signature", nonce + timestamp + section + action + url.getUrl() + Playtomic.GameGuid(), pd);
        
        String joined = "";
        
        for(String item : pd) {
            joined += (joined == "" ? "" : "&") + item;
        }

        url.setData(escape(PlaytomicEncrypt.encodeToString(joined)));

        return url;
    }
    
    private static void generateKey(String name, String key, ArrayList<String> arr) throws Exception {        
        String[] sa = new String[arr.size()]; 
        sa = arr.toArray(sa);        
        Arrays.sort(sa);
        
        String joined = "";
        for(String item : sa) {
            joined += (joined == "" ? "" : "&") + item;
        }
        
        arr.add(name + "=" + PlaytomicEncrypt.md5(joined + key));
    }
    
    private static String escape(String str)    {
        str = str.replace("%", "%25");
        str = str.replace(";", "%3B");
        str = str.replace("?", "%3F");
        str = str.replace("/", "%2F");
        str = str.replace(":", "%3A");
        str = str.replace("#", "%23");
        str = str.replace("&", "%26");
        str = str.replace("=", "%3D");
        str = str.replace("+", "%2B");
        str = str.replace("$", "%24");
        str = str.replace(",", "%2C");
        str = str.replace(" ", "%20");
        str = str.replace("<", "%3C");
        str = str.replace(">", "%3E");
        return str.replace("~", "%7E");
    }
    
    private void notifyListener(PlaytomicHttpResponse playtomicResponse) {
        if (mHttpRequestListener != null) {
            mHttpRequestListener.onRequestFinished(playtomicResponse);
        }
    }    
}