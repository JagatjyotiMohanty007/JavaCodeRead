package javademo;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.print.event.PrintJobAttributeEvent;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.microsoft.windowsazure.core.utils.KeyStoreType;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.management.*;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.management.configuration.ManagementConfiguration;
import com.microsoft.windowsazure.management.models.LocationsListResponse;
import com.microsoft.windowsazure.management.models.LocationsListResponse.Location;
public class Demo {
	static String uri = "https://jmohanty.documents.azure.com:443/";
	  static String subscriptionId = "e2186114-ebc1-4579-87b0-d455b33cd6cf";
	  static String keyStoreLocation = "c:\\certificates\\AzureJavaDemo.jks";
	  static String keyStorePassword = "JMohantyCertPW";

	  public static void main(String[] args) throws IOException, URISyntaxException, ServiceException, ParserConfigurationException, SAXException {
	    Configuration config = ManagementConfiguration.configure(
	      new URI(uri), 
	      subscriptionId,
	      keyStoreLocation, // the file path to the JKS
	      keyStorePassword, // the password for the JKS
	      KeyStoreType.jks // flags that I'm using a JKS keystore
	    );

	    // create a management client to call the API
	    ManagementClient client = ManagementService.create(config);

	    // get the list of regions
	    LocationsListResponse response = client.getLocationsOperations().list();
	    ArrayList locations = response.getLocations();

	    // write them out
	    for( int i=0; i<locations.size(); i++){
	      System.out.println(((Location) locations.get(i)).getDisplayName());
	    }
	  }
}
