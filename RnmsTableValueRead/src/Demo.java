import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo {
	public static void main(String args[]) throws FileNotFoundException, IOException {
		String text = "06-29-2017 10:21:04.570	CLICKED -(YES TO DELETE TRAIN ID:WFF0717)";
				String stringSearch="VALUES ('";
				String stringSearch1="InsertNewTrainIDIntoDatabase1:INSERT INTO tblTrainsActive";
		//String pattern = ".*\\CLICKED -(YES TO DELETE TRAIN ID:";
		String f = "C:\\MRSLogs\\OCM\\OCM_LOGS_2017-06-29\\TestAssociation.txt";
		String line;
		BufferedReader br = new BufferedReader(new FileReader(f));
		while (br.readLine() != null) {
			line = br.readLine();
			line ="06-29-2017 10:25:44.890	InsertNewTrainIDIntoDatabase1:INSERT INTO tblTrainsActive(Symbol, EngineID, InUsed, Subdivision, TerritoryAssignmentID, TrackGUID, SymbolComment, Direction, CallTime, Units, UnitComment, DepTime, ArrTime, FinalTime, OLoads, OEmts, OTons, OLength, DLoads, DEmts, DTons, DLength, MaxSpd, TonsPerOperativeBrake, HazMat, HighWide, EngName, EngOnDuty, EngTimeUp, CondName, CondOnDuty, CondTimeUp,EngNameRel, EngOnDutyRel, EngTimeUpRel, CondNameRel, CondOnDutyRel, CondTimeUpRel,EngNameP, EngOnDutyP, EngTimeUpP,CondNameP, CondOnDutyP, CondTimeUpP, Origin, Dest, EOTD, HOTD, CrewCurrent, CrewRelief, CrewPending, DelayInfo, TrackBull, BadOrderSetOuts, TrainSheetEvents, UnitsP, EngineIDP, OsEvents, Helper, KeyTrain, TrainGUID, TotalHP, ScheduleStatus, TrainLink, TrailingHorsePower, TrainType, StackOrder, SpecialNote, PseudoName, SubType, CBTCEquipped, ActiveSubTrainSheet, MiscellanousInfoOne, MiscellanousInfoTwo, MiscellanousInfoThree, HomeRoadCode, PullerTrain, TrainIsNotActive, HOS, TotalCars, VIA, Release, ScheduleType) VALUES ('LOC7283','MRS   7283','1','4','143','143251','','SUBINDO','6/10/2017 12:25:44 AM','','','12:00:00 AM','12:00:00 AM','6/10/2017 12:25:44 AM','0','0','0','40','0','0','0','0','25','0',0,0,'','6/10/2017 12:25:44 AM','6/10/2017 12:25:44 AM','NONE','6/10/2017 12:25:44 AM','6/10/2017 12:25:44 AM','NONE','6/10/2017 12:25:44 AM','6/10/2017 12:25:44 AM','NONE','6/10/2017 12:25:44 AM','6/10/2017 12:25:44 AM','NONE','6/10/2017 12:25:44 AM','6/10/2017 12:25:44 AM','NONE','6/10/2017 12:25:44 AM','6/10/2017 12:25:44 AM','','','','','|Engineer|NONE|NONE|NONE|||0|;NONE|Conductor|NONE|NONE|NONE|||0|;NONE|ConductorTwo|NONE|NONE|NONE|||0|;NONE|ConductorThree|NONE|NONE|NONE|||0|;NONE|BrakeMan|NONE|NONE|NONE|||0|;NONE|BrakeManTwo|NONE|NONE|NONE|||0|;NONE|BrakeManThree|NONE|NONE|NONE|||0|;NONE|EngineerTrainee|NONE|NONE|NONE|||0|;NONE|ConductorTrainee|NONE|NONE|NONE|||0|0000','NONE|Engineer|NONE|NONE|NONE|||0|;NONE|Conductor|NONE|NONE|NONE|||0|;NONE|ConductorTwo|NONE|NONE|NONE|||0|;NONE|ConductorThree|NONE|NONE|NONE|||0|;NONE|BrakeMan|NONE|NONE|NONE|||0|;NONE|BrakeManTwo|NONE|NONE|NONE|||0|;NONE|BrakeManThree|NONE|NONE|NONE|||0|;NONE|EngineerTrainee|NONE|NONE|NONE|||0|;NONE|ConductorTrainee|NONE|NONE|NONE|||0|0000','NONE|Engineer|NONE|NONE|NONE|||0|;NONE|Conductor|NONE|NONE|NONE|||0|;NONE|ConductorTwo|NONE|NONE|NONE|||0|;NONE|ConductorThree|NONE|NONE|NONE|||0|;NONE|BrakeMan|NONE|NONE|NONE|||0|;NONE|BrakeManTwo|NONE|NONE|NONE|||0|;NONE|BrakeManThree|NONE|NONE|NONE|||0|;NONE|EngineerTrainee|NONE|NONE|NONE|||0|;NONE|ConductorTrainee|NONE|NONE|NONE|||0|0000','','','','','','','','0','0','470BD388-EE1A-42E3-BC94-83D62164CFA9',0,'','','0.00','',0,0,'LOC7283','','1','425D59BE-5D93-4896-999F-98485459A981','','','','',0,0,'8/24/2013','0','','8/24/2013','')";
			
			int indexfound = line.indexOf(stringSearch);
			
			int indexfound1 = line.indexOf(stringSearch1);	
			
			System.out.println("indexfound not found"+indexfound);
			int searchstringlength = stringSearch.length();					
			int finallength = indexfound+ searchstringlength;		
			String LocID = line.substring(finallength, finallength+7);
			System.out.println("LocID.."+LocID);
		
			String outpout =  "06-29-2017 10:21:04.570" + " "+ "Symbol=" + " "+ LocID;
			System.out.println("output.."+ outpout);
			
			//Pattern r = Pattern.compile(pattern);
			//Matcher m = r.matcher(line);
			/*if (m.find()) {
				System.out.println("Found value: " + m.group(0));
				System.out.println("Found value: " + m.group(1));
				System.out.println("Found value: " + m.group(2));
			} else {
				System.out.println("NO MATCH");
			}*/
		}
	}
}