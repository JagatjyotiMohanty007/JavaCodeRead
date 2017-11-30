package sendQpid;

import java.util.Properties;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;


public class Hello implements Runnable{

    public Hello() {
    }
    public static int messageCount = 0;
    
    public void run()
	   {
	 
	      //Looping from 1 to 10 to display numbers from 1 to 10
	      for ( int i=1; messageCount<=10; i++)
	      {
	         System.out.println( "Messag from Second Thread : " +i);
	 
	        /*taking a delay of one second before displaying next number
	         *
	         * "Thread.sleep(1000);" - when this statement is executed,
	         * this thread will sleep for 1000 milliseconds (1 second)
	         * before executing the next statement.
	         *
	         * Since we are making this thread to sleep for one second,
	         * we need to handle "InterruptedException". Our thread
	         * may throw this exception if it is interrupted while it
	         * is sleeping.
	         */
	         try
	         {
	             Thread.sleep(1000);
	         }
	         catch (InterruptedException interruptedException)
	         {
	            /*Interrupted exception will be thrown when a sleeping or waiting
	             * thread is interrupted.
	             */
	             System.out.println( "Second Thread is interrupted when it is sleeping" +interruptedException);
	         }
	      }
	    }
    	
    	
    public static void main(String[] args) throws Exception {
        Hello hello = new Hello();
        hello.runTest();
    }

    /**
     * @throws Exception
     */
    /**
     * @throws Exception
     */
    private void runTest() throws Exception {
        System.out.println("qpid_message --- start");
      Properties properties = new Properties();
         System.out.println("qpid_message --- step1");
      properties.load(this.getClass().getResourceAsStream("Hello.Properties"));
           System.out.println("qpid_message --- step2");
                  Context context = new InitialContext(properties);  
          System.out.println("qpid_message --- step3");
        //   System.out.println(context +"qpid_message --- start");
	
       // String  CF_LOOKUP_NAME = "qpidConnectionFactory";
       ConnectionFactory connectionFactory
          = (ConnectionFactory)context.lookup("qpidConnectionFactory");
        
     // ConnectionFactory connectionFactory = (ConnectionFactory)context.lookup("CF_LOOKUP_NAME"); 
        System.out.println("qpid_message --> step4");
      Connection connection = connectionFactory.createConnection();                   
      connection.start(); 
     
      Session session = connection.createSession(true,Session.SESSION_TRANSACTED);
      Queue queue = (Queue)context.lookup("myqueue");
      MessageProducer producer = session.createProducer(queue);
      
          TextMessage message = session.createTextMessage();
          
          message.setText("TestMessageas JAGAT gIRISHA");
          for (int i=1;i<100000; i++){
          producer.send(message);
          }
          session.commit();
          System.out.println("qpid_message Sent");
          
//          MessageConsumer consumer= session.createConsumer(queue);
//          TextMessage message1 = (TextMessage)consumer.receive();
//          session.commit();
//         
//          System.out.println(message1.getText());
//          System.out.println("qpid_message Received"); 
//          
//          System.out.println(consumer.toString()); 
      session.close();
      connection.close();                                                              
    }

}
