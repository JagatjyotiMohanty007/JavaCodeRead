/* ----------------------------------------------------------------------------- *
 * This Sample Code is provided for  the purpose of illustration only and is not * 
 * intended  to be used in a production  environment.  THIS SAMPLE  CODE AND ANY * 
 * RELATED INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER * 
 * EXPRESSED OR IMPLIED, INCLUDING  BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF * 
 * MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.                      * 
 * ----------------------------------------------------------------------------- */


//package com.microsoft.azure.gateway;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.azure.gateway.core.*;
import com.microsoft.azure.gateway.messaging.*;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import org.json.simple.JSONObject;

public class iotWriter  extends GatewayModule {
    private com.microsoft.azure.sdk.iot.device.DeviceClient client;
    private static JSONObject jObject = null;
    
    protected static class EventCallback
            implements com.microsoft.azure.sdk.iot.device.IotHubEventCallback
    {
        public void execute(com.microsoft.azure.sdk.iot.device.IotHubStatusCode status, Object context)
        {
            Integer i = (Integer) context;
            System.out.println("IoT Hub responded to message " + i.toString()  + " with status " + status.name());
        }
    }
    
    public iotWriter (long address, Broker broker, String configuration) {
        super(address, broker, configuration);
        System.out.println("*************************************************");
        System.out.println("** My IoT Writer Module Started");
        System.out.println("*************************************************");
        
        // Read the args configured in gw-config.json for IoT hub writer
        System.out.println("Configuration : " + configuration);
        JsonElement jelement = new JsonParser().parse(configuration);
        JsonObject  jobject = jelement.getAsJsonObject();
        
        String result1 = jobject.get("DeviceConnectionString1").toString();
        String result2 = jobject.get("DeviceConnectionString2").toString();
        
       // Get 1st device connection string listed in gw-config.json file and use that to send data to Azure IoT Hub.
        String deviceConnectionString1;
        deviceConnectionString1 = result1.substring(1);
                
        System.out.println("Device connection strings are " + deviceConnectionString1 + "  " + result2.substring(1));
        
        System.out.println("IoT:Start");
        System.out.println("Sending messsages to " + deviceConnectionString1 +  " using AMQPS protocol" );
       
        try
        {
         client = new com.microsoft.azure.sdk.iot.device.DeviceClient(deviceConnectionString1, IotHubClientProtocol.AMQPS);// You can also use MQTT protocol here.
         client.open();
        }
        
       catch (Exception e)
       {
           e.printStackTrace();
       }
    }

    @Override
    public void start() {
       
       
    }

    @Override
    public void receive(Message message) {
        System.out.println("IoT:Received message from Gateway via Timer module and publish that message using device id registered in Azure IoT Hub");
        com.microsoft.azure.sdk.iot.device.Message msg = new com.microsoft.azure.sdk.iot.device.Message(message.toString());
        EventCallback callback = new EventCallback();
        client.sendEventAsync(msg, callback, 0);
    }

    @Override
    public void destroy() {
        // Since the time in start() is a daemon, the process will automatically
        // cancel it. Therefore we don't need to terminate the timer here.
    }
}
