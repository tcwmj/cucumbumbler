package com.lombardrisk.activemq.queue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.lombardrisk.XML.Java2XML;
import com.lombardrisk.utils.DBQuery;
import com.lombardrisk.utils.PropHelper;


/**
 * queue: Producer
 */
//dsExportForAutoTestQueue 
//dsRetrieveForAutoTestQueue
public class Producer {
	static String exportPath = PropHelper.getProperty("export.path").trim();
	public static void main(String[] args) throws IOException {
		String action = args[0];
		String Regulator=args[1];
		String referenceDate=args[3];
		String entityName=args[2];
		String returnid=args[4];
		String queueName=null;
		if(action.equalsIgnoreCase("retrieve"))
			queueName="dsRetrieveForAutoTestQueue";
		else if(action.equalsIgnoreCase("export"))
			queueName="dsExportForAutoTestQueue";
		
		String path=action+".xml";
		
		String SQL = "SELECT \"PREFIX\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "'  AND \"STATUS\"='A' ";
		String prefix=DBQuery.queryRecord(SQL);
		
		SQL = "SELECT \"IMPLEMENTATION_VERSION\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "'  AND \"STATUS\"='A' ";
		String IMPLEMENTATION_VERSION=DBQuery.queryRecord(SQL);
		
		SQL = "SELECT \"ID\" FROM \"USR_NATIVE_ENTITY\" WHERE \"ENTITY_NAME\"=" + entityName ;
		String entityID=DBQuery.queryRecord(SQL);
		
		String ID_Start = getRegulatorIDRangeStart(Regulator);
        String ID_End = getRegulatorIDRangEnd(Regulator);
        SQL ="SELECT \"Return\" FROM \"CFG_RPT_Rets\" where \"ReturnId\"="+returnid+" and \"ID\" between " + ID_Start + " and " + ID_End; 
		String formCode=DBQuery.queryRecord(SQL);
		
		SQL ="SELECT \"Version\" FROM \"CFG_RPT_Rets\" where \"ReturnId\"="+returnid+" and \"ID\" between " + ID_Start + " and " + ID_End; 
		String formVersion=DBQuery.queryRecord(SQL);

		
		
		if(action.equalsIgnoreCase("retrieve"))
			Java2XML.BuildRetrieveXML(prefix, IMPLEMENTATION_VERSION, referenceDate, entityID, returnid, formCode, formVersion);
		else if(action.equalsIgnoreCase("export")){
			SQL="SELECT  \"DATA_SCHEDULE_EXPORT_ID\" FROM \"CFG_DATA_SCHEDULE_VIEW\" WHERE \"ID\" IN (SELECT  \"ID\" FROM \"CFG_DATA_SCHEDULE_EXPORT\" WHERE \"EXPORT_FORMAT_ID\" IN (SELECT \"ID\" FROM \"CFG_EXPORT_FORMAT\" WHERE \"EXPORT_FORMAT_TYPE\"='DataSchedule'))";
			String ScheduleExportId=DBQuery.queryRecord(SQL);
			
			Java2XML.BuildExportXML(prefix, IMPLEMENTATION_VERSION, referenceDate, entityID, returnid, formCode, formVersion,ScheduleExportId,exportPath);
		}
		
		
		// Create a ConnectionFactory
		String user = ActiveMQConnection.DEFAULT_USER;
		String password = ActiveMQConnection.DEFAULT_PASSWORD;
		String url = ActiveMQConnection.DEFAULT_BROKER_URL;
		ConnectionFactory contectionFactory = new ActiveMQConnectionFactory(user, password, url);
		
		try {
			// Create a Connection
			Connection connection = contectionFactory.createConnection();
			connection.start();
			
			// Create a Session
			Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			
			// Create the destination (Topic or Queue)
			Destination destination = session.createQueue(queueName);
			
			// Create a MessageProducer from the Session to the Topic or Queue
			MessageProducer producer = session.createProducer(destination);

			// Create a messages
			StringBuilder sb = new StringBuilder();
			
			File file = new File(path);

			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			
			TextMessage message = session.createTextMessage(sb.toString());

			producer.send(message);
			System.out.println("send message completed ");
			
			session.commit();
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public static String getRegulatorIDRangeStart(String Regulator) {
        String SQL = "SELECT \"ID_RANGE_START\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "'  AND \"STATUS\"='A' ";
        return DBQuery.queryRecord(SQL);
    }

    public static String getRegulatorIDRangEnd(String Regulator) {
        String SQL = "SELECT \"ID_RANGE_END\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "' AND \"STATUS\"='A'  ";
        return DBQuery.queryRecord(SQL);
    }

}
