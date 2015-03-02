import java.util.stream.Stream;

import javax.xml.transform.stream.StreamSource;


public class TestJava8 {
	
	public static void main(String[] args) {
		Stream.generate(()->1).limit(8).forEach( (i) -> System.out.println("No " + i) );
	
	}
	
	
}
