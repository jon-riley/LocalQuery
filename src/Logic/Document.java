package Logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

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
		switch (this.getFileExtension()) {
		case "txt": 
			Scanner textScanner = new Scanner(file);
			String textContent = "";
			while (textScanner.hasNextLine()) {
				textContent +=  " * " + textScanner.nextLine();
			}
			textScanner.close();
			return textContent;
		case "pdf": 
			PDDocument pdf = PDDocument.load(file);
			return new PDFTextStripper().getText(pdf);
		case "doc":
		case "docx":
			InputStream inputStream = new FileInputStream(this.file);
			XWPFDocument wordDocument = new XWPFDocument(inputStream);
			XWPFWordExtractor textExtractor = new XWPFWordExtractor(wordDocument);
			textContent = textExtractor.getText();
			textExtractor.close();
			return textContent;
		default:
			throw new Error("Error: File type not found or supported.");
		}
	}
	
}