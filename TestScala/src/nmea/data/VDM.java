package nmea.data;

import java.nio.channels.AcceptPendingException;
import java.util.Arrays;

import nmea.data.Data.*;

public enum VDM {
	START(new ConstantString("!AIVDM")),
	TOTAL_SENTENCES(new Int().setRange(1,  9)),
	NO_SENTENCES(new Int().setRange(1, 9)),
	ID(new Int().setRange(1, 9).permitNull()),
	CHANEL(new Char('A', 'B')),
	TEXT(new AString()),
	FILL_NUM(new Int().setRange(0, 5));	
	
	public Data data;
	VDM(Data data){
		this.data = data;
	}
	
	public static Interpreter<VDM> Int = new Interpreter<VDM>(VDM.class, VDM.values(), Arrays.stream(VDM.values()).map((VDM vdm) -> vdm.data));
	
	public static void test(){
		String testData = "!AIVDM,1,1,,B,16:>>s5Oh08dLO8AsMAVqptj0@>p,0*67";
		VDM.Int.parseWithCheckSum(testData);
		System.out.println(VDM.Int.toString());
	}
	
	public static BitAnalyzer transfer(String str){
		BitAnalyzer bitAnalyzer = new BitAnalyzer(str.length() * 6);
		for (int i=0; i<str.length(); ++i){
			char c = str.charAt(i);
			int d = c + 0b101000;
//			c = (char) ((d > (char)0b10000000) ? d + (char)0b100000 : d+(char)0b101000);
			d = d > 0b10000000 ? d + 0b100000 : d+0b101000;
//			d = d & 0b111111;
			for (int mask=0b100000; mask > 0; mask >>= 1){
				bitAnalyzer.acceptBit( ((d & mask) > 0) ? 1 : 0);
			}
		}
		return bitAnalyzer;
	}
	
	public static void decodeV1(String str){
		decodeV1(transfer(str));		
	}
	
	public static void decodeV1(BitAnalyzer analyzer){
		BitAnalyzer[] ret = analyzer.split(new int[]{6, 2, 30, 4, 8, 10, 1, 28, 27, 12, 9, 6, 4, 1, 1, 19});
		for (BitAnalyzer a : ret){
			System.out.println(a.getInt() + "\t" + a.data.length + "\t" + a.toBitString());
		}
		
	}
	
	public static void main(String[] args) {
		decodeV1("16:>>s5Oh08dLO8AsMAVqptj0@>p");
	}
	
}
