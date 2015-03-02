package nmea.data;

import java.util.Arrays;

import nmea.data.Data.Char;
import nmea.data.Data.ConstantString;

public enum GLL {
	START(new ConstantString("$GPGLL")),
	NS_DEGREE(new DataNMEA.NSGeoDegree()),
	NS_SPHERE(new Char('N', 'S')),
	WE_DEGREE(new DataNMEA.WEGeoDegree()),
	WE_SPHERE(new Char('W', 'E')),
	UTC_TIME(new DataNMEA.UTCTime()),
	POSITION_STATE(new DataNMEA.PositioningState());
	
	public Data data;
	GLL(Data data){
		this.data = data;
	}
	
	public static Interpreter<GLL> Int = 
			new Interpreter<GLL>(GLL.class, GLL.values(), 
					Arrays.stream(GLL.values()).map((GLL gll) -> gll.data));
	
//	$GPGLL,4250.5589,S,14718.5084,E,092204.999,A*2D
	
	public static void test(){
		String testData = "$GPGLL,4250.5589,S,14718.5084,E,092204.999,A*2D";
		GLL.Int.parseWithCheckSum(testData);
		System.out.println(GLL.Int.toString());
	}
	
}
