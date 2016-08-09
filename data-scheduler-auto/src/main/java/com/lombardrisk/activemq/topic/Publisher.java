package com.lombardrisk.activemq.topic;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * topic: Publisher
 */
public class Publisher {

	public static void main(String[] args) {
		
		String user = ActiveMQConnection.DEFAULT_USER;
		String password = ActiveMQConnection.DEFAULT_PASSWORD;
		String url = "tcp://localhost:61616";
		String subject = "TOOL.DEFAULT.TOPIC";
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(user, password, url);
		Connection connection;
		try {
			connection = factory.createConnection();
			connection.start();
			Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(subject);
			MessageProducer producer = session.createProducer(topic);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			for (int i = 0; i < 20; i++) {
				MapMessage message = session.createMapMessage();
				Date date = new Date();
				message.setLong("count", date.getTime());
				Thread.sleep(1000);
				producer.send(message);
				System.out.println("--send message：" + date);
			}
			session.commit();
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
