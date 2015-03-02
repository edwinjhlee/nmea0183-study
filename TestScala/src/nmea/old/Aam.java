package nmea.old;

import java.util.List;

import nmea.old.AbstractNmea;

public class Aam {
	
	public static class Message extends AbstractNmea.Message{
		
	}
	
	public static class Codec implements AbstractNmea.Codec<Message>{

		@Override
		public Message decode(String content) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<String> encode(Message message) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
	
	
}
