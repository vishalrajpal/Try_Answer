package test;

import java.util.HashMap;
import java.util.Map;

public class Utilities {

	public static Map<String, Integer> digits = new HashMap<>();
	public static Map<String, Integer> elevenToNineteen = new HashMap<>();
	public static Map<String, Integer> tees = new HashMap<>();
	public static Map<String, Integer> magnitudes = new HashMap<>();
	public static void initializeMaps() {
		if(digits.size() == 0 || tees.size() == 0) {
			digits.put("one", 1);
			digits.put("two", 2);
			digits.put("three", 3);
			digits.put("four", 4);
			digits.put("five", 5);
			digits.put("six", 6);
			digits.put("seven", 7);
			digits.put("eight", 8);
			digits.put("nine", 9);
			
			elevenToNineteen.put("eleven", 11);
			elevenToNineteen.put("twelve", 12);
			elevenToNineteen.put("thirteen", 13);
			elevenToNineteen.put("fourteen", 14);
			elevenToNineteen.put("fifteen", 15);
			elevenToNineteen.put("sixteen", 16);
			elevenToNineteen.put("seventeen", 17);
			elevenToNineteen.put("eighteen", 18);
			elevenToNineteen.put("nineteen", 19);
			
			tees.put("ten", 10);
			tees.put("twenty", 20);
			tees.put("thirty", 30);
			tees.put("forty", 40);
			tees.put("fifty", 50);
			tees.put("sixty", 60);
			tees.put("seventy", 70);
			tees.put("eighty", 80);
			tees.put("ninety", 90);
			
			magnitudes.put("hundred", 100);
			magnitudes.put("thousand", 1000);
			magnitudes.put("million", 1000000);
		}
	}
	
	public static int textToInt(String str) {
		initializeMaps();
		String[] strs = str.split(" ");
		int value = 0;
		String currentStr;
		int lastMagnitude = Integer.MIN_VALUE;
		int currentMagnitude;
		for(int strCounter = 0; strCounter < strs.length; strCounter++) {
			currentStr = strs[strCounter];
			if(digits.containsKey(currentStr) || elevenToNineteen.containsKey(currentStr) || tees.containsKey(currentStr)) {
				value += digits.get(strs[strCounter]);
			} else if(magnitudes.containsKey(currentStr)) {
				currentMagnitude = magnitudes.get(currentStr);
				if(currentMagnitude < lastMagnitude) {
					//TODO
					//value += textToInt(str.substring(str.inde))
				} else {
					value *= magnitudes.get(currentStr);
				}				
			}
		}
		return value;
	}
	
}
