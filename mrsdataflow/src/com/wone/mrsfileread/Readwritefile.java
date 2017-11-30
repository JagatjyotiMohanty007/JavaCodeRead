package com.wone.mrsfileread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Readwritefile {

	public Readwritefile() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
			try{
		
			// OutputStream outputStream = new FileOutputStream("c:\\data\\output.tx
		BufferedReader reader = null;
		BufferedWriter bw = null;
		FileWriter fw = null;
		String content="";
		boolean testdatecolumn=false;
		
				// read file
				File file = new File("C:\\MRSLogs\\Text.txt");
				FileReader fileReader = new FileReader(file);
				reader = new BufferedReader(fileReader);
				fw = new FileWriter("C:\\MrsDestinationlogs");
				bw = new BufferedWriter(fw);
				while ((content = (String) reader.readLine()) != null) {
				
					

					String datepart = null;
					String timepart = null;
					Integer cnd = null;
					int filetime = 0;
					String[] data = null;

					if (content != null && content.length() > 0 && !content.trim().isEmpty()) {
						data = content.split(" ");
					}
					if (data != null) {
						datepart = data[0];
						

					}
					System.out.println("datepart...." + datepart);
					
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
							}
							if (timepart != null && timepart.length() > 2) {
								filetime = Integer.parseInt(timepart.substring(0, 2));
								System.out.println("filetime.." + filetime);
							}
							if (filereadhr == filetime) {
								timematch = true;
							} else {
								continue;
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
					if (fileReader != null)
						fileReader.close();
					if (bw != null)
						bw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}


	
	}