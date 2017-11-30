//package com.microsoft.azure.gateway.sample;
/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

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
import com.microsoft.azure.iothub.IotHubStatusCode;
import com.microsoft.azure.iothub.IotHubEventCallback;
import com.microsoft.azure.iothub.IotHubMessageResult;
import java.io.IOException;
import java.net.URISyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.*;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.function.*;
import java.util.logging.*;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;


public class SendModuleNew extends GatewayModule {
//implements Runnable{
	
	private static DeviceClient client;
	
	private static class EventCallback implements IotHubEventCallback
	{
	public void execute(IotHubStatusCode status, Object context) {
    System.out.println("IoT Hub responded to message with status: " + status.name());

    if (context != null) {
      synchronized (context) {
        context.notify();
      }
 
    }
	}}
	
    /**
     * Constructs a {@link GatewayModule} from the provided address and {@link Broker}. A {@link GatewayModule} should always call this super
     * constructor before any module-specific constructor code.
     *
     * @param address       The address of the native module pointer
     * @param broker        The {@link Broker} to which this module belongs
     * @param configuration The module-specific configuration
     */
	 public SendModuleNew(long address, Broker broker, String configuration) {
	        super(address, broker, configuration);
			System.out.println("SendModule Module constructor: ");
			String deviceConnectionSting;
			deviceConnectionSting= configuration.substring(22);
			
    
     System.out.println("Configuration : " + configuration);
     JsonElement jelement = new JsonParser().parse(configuration);
     JsonObject  jobject = jelement.getAsJsonObject();
     String      deviceConnectionSting1  = jobject.get("connection_String").toString();
     //String result2 = jobject.get("DeviceConnectionString2").toString();
     
    // Get 1st device connection string listed in gw-config.json file and use that to send data to Azure IoT Hub.
    // String deviceConnectionString;
     //deviceConnectionString = result.substring(1);
             
     System.out.println("Device connection strings are Jagatjyoti " + deviceConnectionSting1 + "  " + deviceConnectionSting1.substring(1));
     
     System.out.println("IoT:Start");
     System.out.println("Sending messsages to by Jagat" + deviceConnectionSting +  " using AMQPS protocol" );
			System.out.println("IoT : Start");
			try{
			
			client = new DeviceClient(deviceConnectionSting, IotHubClientProtocol.AMQPS);
			  client.open();
			}catch(URISyntaxException ex){
				System.out.println("URI exceptiom");
			}
			catch(IOException e){
				System.out.println("IOException exceptiom");
			}catch(Exception exp){
				System.out.println("exp exception");
			}
	    }
	 @Override
	    public void start() {
	       
	       
	    }
	

	    @Override
	    public void receive(Message message) {
	        System.out.println("SendModule Module Received: " + message);
			//send to IOT
	       /*com.microsoft.azure.gateway.messaging.Message m = new com.microsoft.azure.gateway.messaging.Message(new Date().toString().getBytes(), null);
	        try {
	        	SendModule.this.publish(m);
            } catch (Exception e) {
                e.printStackTrace();
            } */
	        try {
	  //      com.microsoft.azure.sdk.iot.device.Message msg1 = new com.microsoft.azure.sdk.iot.device.Message(message.toString());
	        
	        com.microsoft.azure.iothub.Message msg = new com.microsoft.azure.iothub.Message(message.toString());
	        msg.setMessageId(java.util.UUID.randomUUID().toString());
	        Object lockobj = new Object();
	        EventCallback callback = new EventCallback();
	        client.sendEventAsync(msg, callback, lockobj);
	        synchronized (lockobj) {
	            lockobj.wait();
	          }
	          Thread.sleep(1000);
	        } catch (InterruptedException e) {
	            System.out.println("Finished.");
	        }
	       
	    }

	    @Override
	    public void destroy() {
			 System.out.println("SendModule Module destroy: ");
	        //No cleanup necessary
	    }
	}
