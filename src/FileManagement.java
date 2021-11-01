import java.io.File;
import java.util.ArrayList;

public class FileManagement {
	
	private File root;
	
	FileManagement(File root) {
		if (root.isFile()) {
			throw new Error("Error: root is not a folder.");
		}
		this.root = root;
	}
	
	public ArrayList<Document> listFilesOfFolder(File folder) {
		ArrayList<Document> filePaths = new ArrayList<>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesOfFolder(fileEntry);
			} else {
				filePaths.add(new Document(fileEntry.toString()));
			}
		}
		return filePaths;
	}
	
	public ArrayList<Document> getFiles() {
		return listFilesOfFolder(root);
	}
	
	public File getRoot() {
		return this.root;
	}
	
	public void setRoot(File newRoot) {
		this.root = newRoot;
	}
}
