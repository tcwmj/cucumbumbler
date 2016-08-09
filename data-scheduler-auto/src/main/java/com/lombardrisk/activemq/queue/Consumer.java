package com.lombardrisk.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * queue: Consumer
 */
public class Consumer {

	public static void main(String[] args) {
		
		String user = ActiveMQConnection.DEFAULT_USER;
		String password = ActiveMQConnection.DEFAULT_PASSWORD;
		String url = ActiveMQConnection.DEFAULT_BROKER_URL;
		String subject = "dsRetrieveForAutoTestQueue";
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory( user, password, url);
		Connection connection;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(subject);
			MessageConsumer message = session.createConsumer(destination);
			message.setMessageListener(new MessageListener() {
				public void onMessage(Message msg) {
					TextMessage message = (TextMessage) msg;
					try {
						System.out.println("--receive message：" + message.getText());
						session.commit();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			

			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		} 
	}

}
