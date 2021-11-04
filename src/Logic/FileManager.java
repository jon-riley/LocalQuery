package Logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class FileManager {

	private File root;
	private HashSet<Document> documentPaths;
	
	FileManager(File root) {
		if (root.isFile()) {
			throw new Error("Error: root is not a folder.");
		}
		this.documentPaths = new HashSet<Document>();
		this.root = root;
	}

	public ArrayList<Document> getFiles() {
		return new ArrayList<Document>(this.listFilesOfFolder(root));
	}

	private HashSet<Document> listFilesOfFolder(File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesOfFolder(fileEntry);
			} else {
				Document document = new Document(fileEntry.toString());
				switch (document.getFileExtension()) {
				case "txt":
				case "pdf":
				case "doc":
				case "docx":
					documentPaths.add(new Document(fileEntry.toString()));
				}
			}
		}
		return documentPaths;
	}

	public ArrayList<Document> filterByFileType(String fileExtension) {
		ArrayList<Document> filteredList = this.getFiles();
		filteredList.removeIf(doc -> !doc.toString().contains(fileExtension));
		return filteredList;
	}

	public ArrayList<Document> filterBySize(long sizeMin, long sizeMax) {
		ArrayList<Document> filteredList = this.getFiles();
		filteredList.removeIf(doc -> doc.length() < sizeMin || doc.length() > sizeMax);
		return filteredList;
	}

	public ArrayList<Document> sortByFileName(boolean ascending) {
		ArrayList<Document> documents = this.getFiles();
		Collections.sort(documents, new Comparator<Document>() {
			@Override
			public int compare(Document d1, Document d2) {
				return d1.getName().compareTo(d2.getName());
			}
		});
		if (!ascending)
			Collections.sort(documents, Collections.reverseOrder());
		return documents;
	}

	public ArrayList<Document> sortByFileType(boolean ascending) {
		ArrayList<Document> documents = this.getFiles();
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

	public ArrayList<Document> sortByDateModified(boolean ascending) {
		ArrayList<Document> documents = this.getFiles();
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

	public ArrayList<Document> sortBySize(boolean ascending) {
		ArrayList<Document> documents = this.getFiles();
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

	public ArrayList<Document> sortByMatches(boolean ascending) {
		ArrayList<Document> documents = this.getFiles();
		Query query = new Query(this);
		Collections.sort(documents, new Comparator<Document>() {
			@Override
			public int compare(Document d1, Document d2) {
				if (query.getTextMatchesByDocument(d1) > query.getTextMatchesByDocument(d2))
					return 1;
				else if (query.getTextMatchesByDocument(d1) < query.getTextMatchesByDocument(d2))
					return -1;
				else
					return 0;
			}
		});
		if (!ascending)
			Collections.sort(documents, Collections.reverseOrder());
		return documents;
	}
	
	public File getRoot() {
		return this.root;
	}

	public void setRoot(File newRoot) {
		this.root = newRoot;
	}
}
