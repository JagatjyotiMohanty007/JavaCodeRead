package com.wone.mrsfileread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReaddmrsOCM {

	public ReaddmrsOCM() {
		// TODO Auto-generated constructor stub
	}

	private static String previousdatawithDatetime = null;;

	/*
	 * public void setTemp(String temp) { this.temp = temp; }
	 * 
	 * public String getTemp() { return temp ; }
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		InputStream is = null;
		BufferedReader bfReader = null;
		String mrsSourceFileLocation = "";
		String mrsSourceFolderLocation = "";
		String mrsLogDestinationFoderpath = "";

		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("C:/Configurationfiles/config.properties"));
			// Context context = new InitialContext(properties);
			mrsSourceFileLocation = (String) (properties.getProperty("Sourcefilepath"));
			mrsSourceFolderLocation = (String) (properties.getProperty("Sourcefolderepath"));
			mrsLogDestinationFoderpath = (String) (properties.getProperty("Destinationfolderepath"));
			/*Date currentdate = new Date();
			SimpleDateFormat dateformat = new SimpleDateFormat("YYYY-MM-DD");
			System.out.println("dateformat.."+dateformat);
			String DateToStr1 = dateformat.format(currentdate).toString();
			System.out.println("YYYY-MM-DD......." + DateToStr1);
*/
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
		    Date now = new Date();
		    String strDate = sdfDate.format(now);
		    System.out.println("strDate..."+strDate);
		    String[] ocmdate = strDate.split(" ");
		    String filedateextn = ocmdate[0];
			String mrscbtctwelogsSourcePath = mrsSourceFolderLocation +"/" + "cbtctwelogs" +"/"+ "CBTC_TWE_LOGS_"+filedateextn; 
			String ocmlogsSourcePath = mrsSourceFolderLocation +"/" + "OCM" +"/"+ "OCM_LOGS_"+filedateextn;
			
			String mrscbtctwelogsDestinationFoderpath = mrsLogDestinationFoderpath + "/"  +"CBTC_TWE_LOGS" ;
			String ocmlogsDestinationFoderpath = mrsLogDestinationFoderpath + "/"  +"OCM_LOGS" ;
					
			System.out.println("Sourcefolderepath =" + mrsSourceFolderLocation);
			System.out.println("*************Read from properties file completed*********************");
			ReadMrsData obj = new ReadMrsData();
			ReadMrsData obj1 = new ReadMrsData();
			
			/* for mrscbtc logs
			 * 
			 */
			System.out.println("mrscbtctwelogsSourcePath......"+mrscbtctwelogsSourcePath);
			
			String list[] = obj.listofFilesandSubdirectoryInaDirectory(mrscbtctwelogsSourcePath);
			String list1[] = obj1.listofFilesandSubdirectoryInaDirectory(ocmlogsSourcePath);
			
			for (String filename : list) {
				String date = obj.getlocalSystemdateforcreatingfolder();
				String time = obj.getlocalSystemTimeforcreatingfolder();

				String[] systemdateformatloc = date.split(":");
				String systemdateformat1 = systemdateformatloc[0];
				String systemdateformat2 = systemdateformatloc[1];

				String mrscbtctwelogsdynamicfolderpath = obj.createDynamicMrsFolders(systemdateformat1, time,	mrscbtctwelogsDestinationFoderpath);
				
				String mrscbtctwelogsSourcefilepathwithname =mrscbtctwelogsSourcePath +"/"+filename;

				String newfilename = filename.substring(0, filename.length() - 3);

				String newtime = time.substring(0, 2);
				int newtimehrdely = Integer.parseInt(newtime);
				newtimehrdely = newtimehrdely - 1;

				//String dynamicfolderpathwithfilename = dynamicfolderpath + "/" + newfilename + newtimehrdely + "_LOG.txt";
				String mrscbtctwelogsdynamicfolderpathwithfilename = mrscbtctwelogsdynamicfolderpath + "/" + newfilename + newtimehrdely + "_LOG.txt";
				obj.readMrsSourceFileAndWritetoDestination(mrscbtctwelogsSourcefilepathwithname, mrscbtctwelogsdynamicfolderpathwithfilename,date, time);
			}
			
			for (String filename1 : list1) {
				String date1 = obj1.getlocalSystemdateforcreatingfolder();
				String time1 = obj1.getlocalSystemTimeforcreatingfolder();

				String[] systemdateformatloc1 = date1.split(":");
				String systemdateformat11 = systemdateformatloc1[0];
				String systemdateformat21 = systemdateformatloc1[1];

				String mrscbtctwelogsdynamicfolderpath1 = obj1.createDynamicMrsFolders(systemdateformat11, time1,ocmlogsDestinationFoderpath);
				
				String mrscbtctwelogsSourcefilepathwithname1 =ocmlogsSourcePath +"/"+filename1;

				String newfilename1 = filename1.substring(0, filename1.length() - 4);

				String newtime1 = time1.substring(0, 2);
				int newtimehrdely1 = Integer.parseInt(newtime1);
				newtimehrdely1 = newtimehrdely1 - 1;

				//String dynamicfolderpathwithfilename = dynamicfolderpath + "/" + newfilename + newtimehrdely + "_LOG.txt";
				String mrscbtctwelogsdynamicfolderpathwithfilename1 = mrscbtctwelogsdynamicfolderpath1 + "/" + newfilename1 + newtimehrdely1 + "_LOG.txt";
				obj1.readMrsSourceFileAndWritetoDestination(mrscbtctwelogsSourcefilepathwithname1, mrscbtctwelogsdynamicfolderpathwithfilename1,date1, time1);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	String createDynamicMrsFolders(String systemdate, String systemtime, String destingationpath) {

		String dynamicfolderpath = "";
		String destinationpath = destingationpath;
		try {

			System.out.println("systemdate...." + systemdate);
			String yr = systemdate.substring(6, 10);
			System.out.println("year= " + yr);
			String month = systemdate.substring(0, 2);
			System.out.println("Month= " + month);

			String day = systemdate.substring(3, 5);
			System.out.println("day= " + day);

			String hr1 = systemtime.substring(0, 2);
			System.out.println("hr1= " + hr1);
			int hr = Integer.parseInt(hr1);
			if(hr ==0){
				hr=24;
			}
			hr = hr - 1;
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

			month = map.get(month);
			System.out.println(month);

			// String number=Integer.parseInt(month);
			/*
			 * switch(month){ case "01": month ="JAN"; break; case "02": month
			 * ="FEB"; break; case "03": month ="MAR"; break; case "04": month
			 * ="APR"; break; case "05": month ="MAY"; break; case "06": month
			 * ="JUN"; break; case "07": month ="JUL"; break; case "08": month
			 * ="AUG"; break; case "09": month ="SEP"; break; case "10": month
			 * ="OCT"; break; case "11": month ="NOV"; break; case "12": month
			 * ="DEC"; break; default: System.out.println("Not in CONVERTED"); }
			 */

			// String many="/YYYY/MMM/DD/HH"; // multiple directories
			dynamicfolderpath = destinationpath + "/" + yr + "/" + month + "/" + day + "/" + hr;

			System.out.println("month..." + month);
			System.out.println("dynamicfolderpath.." + dynamicfolderpath);

			File files = new File(dynamicfolderpath);

			if (!files.exists()) {
				if (files.mkdirs()) {
					System.out.println("Directories : " + dynamicfolderpath + " created");
				}
			}
		} catch (Exception e) {
			System.err.println("Error : " + e.getMessage());
		}

		return dynamicfolderpath;
	}

	void readMrsSourceFileAndWritetoDestination(String mrsSourcefilepathwithname, String dynamicfolderpathwithfilename,String date, String time) {
		BufferedReader reader = null;
		BufferedWriter bw = null;
		FileWriter fw = null;
		String source = mrsSourcefilepathwithname;
		String destination = dynamicfolderpathwithfilename;

		String localsystemTime = time;
		String content = null;
		// String OldContent = "";
		boolean timematch = false;
		boolean datematch = false;
		boolean testdatecolumn = false;

		String[] systemdateformatloc = date.split(":");
		String systemdateformat1 = systemdateformatloc[0];
		String systemdateformat2 = systemdateformatloc[1];
		String localSystemDate = systemdateformat1;
System.out.println("time..."+time);
		String hour = time.substring(0, 2);
		String minutes = time.substring(3, 5);
		String sec = time.substring(6, 8);

		int filereadhr;
		
		System.out.println("hr" + hour);
		//System.out.println("hr" + filereadhr);
		System.out.println("minutes" + minutes);
		System.out.println("sec" + sec);

		System.out.println("mrsSourcefilepathwithname" + source);
		System.out.println("mrsSourcefilepathwithname" + destination);
		System.out.println("date" + localSystemDate);
		System.out.println("time" + localsystemTime);

		// OutputStream outputStream = new FileOutputStream("c:\\data\\output.tx
		try {
			// read file
			File file = new File(source);
			FileReader fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
			fw = new FileWriter(destination);
			bw = new BufferedWriter(fw);
			String newcontent;
			int changetime;
			while ((content = (String) reader.readLine()) != null) {
				//content = content.concat(content).concat("\n");
				// System.out.println(content);
				newcontent=content;
				String datepart = null;
				String timepart = null;
				Integer cnd = null;
				int filetime = 0;
				String[] data = null;

				
				// datepart =content.substring(0,10).toString();
				// System.out.println("datepart...."+datepart);

				if (content != null && content.length() > 0 && !content.trim().isEmpty()) {
					data = content.split(" ");
				}
				if (data != null) {
					datepart = data[0];
					// timepart = data[1];

				}
				System.out.println("datepart...." + datepart +"systemdateformat2..."+systemdateformat1 +"systemdateformat2..." +systemdateformat2);
				// System.out.println("timepart...."+timepart);
				if (datepart != null && datepart.length() > 2) {
					String condn = datepart.substring(0, 2);
					System.out.println("condn......" + condn);
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
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

						System.out.println("datepart" + datepart);
						System.out.println("systemdateformat1" + systemdateformat1);
						System.out.println("systemdateformat2" + systemdateformat2);

						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
						datematch = true;
						if (data != null) {
							timepart = data[1];
							System.out.println("timepart..."+timepart);
						}
						if (timepart != null && timepart.length() > 2) {
							filetime = Integer.parseInt(timepart.substring(0, 2));
							System.out.println("filetime.." + filetime);
							filereadhr = Integer.parseInt(hour);
							if(filereadhr==0)
							{
								filereadhr=24;
							}
							filereadhr= filereadhr -1;
							System.out.println("filetime.." + filetime);
											
						if (filereadhr == filetime) {							
							timematch = true;
						}else if( filetime < filereadhr){
							
						}else if( filetime > filereadhr){
							break;
						}
						/*else if(!(filereadhr == filetime) && !testdatecolumn ) {
												
							break;
						}*/
						

					}
					
					else {
						// write on condition and continue
						if (!testdatecolumn) {
							bw.write(content);
							bw.newLine();
						}
						continue;
					}
					}
				if (timematch && datematch) {
					bw.write(content);
					bw.newLine();
					// ReadMrsData.previousdatawithDatetime=content;
				}
				System.out.println("Done");
			} // while end
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

	 Map readFileInsideDirectory(String pathofmrslogfolder) {
		String folderpath = pathofmrslogfolder;
		File folder = new File(folderpath);
		// File folder = new File("F:/Path");
             ArrayList listoffile = new ArrayList();
            Map<String,ArrayList> listfileinfolder = new HashMap<String,ArrayList>();
		File[] files = folder.listFiles();
		
		for (File file : files) {
		  if (file.isFile()) {
				System.out.println(file.getName());
				String filename =file.getName().toString();
				listoffile.add(filename);
			}			
			// System.out.println("file/directory let see"+file);
			else if (file.isDirectory()) {
				 readFileInsideDirectory(file.getAbsolutePath());
				System.out.println("its a directory.." + file.getAbsolutePath());
				String subdirectorypath = file.getAbsolutePath().toString();
				readFileInsideDirectory(subdirectorypath);
			}
		  listfileinfolder.put(pathofmrslogfolder, listoffile);
		}
		return listfileinfolder;
	}

	String[] listofFilesandSubdirectoryInaDirectory(String pathofmrslogfolder) {
		File f = null;
		String[] paths = null;
		String folderpath = pathofmrslogfolder;
		// How To List Only The Files In A Directory?
		try {

			// create new file
			// f = new File("c:/test");
			f = new File(folderpath);

			// array of files and directory
			paths = f.list();

			// for each name in the path array
			for (String path : paths) {

				// prints filename and directory name
				System.out.println(path);
			}

		} catch (Exception e) {

			// if any error occurs
			e.printStackTrace();
		}
		return paths;
	}

	// How to list all files in directory and its sub directories recursively?
	ArrayList  listFiles(String path) {
		File folder = new File(path);

		File[] files = folder.listFiles();
		ArrayList<String> filenames = new ArrayList<String>();
		for (File file : files) {
			if (file.isFile()) {
				System.out.println(file.getName());
			} else if (file.isDirectory()) {
				listFiles(file.getAbsolutePath());
			}
		}
		return filenames;
	}
	

	String getlocalSystemdateforcreatingfolder() {
		// System.out.println(java.time.LocalDate.now());
		String systemdate = null;
		/*
		 * systemdate = java.time.LocalDate.now().toString();
		 * System.out.println("systemdate..."+systemdate);
		 * 
		 * long timeNow = System.currentTimeMillis();
		 * System.out.println("long time ="+timeNow);
		 * System.out.println(java.time.LocalTime.now());
		 */
		Date curDate = new Date();
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/YYYY");
		String DateToStr1 = format1.format(curDate);
		System.out.println("MM/dd/YYYY =" + DateToStr1);

		SimpleDateFormat format2 = new SimpleDateFormat("MM-dd-YYYY");
		String DateToStr2 = format2.format(curDate);
		System.out.println("MM-dd-YYYY=" + DateToStr2);

		systemdate = DateToStr2 + ":" + DateToStr1;
		System.out.println("systemdate =" + systemdate);

		return systemdate;
	}

	String getlocalSystemTimeforcreatingfolder() {
		// System.out.println(java.time.LocalDate.now());

		// long timeNow = System.currentTimeMillis();
		// System.out.println("long time ="+timeNow);
		System.out.println(java.time.LocalTime.now());
		String systemtime = java.time.LocalTime.now().toString();
		System.out.println(java.time.LocalTime.now().getHour());
		// int hr = java.time.LocalTime.now().getHour();
		return systemtime;
	}
}
