package com.example.dreams.view;

public class Constants {
	public static String[] colors = {"Red", "Green", "Blue"};
	public static String[] emotions = {"Good", "Bad", "Neutral"};
	public static int fileNameLength = 10;
	
	public static String getFileName(int fileNum) {
		String numString = String.valueOf(fileNum);
		int digits = numString.length();
		for (int i = 0; i< Constants.fileNameLength-digits; i++) {
			numString = "0" + numString;
		}
		return numString;
	}
}
