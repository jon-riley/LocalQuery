package Logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
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
	
	public String getFilePathWithoutExtension() {
		String filename = "";
		try {
            if (file != null && file.exists()) {
                String name = file.toString();
                filename = name.replaceFirst("[.][^.]+$", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return filename;
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
			PDFTextStripper textStripper = new PDFTextStripper();
			textContent = textStripper.getText(pdf);		
			pdf.close();
			return textContent;
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
	
	public ArrayList<BufferedImage> getContentImages() {
		ArrayList<BufferedImage> images = new ArrayList<>();
		try {
			switch(this.getFileExtension()) {
			case "pdf": 
				PDDocument pdf = PDDocument.load(this.file);
				for (PDPage page : pdf.getPages()) {
					for (COSName name : page.getResources().getXObjectNames()) {
						PDXObject obj = page.getResources().getXObject(name);
						if (obj instanceof PDImageXObject) {
							PDImageXObject image = (PDImageXObject)obj;
							images.add(image.getImage());
						}
					}
				}				
				pdf.close();
				break;
			default:
				throw new Error("Error: File type not found or supported");
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return images;
	}
	
	public void extractImages() throws IOException {
		ArrayList<BufferedImage> images = this.getContentImages();
		int imageCounter = 1;
		for (BufferedImage image : images) {
			String filename = this.getFilePathWithoutExtension() + "-" + imageCounter + ".jpg";
			ImageIO.write(image, "jpg", new File(filename));
			imageCounter++;
		}
	}

}