import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class Query {

	private String[] keywordsCollection;
	private File imageZipFolder;
	private FileManager manager; 

	Query(FileManager manager) {
		this.manager = manager;
		this.keywordsCollection = null;
		this.imageZipFolder = null;
	}
	
	public ArrayList<Document> search(String keywords, boolean andOperation, boolean exactOperation) {
		this.setKeywords(keywords);
		Set<Document> matchingDocuments = new HashSet<>();
		this.manager.getFiles().forEach(document -> {
			int matchCounter = 0;
			for (String keyword : this.keywordsCollection) {
				try {
					// search first if filename matches
					if (StringUtils.containsIgnoreCase(document.getName(), keyword)) {
						matchingDocuments.add(document);
					} 
					// search otherwise for content 
					else {
						String content = document.getContentText();
						
						if (!andOperation && exactOperation) {
							if (content.contains(keyword)) {
								matchingDocuments.add(document);
							}
						} else if (andOperation && exactOperation) {
							if (content.contains(keyword)) {
								matchCounter++;
								if (matchCounter == this.keywordsCollection.length) {
									matchingDocuments.add(document);
								}
							}
						} else if (andOperation && !exactOperation) {
							if (StringUtils.containsIgnoreCase(content, keyword)) {
								matchCounter++;
								if (matchCounter == this.keywordsCollection.length) {
									matchingDocuments.add(document);
								}
							}
						} else if (!(andOperation && exactOperation)) {
							if (StringUtils.containsIgnoreCase(content, keyword)) {
								matchingDocuments.add(document);
							}
						}
					}
				} catch (IOException e) {
					System.out.println("Cannot find or support specified file");
					e.printStackTrace();
				}
			}
		});
		return new ArrayList<Document>(matchingDocuments);
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

	private void setKeywords(String keywords) {
		this.keywordsCollection = keywords.split(",");
		for (int i = 0; i < this.keywordsCollection.length; i++) {
			keywordsCollection[i] = keywordsCollection[i].trim();
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
