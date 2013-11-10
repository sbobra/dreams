package com.example.dreams.view;

import com.example.dreams.R;

public class Constants {
	public static String[] colors = { "Red", "Green", "Blue" };
	public static int[] colorsIDs = { R.color.red, R.color.green, R.color.blue };
	public static String[] emotions = { "Good", "Neutral", "Bad" };
	public static int[] emotionFadedIDs = { R.drawable.ic_happy_black,
			R.drawable.ic_meh_black, R.drawable.ic_sad_black };
	public static int[] emotionIDs = { R.drawable.ic_positive,
			R.drawable.ic_neutral, R.drawable.ic_negative };
	public static int fileNameLength = 10;

	public static String getFileName(int fileNum) {
		String numString = String.valueOf(fileNum);
		int digits = numString.length();
		for (int i = 0; i < Constants.fileNameLength - digits; i++) {
			numString = "0" + numString;
		}
		return numString;
	}
}
