package nmea.data;

import nmea.data.Data.EnumInfo;


public class DataNMEA {
	public static class _UTCTime{
		final int hh;
		final int mm;
		final int ss;
		final int sss;
		
		public _UTCTime(int hh, int mm, int ss, int sss) {
			// TODO: arguments validation
			this.hh = hh;
			Data.rangeCheck(hh, 0, 23);
			this.mm = mm;
			Data.rangeCheck(mm, 0, 59);
			this.ss = ss;
			Data.rangeCheck(ss, 0, 59);
			this.sss = sss;
			Data.rangeCheck(sss, 0, 999);
		}
		
		public String toString(){
			return String.format("%2d%2d%2d.%3d", 
					this.hh, this.mm, this.ss, this.sss);			
		}
		
		public String toHumanString(){
			return toString();
		}
		
		public static _UTCTime parse(String content){
			
			String hh = content.substring(0, 2);
			String mm = content.substring(2, 4);
			String ss = content.substring(4, 6);
			String sss = "000";
			if (content.charAt(6) == '.')
				content.substring(7, 10);
			return new _UTCTime(Integer.parseInt(hh), Integer.parseInt(mm), 
					Integer.parseInt(ss), Integer.parseInt(sss));
		}
	}
	
	public static class UTCTime extends Data<_UTCTime, UTCTime>{
		public UTCTime(){
			this.fixlen(10);
		}
		
		@Override
		public void parse(String content) {
			this.value = _UTCTime.parse(content);
		}
	}
	
	
	public static class _UTCDate{
		final int dd;
		final int mm;
		final int yy;
		
		public _UTCDate(int dd, int mm, int yy) {
			// TODO: arguments validation, Refer the NMEA protocol...
			this.dd = dd;
			Data.rangeCheck(dd, 0, 31);
			this.mm = mm;
			Data.rangeCheck(mm, 0, 12);
			this.yy = yy;
			Data.rangeCheck(yy, 0, 99);
		}
		
		public String toString(){
			return String.format("%2d%2d%2d", 
					this.dd, this.mm, this.yy);			
		}
		
		public static _UTCDate parse(String content){
			
			String dd = content.substring(0, 2);
			String mm = content.substring(2, 4);
			String yy = content.substring(4, 6);
			return new _UTCDate(Integer.parseInt(dd), Integer.parseInt(mm), 
					Integer.parseInt(yy));
		}
	}
	
	public static class UTCDate extends Data<_UTCDate, UTCDate>{
		public UTCDate(){
			this.fixlen(6);
		}
		
		@Override
		public void parse(String content) {
			this.value = _UTCDate.parse(content);
		}
	}
	
	
	public static class _NSGeoDegree {
		final int dd;
		final int mm;
		final int mmmm;
		public _NSGeoDegree(int dd, int mm, int mmmm){
			this.dd = dd;
			Data.rangeCheck(dd, 0, 99);
			this.mm = mm;
			Data.rangeCheck(dd, 0, 99);
			this.mmmm = mmmm;
			Data.rangeCheck(dd, 0, 9999);
		}
		
		@Override
		public String toString(){
			return String.format("%2d%2d.%4d", 
					this.dd, this.mm, this.mmmm);
		}
		
		public static _NSGeoDegree parse(String content){
			String dd = content.substring(0, 2);
			String mm = content.substring(2, 4);
			String mmmm = content.substring(5, 9);
			return new _NSGeoDegree(Integer.parseInt(dd), 
					Integer.parseInt(mm),
					Integer.parseInt(mmmm));
		}
	}
	
	public static class NSGeoDegree extends Data<_NSGeoDegree, NSGeoDegree>{
		public NSGeoDegree(){
			this.fixlen(9);
		}
		
		@Override
		public void parse(String content) {
			this.value = _NSGeoDegree.parse(content);
		}
	}	
	
	public static class _WEGeoDegree {
		final int ddd;
		final int mm;
		final int mmmm;
		public _WEGeoDegree(int ddd, int mm, int mmmm){
			this.ddd = ddd;
			Data.rangeCheck(ddd, 0, 999);
			this.mm = mm;
			Data.rangeCheck(mm, 0, 99);
			this.mmmm = mmmm;
			Data.rangeCheck(mmmm, 0, 9999);
		}
		
		@Override
		public String toString(){
			return String.format("%3d%2d.%4d", 
					this.ddd, this.mm, this.mmmm);
		}
		
		public static _WEGeoDegree parse(String content){
			String ddd = content.substring(0, 3);
			String mm = content.substring(3, 5);
			String mmmm = content.substring(6, 10);
			return new _WEGeoDegree(Integer.parseInt(ddd), 
					Integer.parseInt(mm),
					Integer.parseInt(mmmm));
		}
	}
	
	public static class WEGeoDegree extends Data<_WEGeoDegree, WEGeoDegree>{
		public WEGeoDegree(){
			this.fixlen(10);
		}
		
		@Override
		public void parse(String content) {
			this.value = _WEGeoDegree.parse(content);
		}
	}
	
//	new EnumInfo<RMCMode>(RMCMode.values(), "Self-Positioning", "Diff", "Guess", "Data No Effect")
	

	public static enum PositioningStateOption{
		A, V
	}
	
	public static class PositioningState extends EnumInfo<PositioningStateOption>{
		public PositioningState(){
			super(PositioningStateOption.values(), "Effective Positioning", "Wrong Positioning");
		}
	}


}
