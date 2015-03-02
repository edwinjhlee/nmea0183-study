package nmea.old;

import java.util.*;
import java.util.stream.Stream;

import javax.xml.transform.stream.StreamSource;

public class GSA {
	public static enum Mode{ 
		Manual('M'), Auto('A');

		final char label;
		Mode(char n){
			this.label = n;	
		}
		
		public static Mode build(char n){
			if (n == 'M') return Manual;
			if (n == 'A') return Auto;
			throw new IllegalArgumentException("Wrong Mode.");
		}
		
		public static Mode build(String n){
			if (n.length() != 1) throw new IllegalArgumentException("String with only one char should be given.");
			return build(n.charAt(0));
		}
	
	};
	
	public static enum PositionalMode { None(1), Two(2), Three(3);
		int label;
		PositionalMode(int label) {
			this.label = label;
		}
		public static PositionalMode build(int n){
			if ((n<1) || (n>3)) throw new IllegalArgumentException("Only 1 to 3, passing value is " + n);
			return (n==1) ? None : ( (n==2) ? Two : Three );			
		}
	};
	
	public static class Message extends AbstractNmea.Message{
		final Mode mode;
		final PositionalMode positionalMode;
		final ArrayList<Integer> prnNoList;
		final float pdop;
		final float hdop;
		final float vdop;
		final int checksum;	
		public Message(Mode mode, PositionalMode posMode, ArrayList<Integer> prnNoList, float pdop, float hdop, float vdop, int checksum) {
			this.mode = mode;
			this.positionalMode = posMode;
			this.prnNoList = prnNoList;
			this.pdop = pdop;
			this.hdop = hdop;
			this.vdop = vdop;
			this.checksum = checksum;					
		}
	}
	
	public static class Codec implements AbstractNmea.Codec<Message>{

		String START = "$GPGSA";
		
		@Override
		public Message decode(String content) {
			String items[] = content.split(",");
			for (int i=0; i<items.length; ++i)
				items[i] = items[i].trim();
			
			if (START.equals(items[0]) == false)
				throw new IllegalStateException("Content is suitable for GSA decoder");		
			
			int l = items.length;
			
			ArrayList<Integer> prnNoList = new ArrayList<Integer>(l - 7);
			for (int i=2; i<l-4; ++i) prnNoList.add(Integer.parseInt(items[i]));
			
			return new Message(Mode.build(items[1]), 
					PositionalMode.build(Integer.parseInt(items[2])), 
					prnNoList,
					Float.parseFloat(items[l-4]),
					Float.parseFloat(items[l-3]), 
					Float.parseFloat(items[l-2]), 
					Integer.parseInt((items[l-1]), 16));
		}

		@Override
		public List<String> encode(Message message) {
			ArrayList<String> ret = new ArrayList<>(7 + message.prnNoList.size());
			ret.add(START);
			ret.add(Character.toString(message.mode.label));
			ret.add(Integer.toString(message.positionalMode.label));
			for (Integer e : message.prnNoList)
				ret.add(Integer.toString(e));
			message.prnNoList.forEach((e) -> ret.add(Integer.toString(e)));
			ret.add(Float.toString(message.vdop));
			ret.add(Float.toString(message.hdop));
			ret.add(Float.toString(message.pdop));
			ret.add(Integer.toHexString(message.checksum));
			return ret;
		}
		
	}
}
