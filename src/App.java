import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;

public class App {

	public static void main(String[] args) throws IOException {
		// set up root
		File root = new File("C:/Users/Jon/Desktop/Dataset/");
		FileManagement fm = new FileManagement(root);
		
		// list filenames and their file types
		fm.getFiles().forEach((document) -> System.out.println(document.getName()));
		fm.getFiles().forEach((document) -> System.out.println(document.getFileExtension()));
		
	}	

}
