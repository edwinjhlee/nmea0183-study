package nmea.old;

import java.util.List;

import nmea.old.BasicFormat.*;

public class GGA {
	public static class Message extends AbstractNmea.Message{
		final UTC utc;
		final GeoDegree nsDegree;
		final NSSphere nsSphere;
		final GeoDegree weDegree;
		final WESphere weSphere;
		final GPSState gpsState;
		final int satalitesNum;
		final float hdopVeritcalFactor;
		final float height;
		// M
		final float ovalHeight;
		// M
		final int diffTime;
		final int diffId;
		final int checksum;
		
		public Message(UTC utc, GeoDegree nsDegree, NSSphere nsSphere,
				GeoDegree weDegree, WESphere weSphere,
				GPSState gpsState, int satalitesNum,
				float hdopVerticalFactor,
				float height, float ovalHeight,
				int diffTime, int diffId,
				int checksum){
			this.utc = utc;
			this.nsDegree = nsDegree;
			this.nsSphere = nsSphere;
			this.weDegree = weDegree;
			this.weSphere = weSphere;
			this.gpsState = gpsState;
			this.satalitesNum = satalitesNum;
			this.hdopVeritcalFactor = hdopVerticalFactor;
			this.height = height;
			this.ovalHeight = ovalHeight;
			this.diffTime = diffTime;
			this.diffId = diffId;
			this.checksum = checksum;
		}
		
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
