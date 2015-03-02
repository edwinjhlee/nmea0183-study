package nmea.data;

import java.util.ArrayList;

public class BitAnalyzer {
	int[] data;
	int counter;
	
	public BitAnalyzer(int size){
		data = new int[size];
		counter = 0;
	}
	
	public BitAnalyzer acceptBit(int bit){
		if ((bit!=0)&&(bit!=1))
			throw new IllegalArgumentException("Only 1 or 0 passed.");
		
		data[counter ++] = bit;
		return this;
	}
	
	public BitAnalyzer acceptBits(int[] bits){
		for (int bit : bits)
			acceptBit(bit);
		return this;
	}
	
	public int[] getBits(int start, int endExclusive){
		int[] ret = new int[endExclusive - start];
		for (int i=start; i<endExclusive; ++i)
			ret[i-start] = data[i];
		return ret;
	}
	
	public BitAnalyzer[] split(int[] partlen){
		int start = 0;
		BitAnalyzer[] ret = new BitAnalyzer[partlen.length];
		for (int i=0; i<partlen.length; ++i){
			ret[i] = new BitAnalyzer(partlen[i]).acceptBits(this.getBits(start, start+partlen[i]));
			start += partlen[i];
		}
		return ret;
	}
	
	public String toBitString(){
		StringBuilder sb = new StringBuilder();
		for (int b : data)
			sb.append(b==0 ? "0" : "1");
		return sb.toString();
	}
	
	public int getInt(){
		int ret = 0;
		for (int b : data)
			ret = (ret << 1) + b;
		return ret;
	}
	
	public int getRevInt(){
		int ret = 0;
		for (int i=data.length-1; i>=0; --i)
			ret = (ret << 1) + data[i];
		return ret;
	}
	
	public static void main(String[] args) {
		BitAnalyzer bit = new BitAnalyzer(8);
		int num = bit.acceptBits(new int[]{0, 0, 0, 0, 0, 0, 1, 1}).getInt();
		System.out.println("\t" + num);
	}
	
}
