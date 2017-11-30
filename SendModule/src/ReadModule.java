//package com.microsoft.azure.gateway.sample;
/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

import com.microsoft.azure.gateway.core.GatewayModule;
import com.microsoft.azure.gateway.core.Broker;
import com.microsoft.azure.gateway.messaging.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.google.gson.Gson;

import java.util.Properties;

public class ReadModule extends GatewayModule {

	private boolean threadStop;
	Context context = null;
	public static Session session = null;
	public static Connection connection = null;
	public static MessageProducer producer = null;
	Queue queue = null;
	String jsonobj = null;

	/**
	 * Constructs a {@link GatewayModule} from the provided address and
	 * {@link Broker}. A {@link GatewayModule} should always call this super
	 * constructor before any module-specific constructor code.
	 *
	 * @param address
	 *            The address of the native module pointer
	 * @param broker
	 *            The {@link Broker} to which this module belongs
	 * @param configuration
	 *            The module-specific configuration
	 */
	public ReadModule(long address, Broker broker, String configuration) {
		super(address, broker, configuration);
		this.threadStop = false;
		System.out.println("ReadModule Senser Module Constructor.....: ");
		
	}

	/*
	 * Session getConnection() throws Exception {
	 * 
	 * Properties properties = new Properties();
	 * properties.load(this.getClass().getResourceAsStream("Hello.Properties"));
	 * Context context = new InitialContext(properties); ConnectionFactory
	 * connectionFactory = (ConnectionFactory)
	 * context.lookup("qpidConnectionFactory"); connection =
	 * connectionFactory.createConnection(); connection.start(); Session session
	 * = connection.createSession(true, Session.SESSION_TRANSACTED); Queue queue
	 * = (Queue) context.lookup("myqueue"); producer =
	 * session.createProducer(queue); return session;
	 * 
	 * }
	 */
	public static void main(String args[]) {

		System.out.println("Main ReadModule Module Constructor.....: ");

	}
	/*public Session getSession(){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("C:/Configurationfiles/config.properties"));
			Context context = new InitialContext(properties);
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("qpidConnectionFactory");
			connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
			 queue = (Queue) context.lookup("myqueue");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return session;
	
			
	}*/
	/*public Session getSession(){
		Properties properties = new Properties();
		try {
			
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("qpidConnectionFactory");
			connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
			properties.load(new FileInputStream("C:/Configurationfiles/config.properties"));
			Context context = new InitialContext(properties);
			 queue = (Queue) context.lookup("myqueue");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return session;

			
	}	*/
/*	public Queue getQueueName(){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("C:/Configurationfiles/config.properties"));
			Context context = new InitialContext(properties);
			 queue = (Queue) context.lookup("myqueue");
			
		} catch (IOException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return queue;
	
			
	}*/

	@Override
	public void start() {
		
		new Thread(() -> {
			while (!this.threadStop) {
				Properties properties = new Properties();
				try {
					//properties.load(this.getClass().getResourceAsStream("Hello.Properties"));
					properties.load(new FileInputStream("C:/Configurationfiles/config.properties"));
					Context context = new InitialContext(properties);
					ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("qpidConnectionFactory");
					connection = connectionFactory.createConnection();
					connection.start();
					Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
					Queue queue = (Queue) context.lookup("myqueue");
					//session =	getSession();
					//queue = getQueueName();
					 MessageConsumer consumer= session.createConsumer(queue);
			          TextMessage message = (TextMessage)consumer.receive();
			          String msg = message.getText().toString();
			          Gson gson = new Gson();
				    // convert java object to JSON format
				     jsonobj = gson.toJson(msg);
				      System.out.println("Json object is ="+jsonobj);
						
						HashMap<String, String> map = new HashMap<>();
						// map.put("Source", new Long(this.hashCode()).toString());
						// map.put("Source", "Jagatjyoti1123456");
						map.put("Source", jsonobj);
						// Get "ReadModule" reading
						Double uniquenumber = Math.random() * 50;

						// Construct message

						Message m = new Message(uniquenumber.toString().getBytes(), map);

						// Publish message
						this.publish(m);

						// Sleep
						Thread.sleep(500);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("ReadModule  Module .....:IOException ");
					e.printStackTrace();
				}  catch (JMSException e) {
					// TODO Auto-generated catch block
					System.out.println("ReadModule  Module .....:JMSException ");
					e.printStackTrace();
				
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void receive(Message message) {
		// Ignores incoming messages
		System.out.println("ReadModule Module Received.....: " + message);
	}

	@Override
	public void destroy() {
		// Causes the publishing thread to stop
		System.out.println("ReadModule Module Destroy.....: ");
		this.threadStop = true;
	}
}
