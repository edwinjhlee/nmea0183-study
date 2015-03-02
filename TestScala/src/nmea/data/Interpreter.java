package nmea.data;

import static nmea.data.Data.*;

import java.util.*;
import java.util.stream.Stream;

import nmea.data.Data.Char;

import nmea.data.GGA;
import nmea.data.RMC;

public class Interpreter<T extends Enum<T>>{
	
	EnumMap<T, Data> map;
	
	final T[] enumList;
	final Data[] dataList;
	public Data[] getDataList(){
		return this.dataList;
	}
	
	public T[] getEnumList(){
		return this.enumList;
	}
	
	public Interpreter(Class cls, T[] enumValues, Data[] data){
		map = new EnumMap<T, Data>(cls);
		this.enumList = enumValues;
		this.dataList = data;
		for (int i=0; i<enumValues.length; ++i)
			this.map.put(enumValues[i], data[i]);
	}
	
	public Interpreter(Class cls, T[] enumValues, Stream<Data> stream){
		this(cls, enumValues, (Data[])stream.toArray( (size) -> new Data[size] ));
	}
	
	public void parseWithCheckSum(String content){
		content = content.trim();
		int len = content.length();
		int checkSum = Integer.parseInt(content.substring(len-2), 16);
		
		content = content.substring(0, len-3);
		String[] items = content.split(",");
//		for (String e : items){
//			System.out.println(e);
//		}
		Data[] parsers = this.getDataList();
		
		if (items.length != parsers.length)
			throw createIllegalArgumentException(parsers.length + " items", "only " + items.length + " items");
		
		for (int i=0; i<items.length; ++i){
			Data d = parsers[i];
			// TODO: remove trim as a strict format
			d.strictParse(items[i].trim());
		}
	}
	
	public String[] display(){
		String[] strings = new String[this.getDataList().length << 1];
		for (int i=0; i<this.getDataList().length; ++i){
			strings[ i << 1 ] = this.getEnumList()[i].name();
			strings[ (i << 1) + 1] = this.getDataList()[i].toHumanString();
		}
		return strings;		
	}
	
	@Override
	public String toString(){
		String[] ret = display();
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<ret.length; i+=2)
			sb.append(ret[i]).append("\t").append(ret[i+1]).append("\n");
		return sb.toString();
	}
	
	public static void main(String[] argv){
		GGA.test();
		RMC.test();
		GLL.test();
		VDM.test();
	}

}
