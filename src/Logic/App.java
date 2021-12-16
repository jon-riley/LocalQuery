package Logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class App {

	public static void main(String[] args) throws IOException {
		// temporary console tester //
		
		// Enter root path
//		System.out.println("Please input a root path...(eg. C:/Users/Jon/Desktop/Data)");
//		Scanner sc = new Scanner(System.in);
//		String root = sc.nextLine();
//		System.out.println("You have entered " + root);
		
		Document doc = new Document("C:/Users/Jon/Desktop/Data/UNCC.docx");
		BufferedImage img = ImageIO.read(new File("C:/Users/Jon/Desktop/Data/boat.jpg"));
		
		System.out.println("Number of matches (hoping for one): " + doc.compareImages(img));
		
		
		
		
		
//		FileManager fm = new FileManager(new File(root));		
//		// Enter keywords
//		System.out.println("Enter comma delimited keywords to search... (eg. Hello, biology, investment)");
//		String keywords = sc.nextLine();
//		System.out.println("Your keywords: " + keywords);
//		
//		// Enter matching operation
//		System.out.println("OR or AND operation? (eg. docs only need to match at least one keyword, or must match all keywords)... ");
//		System.out.println("Enter OR or AND"); 
//		String keywordMatchingOperation = sc.nextLine();
//		System.out.println("You selected: " + keywordMatchingOperation);
//		boolean operation = false;
//		if (keywordMatchingOperation.equalsIgnoreCase("AND"))
//			operation = true;
//		
//		// Enter case operation
//		System.out.println("Would you like to case check? Y/N");
//		String caseCheck = sc.nextLine();
//		boolean caseOperation = true;
//		if (caseCheck.equals("N"))
//			caseOperation = false; 
//
//		// Query results
//		System.out.println("Search results...");
//		ArrayList<Document> results = fm.search(keywords, operation, caseOperation);
//		results.forEach(document -> System.out.println(document.getName()));
//		
//		// Filter all results
//		System.out.println("* * * * * * * * * * * * * * * * * * * * \n Filter by extension: txt, pdf..."); 
//		String ext = sc.nextLine();
//		ArrayList<String> testTypes = new ArrayList<>(Arrays.asList(ext));
//		fm.filterByFileType(testTypes).forEach(document -> System.out.println(document.getName()));
//		
//		
//		// Sort all results 
//		System.out.println("* * * * * * * * * * * * * * * * * * * * \n Sorting..."); 
//		// sort by date modified
//		System.out.println("Sorting by date modified");
//		fm.sortByDateModified(false).forEach(document -> System.out.println(document.getName()));
//		// sort by filename 
//		System.out.println("Sorting by filename"); 
//		fm.sortByFileName(true).forEach(document -> System.out.println(document.getName()));
//		sc.close();
	}	
}
