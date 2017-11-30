



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WritetologFile {


	private static final String FILENAME = "C:\\MRSLogs\\OCM-01A\\Wabtec$\\OCM\\Diagnostics\\Livelog.txt";

	public static void main(String[] args) throws Throwable {
    boolean wflag=true;
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			int cout=0;
			
			      
			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			while(wflag){
				Thread.sleep(1000);
				  Date date = new Date();
			        long timeInMilliSeconds = date.getTime();
			    			        SimpleDateFormat ft =  new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			    			        String datenow = ft.format(date);
			        System.out.println("Current Date: " + datenow);
			StringBuffer content = new StringBuffer("06/27/2017 05:00:00.0117985 UTC-03:00,Info,Link,RX,mrs.b:03.0003,mrs.l.mrs.7266:itc,1021,0,04-03-FD-03-09-00-00-15-00-00-00-00-59-52-10-7F-25-00-3B-00-30-6D-72-73-2E-62-3A-30-33-2E-30-30-30-33-00-6D-72-73-2E-6C-2E-6D-72-73-2E-37-32-36-36-3A-69-74-63-00-4D-52-53-20-FB-A8-04-1E-B9-23-F2-9A-01-00-02-01-A8-59-68-5D-00-0E-92-FA-4C") ;
			//content.append(datenow);
			//content.append("\n");
				
				cout++;
				String contentwrite = content.toString();
				System.out.println("wrtting to file :----"+contentwrite +"\n\n\n");
			bw.write(contentwrite);
			System.out.println(datenow+"----line: "+ cout +":" +content  );
			}
			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}

}