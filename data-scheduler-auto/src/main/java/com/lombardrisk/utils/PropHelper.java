package com.lombardrisk.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Create by Leo Tu on 2016-05-19
 */

public class PropHelper {

	 public static String getProperty(String key) {
		 String strValue =null;
		 Properties pps = new Properties();
		 InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream("product.properties"));
			pps.load(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 Enumeration en = pps.propertyNames(); 
		 while(en.hasMoreElements()) {
			 
			 String strKey = (String) en.nextElement();
			 if(strKey.equalsIgnoreCase(key)){
				 strValue = pps.getProperty(strKey);
				 break;
		 	}      
		 }
		 return strValue;
	 }
}
