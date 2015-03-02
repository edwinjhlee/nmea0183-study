package nmea.old;

import java.util.*;

public class BasicFormat {
	public static class UTC{
		final int hh;
		final int mm;
		final int ss;
		final int sss;
		
		public UTC(int hh, int mm, int ss, int sss) {
			// TODO: arguments validation
			this.hh = hh;
			this.mm = mm;
			this.ss = ss;
			this.sss = sss;
		}
		
		public String toString(){
			return String.format("%2d%2d%2d.%3d", 
					this.hh, this.mm, this.ss, this.sss);			
		}
		
		public static UTC parse(String content){
			String hh = content.substring(0, 2);
			String mm = content.substring(2, 4);
			String ss = content.substring(4, 6);
			String sss = "000";
			if (content.charAt(6) == '.')
				content.substring(7, 10);
			return new UTC(Integer.parseInt(hh), Integer.parseInt(mm), 
					Integer.parseInt(ss), Integer.parseInt(sss));
		}
	}
	
	public static class GeoDegree {
		final int dd;
		final int mm;
		final int mmmm;
		public GeoDegree(int dd, int mm, int mmmm){
			this.dd = dd;
			this.mm = mm;
			this.mmmm = mmmm;
		}
		
		@Override
		public String toString(){
			return String.format("%2d%2d.%4d", 
					this.dd, this.mm, this.mmmm);
		}
		
		public static GeoDegree parse(String content){
			String dd = content.substring(0, 2);
			String mm = content.substring(2, 4);
			String mmmm = content.substring(5, 9);
			return new GeoDegree(Integer.parseInt(dd), 
					Integer.parseInt(mm),
					Integer.parseInt(mmmm));
		}
	}
	
	// TODO: There are duplicated code below, I don't figure it out how to deal with it. Annotation and Enum could be one solution.
	
	public static enum NSSphere{
		N, S;
	}
	
//	public static enum NSSphere{
//		N('N'), S('S');
//		
//		final char label;
//		NSSphere(char label) {
//			this.label = label;
//		}
//
//		public static NSSphere build(char label){			
//			if (label == 'N') return N;
//			if (label == 'S') return S;
//			throw new IllegalArgumentException("Expect N or S but get " + label);
//		}
//		
//		public String labelToString(){
//			return Character.toString(this.label);
//		}
//	}
//	
	
	public static enum WESphere{
		W, E;
	}
		
//	public static enum WESphere{
//		W('W'), E('E');
//		
//		final char label;
//		WESphere(char label) {
//			this.label = label;			
//		}
//		
//		public static WESphere build(char label){			
//			if (label == 'W') return W;
//			if (label == 'E') return E;
//			throw new IllegalArgumentException("Expect W or E but get " + label);
//		}
//		
//		public String labelToString(){
//			return Character.toString(this.label); 
//		}
//				
//	}
	
	public static enum GPSState{
		NONE_POS(0), NONE_DIFF_POS(1), DIFF_POS(2), CAL(6);
		
		final int label;
		GPSState(int label) {
			this.label = label;
		}
		
		public String labelToString(){
			return Integer.toString(this.label);
		}
	}
	
//	public static enum GPSState_{
//		NONE_POS, NONE_DIFF_POS, DIFF_POS, CAL;
//		
//		public static class Interpreter<T, E>{
//			HashMap<T, E> values;
//			
//			public Interpreter(Enum[] e, T ... values){
//				this.values = values;
//			}
//			public 
//			
//		}
//	}
	
}
