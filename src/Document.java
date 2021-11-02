import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class Document extends File {
	
	private String path;
	private File file;
	
	public Document(String filePath) {
		super(filePath);
		this.path = filePath;
		this.file = new File(this.path);
	}
	
	public String getFileExtension() {
		String path = this.toString();
		int index = path.lastIndexOf('.');
		if (index > 0) 
			return path.substring(index+1);
		else 
			return "";
	}
	
	public String getLastModified() {
		SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return date.format((file.lastModified()));
	}
	
	public String getContentText() throws IOException {
		if (this.getFileExtension().equals("txt")) {
			Scanner textScanner = new Scanner(file);
			String textContent = "";
			while (textScanner.hasNextLine()) {
				textContent +=  " * " + textScanner.nextLine();
			}
			textScanner.close();
			return textContent;
		} else if (this.getFileExtension().equals("pdf")) {
			PDDocument pdf = PDDocument.load(file);
			return new PDFTextStripper().getText(pdf);
		} else {
			throw new Error("Error: File type not found or supported.");
		}
	}
	
}