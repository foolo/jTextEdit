package texteditor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecentFilesCollection {

	private ArrayList<String> m_recentFiles = new ArrayList<>();

	public RecentFilesCollection(byte[] b) {
		m_recentFiles.clear();
		if (b == null) {
			return;
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bais);
			ArrayList<?> recentFiles = (ArrayList<?>) ois.readObject();
			for (Object o: recentFiles) {
				m_recentFiles.add((String)o);
			}
		}
		catch (IOException | ClassNotFoundException ex) {
			Logger.getLogger(RecentFilesCollection.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public ArrayList<String> GetRecentFiles() {
		return m_recentFiles;
	}

	public void AddFile(File f) {
		try {
			removeFile(f);
			String fullPathToAdd = f.getCanonicalPath();
			m_recentFiles.add(fullPathToAdd);
		}
		catch (IOException ex) {
			Logger.getLogger(RecentFilesCollection.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void removeFile(File f) {
		try {
			String fullPathToRemove = f.getCanonicalPath();
			for (Iterator<String> iterator = m_recentFiles.iterator(); iterator.hasNext();) {
				String fullPath = iterator.next();
				if (fullPath.equals(fullPathToRemove)) {
					iterator.remove();
				}
			}
		}
		catch (IOException ex) {
			Logger.getLogger(RecentFilesCollection.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void Clear() {
		m_recentFiles.clear();
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(m_recentFiles);
			return baos.toByteArray();
		}
		catch (IOException ex) {
			Logger.getLogger(RecentFilesCollection.class.getName()).log(Level.SEVERE, null, ex);
			return new byte[]{};
		}
	}
}
