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

import java.util.LinkedHashMap;

public class PlaytomicLink {

    private LinkedHashMap<String, String> mClicks = new LinkedHashMap<String, String>();

    public void trac(String url, String name, String group) {
        int unique = 0;
        int bunique = 0;
        int total = 0;
        int btotal = 0;

        String key = url + "." + name;
        String baseurl = url.replace("http://", "");
        baseurl = baseurl.replace("https://", "");

        int i = baseurl.indexOf("/");

        if (i >= 0) {
            baseurl = baseurl.substring(i);
        }

        String baseurlname = baseurl;
        int www = baseurlname.indexOf("www.");

        if (www == 0) {
            baseurlname = baseurlname.substring(4);
        }

        if (mClicks.containsKey(key)) {
            total = 1;
        } else {
            total = 1;
            unique = 1;
            mClicks.put(key, key);
        }

        if (mClicks.containsKey(baseurlname)) {
            btotal = 1;
        } else {
            btotal = 1;
            bunique = 1;
            mClicks.put(baseurlname, baseurlname);
        }

        Playtomic.Log().link(baseurl, baseurlname, "DomainTotals", bunique, btotal, 0);

        Playtomic.Log().link(url, name, group, unique, total, 0);

        Playtomic.Log().forceSend();
    }
}
