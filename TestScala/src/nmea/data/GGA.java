package nmea.data;

import java.util.Arrays;

import nmea.data.Data.Char;
import nmea.data.Data.ConstantLetter;
import nmea.data.Data.ConstantString;
import nmea.data.Data.Int;
import nmea.data.Data.SignedFloat;
import nmea.data.Data.StateSet;
import nmea.data.Data.UnsignedFloat;

public enum GGA{
	
	START(new ConstantString("$GPGGA")),
	UTC_TIME(new DataNMEA.UTCTime()),
	NS_DEGREE(new DataNMEA.NSGeoDegree()),
	NS_SPHERE(new Char('N', 'S')),
	WE_DEGREE(new DataNMEA.WEGeoDegree()),
	WE_SPHERE(new Char('W', 'E')),
	GPS_STATE(new StateSet<GPSState>(GPSState.values(), 0, 1, 2, 6)),
	SATELITE_NUM(new Int().fixlen(2).setRange(0, 12)),
	HDOP_HORIZONTAL_FACTOR(new UnsignedFloat()),
	HEIGHT(new SignedFloat()),
	HEIGHT_M(ConstantLetter.M), // (ConstantLetter.M), //(new ConstantLetter('M')),
	OVAL_HEIGHT(new SignedFloat()),
	OVAL_HEIGHT_M(ConstantLetter.M),
	DIFF_TIME(new Int().permitNull()),
	DIFF_STATION_ID(new Int().permitNull().fixlen(4));
	
	public static enum GPSState{
		NONE, NON_DIFF, DIFF, CAL
	}
	
	public Data data;
	GGA(Data data){
		this.data = data;
	}
	
	public static Interpreter<GGA> Int = 
			new Interpreter<GGA>(GGA.class, GGA.values(), 
					Arrays.stream(GGA.values()).map((GGA gga) -> gga.data));
	
	public static void test(){
		// Test GGA
		String testData = "$GPGGA,121252.000,3937.3032,N,11611.6046,E,1,05,2.0,45.9,M,-5.7,M,,0000*77";
		GGA.Int.parseWithCheckSum(testData);
		System.out.println(GGA.Int.toString());
	}
	
}

