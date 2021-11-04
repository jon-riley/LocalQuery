package Logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class FileManager {

	private File root;
	private HashSet<Document> documentPaths;
	private ArrayList<Document> documentMatches;

	public FileManager(File root) {
		if (root.isFile()) {
			throw new Error("Error: root is not a folder.");
		}
		this.documentPaths = new HashSet<Document>();
		this.root = root;
		this.documentMatches = this.getFiles();
	}

	public ArrayList<Document> getFiles() {
		return new ArrayList<Document>(this.listFilesOfFolder(root));
	}

	private HashSet<Document> listFilesOfFolder(File folder) {
		if (folder.isDirectory()) {
			try {
				DirectoryStream<Path> ds;
				ds = Files.newDirectoryStream(Paths.get(folder.toString()));
				ds.forEach(path -> {
					File fileEntry = path.toFile();
					Document document = new Document(fileEntry.toString());
					if (!document.getFileExtension().equals("zip") && document.isDirectory()) {
							listFilesOfFolder(fileEntry);
					}
					switch (document.getFileExtension()) {
					case "txt":
					case "pdf":
						// case "doc":
						// case "docx":
						this.documentPaths.add(new Document(fileEntry.toString()));
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this.documentPaths;
	}

	public ArrayList<Document> search(String keywords, boolean andOperation, boolean exactOperation) {
		this.documentMatches = (new Query(this)).search(keywords, andOperation, exactOperation);
		return this.documentMatches;
	}

	public ArrayList<Document> filterByFileType(String fileExtension) {
		ArrayList<Document> filteredList = this.documentMatches;
		filteredList.removeIf(doc -> !doc.toString().contains(fileExtension));
		return filteredList;
	}

	public ArrayList<Document> filterBySize(long sizeMin, long sizeMax) {
		ArrayList<Document> filteredList = this.documentMatches;
		filteredList.removeIf(doc -> doc.length() < sizeMin || doc.length() > sizeMax);
		return filteredList;
	}

	public ArrayList<Document> sortByFileName(boolean ascending) {
		Collections.sort(this.documentMatches, new Comparator<Document>() {
			@Override
			public int compare(Document d1, Document d2) {
				return d1.getName().compareToIgnoreCase(d2.getName());
			}
		});

		if (!ascending)
			Collections.reverse(documentMatches);
			return this.documentMatches;
	}

	public ArrayList<Document> sortByFileType(boolean ascending) {
		Collections.sort(this.documentMatches, new Comparator<Document>() {
			@Override
			public int compare(Document d1, Document d2) {
				return d1.getFileExtension().compareTo(d2.getFileExtension());
			}
		});
		if (!ascending)
			Collections.reverse(documentMatches);
		return this.documentMatches;
	}

	public ArrayList<Document> sortByDateModified(boolean ascending) {
		Collections.sort(this.documentMatches, new Comparator<Document>() {
			@Override
			public int compare(Document d1, Document d2) {
				return d1.getLastModified().compareTo(d2.getLastModified());
			}
		});
		if (!ascending)
			Collections.reverse(documentMatches);
		return this.documentMatches;
	}

	public ArrayList<Document> sortBySize(boolean ascending) {
		Collections.sort(this.documentMatches, new Comparator<Document>() {
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
			Collections.reverse(documentMatches);
		return this.documentMatches;
	}

	public ArrayList<Document> sortByMatches(boolean ascending) {
		Query query = new Query(this);
		Collections.sort(this.documentMatches, new Comparator<Document>() {
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
			Collections.reverse(documentMatches);
		return this.documentMatches;
	}

	public void setRoot(File newRoot) {
		this.root = newRoot;
	}

	public File getRoot() {
		return this.root;
	}

}
