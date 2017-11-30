package sendQpid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class RecursiveReadLiveData {

	private static final String String = null;

	public RecursiveReadLiveData() {
		// TODO Auto-generated constructor stub
	}

	boolean deleteflag = true;
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
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		InputStream is = null;
		BufferedReader bfReader = null;
		
		String mrsSourceFolderLocation = "";
		String mrsLogDestinationFoderpath = "";

		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("C:/Configurationfiles/config.properties"));				
			mrsSourceFolderLocation = (String) (properties.getProperty("Sourcefolderepath"));
			mrsLogDestinationFoderpath = (String) (properties.getProperty("Destinationfolderepath"));
			RecursiveReadLiveData obj = new RecursiveReadLiveData();
			String date = obj.getlocalSystemdateforcreatingfolder();
			String time =obj.getlocalSystemTimeforcreatingfolder();
			obj.readfilesInFolder(mrsSourceFolderLocation, date, time,mrsLogDestinationFoderpath);
			
			
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	int count=0;
	String  temp= null;
	void readfilesInFolder(String mrsSourcePath,String strDate,String time,String destinationpath){
		try{
			
			System.out.println("destinationpath.."+destinationpath);
	File folder = new File(mrsSourcePath);
	File[] files = folder.listFiles();
	
	String dyndestinationpath = "";
	for (File file : files) {
		
		if(count==0){
			  temp= destinationpath; 
			  dyndestinationpath = destinationpath;
			  System.out.println("temp.."+temp);
			  }
		{
			dyndestinationpath = destinationpath ;
			System.out.println("dyndestinationpath.22222222222222222222222222."+dyndestinationpath);
		}
		count++;
		if (file.isFile()) {
			System.out.println(file.getName());
			String filename = file.getName();
			
			String mrsSourcePathwithFileName = mrsSourcePath +"\\"+filename;	
			System.out.println("...mrsSourcePathwithFileName..."+mrsSourcePathwithFileName);
			String newfilename = filename.substring(0, filename.length() - 4);	
			String newtime = time.substring(0, 2);
			int newtimehrdely = Integer.parseInt(newtime);
			if(newtimehrdely==0)
			{
				newtimehrdely=24;
				deleteflag = true;
			}
			newtimehrdely = newtimehrdely - 1;		
			
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
		    Date now = new Date();
		    String strDate1 = sdfDate.format(now);		    
		   
		    String yr = strDate1.substring(0, 4);
			String month = strDate1.substring(5, 7);
			String day = strDate1.substring(8, 10);
			System.out.println("day= " + day);
             String hr =newtime;				
			month = RecursiveReadLiveData.getMonth(month);		
		
			dyndestinationpath =  dyndestinationpath + "/" + yr + "/" + month + "/" + day + "/" + hr;
			
			System.out.println("dyndestinationpath...333333333333333333." +dyndestinationpath);
			String mrscbtctwelogsdynamicfolderpathwithfilename = dyndestinationpath + "/" + newfilename + newtimehrdely + "_LOG.txt";
			File dynfiles = new File(dyndestinationpath);
System.out.println("path pass for folder creation "+dyndestinationpath);
			if (!dynfiles.exists()) {
				if (dynfiles.mkdirs()) {
					System.out.println("Directories : " + dyndestinationpath + " created");
				}
			}
			
			System.out.println("mrscbtctwelogsdynamicfolderpathwithfilename...."+mrscbtctwelogsdynamicfolderpathwithfilename);
			System.out.println("destinationpath...."+destinationpath);
			
			readMrsSourceFileAndWritetoDestination(mrsSourcePathwithFileName, mrscbtctwelogsdynamicfolderpathwithfilename, strDate1, time);
			//readMrsHistoricalSourceFileAndWritetoDestination(mrsSourcePathwithFileName, dyndestinationpath, strDate, time);
		} else if (file.isDirectory()) {
			String srcpath =file.getAbsolutePath().toString();//need to be thr and pass for recursive read
			System.out.println("srcpath.file.getAbsolutePath().."+srcpath);
			
			String remove = file.getParent().toString();
			remove = remove +"\\";
			System.out.println("remove..."+remove);
			String base = file.getAbsolutePath().toString();
			System.out.println("base ="+base);
			
			 int i = base.length();
			 System.out.println("i..."+i);
			 int j = remove.length();
			 System.out.println("j"+j);
			 String result = base.substring(j, i);
			 System.out.println("result.."+result);				
			
				result= dyndestinationpath +"/" +result;
				System.out.println("result.1111111."+result);
			
			
			readfilesInFolder(srcpath,strDate,time,result);
		}
	}
	}catch(Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	

	String createDynamicMrsFolders(String systemdate, String systemtime, String destingationpath) {

		String dynamicfolderpath = destingationpath;
		
		try {
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
		    Date now = new Date();
		    String strDate = sdfDate.format(now);
		    System.out.println("strDate..."+strDate);
		   
		    String yr = strDate.substring(0, 4);
			String month = strDate.substring(5, 7);
			String day = strDate.substring(8, 10);
			System.out.println("day= " + day);

			String systemtime1 = systemtime;
			systemtime1 = systemtime.substring(0,2);
		    int hr = Integer.parseInt(systemtime1);
			hr =hr-1;			
			month = RecursiveReadLiveData.getMonth(month);		
			System.out.println(month);
			System.out.println("destinationpath :"+dynamicfolderpath);
			String tempdestpath =dynamicfolderpath;
			dynamicfolderpath = dynamicfolderpath + "/" + yr + "/" + month + "/" + day + "/" + hr;

			System.out.println("month..." + month);
			System.out.println("dynamicfolderpath.." + dynamicfolderpath);

			File files = new File(dynamicfolderpath);

			if (!files.exists()) {
				if (files.mkdirs()) {
					System.out.println("Directories : " + dynamicfolderpath + " created");
				}
			}
			dynamicfolderpath = tempdestpath;
		} catch (Exception e) {
			System.err.println("Error : " + e.getMessage());
		}

		return dynamicfolderpath;
	}

	void readMrsSourceFileAndWritetoDestination(String mrsSourcefilepathwithname, String dynamicfolderpathwithfilename,String date, String time)
			 {
		BufferedReader reader = null;
		BufferedWriter bw = null;
		FileWriter fw = null;
		String source = mrsSourcefilepathwithname;
		String destination = dynamicfolderpathwithfilename;

	System.out.println("destination.."+destination);
		String content = null;
		boolean timematch = false;
		boolean datematch = false;
		boolean testdatecolumn = false;
		
		
		 String sysdate =date;

		 String systimepart = time.substring(0,2);
		 
		int hour = Integer.parseInt(time.substring(0, 2));
		int minutes = Integer.parseInt(time.substring(3, 5));
		int sec = Integer.parseInt(time.substring(6, 8));

		int filereadhr = hour - 1;

		Date curDate = new Date();
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/YYYY");
		String DateToStr1 = format1.format(curDate);
		System.out.println("MM/dd/YYYY =" + DateToStr1);

		SimpleDateFormat format2 = new SimpleDateFormat("MM-dd-YYYY");
		String DateToStr2 = format2.format(curDate);
		System.out.println("MM-dd-YYYY=" + DateToStr2);

		
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
				System.out.println("datepart...." + datepart);
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
					if (datepart.equalsIgnoreCase(DateToStr1) || datepart.equalsIgnoreCase(DateToStr2)) {
						
						datematch = true;
						if (data != null) {
							timepart = data[1];
						}
						if (timepart != null && timepart.length() > 2) {
							filetime = Integer.parseInt(timepart.substring(0, 2));
							System.out.println("filetime.." + filetime);
						}
						
						if (filereadhr == filetime) {
							 changetime = filetime;
							timematch = true;
						} else {
							break;
						}

					}
					
					else {
						// write on condition and continue
						if (!testdatecolumn) {
							bw.write(content);
							bw.newLine();
						}

						continue;
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
