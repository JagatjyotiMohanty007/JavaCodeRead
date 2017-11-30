import com.microsoft.azure.gateway.core.GatewayModule;

/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

import com.microsoft.azure.gateway.core.GatewayModule;
import com.microsoft.azure.gateway.core.Broker;
import com.microsoft.azure.gateway.messaging.Message;
import com.microsoft.azure.gateway.core.GatewayModule;
import com.microsoft.azure.gateway.core.Broker;
import com.microsoft.azure.gateway.messaging.Message;
import com.microsoft.azure.iothub.*;


import com.microsoft.azure.iothub.DeviceClient;
import com.microsoft.azure.iothub.IotHubClientProtocol;
import com.microsoft.azure.iothub.IotHubStatusCode;
import com.microsoft.azure.iothub.IotHubEventCallback;
import com.microsoft.azure.iothub.IotHubMessageResult;

import com.microsoft.azure.iot.service.exceptions.IotHubException;
import com.microsoft.azure.iot.service.sdk.Device;
import com.microsoft.azure.iot.service.sdk.RegistryManager;
import com.microsoft.azure.eventhubs.*;
import com.microsoft.azure.servicebus.*;
import java.io.IOException;
import java.net.URISyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.google.gson.Gson;

import java.util.Properties;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import java.nio.charset.Charset;
import java.time.*;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;
import java.util.function.*;
import java.util.logging.*;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;


public class DeleteModule extends GatewayModule { 
	private boolean threadStop;
	Context context = null;
	public static Session session = null;
	public static Connection connection = null;
	public static MessageProducer producer = null;
	Queue queue = null;
	String jsonobj = "JJM";
	int numberofThread=0;
	
	public DeleteModule(long address, Broker broker, String configuration) {
		super(address, broker, configuration);
		//this.threadStop = false;
		System.out.println("ReadModule Senser Module Constructor.....: ");
		
	}

	public void deQueueMessageFromQpid(){
		System.out.println(" Message deleted...");
		Properties properties = new Properties();
		
		try {
		//	Queue destination = session.createQueue("your_q");

		//	QueueBrowser browser = session.createBrowser(destination);

		//	Enumeration<?> enum1 = browser.getEnumeration();

			//while(enum1.hasMoreElements())
			//{
			 //  TextMessage msg = (TextMessage)enum1.nextElement();
			/*   if(msg.getStringProperty("any_prop").equals("some_prop"))
			   {
			       MessageConsumer consumer = session.createConsumer(destination, "id='" +   msg.getStringProperty("id") + "'");
			      consumer.receive(1000);*/
			 //  }
			//}
			properties.load(new FileInputStream("C:/Configurationfiles/config.properties"));
			Context context = new InitialContext(properties);
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("qpidConnectionFactory");
			connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
			Queue queue = (Queue) context.lookup("myqueue");
			String nothd = (String) context.lookup("numberofThread");
			numberofThread = Integer.parseInt(nothd);

			 
			//connection = connectionFactory.createConnection();
			 // session = connection.createSession(true,-1);
			//  Queue queue = (Queue) QueueConnectionFactory.getInitialContext().lookup("/queue/DLQ");
			  QueueBrowser browser = session.createBrowser(queue);
			  Enumeration<?> enum1 = browser.getEnumeration();

			  while(enum1.hasMoreElements()) {
			    TextMessage msg = (TextMessage)enum1.nextElement();
			    MessageConsumer consumer = session.createConsumer(queue, "JMSMessageID='" +  msg.getJMSMessageID()  + "'");
			    //You can try starting the connection outside while loop as well, I think I started it inside while loop by mistake, but since this code worked I am hence letting you know what worked  
			   // connection.start();
			    javax.jms.Message message = consumer.receive(1000) ;
			    if ( message != null ) {
			        //do something with message
			    }
			  }
			} 
			finally {
			 session.commit();			  
			DeviceClient consumer;
			consumer.close();			
//			browser.close();
			session.close();
			connection.close();
			}

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
		//this.threadStop = true;
	}

}
