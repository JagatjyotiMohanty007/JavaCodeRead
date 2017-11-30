package com.wone.mrsfileread;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
//import org.apache.commons.io.FileUtils.cleanDirectory;

public class DeleteMRSFolder {

		// TODO Auto-generated constructor stub
	
		
		  private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		    static final long MILLIS_IN_A_DAY = 1000*60*60*24;
		    public static void main(String[] args) {

		        Date date = new Date();
		        System.out.println(sdf.format(date));

		        Calendar cal = Calendar.getInstance();
		        System.out.println(sdf.format(cal.getTime()));

		        LocalDateTime now = LocalDateTime.now();
		        System.out.println(dtf.format(now));

		        LocalDate localDate = LocalDate.now();
		        System.out.println(DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate));

		        Date curDate = new Date();
				SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/YYYY");
				String DateToStr1 = format1.format(curDate);
				System.out.println("MM/dd/YYYY =" + DateToStr1);

				SimpleDateFormat format2 = new SimpleDateFormat("MM-dd-YYYY");
				String DateToStr2 = format2.format(curDate);
				System.out.println("MM-dd-YYYY=" + DateToStr2);
			
				String deletecbtclogs ="C:\\MrsDestinationlogs\\CBTC_TWE_LOGS";
				String deleteocmlogs ="C:\\MrsDestinationlogs\\OCM_LOGS";
				
				Date date1 = new Date(); // Or where ever you get it from
				Date yesterday = new java.sql.Date(new java.util.Date().getTime() - MILLIS_IN_A_DAY);
				//String yesterdayStr = yesterday.toString();
				System.out.println("today date ="+date1);
				System.out.println("Yesterday date ="+yesterday);
				
				
			
				String yesterdayStr = yesterday.toString();
				System.out.println("yesterdayStr. datee to sting date."+yesterdayStr);
				String yr = yesterdayStr.substring(0,4);
				String month = yesterdayStr.substring(5,7);
				String day = yesterdayStr.substring(8,10);
				System.out.println("yr..."+yr + "month.."+month + "day.."+day);
				
				DeleteMRSFolder obj = new DeleteMRSFolder();
				try {
					obj.deleteDirectory(deletecbtclogs);
					//obj.deleteDirectory(deleteocmlogs);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
		    }
		    public  void deleteDirectory(String directoryFilePath) throws IOException
		    {
		        Path directory = Paths.get(directoryFilePath);

		        if (Files.exists(directory))
		        {
		            Files.walkFileTree(directory, new SimpleFileVisitor<Path>()
		            {
		                @Override
		                public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException
		                {
		                    Files.delete(path);
		                    return FileVisitResult.CONTINUE;
		                }

		                public FileVisitResult postVisitDirectory(Path directory, IOException ioException) throws IOException
		                {
		                    Files.delete(directory);
		                    return FileVisitResult.CONTINUE;
		                }
		            });
		        }
		    }
}
