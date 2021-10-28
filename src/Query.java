import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Query {

	private FileManagement manager;
	private String[] keywordsCollection;
	private File imageZipFolder;

	Query(FileManagement manager) {
		this.manager = manager;
		this.keywordsCollection = null;
		this.imageZipFolder = null;
	}

//	Query(FileManagement manager, String keywords) {
//		this.manager = manager;
//		this.setKeywords(keywords);
//		imageZipFolder = null;
//	}
//
//	Query(FileManagement manager, String keywords, String imageZipPath) {
//		this.manager = manager;
//		this.setKeywords(keywords);
//		this.setImageZipFolder(imageZipPath);
//	}

	public ArrayList<Document> search() {
		ArrayList<Document> matchingDocuments = new ArrayList<>();
		this.manager.getFiles().forEach(document -> {
			try {
				String content = document.getContentText();
				for (String keyword : this.keywordsCollection) {
					if (content.contains(keyword)) {
						matchingDocuments.add(document);
					}
				}
			} catch (IOException e) {
				System.out.println("Error: File not found or supported.");
				e.printStackTrace();
			}
		});
		return matchingDocuments;
	}

	public ArrayList<Document> filter(String fileExtension) {
		ArrayList<Document> filteredList = this.manager.getFiles();
		filteredList.removeIf(doc -> !doc.toString().contains(fileExtension));
		return filteredList;
	}

	public ArrayList<Document> filter(long sizeMin, long sizeMax) {
		ArrayList<Document> filteredList = this.manager.getFiles();
		filteredList.removeIf(doc -> doc.length() < sizeMin && doc.length() > sizeMax);
		return filteredList;
	}

	public int getTextMatchesByDocument(Document document) {
		try {
			String content = document.getContentText();
			int matches = 0;
			for (String keyword : this.keywordsCollection) {
				matches += StringUtils.countMatches(content, keyword);
			}
			return matches;

		} catch (IOException e) {
			System.out.println("Error: File not found or supported.");
			e.printStackTrace();
		}
		return 0;
	}

	public void setKeywords(String keywords) {
		this.keywordsCollection = keywords.split(",");
		for (String str : keywordsCollection) {
			str.trim();
		}
	}

	public void setImageZipFolder(String imageZipPath) {
		this.imageZipFolder = new File(imageZipPath);
	}

	public String[] getKeywords() {
		return this.keywordsCollection;
	}

	public File getImageZipFolder() {
		return this.imageZipFolder;
	}

}
