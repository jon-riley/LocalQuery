import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class Document extends File {
	
	private String path;
	
	public Document(String filePath) {
		super(filePath);
		this.path = filePath;
	}
	
	public String getFileExtension() {
		String path = this.toString();
		int index = path.lastIndexOf('.');
		if (index > 0) {
			String ext = path.substring(index+1);
			return ext;
		} else {
			return "";
		}
	}
	
	public String getContentText() throws IOException {
		if (this.getFileExtension().equals("txt")) {
			Scanner textScanner = new Scanner(new File(this.path));
			String textContent = "";
			while (textScanner.hasNextLine()) {
				textContent +=  " * " + textScanner.nextLine();
			}
			return textContent;
		} else if (this.getFileExtension().equals("pdf")) {
			PDDocument pdf = PDDocument.load(new File(this.path));
			return new PDFTextStripper().getText(pdf);
		} else {
			throw new Error("Error: File type not found or supported.");
		}
	}
}