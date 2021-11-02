import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

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

	public ArrayList<Document> search(String keywords, boolean andOperation, boolean exactOperation) {
		this.setKeywords(keywords);
		Set<Document> matchingDocuments = new HashSet<>();
		this.manager.getFiles().forEach(document -> {
			int matchCounter = 0;
			for (String keyword : this.keywordsCollection) {
				try {
					String content = document.getContentText();
					// or, exact
					// and, exact
					// and, not exact
					// or, not exact
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
				} catch (IOException e) {
					System.out.println("Cannot find or support specified file");
					e.printStackTrace();
				}
			}
		});
		return new ArrayList<Document>(matchingDocuments);
	}

	public ArrayList<Document> filter(String fileExtension, ArrayList<Document> documents) {
		ArrayList<Document> filteredList = documents;
		filteredList.removeIf(doc -> !doc.toString().contains(fileExtension));
		return filteredList;
	}

	public ArrayList<Document> filter(long sizeMin, long sizeMax, ArrayList<Document> documents) {
		ArrayList<Document> filteredList = documents;
		filteredList.removeIf(doc -> doc.length() < sizeMin || doc.length() > sizeMax);
		return filteredList;
	}

	public ArrayList<Document> sortByFileType(ArrayList<Document> documents, boolean ascending) {
		Collections.sort(documents, new Comparator<Document>() {
			@Override
			public int compare(Document d1, Document d2) {
				return d1.getFileExtension().compareTo(d2.getFileExtension());
			}
		});
		if (!ascending)
			Collections.sort(documents, Collections.reverseOrder());
		return documents;
	}

	public ArrayList<Document> sortByDateModified(ArrayList<Document> documents, boolean ascending) {
		Collections.sort(documents, new Comparator<Document>() {
			@Override
			public int compare(Document d1, Document d2) {
				return d1.getLastModified().compareTo(d2.getLastModified());
			}
		});
		if (!ascending)
			Collections.sort(documents, Collections.reverseOrder());
		return documents;
	}
	
	public ArrayList<Document> sortBySize(ArrayList<Document> documents, boolean ascending) {
		Collections.sort(documents, new Comparator<Document>() {
			@Override
			public int compare(Document d1, Document d2) {
				if (d1.length() > d2.length())
					return 1;
				else if (d1.length() < d2.length())
					return -1;
				else 
					return 0;
			}
		});
		if (!ascending)
			Collections.sort(documents, Collections.reverseOrder());
		return documents;
	}
	
	public ArrayList<Document> sortByMatches(ArrayList<Document> documents, boolean ascending) {
		Collections.sort(documents, new Comparator<Document>() {
			@Override
			public int compare(Document d1, Document d2) {
				if (getTextMatchesByDocument(d1) > getTextMatchesByDocument(d2))
					return 1;
				else if (getTextMatchesByDocument(d1) < getTextMatchesByDocument(d2))
					return -1;
				else 
					return 0;
			}
		});
		if (!ascending)
			Collections.sort(documents, Collections.reverseOrder());
		return documents;
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
