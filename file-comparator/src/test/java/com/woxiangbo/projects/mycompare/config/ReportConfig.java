package com.woxiangbo.projects.mycompare.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;


public class ReportConfig {
	public static char csvSeparateSymbol = ',';
//	public static String resultDir = "D:/Colline Compare Result/";
	
	public static String getResultDir(){
		try {
			XMLConfiguration config = new XMLConfiguration("ResultConfig.xml");
			return config.getString("resultDir");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getStatisticsFileName() {
	    try {
            XMLConfiguration config = new XMLConfiguration("ResultConfig.xml");
            return config.getString("StatisticsFile");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;
	}

	public static String customConflictWithExclude(){
		return "error ):Custom Keys conflict with Exclude Columns .";
	}
	public static String keyNotFound(String key,String fileName){
		return "error ):Custom Key " + key + "do NOT exists in " + fileName;
	}
	public static String fileNotFount(String fileName){
		return "error ): " +fileName + " do NOT exists!";
	}
	public static String keysNotUnique(String fileName){
		return "error ): Custom Key find more than 1 items in file " + fileName +",Please check custom keys.";
	}
	public static void main(String[] args) {
		
		
	}
}
