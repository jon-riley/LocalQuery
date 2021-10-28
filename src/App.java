import java.io.File;
import java.io.IOException;

public class App {

	public static void main(String[] args) throws IOException {
		// set up root
		File root = new File("C:/Users/Jon/Desktop/Dataset/");
		FileManagement fm = new FileManagement(root);
		
		// list filenames and their file types - tester
		fm.getFiles().forEach((document) -> System.out.println("View all documents... " + document.getName()));
		fm.getFiles().forEach((document) -> System.out.println("View all file types... " + document.getFileExtension()));
		
		// query search - tester
		Query query = new Query(fm);
		query.setKeywords("Hello");
		query.search().forEach(doc -> System.out.println("Query searching... " + doc.getName()));
		
		// query filters - tester
		query.filter("txt").forEach(doc -> System.out.println("Query filter for text... " + doc.getName()));
		query.filter("pdf").forEach(doc -> System.out.println("Query filter for pdf... " + doc.getName()));
		
	}	
}
