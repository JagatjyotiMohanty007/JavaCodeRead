/**
* @author developer08 Jagatjyoti Mohanty Version 1.0 
* Email id: jmohanty.wabtec.com
* The code has been developed to move the MRS & OCM daily logs on hourly basis
* to a new destination.
* The source and destination may configured from out side in a configuration file.
*  Later if we need to add other source and destination other than MRS we can
*  go with the same code and we can read the property file and source destination and
*  pass it to the method.
*  Approch 2- Read data of last 1hr only and write to last 1hr folder.
*  
*/

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.google.gson.Gson;
import java.text.DateFormat;


public class Readcolumn {
	
	
	 	public static String getMonth(String month) {
	 		Map<String, String> map = new HashMap<String, String>();
	 		map.put("01", "JAN");
	 		map.put("02", "FEB");
	 		map.put("03", "MAR");
	 		map.put("04", "APR");
	 		map.put("05", "MAY");
	 		map.put("06", "JUN");
	 		map.put("07", "JUL");
	 		map.put("08", "AUG");
	 		map.put("09", "SEP");
	 		map.put("10", "OCT");
	 		map.put("11", "NOV");
	 		map.put("12", "DEC");
	 		String monthinletter = map.get(month);
	 		return monthinletter;
	 	}
	 static	boolean hr23= false;
	 	public static void main(String[] args) {	 		
			
			
	 		// TODO Auto-generated method stub
	 		String mrsSourceFolderLocation = "";
	 		String mrsLogDestinationFoderpath = "";
	 		try {
	 			Properties properties = new Properties();
	 			properties.load(new FileInputStream("C:/Configurationfiles/config.properties"));
	 			mrsSourceFolderLocation = (String) (properties.getProperty("Sourcefolderepath"));
	 			mrsLogDestinationFoderpath = (String) (properties.getProperty("Destinationfolderepath"));
	 			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 			Date now = new Date();
	 			String strDate = sdfDate.format(now);
	 			String[] ocmdate = strDate.split(" ");
	 			String filedateextn = ocmdate[0];	 			
	 			String  timepart23 = ocmdate[1];
	 			String time23 = timepart23.substring(0,2).toString();
	 			int hrfor23 = Integer.parseInt(time23);	 
	 			if(hrfor23==0){
	 				hr23=true;
	 				Readcolumn obj = new Readcolumn();
	 				filedateextn =obj.getYesterdayDateforfolderName();
	 			}
	 			String mrscbtctwelogsSourcePath = mrsSourceFolderLocation + "/" + "cbtctwelogs" + "/" + "CBTC_TWE_LOGS_" + filedateextn;
	 			String mrscbtctwelogsDestinationFoderpath = mrsLogDestinationFoderpath + "/" + "CBTC_TWE_LOGS";
	 			String ocmlogsSourcePath = mrsSourceFolderLocation + "/" + "OCM" + "/" + "OCM_LOGS_" + filedateextn;
	 			String ocmlogsDestinationFoderpath = mrsLogDestinationFoderpath + "/" + "OCM_LOGS";
	 			writeToDestination(mrscbtctwelogsSourcePath,mrscbtctwelogsDestinationFoderpath );
	 			writeToDestination(ocmlogsSourcePath,ocmlogsDestinationFoderpath );
	 			} 
	 			catch (IOException ex) {
	 			// TODO Auto-generated catch block
	 			ex.printStackTrace();
	 		} catch (Exception e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	 		
	 	}
	 	
	 public static void writeToDestination(String mrscbtctwelogsSourcePath, String mrscbtctwelogsDestinationFoderpath){
	/* 	boolean deleteflag = false;
	 	final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
	 	String deletecbtcDirectorypath = mrscbtctwelogsDestinationFoderpath;*/
		 Readcolumn obj = new Readcolumn();
	 	String list[] = obj.listofFilesandSubdirectoryInaDirectory(mrscbtctwelogsSourcePath);
	 	String date = obj.getlocalSystemdateforcreatingfolder();
	 	String time = obj.getlocalSystemTimeforcreatingfolder();
	 	if(hr23)
	 	{
	 		 date = obj.getlocalSystemdateforcreatingyesterdayfolder();//yesterdate date
		 	 //time = obj.getlocalSystemTimeforcreatingfolder();//yresterday time not needed
	 	}
	 	try {
	 		for (String filename : list) {
	 			System.out.println("File name = "+filename);

	 			String[] systemdateformatloc = date.split(":");
	 			String systemdateformat1 = systemdateformatloc[0];
	 			String mrscbtctwelogsdynamicfolderpath = obj.createDynamicMrsFolders(systemdateformat1, time,
	 					mrscbtctwelogsDestinationFoderpath);
	 			String mrscbtctwelogsSourcefilepathwithname = mrscbtctwelogsSourcePath + "/" + filename;
	 			String newfilename = filename.substring(0, filename.length() - 4);
	 			String newtime = time.substring(0, 2);
	 			int newtimehrdely = Integer.parseInt(newtime);
	 			if (newtimehrdely == 0) {
	 				newtimehrdely = 24;
	 				//newtimehrdely = newtimehrdely - 1;
	 				//deleteflag = true;
	 			}
	 			newtimehrdely = newtimehrdely - 1;
	 			String mrscbtctwelogsdynamicfolderpathwithfilename = mrscbtctwelogsdynamicfolderpath + "/"
	 					+ newfilename + "_" + newtimehrdely + ".txt";
	 			
	 			obj.readMrsSourceFileAndWritetoDestinationAssociation(mrscbtctwelogsSourcefilepathwithname,mrscbtctwelogsdynamicfolderpathwithfilename, date, time);
	 			obj.readMrsSourceFileAndWritetoDestinationDeAssociation(mrscbtctwelogsSourcefilepathwithname,mrscbtctwelogsdynamicfolderpathwithfilename, date, time);
	 			
	 		/*	if (deleteflag) {
	 				Date yesterday = new java.sql.Date(new java.util.Date().getTime() - MILLIS_IN_A_DAY);
	 				String yesterdayStr = yesterday.toString();
	 				String yr = yesterdayStr.substring(0, 4);
	 				String month = yesterdayStr.substring(5, 7);
	 				String day = yesterdayStr.substring(8, 10);
	 				month = MrsLiveDataReadold.getMonth(month);
	 				deletecbtcDirectorypath = deletecbtcDirectorypath + "/" + yr + "/" + month + "/" + day;						
	 			obj.deleteDirectory(deletecbtcDirectorypath);
	 			}*/

	 		}
	 	} catch (NullPointerException ex) {
	 		System.out.println("file path not found in sidsource folder folder to read");
	 	
	 	}
	 	/*catch (IOException ex) {	
	 		
	 		System.out.println("folder not found for deleting... folder already deleted");
	 	}*/
	 catch (Exception e) {
	 		// TODO Auto-generated catch block
	 		e.printStackTrace();
	 	}
	 	
	 	
	 }

	 	/****
	 	 * 
	 	 * The code has been developed to remove the MRS & OCM daily logs on hourly
	 	 * basis which has been copied to new destination. After the successful
	 	 * acknowledgement from blob storage that the data got copied can be delete.
	 	 * 
	 	 * 
	 	 */
	 	public void deleteDirectory(String directoryFilePath) throws IOException {
	 		Path directory = Paths.get(directoryFilePath);

	 		if (Files.exists(directory)) {
	 			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
	 				@Override
	 				public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes)
	 						throws IOException {
	 					Files.delete(path);
	 					return FileVisitResult.CONTINUE;
	 				}

	 				public FileVisitResult postVisitDirectory(Path directory, IOException ioException) throws IOException {
	 					Files.delete(directory);
	 					return FileVisitResult.CONTINUE;
	 				}
	 			});
	 		}
	 	}

	 	/****
	 	 * The code has been developed to create dynamically folders by taking the
	 	 * system date and system time and destination path where the dynamic folder
	 	 * need to be created.Indestination path after that YYYY MM DD format
	 	 * 
	 	 * 
	 	 */
	 	String createDynamicMrsFolders(String systemdate, String systemtime, String destingationpath) {

	 		String dynamicfolderpath = "";
	 		String destinationpath = destingationpath;
	 		try {
	 			String yr = systemdate.substring(6, 10);
	 			String month = systemdate.substring(0, 2);
	 			String day = systemdate.substring(3, 5);
	 			String hr1 = systemtime.substring(0, 2);
	 			int hr = Integer.parseInt(hr1);
	 			if (hr == 0) {
	 				hr = 24;
	 				//hr = hr - 1;
	 			}
	 			hr = hr - 1;
	 			String strghr =""; 
	 			if(hr<10)
	 			{
	 				strghr ="0"+hr;
	 			}
	 			else{
	 				strghr = Integer.toString(hr);
	 			}

	 			//month = getMonth(month);//may change if needed
	 			dynamicfolderpath = destinationpath + "/" + yr + "/" + month + "/" + day + "/" + strghr;
	 			File files = new File(dynamicfolderpath);

	 			if (!files.exists()) {
	 				if (files.mkdirs()) {
	 					System.out.println("Directories : " + dynamicfolderpath + " created");
	 				}
	 			}
	 		} catch (Exception e) {
	 			System.err.println("Error : In directory Creation " + e.getMessage());
	 		}

	 		return dynamicfolderpath;
	 	}

	 	/****
	 	 * This method is written to read data from the source path and write in to
	 	 * the destination files in different directory structure like YYYY --MMM
	 	 * --DD --HR(00) --HR(23) Here tis method takes four parameters one for
	 	 * source location to read data from another is destination location root
	 	 * path where is the dynamic folder has been created their it create the
	 	 * file and write the 1hr date prior to the system time here the system time
	 	 * is the server time where it runs. In this code we were comparing the date
	 	 * in every line of the log file with the system date, to check it current
	 	 * date. and comparing the file time with the system time if both match we
	 	 * were writing the file in to the new hrly based file. Else we were
	 	 * checking if the line doesnot contain a date in that we were writing it to
	 	 * the previous line with previous date.
	 	 * 
	 	 * 
	 	 * 
	 	 * 
	 	 * 
	 	 */
	 	void readMrsSourceFileAndWritetoDestinationAssociation(String mrsSourcefilepathwithname, String dynamicfolderpathwithfilename,	String date, String time) {
	 		BufferedReader reader = null;
	 		BufferedWriter bw = null;
	 		FileWriter fw = null;
	 		String source = mrsSourcefilepathwithname;
	 		String destination = dynamicfolderpathwithfilename;
	 		String content = null;
	 		boolean timematch = false;
	 		boolean datematch = false;
	 		boolean testdatecolumn = false;
	 		boolean insertstmt =false;
	 		boolean Locflag=false;

	 		String[] systemdateformatloc = date.split(":");
	 		String systemdateformat1 = systemdateformatloc[0];
	 		String systemdateformat2 = systemdateformatloc[1];
	 		String hour = time.substring(0, 2);
	 		/****
	 		 * may needed for triggering in minutes that time we can check that may
	 		 * needed for triggering in secs that time we can check that
	 		 */
	 		/*
	 		 * String minutes = time.substring(3, 5); String sec = time.substring(6,
	 		 * 8);
	 		 */
	 		int filereadhr = 0;

	 		try {
	 			// read file
	 			File file = new File(source);
	 			FileReader fileReader = new FileReader(file);
	 			reader = new BufferedReader(fileReader);
	 			fw = new FileWriter(destination);
	 			bw = new BufferedWriter(fw);
	 			while ((content = (String) reader.readLine()) != null) {
	 				String datepart = null;
	 				String timepart = null;
	 				int filetime = 0;
	 				String[] data = null;	
	 				int indexfound1 = 0;
	 				String LocID = null;
	 				String jsonobj=null;
	 				String acntime=null;
	 				
	 				String stringSearch="VALUES ('";
					//String srchinsert ="InsertNewTrainIDIntoDatabase1:INSERT INTO tblTrainsActive";
	 			
					//int indexsrchinsert = content.indexOf(srchinsert);
	 				//int searchstringlength1 = stringSearch.length();	
	 			
	 				
	 				/*if(indexsrchinsert == -1)
	 				{
	 					insertstmt = false;
	 				}else{
	 					insertstmt = true;
	 					
	 				
	 				}*/
	 				//if(insertstmt){
	 				 indexfound1 = content.indexOf(stringSearch);
	 				//}
	 				if(indexfound1 == -1){
	 					Locflag = false;
	 					
	 				}else{
	 					Locflag=true;
	 				}	 				
	 				
	 				int searchstringlength = stringSearch.length();	 				
	 				
	 				int finallength = indexfound1+ searchstringlength;	
	 				if(Locflag){
	 				 LocID = content.substring(finallength, finallength+7);	 				
	 				}
	 				
	 				if (content != null && content.length() > 0 && !content.trim().isEmpty()) {
	 					data = content.split(" ");
	 				}
	 				if (data != null) {
	 					datepart = data[0];

	 				}
	 				if (datepart != null && datepart.length() > 2) {
	 					String condn = datepart.substring(0, 2);
	 					if (condn.contentEquals("01") || condn.contentEquals("02") || condn.contentEquals("03")
	 							|| condn.contentEquals("04") || condn.contentEquals("05") || condn.contentEquals("06")
	 							|| condn.contentEquals("07") || condn.contentEquals("08") || condn.contentEquals("09")
	 							|| condn.contentEquals("10") || condn.contentEquals("11") || condn.contentEquals("12")) {
	 						testdatecolumn = true;
	 					} else {
	 						testdatecolumn = false;
	 					}
	 				}
	 				if (datepart != null && datepart.length() > 0)
	 					if (datepart.equalsIgnoreCase(systemdateformat1) || datepart.equalsIgnoreCase(systemdateformat2)) {

	 						datematch = true;
	 						if (data != null) {
	 							timepart = data[1];
	 						}
	 						if (timepart != null && timepart.length() > 2) {
	 							filetime = Integer.parseInt(timepart.substring(0, 2));
	 							filereadhr = Integer.parseInt(hour);
	 							acntime = timepart.substring(0,12);	 						
	 							if(Locflag){
	 							String assoctionmsg = datepart +" " + acntime + " " + "Symbol:" +  LocID;	
	 							System.out.println("assoctionmsg.."+assoctionmsg);
	 								 			 				
	 			 				Gson gson = new Gson();
	 						    // convert java object to JSON format
	 						     jsonobj = gson.toJson(assoctionmsg);
	 						      System.out.println("Json object is ="+jsonobj);
	 						      
	 							}
	 							if (filereadhr == 0) {
	 								filereadhr = 24;
	 							}
	 							filereadhr = filereadhr - 1;
	 							if (filereadhr == filetime) {
	 								timematch = true;
	 							} else if (filetime < filereadhr) {
	 								timematch = false;
	 							} else if (filetime > filereadhr) {
	 								break;
	 							}
	 						}
	 					} else {

	 						if (!testdatecolumn && timematch) {

	 							bw.write(content);
	 							bw.newLine();
	 						}
	 						continue;
	 					}

	 				if (timematch && datematch && Locflag) {
	 					bw.write(jsonobj);
	 					bw.newLine();
	 				}
	 			}
	 		} catch (IOException e) {
	 			e.printStackTrace();
	 		} finally {
	 			try {
	 				if (reader != null)
	 					reader.close();
	 				if (bw != null)
	 					bw.close();
	 			} catch (IOException ex) {
	 				ex.printStackTrace();
	 			}
	 		}

	 	}

		void readMrsSourceFileAndWritetoDestinationDeAssociation(String mrsSourcefilepathwithname, String dynamicfolderpathwithfilename,	String date, String time) {
 		BufferedReader reader = null;
 		BufferedWriter bw = null;
 		FileWriter fw = null;
 		String source = mrsSourcefilepathwithname;
 		String destination = dynamicfolderpathwithfilename;
 		String content = null;
 		boolean timematch = false;
 		boolean datematch = false;
 		boolean testdatecolumn = false;
 		boolean trainflag=false;

 		String[] systemdateformatloc = date.split(":");
 		String systemdateformat1 = systemdateformatloc[0];
 		String systemdateformat2 = systemdateformatloc[1];
 		String hour = time.substring(0, 2);
 		/****
 		 * may needed for triggering in minutes that time we can check that may
 		 * needed for triggering in secs that time we can check that
 		 */
 		/*
 		 * String minutes = time.substring(3, 5); String sec = time.substring(6,
 		 * 8);
 		 */
 		int filereadhr = 0;

 		try {
 			// read file
 			File file = new File(source);
 			FileReader fileReader = new FileReader(file);
 			reader = new BufferedReader(fileReader);
 			fw = new FileWriter(destination);
 			bw = new BufferedWriter(fw);
 			String stringSearch="CLICKED -(YES TO DELETE TRAIN ID:";
 			while ((content = (String) reader.readLine()) != null) {
 				String datepart = null;
 				String timepart = null;
 				String disassociationpart = null;
 				String associationpart=null;
 				String TrainId=null;
 				int filetime = 0;
 				String disacntime = null;
 				String[] data = null;
 				int searchstringlength = stringSearch.length();	
 				int indexfound = content.indexOf(stringSearch);	
 				String jsonobj=null;
 				if(indexfound==-1){
 					trainflag=false; 					
 				}else{
 					trainflag=true;
 				} 							
 				int finallength = indexfound+ searchstringlength;	
 				if(trainflag){
 				 TrainId = content.substring(finallength, content.length()-1); 				
 				}
 				if (content != null && content.length() > 0 && !content.trim().isEmpty()) {
 					data = content.split(" ");
 				}
 				if (data != null) {
 					datepart = data[0];

 				}
 				if (datepart != null && datepart.length() > 2) {
 					String condn = datepart.substring(0, 2);
 					if (condn.contentEquals("01") || condn.contentEquals("02") || condn.contentEquals("03")
 							|| condn.contentEquals("04") || condn.contentEquals("05") || condn.contentEquals("06")
 							|| condn.contentEquals("07") || condn.contentEquals("08") || condn.contentEquals("09")
 							|| condn.contentEquals("10") || condn.contentEquals("11") || condn.contentEquals("12")) {
 						testdatecolumn = true;
 					} else {
 						testdatecolumn = false;
 					}
 				}
 				if (datepart != null && datepart.length() > 0)
 					if (datepart.equalsIgnoreCase(systemdateformat1) || datepart.equalsIgnoreCase(systemdateformat2)) {

 						datematch = true;
 						if (data != null) {
 							timepart = data[1]; 				
 						}
 						if (timepart != null && timepart.length() > 2) {
 							filetime = Integer.parseInt(timepart.substring(0, 2));
 							filereadhr = Integer.parseInt(hour);
 							disacntime = timepart.substring(0,12);
 							if(trainflag){
 							String disassoctionmsg = datepart +" " + disacntime + " " +stringSearch + " "+ TrainId;
 							
 			 				
 			 				Gson gson = new Gson();
 						    // convert java object to JSON format
 						     jsonobj = gson.toJson(disassoctionmsg);
 						      System.out.println("Json object is ="+jsonobj);
 						      
 							}
 							if (filereadhr == 0) {
 								filereadhr = 24;
 							}
 							filereadhr = filereadhr - 1;
 							if (filereadhr == filetime) {
 								timematch = true;
 							} else if (filetime < filereadhr) {
 								timematch = false;
 							} else if (filetime > filereadhr) {
 								break;
 							}
 						}
 					} else {

 						if (!testdatecolumn && timematch) {

 							bw.write(content);
 							bw.newLine();
 						}
 						continue;
 					}
 				

 				if (timematch && datematch && trainflag) {
 					
 					bw.write(jsonobj);
 					bw.newLine();
 				}
 			}
 		} catch (IOException e) {
 			e.printStackTrace();
 		} finally {
 			try {
 				if (reader != null)
 					reader.close();
 				if (bw != null)
 					bw.close();
 			} catch (IOException ex) {
 				ex.printStackTrace();
 			}
 		}

 	}

	 	String[] listofFilesandSubdirectoryInaDirectory(String pathofmrslogfolder) {
	 		File f = null;
	 		String[] paths = null;
	 		String folderpath = pathofmrslogfolder;
	 		// How To List Only The Files In A Directory?
	 		try {
	 			f = new File(folderpath);
	 			paths = f.list();
	 			for (String path : paths) {
	 				System.out.println(path);
	 			}

	 		} catch (NullPointerException ex) {
	 			System.out.println("file path / folder path not found in source" + pathofmrslogfolder);
	 		} catch (Exception e) {
	 			System.out.println("Error  found is " + e);
	 		}
	 		return paths;
	 	}
	 	
	 	Date yesterday() {
		    final Calendar cal = Calendar.getInstance();
		    cal.add(Calendar.DATE, -1);
		    return cal.getTime();
		}
		

		 String getYesterdayDateString() {
		        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		        return dateFormat.format(yesterday());
		}
		 String getYesterdayDateforfolderName() {
		        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		        return dateFormat.format(yesterday());
		}
		 String getlocalSystemdateforcreatingyesterdayfolder() {
		 		String systemdate = null;		 		
		 		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/YYYY");		 	
		 		SimpleDateFormat format2 = new SimpleDateFormat("MM-dd-YYYY");
		 		String DateToStr1 = format1.format(yesterday());
		 		String DateToStr2 = format2.format(yesterday());
		 		systemdate = DateToStr2 + ":" + DateToStr1;
		 		return systemdate;
		 	}
	 	String getlocalSystemdateforcreatingfolder() {
	 		String systemdate = null;
	 		Date curDate = new Date();
	 		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/YYYY");
	 		String DateToStr1 = format1.format(curDate);
	 		SimpleDateFormat format2 = new SimpleDateFormat("MM-dd-YYYY");
	 		String DateToStr2 = format2.format(curDate);
	 		systemdate = DateToStr2 + ":" + DateToStr1;
	 		return systemdate;
	 	}

	 	String getlocalSystemTimeforcreatingfolder() {
	 		String systemtime = java.time.LocalTime.now().toString();	 		
	 		return systemtime;
	 	}
	 }
