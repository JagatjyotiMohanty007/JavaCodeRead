package sendQpid;
import com.google.gson.Gson;
import java.lang.*;
public class Message {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 
		String msg ="{"
				 + " \"railroad\":\"MRS\",\"lastPollTime\":\"2017-03-31T19:09:23.315315-04:00\","
				 + " \"unitId\":\"7\",\"unitIP\":\"10.28.56.66\",\"unitDesc\":\"P1-10 (3) / EE-SD-FA-0FRC-257 (KM 257+768)\",\""
				 + "  \"wiuAddress\":\"712510001510\",\"ioSlotList\":[{\"cardIdx\":\"1\",\"trackCurrentList\":[{\"trackName\":\"2E_C1_IN\",\"rxCurrentAmps\":\"0.05\",\"txCurrentAmps\":\"0.45\"}]},\"\""
				 + " {\"cardIdx\":\"2\",\"trackCurrentList\":[{\"trackName\":\"4D_C1_IN\",\"rxCurrentAmps\":\"2.53\",\"txCurrentAmps\":\"2.48\"}\",\""
			     + " {\"trackName\":\"2D_C1_IN\",\"rxCurrentAmps\":\"2.52\",\"txCurrentAmps\":\"2.45\"}]}]}\"";

		System.out.println("Messages ="+msg);
		String msg1 ="{"
				+ " \"messageDescription\":\"FDH_TELEMETRIES\",\"railroad\":\"MRS\",\"lastPollTime\":\"2017-04-07T10:49:59.1993102-04:00\","
				+ " \"unitId\":\"4\",\"unitIP\":\"10.28.56.43\",\"unitDesc\":\"P1-10 (2) / EE-SD-FA-0FRC-250 (KM 250+853)\","
				+ " \"wiuAddress\":\"712510001410\",\"ioSlotList\":[{\"cardIdx\":\"1\",\"trackCurrentList\":[{\"trackName\":\"2D_C1_IN\",\"rxCurrentAmps\":\"0.05\",\"txCurrentAmps\":\"0.15\"},{\"trackName\":\"4D_C1_IN\",\"rxCurrentAmps\":\"0.05\",\"txCurrentAmps\":\"0.15\"}]},{\"cardIdx\":\"2\",\"trackCurrentList\":[{\"trackName\":\"2E_C1_IN\",\"rxCurrentAmps\":\"0.05\",\"txCurrentAmps\":\"0.07\"},{\"trackName\":\"4E_C1_IN\",\"rxCurrentAmps\":\"2.45\",\"txCurrentAmps\":\"2.43\"}]}]}\" ";

		System.out.println("Message1"+msg1);
		 Gson gson = new Gson();

	      //convert java object to JSON format
	      String json = gson.toJson(msg1);
	      System.out.println("Json object is ="+json);
	      json = json.replaceAll("\\\\"," ");
	      System.out.println("Json object is ="+json);

	}

}
