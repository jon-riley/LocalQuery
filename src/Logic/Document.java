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
import java.awt.Color;

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
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.hwpf.extractor.Word6Extractor;
//import org.apache.poi.hwpf.extractor.WordExtractor;
//import org.apache.poi.hwpf.HWPFDocument;
//import org.apache.poi.hwpf.HWPFOldDocument;


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
//		case "doc":
//        	WordExtractor extractor = null;
//			Word6Extractor extractor6 = null;
//       		try { //newer doc file
//            	InputStream fis = new FileInputStream(this.file);
//            	HWPFDocument document = new HWPFDocument(fis);
//            	extractor = new WordExtractor(document);
//            	String fileData = extractor.getText();
//            	return fileData;
//        	} catch (Exception old) {
//            	try { //pre-2007 doc file
//            		POIFSFileSystem fis = new POIFSFileSystem(this.file);
//            		HWPFOldDocument document = new HWPFOldDocument(fis);
//            		extractor6 = new Word6Extractor(document);
//            		String fileData = extractor6.getText();
//            		return fileData;
//				} catch (IOException e) {
//            		e.printStackTrace();
//        		}
//        	}
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

	public boolean compareImages() throws IOException {
		//replace with user image
		BufferedImage img1 = ImageIO.read(new File("D:\\Images\\test1.jpg"));
		//replace with document image
      	BufferedImage img2 = ImageIO.read(new File("D:\\Images\\test2.jpg"));
		  
     	int w1 = img1.getWidth();
     	int w2 = img2.getWidth();
        int h1 = img1.getHeight();
        int h2 = img2.getHeight();
        if ((w1!=w2)||(h1!=h2)) {
           System.out.println("Both images should have same dimensions");
		   return false;
        } else {
           long diff = 0;
           for (int j = 0; j < h1; j++) {
              for (int i = 0; i < w1; i++) {
                 //Getting the RGB values of a pixel
                 int pixel1 = img1.getRGB(i, j);
                 Color color1 = new Color(pixel1, true);
                 int r1 = color1.getRed();
                 int g1 = color1.getGreen();
                 int b1 = color1.getBlue();
                 int pixel2 = img2.getRGB(i, j);
                 Color color2 = new Color(pixel2, true);
                 int r2 = color2.getRed();
                 int g2 = color2.getGreen();
                 int b2= color2.getBlue();
                 //sum of differences of RGB values of the two images
                 long data = Math.abs(r1-r2)+Math.abs(g1-g2)+ Math.abs(b1-b2);
                 diff = diff+data;
              }  
           }
          double avg = diff/(w1*h1*3);
          double percentage = (avg/255)*100;
          System.out.println("Difference: "+percentage);

		//if similarity is above 70% then it's a match (can change easily)
		  if (percentage >= 70) {
			    return true;
		  } else {
			    return false;
		  }
      }			
	}
}