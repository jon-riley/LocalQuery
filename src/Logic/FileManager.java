package Logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class FileManager {

	public static final ArrayList<String> SUPPORTED_FILE_TYPES = new ArrayList<>(
			Arrays.asList("pdf", "txt", "docx", "pptx", "xlsx")
			);
	
	private File root;
	private HashSet<Document> documentPaths;
	private ArrayList<Document> documentMatches;

//	image file path and buffer image
	private File userImagePath;
	private BufferedImage userImage;

	public FileManager(File root) {
		if (root.isFile()) {
			throw new Error("Error: root is not a folder.");
		}
		this.documentPaths = new HashSet<Document>();
		this.root = root;
		this.documentMatches = this.getFiles();
		this.userImage = null;
		this.userImagePath = null;
	}

	public ArrayList<Document> getFiles() {
		this.documentMatches = new ArrayList<Document>(this.listFilesOfFolder(root));
		return this.documentMatches;
//		return new ArrayList<Document>(this.listFilesOfFolder(root));
	}

	private HashSet<Document> listFilesOfFolder(File folder) {
		if (folder.isDirectory()) {
			try {
				DirectoryStream<Path> ds;
				ds = Files.newDirectoryStream(Paths.get(folder.toString()));
				ds.forEach(path -> {
					File fileEntry = path.toFile();
					Document document = new Document(fileEntry.toString());
					if (!document.getFileExtension().equals("zip") && document.isDirectory() && !document.getName().contains("~$")) {
						listFilesOfFolder(fileEntry);
					}
					switch (document.getFileExtension()) {
					case "txt":
					case "pdf":
					case "docx":
					case "xlsx":
					case "pptx":
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

	private boolean validateFileExtensions(ArrayList<String> fileExtensions) {
		for (String fileExtension : fileExtensions) {
			if (!SUPPORTED_FILE_TYPES.contains(fileExtension)) {
				System.out.println("A file extension in list is not supported");
				return false;
			}
		}
		return true;
	}

	public ArrayList<Document> filterByFileType(ArrayList<String> fileExtensions) {
		if (validateFileExtensions(fileExtensions)) {
			ArrayList<Document> filteredList = this.documentMatches;
			filteredList.removeIf(doc -> {				
				for (String fileExtension : fileExtensions) {
					if (doc.toString().contains(fileExtension) && SUPPORTED_FILE_TYPES.contains(fileExtension)) 
						return false;
				}
				return true;
			});
			return filteredList;
		}
		throw new Error("Could not validate all file types in passed list.");
	}

	public ArrayList<Document> filterBySize(long sizeMin, long sizeMax, ArrayList<Document> tempList) {
		tempList.removeIf(doc -> doc.length() < sizeMin || doc.length() > sizeMax);
		return tempList;
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

	public void setUserImagePath(String imagePath) {
		setUserImagePath(new File(imagePath));
	}

	public void setUserImagePath(File imagePath) {
		if (!imagePath.isFile())
			return;
		try {
			this.userImagePath = imagePath;
			this.userImage = ImageIO.read(imagePath);
		} catch (IOException e) {
			System.out.println("File cannot produce BufferedImage object. Check file type.");
			e.printStackTrace();
		}
	}

	public File getUserImagePath() {
		return this.userImagePath;
	}

	public BufferedImage getUserImage() {
		return this.userImage;
	}

	public void setDocumentMatches(ArrayList<Document> list) {
		this.documentMatches = list;
	}
}
