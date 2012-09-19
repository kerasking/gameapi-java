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

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;

public class PlaytomicEncrypt {

	public static String md5(String value) throws Exception {
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		algorithm.reset();
		algorithm.update(value.getBytes());
		byte messageDigest[] = algorithm.digest();
	            
		StringBuffer hexString = new StringBuffer();
		
		for (int i = 0; i < messageDigest.length; i++) {
			hexString.append(Integer.toString((messageDigest[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		return hexString.toString();
	}
	
	/**
     * Encodes the string 'in' using 'flags'.  Asserts that decoding
     * gives the same string.  Returns the encoded string.
     */
    public static String encodeToString(String in) throws Exception {
        /*int[] flagses = { Base64.DEFAULT,
                Base64.NO_PADDING,
                Base64.NO_WRAP,
                Base64.NO_PADDING | Base64.NO_WRAP,
                Base64.CRLF,
                Base64.URL_SAFE };*/
        //String b64 = Base64.encodeToString(in.getBytes(), Base64.NO_WRAP);
        String b64 = new String(Base64.encodeBase64(in.getBytes()));
        return b64;
    }
}
