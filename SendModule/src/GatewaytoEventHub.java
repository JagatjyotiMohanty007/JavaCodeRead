


import com.google.gson.Gson;
import com.microsoft.azure.gateway.core.*;
import com.microsoft.azure.gateway.messaging.*;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;

//Include the following imports to use Service Bus APIs
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
//import com.microsoft.windowsazure.services.servicebus.models.ReceiveMessageOptions;
//import com.microsoft.windowsazure.services.servicebus.models.ReceiveMode;
//import com.microsoft.windowsazure.services.servicebus.models.ReceiveQueueMessageResult;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;

public class GatewaytoEventHub  extends GatewayModule {
	private com.microsoft.azure.sdk.iot.device.DeviceClient client;
	//private static JSONObject jObject = null;
	String jsonobj = null;
	static String IoTackStatus = "";
	ServiceBusContract service=null;
	String sendDataTo = null;
	String srvBusQueueName = null;
	String servBusPrimaryKey = null;
	String iotHubConnString = null;

	public void sendToBroker(String status, String msgID) {
		try{
			Gson gson = new Gson();
			// convert java object to JSON format
			jsonobj = gson.toJson(status);
			System.out.println("Send Module : IoT ACK: Received from IoT HUB:" + jsonobj);
			HashMap<String, String> map = new HashMap<String,String>();
			map.put("FrmSM_AzureIoTack",msgID);
			Message toBroker = new Message(status.getBytes(),map);
			// Publish message to broker
			publish(toBroker);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Send  Module .....:IOException ");
			e.printStackTrace();
		}  
	}

	protected class EventCallback
	implements IotHubEventCallback
	{
		public void execute(com.microsoft.azure.sdk.iot.device.IotHubStatusCode status, Object context)
		{
			System.out.println("Send module callbk : IoT Hub responded to msg ID" + context.toString());
			sendToBroker(status.name(), context.toString());
		}
	}

	public GatewaytoEventHub (long address, Broker broker, String configuration) {
		super(address, broker, configuration);
		System.out.println("*************************************************");
		System.out.println("** IoT Send Module Started");
		System.out.println("*************************************************");
		Properties properties = new Properties();
		/*JsonElement jelement = new JsonParser().parse(configuration);
		JsonObject  jobject = jelement.getAsJsonObject();*/
		//Connection connection = null;
		//Read Configurations from GwConfig.Properties file
		try {
			properties.load(new FileInputStream("C:/ProgramData/Wabtec/GwConfig.Properties"));
			sendDataTo = properties.getProperty("sendDataTo");
			iotHubConnString = properties.getProperty("IoThubConnString");
			servBusPrimaryKey = properties.getProperty("SBconnPrymaryKey");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if(sendDataTo.contentEquals("IoThub")) {
			System.out.println("Send Module : IoT Hub has been selected to send the message");
			// Read the args configured in gw-config.json for IoT hub writer
			/*String IoThubParameter = jobject.get("IoTDeviceConnectionString").toString();
			String iotHubConnectionString = null;
			iotHubConnectionString = IoThubParameter.substring(1);*/
			System.out.println("Send Module : IoT Hub Conn String : "+ iotHubConnString);
			try
			{
				client = new com.microsoft.azure.sdk.iot.device.DeviceClient(iotHubConnString, IotHubClientProtocol.AMQPS);// You can also use MQTT protocol here.
				client.open();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		} else if (sendDataTo.contentEquals("ServiceBus")) {
			// Read the args configured in gw-config.json for IoT hub writer
			System.out.println("Send Module : Service Bus has been selected to send the message");
			/*String serviceBusPkey = null;
			String serviceBusParameter = jobject.get("ServiceBusPrimaryKey").toString();
			serviceBusPkey = serviceBusParameter.substring(1);*/
			System.out.println("Send Module : ServiceBus Primary Key : "+ servBusPrimaryKey);
			srvBusQueueName = properties.getProperty("SBqueueName");
			System.out.println("Send Module : Queue Name : "+ srvBusQueueName);
			//Creating Service bus connection
			try
			{
				Configuration config = ServiceBusConfiguration.configureWithSASAuthentication("SwanupNamespace","RootManageSharedAccessKey",servBusPrimaryKey, ".servicebus.windows.net" );
				service = ServiceBusService.create(config);
			}
			catch (Exception e)
			{
				System.out.println("Send Module : Error In ServiceBus Connection...");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void start() {


	}

	@Override
	public void receive(Message message) {
		BrokeredMessage msgForServiceBus = null;
		System.out.println("\nSend Module : Received msg from ReadModule : " + message.toString());
		if(sendDataTo.contentEquals("IoThub")) { 
			System.out.println("Send Module : Data is sending to IoT hub..");
			com.microsoft.azure.sdk.iot.device.Message msg = new com.microsoft.azure.sdk.iot.device.Message(message.toString());
			EventCallback callback = new EventCallback();
			client.sendEventAsync(msg, callback, message.getProperties().get("Qpid_msg"));
		} else if(sendDataTo.contentEquals("ServiceBus")) { 
			//Sending data to service bus
			try {
				msgForServiceBus = new BrokeredMessage(message.getContent());
				service.sendQueueMessage(srvBusQueueName, msgForServiceBus);
				sendToBroker("SERVICE_BUS_SUCCESS", message.getProperties().get("Qpid_msg"));
				
			} catch (ServiceException e) {
				System.out.println("Send Module : Msg send failed to Service bus [ Msg ID : "+message.getProperties().get("Qpid_msg")+" ]" );
				//Retry the same message after 1 min.
				sendToBroker("SERVICE_BUS_ERROR", message.getProperties().get("Qpid_msg"));
				System.out.println(e.getMessage());
				//System.exit(-1);
			} 
		}
	}

	@Override
	public void destroy() {
		// Since the time in start() is a daemon, the process will automatically
		// cancel it. Therefore we don't need to terminate the timer here.
	}
}


