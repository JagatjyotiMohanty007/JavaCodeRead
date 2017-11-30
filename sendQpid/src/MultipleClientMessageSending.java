

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class MultipleClientMessageSending implements Runnable{
	public MultipleClientMessageSending(){
		
	}
	 Context context = null;
	public static Session session =null;
	 public static  Connection connection =null;
	public static MessageProducer producer = null;
	public int messagecount =10;
	 public void run()
	   {
		 TextMessage message;
		try {
			message = session.createTextMessage();
			 for ( int i=1; i<=messagecount; i++)
		      { 
				 
			String msg ="{"
					 + " \"railroad\":\"MRS\",\"lastPollTime\":\"2017-03-31T19:09:23.315315-04:00\","
					 + " \"unitId\":\"7\",\"unitIP\":\"10.28.56.66\",\"unitDesc\":\"P1-10 (3) / EE-SD-FA-0FRC-257 (KM 257+768)\",\""
					 + "  \"wiuAddress\":\"712510001510\",\"ioSlotList\":[{\"cardIdx\":\"1\",\"trackCurrentList\":[{\"trackName\":\"2E_C1_IN\",\"rxCurrentAmps\":\"0.05\",\"txCurrentAmps\":\"0.45\"}]},\"\""
					 + " {\"cardIdx\":\"2\",\"trackCurrentList\":[{\"trackName\":\"4D_C1_IN\",\"rxCurrentAmps\":\"2.53\",\"txCurrentAmps\":\"2.48\"}\",\""
				     + " {\"trackName\":\"2D_C1_IN\",\"rxCurrentAmps\":\"2.52\",\"txCurrentAmps\":\"2.45\"}]}]}\"";
 
			
			String msg1 ="{"
					+ " \"messageDescription\":\"FDH_TELEMETRIES\",\"railroad\":\"MRS\",\"lastPollTime\":\"2017-04-07T10:49:59.1993102-04:00\","
					+ " \"unitId\":\"4\",\"unitIP\":\"10.28.56.43\",\"unitDesc\":\"P1-10 (2) / EE-SD-FA-0FRC-250 (KM 250+853)\","
					+ " \"wiuAddress\":\"712510001410\",\"ioSlotList\":[{\"cardIdx\":\"1\",\"trackCurrentList\":[{\"trackName\":\"2D_C1_IN\",\"rxCurrentAmps\":\"0.05\",\"txCurrentAmps\":\"0.15\"},{\"trackName\":\"4D_C1_IN\",\"rxCurrentAmps\":\"0.05\",\"txCurrentAmps\":\"0.15\"}]},{\"cardIdx\":\"2\",\"trackCurrentList\":[{\"trackName\":\"2E_C1_IN\",\"rxCurrentAmps\":\"0.05\",\"txCurrentAmps\":\"0.07\"},{\"trackName\":\"4E_C1_IN\",\"rxCurrentAmps\":\"2.45\",\"txCurrentAmps\":\"2.43\"}]}]}\" ";
					
		    message.setText(msg);
		         producer.send(message);
		         session.commit();	          
		         System.out.println("Jagat and Girish message has been tested sucessfully to the queue"+i +msg);
		      }
		} catch (Exception e) {
			try {
				session.close();
			} catch (JMSException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			 try {
				connection.close();
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			e.printStackTrace();
		}	
	      
	    
	        
	      }
	    
	 

	 Session  getConnection() throws Exception {
	       
	      Properties properties = new Properties();	        
	      properties.load(this.getClass().getResourceAsStream("Hello.Properties"));	          
	      Context context = new InitialContext(properties); 
	      ConnectionFactory connectionFactory  = (ConnectionFactory)context.lookup("qpidConnectionFactory");
	       connection = connectionFactory.createConnection();                   
	      connection.start();
	 	 Session session = connection.createSession(true,Session.SESSION_TRANSACTED);
	     Queue queue = (Queue)context.lookup("myqueue");
		 producer = session.createProducer(queue);
	     
	      return session;
	     
	                                              
	    }
	 public static void main(String args[])throws Exception 
     {
        
		 MultipleClientMessageSending obj = new MultipleClientMessageSending();
		  session = obj.getConnection();
	                     
	        //Creating an object of the first thread
		 MultipleClientMessageSending   obj1 = new MultipleClientMessageSending();
 
        //Creating an object of the Second thread
     
		 MultipleClientMessageSending   obj2 = new MultipleClientMessageSending();
        //Starting the first thread
        Thread jagat = new Thread(obj1);
        jagat.start();
        System.out.println("Jagat started sending messages to the Queue");
 
        //Starting the second thread
        Thread girish = new Thread(obj2);
        girish.start();
        System.out.println("Girish started sending messages to the Queue");
        
     }	
	

}
 