package com.woxiangbo.projects.mycompare.listener;

import java.util.List;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

public class MyReporter implements IReporter{

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {
		outputDirectory = "D:/CollineReport/";
		
	}



}
