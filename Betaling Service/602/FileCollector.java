import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FileCollector {

	public FileCollector () {

	}

	public void collectFiles(){
		try{ 
			File folder = new File("602Return/");
			File output = new File("output.txt");
	
			// if file doesnt exists, then create it
			if (!output.exists()) {
				output.createNewFile();	
			}
		
			FileWriter fw = new FileWriter(output.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);


			for (File file : folder.listFiles()) {
				List<String> lines = new ArrayList<String>();
				int size = 0;
				try{
					lines = Files.readAllLines(Paths.get(file.getPath()), Charset.forName("ISO-8859-1"));
				}
				catch (IOException e){
					System.out.println("Something went wrong: " + e);
				}	
		
				size = lines.size();
				for (int i = 2; i < size-2 ;  i++) {
					if(lines.get(i).length() < 128) {
						continue;
					}
					bw.write(lines.get(i) + "\n");
				}
			}

			bw.close();
		}
		catch (IOException e) {
				e.printStackTrace();
		}
	}
}