package nmea.old;

import java.util.List;

public class AbstractNmea {

	public static class Message {
		public String objType;
	}
	
	public static interface Codec<T extends Message> {
		public T decode(String content);
		public List<String> encode(T message);
	}
	
}
