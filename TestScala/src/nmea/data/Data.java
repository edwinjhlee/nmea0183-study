package nmea.data;

import java.util.*;

import nmea.old.BasicFormat.GeoDegree;
import nmea.old.BasicFormat.UTC;

public abstract class Data<T, SUPER> {
	T value;
	HashSet<T> set;
	
	boolean nullable;
	public SUPER permitNull(){
		this.nullable = true;
		return (SUPER) this;
	}
	
	int fixlen;
	public SUPER fixlen(int fixlen){
		this.fixlen = fixlen;
		return (SUPER) this;
	}
	
	public static void checkFixLenIfExist(String content, int fixlen){
		if ((fixlen != NO_FIX_LENGTH) && (content.length() != fixlen))
			throw createIllegalArgumentException("size of string is " + fixlen, content);		
	}
	
	public final static int NO_FIX_LENGTH = -1;
	public final static String NULL_VALUE = "NULL_VALUE";
	
	public static String ERROR_MESSAGE_FORMAT = "Parameter passed in error. Expected %s, but got %s";
	
	public static IllegalArgumentException createIllegalArgumentException(Object expected, Object actual){
		return new IllegalArgumentException(
				String.format(ERROR_MESSAGE_FORMAT, expected, actual));
	}
	
	public static void rangeCheck(int value, int start, int end){
		if ((value < start) || (value > end))
			throw Data.createIllegalArgumentException(String.format("[%d, %d]", start, end), value);
	}
	
	public Data(boolean nullable, T ... values){
		this.nullable = nullable;
		this.fixlen = NO_FIX_LENGTH;
		this.set = new HashSet<T>();
		for (T v : values) this.set.add(v);
	}
	
	public Data(){
		this.nullable = false;
		this.fixlen = NO_FIX_LENGTH;
	}
	
	public Data(T ... values){
		this();
		this.set = new HashSet<T>();
		for (T v : values) this.set.add(v);
	}
	
//	public boolean inSet(T t){
//		return this.set.contains(t);
//	}
	
	public String getValidValues(){
		return this.set!=null ? this.set.toString() : "EMPTY_SET";
	}
	
//	public boolean isValid(T t){
//		return (this.set == null) || (this.set.contains(t));
//	}
	
	public boolean rangeCheck(T t){
		if (this.set == null)
			return false;
		if (this.set.contains(t))
			return true;
		throw new IllegalArgumentException(
			String.format(ERROR_MESSAGE_FORMAT, getValidValues(), t==null ? "NULL" : t.toString()));
	}
	
	public boolean nullString(String content){
		if ("".equals(content)){
			if (this.nullable)
				this.value = null;
			else
				throw createIllegalArgumentException(getValidValues(), "NULL_STRING");
			return true;
		}
		return false;
	}
	
	public void strictParse(String content){
		if (nullString(content)) return;
		checkFixLenIfExist(content, fixlen);
		parse(content);
	}
	
	protected abstract void parse(String content);
	
	@Override
	public String toString(){
		if (this.value != null)
			return this.value.toString();
		// When this.value is null
		if (this.nullable == false)
			throw createIllegalArgumentException("Not NULL value", "Null value");
		return NULL_VALUE;
	}
	
	public String toHumanString(){
		return this.toString();
	}
	
	public static class Int extends Data<Integer, Int>{
		int start, end;
		
		public Int setRange(int startInclusive, int endInclusive){
			this.start = startInclusive;
			this.end = endInclusive;	
			return this;
		}
		
		public Int(){}
		
		public Int(Integer[] values){
			super(values);
		}
		
		
		public void parse(String content){
			this.value = Integer.parseInt(content);
			if (rangeCheck(this.value)) return;
			Data.rangeCheck(this.value, start, end);
		}	
	}

	public static class _UnsignedFloat implements Comparable<_UnsignedFloat>{
		final int intVal;
		final int decVal;
		public _UnsignedFloat(int intVal, int decVal){
			this.intVal = intVal;
			this.decVal = decVal;
		}

		@Override
		public int compareTo(_UnsignedFloat o) {
			return (this.intVal != o.intVal) ? (this.intVal - o.intVal) : (this.decVal - o.decVal);
		}
		
		int getMax(int v){
			int n = 1;
			while (n < v) n *= 10;
			return n;
		}
		
		public float value(){
			return (float)intVal + (float)decVal / (float)getMax(decVal);
		}
		
		@Override
		public String toString(){
			return String.format("%d.%d", intVal, decVal);
//			return intVal + "." + decVal;
		}
		
	}
	
	public static class UnsignedFloat extends Data<_UnsignedFloat, UnsignedFloat>{
		
		int intFixDigitLen;
		int decFixDigitLen;
		
		public UnsignedFloat intFixLen(int intFixDigitLen){
			this.intFixDigitLen = intFixDigitLen;
			return this;
		}
		
		public UnsignedFloat decFixLen(int decFixDigitLen){
			this.decFixDigitLen = decFixDigitLen;
			return this;
		}
		
		public UnsignedFloat() {
			this.intFixDigitLen = NO_FIX_LENGTH;
			this.decFixDigitLen = NO_FIX_LENGTH;
		}

		@Override
		protected void parse(String content) {			
			int pos = content.indexOf(".");
			if (pos == -1){
				checkFixLenIfExist(content, intFixDigitLen);
				checkFixLenIfExist("", decFixDigitLen);
				this.value = new _UnsignedFloat(Integer.parseInt(content), 0);
			}else{
				String a = content.substring(0, pos);
				String b = content.substring(pos+1);
				checkFixLenIfExist(a, intFixDigitLen);
				checkFixLenIfExist(b, decFixDigitLen);
				this.value = new _UnsignedFloat(Integer.parseInt(a), Integer.parseInt(b));
			}
		}
		
		// You should move the toString upward
		
		@Override
		public String toString(){
			if (this.value == null)
				return NULL_VALUE;
			
			String intLen =  this.intFixDigitLen == NO_FIX_LENGTH ? "%d" : "%" + this.intFixDigitLen + "d";
			String decLen =  this.decFixDigitLen == NO_FIX_LENGTH ? "%d" : "%" + this.decFixDigitLen + "d";
			return String.format(intLen+"."+decLen, this.value.intVal, this.value.decVal);
		}
		
	}
	
	public static class _SignedFloat extends _UnsignedFloat{
		boolean sign;
		public _SignedFloat(boolean sign, int intVal, int decVal){
			super(intVal, decVal);
			this.sign = sign;			
		}

		@Override
		public int compareTo(_UnsignedFloat o) {
			// TODO: Could be safer
			return (int)(this.value() - o.value());
		}
		
		int getMax(int v){
			int n = 1;
			while (n < v) n *= 10;
			return n;
		}
		
		public float value(){
			float value = (float)intVal + (float)decVal / (float)getMax(decVal);
			return sign ? value : -value;
		}
		
		@Override
		public String toString(){
			return (sign?"":"-") + super.toString();
		}
		
	}
	
	public static class SignedFloat extends Data<_SignedFloat, SignedFloat>{
		
		int intFixDigitLen;
		int decFixDigitLen;
		
		public SignedFloat intFixLen(int intFixDigitLen){
			this.intFixDigitLen = intFixDigitLen;
			return this;
		}
		
		public SignedFloat decFixLen(int decFixDigitLen){
			this.decFixDigitLen = decFixDigitLen;
			return this;
		}
		
		public SignedFloat() {
			this.intFixDigitLen = NO_FIX_LENGTH;
			this.decFixDigitLen = NO_FIX_LENGTH;
		}

		@Override
		protected void parse(String content) {
			char signCh = content.charAt(0);
			boolean sign = signCh != '-';
			if ((signCh == '+') || (signCh == '-'))
				content = content.substring(1);
				
			int pos = content.indexOf(".");
			if (pos == -1){
				checkFixLenIfExist(content, intFixDigitLen);
				checkFixLenIfExist("", decFixDigitLen);
				this.value = new _SignedFloat(sign, Integer.parseInt(content), 0);
			}else{
				String a = content.substring(0, pos);
				String b = content.substring(pos+1);
				checkFixLenIfExist(a, intFixDigitLen);
				checkFixLenIfExist(b, decFixDigitLen);
				this.value = new _SignedFloat(sign, Integer.parseInt(a), Integer.parseInt(b));
			}
		}
		
		// You should move the toString upward
		
		@Override
		public String toString(){
			if (this.value == null)
				return NULL_VALUE;
			
			String intLen =  this.intFixDigitLen == NO_FIX_LENGTH ? "%d" : "%" + this.intFixDigitLen + "d";
			String decLen =  this.decFixDigitLen == NO_FIX_LENGTH ? "%d" : "%" + this.decFixDigitLen + "d";
			return String.format((this.value.sign?"":"-") + intLen+"."+decLen, this.value.intVal, this.value.decVal);
		}
		
	}
	
	
	public static class StateSet<E extends Enum> extends Int{
		final HashMap<Integer, E> stateMap = new HashMap<>();
		final HashMap<E, Integer> stateInfo = new HashMap<>();

		public StateSet(E[] states, Integer ...  values) {
			super(values);
			for (int i=0; i<values.length; ++i){
				stateMap.put(values[i], states[i]);
				stateInfo.put(states[i], values[i]);
			}
		}
		
		@Override
		public String toString(){
			return String.format("%d => %s", this.value, this.stateMap.get(this.value));
		}
	}

	public static class EnumInfo<E extends Enum> extends Data<E, EnumInfo>{
		
		// Maybe we could get rid of this.
//		final HashMap<String, E> stateMap = new HashMap<>();
		
		final HashMap<E, String> stateInfo = new HashMap<>();
		
		// TODO: There must be a better way.
		final HashMap<String, E> enumValueOf = new HashMap<>();

		public EnumInfo(E[] states, String ...  values) {
			super(states);
			for (int i=0; i<values.length; ++i){
//				stateMap.put(values[i], states[i]);
				enumValueOf.put(states[i].name(), states[i]);				
				stateInfo.put(states[i], values[i]);
			}
		}
		
		@Override
		public String toString(){
			return String.format("%s => %s", this.value == null ? NULL_VALUE : this.value.toString(), this.stateInfo.get(this.value));
		}

		@Override
		protected void parse(String content) {
			E e = enumValueOf.get(content);
			this.value = e;
			if (this.rangeCheck(e)) return;
		}
	}
	
//	public static class StringInfo<E extends Enum> extends AString<StringInfo>{
//		final HashMap<String, E> stateMap = new HashMap<>();
//		final HashMap<E, String> stateInfo = new HashMap<>();
//
//		public StringInfo(E[] states, String ...  values) {
//			super(values);
//			for (int i=0; i<values.length; ++i){
//				stateMap.put(values[i], states[i]);
//				stateInfo.put(states[i], values[i]);
//			}
//		}
//		
//		@Override
//		public String toString(){
//			return String.format("%s => %s", this.value, this.stateMap.get(this.value));
//		}
//
//	}
	
	public static class Char extends Data<Character, Char>{

		public Char(Character ... values){
			super(values);
		}
		
		@Override
		public void parse(String content) {
			this.value = content.charAt(0);
			rangeCheck(this.value);
		}
		
	}
	
	public class CheckSum extends Data<Integer, CheckSum>{
		public void parse(String content){
			if (nullString(content)) return;
			this.value = Integer.parseInt(content, 16);
		}
		@Override
		public String toString(){
			String s = Integer.toHexString(this.value);
			return (this.value < 16) ? "0" + s : s;
		}	
	}
	
	public static class ConstantLetter extends Data<Character, ConstantLetter>{

		public ConstantLetter(char ch) {
			this.value = ch;
		}
		
		@Override
		public void parse(String content) {
			if ((content.length()!=1) || (content.charAt(0) != this.value.charValue())){
				throw createIllegalArgumentException(this.value.toString(), content=="" ? NULL_VALUE : content);
			}
		}
		
		public final static ConstantLetter E = new ConstantLetter('E');
		public final static ConstantLetter M = new ConstantLetter('M');
	}
	
	public static class AString<E extends AString> extends Data<String, E>{
		
		public AString(){}
		
		public AString(String ...  values) {
			super(values);
		}
		
		@Override
		protected void parse(String content) {
			this.value = content;			
		}
	}
	
	public static class ConstantString extends Data<String, ConstantString>{

		public ConstantString(String string) {
			this.value = string;
		}
		
		@Override
		public void parse(String content) {
			if (content.equals(this.value) == false){
				throw createIllegalArgumentException(this.value, content=="" ? NULL_VALUE : content);
			}
		}
		
	}
	
}
