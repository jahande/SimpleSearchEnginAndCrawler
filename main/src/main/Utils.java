package main;

public class Utils {
	public static boolean lastsWith(String source, String last){
		
		int i = source.lastIndexOf(last.charAt(0));
		return i!=-1 && source.substring(i).equals(last);
	}
}
