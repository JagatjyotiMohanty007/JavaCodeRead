package javademo;
import com.microsoft.windowsazure.services.servicebus.*;
import com.microsoft.windowsazure.services.servicebus.models.*;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.core.*;
import com.microsoft.windowsazure.exception.ServiceException;

import javax.xml.datatype.*;

public class ServiceBusService {

	public static void main(String args[]){
		//2222
		long maxSizeInMegabytes = 5120;  
		TopicInfo topicInfo = new TopicInfo("TestTopic");  
		topicInfo.setMaxSizeInMegabytes(maxSizeInMegabytes);
		CreateTopicResult result = service.createTopic(topicInfo);
		//3333
		SubscriptionInfo subInfo = new SubscriptionInfo("AllMessages");
		CreateSubscriptionResult result =
		    service.createSubscription("TestTopic", subInfo);
		//444
		// Create a "HighMessages" filtered subscription  
		SubscriptionInfo subInfo = new SubscriptionInfo("HighMessages");
		CreateSubscriptionResult result = service.createSubscription("TestTopic", subInfo);
		RuleInfo ruleInfo = new RuleInfo("myRuleGT3");
		ruleInfo = ruleInfo.withSqlExpressionFilter("MessageNumber > 3");
		CreateRuleResult ruleResult = service.createRule("TestTopic", "HighMessages", ruleInfo);
		// Delete the default rule, otherwise the new rule won't be invoked.
		service.deleteRule("TestTopic", "HighMessages", "$Default");
		//5555
		// Create a "LowMessages" filtered subscription
		SubscriptionInfo subInfo = new SubscriptionInfo("LowMessages");
		CreateSubscriptionResult result = service.createSubscription("TestTopic", subInfo);
		RuleInfo ruleInfo = new RuleInfo("myRuleLE3");
		ruleInfo = ruleInfo.withSqlExpressionFilter("MessageNumber <= 3");
		CreateRuleResult ruleResult = service.createRule("TestTopic", "LowMessages", ruleInfo);
		// Delete the default rule, otherwise the new rule won't be invoked.
		service.deleteRule("TestTopic", "LowMessages", "$Default");
		//BrokeredMessage message = new BrokeredMessage("MyMessage");
		service.sendTopicMessage("TestTopic", message);
		//666
		for (int i=0; i<5; i++)  {
			// Create message, passing a string message for the body
			BrokeredMessage message = new BrokeredMessage("Test message " + i);
			// Set some additional custom app-specific property
			message.setProperty("MessageNumber", i);
			// Send message to the topic
			service.sendTopicMessage("TestTopic", message);
			}
		////777
		try
		{
		    ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
		    opts.setReceiveMode(ReceiveMode.PEEK_LOCK);

		    while(true)  {
		        ReceiveSubscriptionMessageResult  resultSubMsg =
		            service.receiveSubscriptionMessage("TestTopic", "HighMessages", opts);
		        BrokeredMessage message = resultSubMsg.getValue();
		        if (message != null && message.getMessageId() != null)
		        {
		            System.out.println("MessageID: " + message.getMessageId());
		            // Display the topic message.
		            System.out.print("From topic: ");
		            byte[] b = new byte[200];
		            String s = null;
		            int numRead = message.getBody().read(b);
		            while (-1 != numRead)
		            {
		                s = new String(b);
		                s = s.trim();
		                System.out.print(s);
		                numRead = message.getBody().read(b);
		            }
		            System.out.println();
		            System.out.println("Custom Property: " +
		                message.getProperty("MessageNumber"));
		            // Delete message.
		            System.out.println("Deleting this message.");
		            service.deleteMessage(message);
		        }  
		        else  
		        {
		            System.out.println("Finishing up - no more messages.");
		            break;
		            // Added to handle no more messages.
		            // Could instead wait for more messages to be added.
		        }
		    }
		}
		catch (ServiceException e) {
		    System.out.print("ServiceException encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
		catch (Exception e) {
		    System.out.print("Generic exception encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
		//88888
		// Delete subscriptions
		service.deleteSubscription("TestTopic", "AllMessages");
		service.deleteSubscription("TestTopic", "HighMessages");
		service.deleteSubscription("TestTopic", "LowMessages");

		// Delete a topic
		service.deleteTopic("TestTopic");

		//1111
	Configuration config =
		    ServiceBusConfiguration.configureWithSASAuthentication(
		      "HowToSample",
		      "RootManageSharedAccessKey",
		      "SAS_key_value",
		      ".servicebus.windows.net"
		      );

		ServiceBusContract service = ServiceBusService.create(config);
		TopicInfo topicInfo = new TopicInfo("TestTopic");
		try  
		{
		    CreateTopicResult result = service.createTopic(topicInfo);
		}
		catch (ServiceException e) {
		    System.out.print("ServiceException encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
	}

}
