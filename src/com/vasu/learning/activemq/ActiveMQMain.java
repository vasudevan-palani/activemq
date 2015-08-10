package com.vasu.learning.activemq;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQMain {
	public static void main(String[] args) throws JMSException, NamingException {
		ConnectionFactory connectionFactory;
		Connection connection = null;
		Session session = null;
		Destination destination;
		MessageProducer producer = null;
		Message message;
		boolean useTransaction = false;
		try {
			// An initial context is usually created with a JNDI path. This one
			// is
			// for demonstration purposes only.

			// 1) From the initial context, lookup a JMS connection factory
			// using
			// the unique name for the connection factory.
			connectionFactory = new ActiveMQConnectionFactory();
			// 2) Using the connection factory, create a JMS connection.
			connection = connectionFactory.createConnection();
			// 3) Make sure to call start() on the connection factory to enable
			// messages to start flowing
			connection.start();
			// 4) Using the connection, create a JMS session. In this case we're
			// just using auto-acknowledgement of messages
			session = connection.createSession(useTransaction,
					Session.AUTO_ACKNOWLEDGE);
			// 5) Create a JMS queue using the session
			destination = session.createQueue("TEST.QUEUE");
			// 6a) Create a JMS producer using the session and the destination
			producer = session.createProducer(destination);
			// The JMS delivery mode is persistent by default, but we're just
			// doing
			// it explicitly here
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			// 6b) Create a simple text message containing only a payload
			message = session.createTextMessage("this is a test");
			// 8) Using the producer, send the message to the destination
			producer.send(message);
		} catch (JMSException jmsEx) {
			jmsEx.printStackTrace();
		} finally {
			// 9) Close the objects that were used above
			producer.close();
			session.close();
			connection.close();
		}
	}
}
