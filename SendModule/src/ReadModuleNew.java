
/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
//import java.util.Enumeration;
import java.util.Properties;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.google.gson.Gson;
import com.microsoft.azure.gateway.core.Broker;
import com.microsoft.azure.gateway.core.GatewayModule;
import com.microsoft.azure.gateway.messaging.Message;

public class ReadModuleNew extends GatewayModule {

	//private boolean threadStop;
	long address=0;
	Broker broker=null;
	Context context = null;
	HashSet<String> ackMsgIdSet=new HashSet<String>();  
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
	public ReadModuleNew(long address, Broker broker, String configuration) {
		super(address, broker, configuration);

		//this.threadStop = false;
		System.out.println("*************************************************");
		System.out.println("** IoT Read Module Started");
		System.out.println("*************************************************");
	}

	class ReadMsgFrmQpidQueue extends Thread { 
		private String jsonobj = null;
		private Session tsession = null;
		private MessageConsumer consumerForQpidQ = null;
		private String msg = null;
		private TextMessage message = null;
		private String t_qpidQueueMsgID = null;
		private Connection tConToQpid = null;
		private Queue tQpidQ = null;
		private boolean ack_got = false;


		public ReadMsgFrmQpidQueue(Connection Conn, Queue QpidQ){
			tConToQpid = Conn;
			tQpidQ = QpidQ;
		}

		/* 
	    Logic for
	    > Create Session.
	    > Create Consumer
	    > receiving the message from qpid queue
	    > Publish the same message to Send Module
	    > It will Wait for the ack from SendModule.
	    > After getting the ack it will send back the ack to qpid queue to delete the message. */
		@Override
		public void run() {

			try {
				tsession = tConToQpid.createSession(true, Session.SESSION_TRANSACTED);
				consumerForQpidQ= tsession.createConsumer(tQpidQ);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			System.out.println("Thread Name : " + Thread.currentThread().getName());

			try {
				while (true) {	
					if(!ack_got) {
						message = (TextMessage)consumerForQpidQ.receive();
						msg = message.getText().toString();
						Gson gson = new Gson();
						jsonobj = gson.toJson(msg);
						System.out.println("\n***********************************************");
						System.out.println("Read Module : Msg Rcv Time:"+System.currentTimeMillis());
						System.out.println("Read Module : Received msg from Qpid Queue :->\n"+jsonobj);
						System.out.println("Read Module : Msg" + message.getJMSMessageID());	
						HashMap<String, String> mapMsgProperties = new HashMap<>();
						mapMsgProperties.put("Qpid_msg", message.getJMSMessageID());
						Message msgToSendModule = new Message(jsonobj.toString().getBytes(),mapMsgProperties);
						// Publish message
						publish(msgToSendModule);
						//Write to set
						t_qpidQueueMsgID = message.getJMSMessageID();
						//ackGot = false;
						ack_got = true;
					}

					if(ackMsgIdSet.contains(t_qpidQueueMsgID))
					{
						System.out.println("Read Module : Msg Rcv Time:"+System.currentTimeMillis());
						tsession.commit();
						ackMsgIdSet.remove(t_qpidQueueMsgID);
						System.out.println("Read Module : deleted Msg " + t_qpidQueueMsgID);
						System.out.println("***********************************************");
						ack_got= false;
						//						break;
					} else {
						Thread.sleep(2);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					consumerForQpidQ.close();
					tsession.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}


	}




	@Override
	public void start() {

		//ExecutorService executor = null;
		Properties properties = new Properties();
		Connection connection = null;
		Queue queue = null;

		try {
			properties.load(new FileInputStream("C:/Configurationfiles/config.properties"));
			Context context;
			context = new InitialContext(properties);
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("qpidConnectionFactory");
			queue = (Queue) context.lookup("myqueue");
			connection = connectionFactory.createConnection();
			connection.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} 

		for(int i = 0; i<8; i++)
		{
			ReadMsgFrmQpidQueue t1 = new ReadMsgFrmQpidQueue(connection,queue);
			t1.start();
		}
	}

	@Override
	public void receive(Message message) {
		String ackMsgID = "";
		// Ignores incoming messages
		System.out.println("\nRead Module : Received from Send Module:  " + message);

		ackMsgID = message.getProperties().get("frm Send ack");
		System.out.println("Read Module : msg ID from send : " + ackMsgID);
		//System.out.println("Read Module : msg ID from send: " + message.getProperties());

		if(new String(message.getContent()).equals("\"OK_EMPTY\"")) {
			System.out.println("ACK ok");
			ackMsgIdSet.add(ackMsgID);
			System.out.println("hset value:" + ackMsgIdSet);
		} else {
			System.out.println("ACK ERROR");
		}
	}

	@Override
	public void destroy() {
		// Causes the publishing thread to stop
		System.out.println("ReadModule Module Destroy.....: ");
		//this.threadStop = true;
	}
}