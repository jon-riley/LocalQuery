package Logic;

import java.awt.image.BufferedImage;
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
	private String[] imagePathsCollection;
	private FileManager manager;

	public Query(FileManager manager) {
		this.manager = manager;
		this.keywordsCollection = null;
	}
	
	public ArrayList<Document> search(String keywords, boolean andOperation, boolean exactOperation) {
		BufferedImage userImage = this.manager.getUserImage(); 	// check against file manager's bufferedimage for extracted images from file
		this.setKeywordCollection(keywords);
		Set<Document> matchingDocuments = new HashSet<>();
		this.manager.getFiles().forEach(document -> {
			if (userImage != null) { 
//				TODO:
//				extract document images and set to array/arraylist
//				opencv to compare bufferedimage objects - iterate through array/arraylist and compare to userImage
//				return document when matched
			}
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
	
	private boolean filenameMatchesKeywords(Document document) {
		for (String keyword : this.keywordsCollection) {
			if (document.getName().contains(keyword))
				return true;
		}
		return false;
	}
	
	public int getTextMatchesByDocument(Document document) {
		try {
			String content = document.getContentText();
			int matches = 0;
			for (String keyword : this.keywordsCollection) {
				matches += StringUtils.countMatches(content, keyword);
			}
			if (filenameMatchesKeywords(document)) 
				matches++;
			return matches;
		} catch (IOException e) {
			System.out.println("Error: File not found or supported.");
			e.printStackTrace();
		}
		return 0;
	}

	public void setKeywordCollection(String delimitedKeywords) {
		this.keywordsCollection = delimitedKeywords.trim().split("\\s*,\\s*");
	}
	
	public void setManager(FileManager manager) {
		this.manager = manager;
	}
	
	public void setImagePathsCollection(String[] imagePaths) {
		this.imagePathsCollection = imagePaths;
	}
	
	public String[] getKeywordCollection() {
		return this.keywordsCollection;
	}
	
	public String[] getImagePathsCollection() {
		return this.imagePathsCollection;
	}
	
	public FileManager getManager() {
		return this.manager;
	}

}
