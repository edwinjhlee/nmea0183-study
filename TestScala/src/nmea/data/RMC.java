package nmea.data;

import java.util.Arrays;

import nmea.data.Data.Char;
import nmea.data.Data.ConstantString;
import nmea.data.Data.EnumInfo;
import nmea.data.Data.UnsignedFloat;

public enum RMC{
	START(new ConstantString("$GPRMC")),
	UTC_TIME(new DataNMEA.UTCTime()),
//	POSITION_STATE(new EnumInfo<PositionState>(PositionState.values(), "Effective Positioning", "Wrong Positioning")),
	POSITION_STATE(new DataNMEA.PositioningState()),
	NS_DEGREE(new DataNMEA.NSGeoDegree()),
	NS_SPHERE(new Char('N', 'S')),
	WE_DEGREE(new DataNMEA.WEGeoDegree()),
	WE_SPHERE(new Char('W', 'E')),
	VELOCITY(new UnsignedFloat()), //.intFixLen(3).decFixLen(1)),
	DIRECTION(new UnsignedFloat()), //.intFixLen(3).decFixLen(1)),
	UTC_DATE(new DataNMEA.UTCDate()),
	MAGNETIC(new UnsignedFloat().intFixLen(3).decFixLen(1).permitNull()),
	MAGNETIC_DIRECTION(new Char('E', 'W').permitNull()),
	// We could do better
	MODE(new EnumInfo<RMCMode>(RMCMode.values(), "Self-Positioning", "Diff", "Guess", "Data No Effect"));
	
	public Data data;
	RMC(Data data){
		this.data = data;
	}

	public static enum RMCMode{
		A, D, E, N
	}
	
	public static Interpreter<RMC> Int = new Interpreter<RMC>(RMC.class, RMC.values(), Arrays.stream(RMC.values()).map((RMC rmc) -> rmc.data));
	
	public static void test(){
		// Test RMC
		String testRMC = "$GPRMC,121252.000,A,3958.3032,N,11629.6046,E,15.15,359.95,070306,,,A*54";
//		System.out.println(testRMC.split(","));
//		for (String e : testRMC.split(",")){
//			System.out.println(e);
//		}
		RMC.Int.parseWithCheckSum(testRMC);
		System.out.println(RMC.Int.toString());
	}
	
}


