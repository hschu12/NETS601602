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

public class BS {
	private static String cvrnr = "57808268";
	private	static String subsystem = "KR9";
	private	static String dataSupplierReference = "0000000075";
	private	static String creationDate = "000000";
	private	static String creditorPBSno = "05387353";
	private	static String debitorGroupno = "00001";
	private	static String dato = "31122016";
	private	static String amountPerPerson = "0000000030000"; //300 kr
	private static String dataSupplierIdentification = "CAS";
	private static String mainTextLine = "Jaguar Club of Denmark medlemskab 2017";
	private static String numberOfSections = "00000000001"; // udregnes efter generering (antal sectioner)
	private static Integer numberOfPrefix42 = 0; // udregnes efter generering (antal prefix 042)
	private static Integer netAmount = 0; // udregnes efter generering (totale sum)
	private static Integer numberOfPrefix52 = 0; // udregnes efter generering (antal prefix 052)
	private static Integer numberOfPrefix22 = 0; // udregnes efter generering (antal prefix 022)
	private static Integer paymentNumber = 41440;





	public static void main(String[] args) {
		List<String> lines = new ArrayList<String>();
		int size = 0;
		if (args.length == 0) {
			System.out.println("Missing input file");
		}
		
		if (args.length > 1) {
			System.out.println("Too many arguments");
			System.exit(0);
		}
		
		try{
			lines = Files.readAllLines(Paths.get(args[0]), Charset.forName("UTF-16"));
		}
		catch (IOException e){
			System.out.println("Something went wrong: " + e);
		}	

		size = lines.size();
		System.out.println("Size is " + size);

		try {
	
				File file = new File("output.txt");
	
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
	
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);

				createDD601Start(bw);
				createDD601Second(bw);

				for(int i = 1; i < size; i++) {	
					String line = lines.get(i);
					if(line.length() <= 63) {
						continue;
					}
					String[] parts = line.split(";");
					System.out.println( i);
					System.out.println( i + " " + parts[3] + parts [4]);
					String addressname = "BS022" + creditorPBSno + "0240" + "0000" + "1" + debitorGroupno + "00000000000" + parts[0] + "000000000" + parts[3] + " " + parts[4];
					if (addressname.length() < 128) {
						int filler = 128 - addressname.length();
						for (int k = 0; k < filler; k++) {
							addressname += " ";
						}
					} 
					numberOfPrefix22++;
					bw.write(addressname + "\n");

					String address = "BS022" + creditorPBSno + "0240" + "0000" + "2" + debitorGroupno + "00000000000" + parts[0] + "000000000" + parts[5];
					if (address.length() < 128) {
						int filler = 128 - address.length();
						for (int k = 0; k < filler; k++) {
							address += " ";
						}
					} 
					numberOfPrefix22++;
					bw.write(address + "\n");

					if(!(parts[6].equals(""))) {
						System.out.println("if");
						String address2 = "BS022" + creditorPBSno + "0240" + "0000" + "3" + debitorGroupno + "00000000000" + parts[0] + "000000000" + parts[6];
						if (address2.length() < 128) {
							int filler = 128 - address2.length();
							for (int k = 0; k < filler; k++) {
								address2 += " ";
							}
						} 
						numberOfPrefix22++;
						bw.write(address2 + "\n");
					}
					
					String postnummer = "BS022" + creditorPBSno + "0240" + "0000" + "9" + debitorGroupno + "00000000000" + parts[0] + "000000000" + "               " + parts[7] + "DK";
					if (postnummer.length() < 128) {
						int filler = 128 - postnummer.length();
						for (int k = 0; k < filler; k++) {
							postnummer += " ";
						}
					} 
					numberOfPrefix22++;
					bw.write(postnummer + "\n");

					String dispatch = "BS022" + creditorPBSno + "0240" + "000" + "10" + debitorGroupno + "00000000000" + parts[0] + "                                        " + "0000000000" + "1" + "1";
					if (dispatch.length() < 128) {
						int filler = 128 - dispatch.length();
						for (int k = 0; k < filler; k++) {
							dispatch += " ";
						}
					} 
					numberOfPrefix22++;
					bw.write(dispatch + "\n");

					String amount = "BS042" + creditorPBSno + "0280" + "00000" + debitorGroupno +  "00000000000" + parts[0] + "000000000" + dato + "1" + amountPerPerson;
					String paymentNo = paymentNumber.toString();
					if (paymentNo.length() < 30) {
						int filler = 30 - paymentNo.length();
						for (int k = 0; k < filler; k++ ) {
							paymentNo += " ";
						}
					}

					amount += paymentNo + "00000000000000000        ";
					int sum = Integer.parseInt(amountPerPerson);
					netAmount = netAmount + sum;
					paymentNumber++;

					numberOfPrefix42++;
					bw.write(amount + "\n");

					String textLine1 = "BS052" + creditorPBSno + "0241" + "0000" + "1"  + debitorGroupno + "00000000000" + parts[0] + "000000000" + " " + "Jaguar Club of Denmark co-driver medlemskab";
					if (textLine1.length() < 128) {
						int filler = 128 - textLine1.length();
						for (int k = 0; k < filler; k++) {
							textLine1 += " ";
						}
					} 
					numberOfPrefix52++;
					bw.write(textLine1 + "\n");

					String textLine2 = "BS052" + creditorPBSno + "0241" + "0000" + "2"  + debitorGroupno + "00000000000" + parts[0] + "000000000" + " " + "fra 1/1 2017 til 31/12 2017";
					if (textLine2.length() < 128) {
						int filler = 128 - textLine2.length();
						for (int k = 0; k < filler; k++) {
							textLine2 += " ";
						}
					} 
					numberOfPrefix52++;
					bw.write(textLine2 + "\n");

					String textLine3 = "BS052" + creditorPBSno + "0241" + "0000" + "3"  + debitorGroupno + "00000000000" + parts[0] + "000000000" + " " + " ";
					if (textLine3.length() < 128) {
						int filler = 128 - textLine3.length();
						for (int k = 0; k < filler; k++) {
							textLine3 += " ";
						}
					} 
					numberOfPrefix52++;
					bw.write(textLine3 + "\n");

					String textLine4 = "BS052" + creditorPBSno + "0241" + "0000" + "4"  + debitorGroupno + "00000000000" + parts[0] + "000000000" + " " + "SWIFT-BIC: DABADKKK";
					if (textLine4.length() < 128) {
						int filler = 128 - textLine4.length();
						for (int k = 0; k < filler; k++) {
							textLine4 += " ";
						}
					} 
					numberOfPrefix52++;
					bw.write(textLine4 + "\n");

					String textLine5 = "BS052" + creditorPBSno + "0241" + "0000" + "5"  + debitorGroupno + "00000000000" + parts[0] + "000000000" + " " + "IBAN: DK90 3000 0001 4645 23";
					if (textLine5.length() < 128) {
						int filler = 128 - textLine5.length();
						for (int k = 0; k < filler; k++) {
							textLine5 += " ";
						}
					} 
					numberOfPrefix52++;
					bw.write(textLine5 + "\n");

					String textLine6 = "BS052" + creditorPBSno + "0241" + "0000" + "6"  + debitorGroupno + "00000000000" + parts[0] + "000000000" + " " + " ";
					if (textLine6.length() < 128) {
						int filler = 128 - textLine6.length();
						for (int k = 0; k < filler; k++) {
							textLine6 += " ";
						}
					} 
					numberOfPrefix52++;
					bw.write(textLine6 + "\n");

					String textLine7 = "BS052" + creditorPBSno + "0241" + "0000" + "7"  + debitorGroupno + "00000000000" + parts[0] + "000000000" + " " + "Mvh. Jaguar Club of Denmark";
					if (textLine7.length() < 128) {
						int filler = 128 - textLine7.length();
						for (int k = 0; k < filler; k++) {
							textLine7 += " ";
						}
					} 
					numberOfPrefix52++;
					bw.write(textLine7 + "\n");

				}

				createSectionEnd(bw);
				createDeliveryEnd(bw);

				bw.close();

		} catch (IOException e) {
				e.printStackTrace();
		}
	}

	public static void createDD601Start(BufferedWriter bw) {

		String fullFirstLine = "BS002" + cvrnr + subsystem + "0601" + dataSupplierReference;

		if (fullFirstLine.length() < 49) {
			int filler = 49 - fullFirstLine.length();
			for (int i = 0; i < filler; i++) {
				fullFirstLine += " ";
			}
		}
		fullFirstLine += creationDate;

		if (fullFirstLine.length() < 128) {
			int filler = 128 - fullFirstLine.length();
			for (int i = 0; i < filler; i++) {
				fullFirstLine += " ";
			}
		} 
		try{
			bw.write(fullFirstLine + "\n");
		}
		catch (IOException e) {
			System.out.println("Problem when writing Data delivery start - Collection Data");
		}
	}

	public static void createDD601Second(BufferedWriter bw) {

		if (mainTextLine.length() > 60) {
			System.out.println("Too long main text");
			System.exit(1);
		}
		
		String line = "BS012" + creditorPBSno + "0112";

		if (line.length() < 22) {
			int filler = 22 - line.length();
			for (int i = 0; i < filler; i++) {
				line += " ";
			}
		}
		line += debitorGroupno + dataSupplierIdentification;

		if (line.length() < 46) {
			int filler = 46 - line.length();
			for (int i = 0; i < filler; i++) {
				line += " ";
			}
		} 

		line += creationDate;

		if (line.length() < 68) {
			int filler = 68 - line.length();
			for (int i = 0; i < filler; i++) {
				line += " ";
			}
		} 

		line += mainTextLine;


		if (line.length() < 128) {
			int filler = 128 - line.length();
			for (int i = 0; i < filler; i++) {
				line += " ";
			}
		} 

		try{
			bw.write(line + "\n");
		}
		catch (IOException e) {
			System.out.println("Problem when writing section start - Collections");
		}
	}

	public static void createSectionEnd(BufferedWriter bw) {
		
		String line = "BS092" + creditorPBSno + "0112";

		if (line.length() < 22) {
			int filler = 22 - line.length();
			for (int i = 0; i < filler; i++) {
				line += "0";
			}
		}
		line += debitorGroupno;

		if (line.length() < 31) {
			int filler = 31 - line.length();
			for (int i = 0; i < filler; i++) {
				line += " ";
			}
		}

		String prefix42 = numberOfPrefix42.toString();
		if (prefix42.length() < 11) {
			int filler = 11 - prefix42.length();
			prefix42 = "";
			for (int i = 0; i < filler; i++) {
				prefix42 += "0";
			}
			prefix42 += numberOfPrefix42.toString();
		}

		String totalAmount = netAmount.toString();
		if (totalAmount.length() < 15) {
			int filler = 15 - totalAmount.length();
			totalAmount = "";
			for (int i = 0; i < filler; i++) {
				totalAmount += "0";
			}
			totalAmount += netAmount.toString();
		}

		String prefix52 = numberOfPrefix52.toString();
		if (prefix52.length() < 11) {
			int filler = 11 - prefix52.length();
			prefix52 = "";
			for (int i = 0; i < filler; i++) {
				prefix52 += "0";
			}
			prefix52 += numberOfPrefix52.toString();
		}

		line += prefix42 + totalAmount + prefix52;

		if (line.length() < 83) {
			int filler = 83 - line.length();
			for (int i = 0; i < filler; i++) {
				line += " ";
			}
		}

		String prefix22 = numberOfPrefix22.toString();
		if (prefix22.length() < 11) {
			int filler = 11 - prefix22.length();
			prefix22 = "";
			for (int i = 0; i < filler; i++) {
				prefix22 += "0";
			}
			prefix22 += numberOfPrefix22.toString();
		}

		line += prefix22;

		if (line.length() < 128) {
			int filler = 128 - line.length();
			for (int i = 0; i < filler; i++) {
				line += " ";
			}
		}

		try{
			bw.write(line + "\n");
		}
		catch (IOException e) {
			System.out.println("Problem when writing Section end");
		}
	}

	public static void createDeliveryEnd(BufferedWriter bw) {
		
		String prefix42 = numberOfPrefix42.toString();
		if (prefix42.length() < 11) {
			int filler = 11 - prefix42.length();
			prefix42 = "";
			for (int i = 0; i < filler; i++) {
				prefix42 += "0";
			}
			prefix42 += numberOfPrefix42.toString();
		}

		String totalAmount = netAmount.toString();
		if (totalAmount.length() < 15) {
			int filler = 15 - totalAmount.length();
			totalAmount = "";
			for (int i = 0; i < filler; i++) {
				totalAmount += "0";
			}
			totalAmount += netAmount.toString();
		}

		String line = "BS992" + cvrnr + subsystem + "0601" + numberOfSections + prefix42 + totalAmount + numberOfPrefix52;

		if (line.length() < 83) {
			int filler = 83 - line.length();
			for (int i = 0; i < filler; i++) {
				line += "0";
			}
		}
		
		String prefix22 = numberOfPrefix22.toString();
		if (prefix22.length() < 11) {
			int filler = 11 - prefix22.length();
			prefix22 = "";
			for (int i = 0; i < filler; i++) {
				prefix22 += "0";
			}
			prefix22 += numberOfPrefix22.toString();
		}

		line += prefix22;

		if (line.length() < 128) {
			int filler = 128 - line.length();
			for (int i = 0; i < filler; i++) {
				line += "0";
			}
		}

		try{
			bw.write(line);
		}
		catch (IOException e) {
			System.out.println("Problem when writing delivery end");
		}
	}
}