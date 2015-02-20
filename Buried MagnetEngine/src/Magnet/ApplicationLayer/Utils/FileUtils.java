package Magnet.ApplicationLayer.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
	
	private FileUtils(){
	}
	
	public static String loadAsString(String path){
		String result = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(path)));
			String buffer = "";
			while((buffer = reader.readLine()) != null){
				result += buffer + "\n";
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
