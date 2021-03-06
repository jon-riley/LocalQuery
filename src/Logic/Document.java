package Logic;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;
import java.util.List;

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
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import org.apache.poi.hwpf.extractor.Word6Extractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.HWPFOldDocument;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.extractor.SlideShowExtractor;

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
			return path.substring(index + 1);
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
				textContent += " * " + textScanner.nextLine();
			}
			textScanner.close();
			return textContent;
		case "pdf":
			PDDocument pdf = PDDocument.load(file);
			PDFTextStripper textStripper = new PDFTextStripper();
			textContent = textStripper.getText(pdf);
			pdf.close();
			return textContent;
		case "docx":
			FileInputStream inputStream = new FileInputStream(this.file);
			XWPFDocument wordDocument = new XWPFDocument(inputStream);
			XWPFWordExtractor textExtractor = new XWPFWordExtractor(wordDocument);
			textContent = textExtractor.getText();
			textExtractor.close();
			return textContent;
		case "pptx":
			SlideShow<XSLFShape, XSLFTextParagraph> slideshow = new XMLSlideShow(new FileInputStream(this.file));
			SlideShowExtractor<XSLFShape, XSLFTextParagraph> slideShowExtractor = new SlideShowExtractor<XSLFShape, XSLFTextParagraph>(
					slideshow);
			slideShowExtractor.setCommentsByDefault(true);
			slideShowExtractor.setMasterByDefault(true);
			slideShowExtractor.setNotesByDefault(true);
			String allTextContentInSlideShow = slideShowExtractor.getText();
			slideShowExtractor.close();
			return allTextContentInSlideShow;
		case "xlsx":
			FileInputStream isxlsx = new FileInputStream(this.file);
			XSSFWorkbook xlsxFile = new XSSFWorkbook(isxlsx);
			XSSFExcelExtractor xlsxText = new XSSFExcelExtractor(xlsxFile);
			xlsxText.setFormulasNotResults(true);
			String xlsxContent = xlsxText.getText();
			xlsxText.close();
			return xlsxContent;
		default:
			throw new Error("Error: File type not found or supported.");
		}
	}

	public ArrayList<BufferedImage> getContentImages() {
		ArrayList<BufferedImage> images = new ArrayList<>();
		try {
			switch (this.getFileExtension()) {
			case "txt":
				break;
			case "pdf":
				PDDocument pdf = PDDocument.load(this.file);
				for (PDPage page : pdf.getPages()) {
					for (COSName name : page.getResources().getXObjectNames()) {
						PDXObject obj = page.getResources().getXObject(name);
						if (obj instanceof PDImageXObject) {
							PDImageXObject image = (PDImageXObject) obj;
							images.add(image.getImage());
						}
					}
				}
				pdf.close();
				break;
			case "docx":
				FileInputStream inputStream = new FileInputStream(this.file);
				XWPFDocument wordDocument = new XWPFDocument(inputStream);
				List<XWPFPictureData> piclistDocx = wordDocument.getAllPictures();
				Iterator<XWPFPictureData> iteratorDocx = piclistDocx.iterator();
				while (iteratorDocx.hasNext()) {
					XWPFPictureData pic = iteratorDocx.next();
					byte[] bytepicDocx = pic.getData();
					BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytepicDocx));
					images.add(image);
				}
				break;
			case "pptx":
				XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(this.file));
				for (XSLFPictureData data : ppt.getPictureData()) {
					byte[] bytes = data.getData();
					BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
					images.add(image);
				}
				break;
			case "xlsx":
				FileInputStream isxlsx = new FileInputStream(this.file);
				XSSFWorkbook workbookXLSX = new XSSFWorkbook(isxlsx);
				List<XSSFPictureData> listXLSX = workbookXLSX.getAllPictures();
				for (int i = 0; i < listXLSX.size(); i++) {
					PictureData picture = (PictureData) listXLSX.get(i);
					byte[] bytes = picture.getData();
					BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
					images.add(image);
				}
				break;
			default:
				System.out.println("document is " + this.toString());
				throw new Error("Error: File type not found or supported");
			}
		} catch (IOException e) {
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
		return;
	}

	public int compareImages(BufferedImage userInputImage) throws IOException {
		if (this.getContentImages().size() == 0) {
			return 0;
		}
		int similarCounter = 0;
		for (BufferedImage img : this.getContentImages()) {
			int w1 = userInputImage.getWidth();
			int w2 = img.getWidth();
			int h1 = userInputImage.getHeight();
			int h2 = img.getHeight();
			if ((w1 != w2) || (h1 != h2)) { // check if images have same dimensions
				
			} else {
				long diff = 0;
				for (int j = 0; j < h1; j++) {
					for (int i = 0; i < w1; i++) {
						// Getting the RGB values of a pixel
						int pixel1 = userInputImage.getRGB(i, j);
						Color color1 = new Color(pixel1, true);
						int r1 = color1.getRed();
						int g1 = color1.getGreen();
						int b1 = color1.getBlue();
						int pixel2 = img.getRGB(i, j);
						Color color2 = new Color(pixel2, true);
						int r2 = color2.getRed();
						int g2 = color2.getGreen();
						int b2 = color2.getBlue();
						// sum of differences of RGB values of the two images
						long data = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
						diff = diff + data;
					}
				}
				double avg = diff / (w1 * h1 * 3);
				double percentage = (avg / 255) * 100;

				if (percentage <= 5) {
					similarCounter++;
				}
			}
		}
		return similarCounter;
	}
}