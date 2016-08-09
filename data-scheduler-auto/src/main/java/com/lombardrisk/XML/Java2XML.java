package com.lombardrisk.XML;

import java.io.FileOutputStream;   
import java.io.IOException;   

import org.dom4j.Document;  
import org.dom4j.DocumentHelper;  
import org.dom4j.Element;  
import org.dom4j.io.OutputFormat;  
import org.dom4j.io.XMLWriter;
/*
import org.jdom.Document;   
import org.jdom.Element;   
import org.jdom.JDOMException;   
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
*/
/**
 * Create by Leo Tu on 2016-05-19
 */

public class Java2XML {
	public static void BuildRetrieveXML(String prefix,String implementationVersion,String referenceDate ,String entityId,String returnId,String formCode,String formVersion) throws IOException {
		 Element root = DocumentHelper.createElement("formInstanceImportRequest");
		 Document document = DocumentHelper.createDocument(root);
		 
		 Element element = root.addElement("importType");
		 element.setText("DW");
		 
		 element = root.addElement("importSubType");
		 element.setText("Retreive");
		 
		 element = root.addElement("importLogLevel");
		 element.setText("DEBUG");
		 
		 element = root.addElement("productConfiguration");
		 Element element1=element.addElement("prefix");
		 element1.setText(prefix);

		 element1=element.addElement("implementationVersion");
		 element1.setText(implementationVersion);
		 
		 element = root.addElement("importParameters");
		 element1=element.addElement("dwImportParameters");
		 Element element2=element1.addElement("parentBatchRunInvocationId");
		 element2.setText("0");
		 element1=element.addElement("referenceDate");
		 element1.setText(referenceDate);
		 
		 element1=element.addElement("entityId");
		 element1.setText(entityId);
		 
		 element1=element.addElement("formId");
		 element2=element1.addElement("toolsetFormId");
		 Element element3 =element2.addElement("returnId");
		 element3.setText(returnId);
		 
		 element3=element1.addElement("fullyQualifiedFormId");
		 Element element4=element3.addElement("formCode");
		 element4.setText(formCode);
		 
		 element4=element3.addElement("formVersion");
		 element4.setText(formVersion);
		 
		 OutputFormat format = new OutputFormat("    ",true);  
         format.setEncoding("UTF-8");
         XMLWriter xmlWriter = new XMLWriter(new FileOutputStream("Retrieve.xml"),format);  
      
         xmlWriter.write(document);  
         xmlWriter.close();  

	 }
	 
	 
	 
	 
	 public static void BuildExportXML(String prefix,String implementationVersion,String referenceDate ,String entityId, String returnId,String formCode,String formVersion,String ScheduleExportId,String UserPath) throws IOException {
		 Element root = DocumentHelper.createElement("formInstanceExportRequest");
		 Document document = DocumentHelper.createDocument(root);
		
		 Element element = root.addElement("exportType");
		 element.setText("DataSchedule");
		
		 
		 element = root.addElement("productConfiguration");
		 Element element1=element.addElement("prefix");
		 element1.setText(prefix);

		 element1=element.addElement("implementationVersion");
		 element1.setText(implementationVersion);
		 
		 element = root.addElement("exportParameters");
		 element1=element.addElement("dataScheduleExportParameters");
		 Element element2=element1.addElement("configDataScheduleExportId");
		 element2.setText(ScheduleExportId);
		 
		 element2=element1.addElement("parentBatchRunInvocationId");
		 element2.setText("0");
		 
		 element2=element1.addElement("exportFileUserPath");
		 element2.setText(UserPath);
		 
		 element = root.addElement("formInstanceIds");
		 element2=element1=element.addElement("toolsetFormInstanceId");
		 element1=element2.addElement("processDate");
		 element1.setText(referenceDate);
		 
		 element1=element2.addElement("returnId");
		 element1.setText(returnId);
		 
		 element1=element2.addElement("entityId");
		 element1.setText(entityId);
		 
		 element = root.addElement("exportDestination");
		 element1= element.addElement("file");
		 element2= element1.addElement("filename");
		 element2.setText(entityId+"_"+returnId);
		 
		 element2= element1.addElement("compressOutput");
		 element2.setText("NONE");
		 
		 OutputFormat format = new OutputFormat("    ",true);  
         format.setEncoding("UTF-8");
         XMLWriter xmlWriter = new XMLWriter(new FileOutputStream("Export.xml"),format);  
      
         xmlWriter.write(document);  
         xmlWriter.close();  

	    }
}
