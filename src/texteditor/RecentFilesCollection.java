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
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			m_recentFiles = (ArrayList<String>) o;
		}
		catch (IOException | ClassNotFoundException ex) {
			Logger.getLogger(RecentFilesCollection.class.getName()).log(Level.SEVERE, null, ex);
			m_recentFiles = new ArrayList<>();
		}
	}

	public ArrayList<String> GetRecentFiles() {
		return m_recentFiles;
	}

	public void AddFile(File f) {
		try {
			String fullPathToAdd = f.getCanonicalPath();
			for (Iterator<String> iterator = m_recentFiles.iterator(); iterator.hasNext();) {
				String fullPath = iterator.next();
				if (fullPath.equals(fullPathToAdd)) {
					iterator.remove();
				}
			}
			m_recentFiles.add(fullPathToAdd);
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
