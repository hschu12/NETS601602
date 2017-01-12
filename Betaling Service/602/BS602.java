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

public class BS602 {

	public static List<String> generateArrayOfLines(String path, String charset) {
		List<String> returnList = new ArrayList<String>();
		try{
			returnList = Files.readAllLines(Paths.get(path), Charset.forName(charset));
		}
		catch (IOException e){
			System.out.println("Something went wrong: " + e);
		}	
		return returnList;
	}

	public static void main(String[] args) {
		List<String> lines602 = new ArrayList<String>();
		List<String> lines601 = new ArrayList<String>();
		List<String> linesCSV = new ArrayList<String>();
		List<String> linesCodriver = new ArrayList<String>();
		List<String> lines601Codriver = new ArrayList<String>();



		FileCollector collector = new FileCollector();
		collector.collectFiles();
		
		lines601 = generateArrayOfLines("BS1601.txt", "ISO-8859-1");
		lines602 = generateArrayOfLines("output.txt", "UTF-8");
		linesCSV = generateArrayOfLines("Medlemsliste.csv", "UTF-16");
		linesCodriver = generateArrayOfLines("Codriver.csv", "UTF-16");
		lines601Codriver = generateArrayOfLines("Codriver601.txt", "ISO-8859-1");

		try {
				File file = new File("Manglende Betalinger.csv");
	
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
	
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("Status;" + linesCSV.get(0) + "\n");

				for (int i = 0; i<lines602.size(); i++) {
					String lineFrom602 = lines602.get(i);
					String type = lineFrom602.substring(14,17);
					//make check to see if payment amount is correct/corresponing to demanded amount

					if ( !((type.equals("297")) || (type.equals("236")))) {
						String referenceNumber = lineFrom602.substring(69, 74); // Only works with 5 char referencenumbers.

						for (int k = 0; k < lines601.size() ; k++ ) {
							String lineFrom601 = lines601.get(k);
							String ref601 = lineFrom601.substring(73,78);

							if ( ref601.equals(referenceNumber)) {
								String id = lineFrom601.substring(38,42);

								for (int j = 1; j< linesCSV.size(); j++) {
									String member = linesCSV.get(j);
									String[] parts = member.split(";");

									if (member.length()>63){	

										if (parts[0].equals(id)) {
											int typeInInt = Integer.parseInt(type);

											switch (typeInInt) {
												case 237:	bw.write("Afvist auto. betaling(" + type + ") ;" + member +"\n");
															break;
												case 238:	bw.write("Afmeldt auto. betaling (" + type + ") ;" + member +"\n");
															break;
												case 239:	bw.write("Tilbageført betaling(" + type + ") ;" + member +"\n");
															break;
												case 299:	bw.write("Tilbageført indbetalingskort (" + type + ") ;" + member +"\n");
															break;
												default:	System.out.println("Unknown case for: ID: " + id + " and ref: " + referenceNumber);
											}
										}
									}					
								}

							}
						}
//						System.out.println("Found one: " + type + ": " + referenceNumber);
					}
				}
				for (int i = 0; i<lines602.size(); i++) {
					String lineFrom602 = lines602.get(i);
					String type = lineFrom602.substring(14,17);
					if (type.equals("297")) {
						int demandAmount = Integer.parseInt(lineFrom602.substring(67,72));
						int recievedAmount = Integer.parseInt(lineFrom602.substring(123,128));
						if ( demandAmount > recievedAmount)	{
							String referenceNumber = lineFrom602.substring(72,77);
							for (int k = 0; k < lines601.size() ; k++ ) {
								String lineFrom601 = lines601.get(k);
								String ref601 = lineFrom601.substring(73,78);
								if ( ref601.equals(referenceNumber)) {

									String id = lineFrom601.substring(38,42);

									for (int j = 1; j< linesCSV.size(); j++) {
										String member = linesCSV.get(j);
										String[] parts = member.split(";");

										if (member.length()>63){		

											if (parts[0].equals(id)) {
												int diff = demandAmount - recievedAmount;
												bw.write("For lidt modtaget: Forventet beløb: " + demandAmount + ". Modtaget: " + recievedAmount + ". Mangler: " + diff + " ;" + member +"\n");
											}
										}
									}
								}
							}
						//	System.out.println("297 To little " + referenceNumber);

						}
					}
					else if(type.equals("236")) {
						int demandAmount = Integer.parseInt(lineFrom602.substring(64,69));
						int recievedAmount = Integer.parseInt(lineFrom602.substring(123,128));
						if ( demandAmount > recievedAmount)	{
							String referenceNumber = lineFrom602.substring(69,77);
							System.out.println("236 To little " + referenceNumber + ". Missing implementation");

						}			
					}
				}


				bw.close();

		} catch (IOException e) {
				e.printStackTrace();
		}

		try{
			ArrayList<String> approvedPayments = new ArrayList<String> ();

			File file = new File("Godkendte Betalinger.csv");
			File codrivers = new File("Godkendte Co-drivers.csv");
	
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
	
			FileWriter fw = new FileWriter(codrivers.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			FileWriter fw2 = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw2 = new BufferedWriter(fw2);

			bw.write(linesCodriver.get(0) + "\n");
			bw2.write(linesCSV.get(0) + "\n");

			for (int i = 0; i<lines602.size(); i++) {
					String lineFrom602 = lines602.get(i);
					String type = lineFrom602.substring(14,17);
					if (type.equals("297")) {
			
						String referenceNumber = lineFrom602.substring(72,77);
						for (int k = 0; k < lines601.size() ; k++ ) {
							String lineFrom601 = lines601.get(k);
							String ref601 = lineFrom601.substring(73,78);
							if ( ref601.equals(referenceNumber)) {

								String id = lineFrom601.substring(38,42);
								char[] idsplitted = id.toCharArray();
								int w=0;
								for (w = 0; w < idsplitted.length; w++ ) {
									if (idsplitted[w] != '0') {
										break;
									}
								}
								String realID = id.substring(w);
								for (int j = 1; j< linesCSV.size(); j++) {
									String member = linesCSV.get(j);
									String[] parts = member.split(";");
										
									if (member.length()>63){		

										if (parts[0].equals(realID)) {
											bw2.write(member +"\n");
										}
									}
								}
							}
						}
						for (int k = 0; k < lines601Codriver.size(); k++ ) {
							String lineFromCo601 = lines601Codriver.get(k);
							String ref601 = lineFromCo601.substring(73,78);
							if ( ref601.equals(referenceNumber)) {
								String id = lineFromCo601.substring(38,42);
								char[] idsplitted = id.toCharArray();
								int w=0;
								for (w = 0; w < idsplitted.length; w++ ) {
									if (idsplitted[w] != '0') {
										break;
									}
								}
								String realID = id.substring(w);
								for (int j = 1; j < linesCodriver.size() ; j++ ) {
									String member = linesCodriver.get(j);
									String[] parts = member.split(";");
									if (parts[0].equals(realID)) {
										bw.write(member +"\n");
									}
								}
							}
						}
					}
					else if(type.equals("236")) {
						String referenceNumber = lineFrom602.substring(69,74);
						for (int k = 0; k < lines601.size() ; k++ ) {
							String lineFrom601 = lines601.get(k);
							String ref601 = lineFrom601.substring(73,78);

							if ( ref601.equals(referenceNumber)) {
								String id = lineFrom601.substring(38,42);
								char[] idsplitted = id.toCharArray();
								int w=0;
								for (w = 0; w < idsplitted.length; w++ ) {
									if (idsplitted[w] != '0') {
										break;
									}
								}
								String realID = id.substring(w);
								for (int j = 1; j< linesCSV.size(); j++) {
									String member = linesCSV.get(j);
									String[] parts = member.split(";");
										
									if (member.length()>63){		

										if (parts[0].equals(realID)) {
											bw2.write(member +"\n");
										}
									}
								}
							}
						}			
					}
				}

			bw.close();
			bw2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}