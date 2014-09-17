package texteditor;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class FileHandler {

	static boolean WriteFile(File file, String text, Component component) {
		try {
			FileWriter fileWriter;
			fileWriter = new FileWriter(file);
			fileWriter.write(text);
			fileWriter.close();
			return true;
		}
		catch (IOException ex) {
			Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
			JOptionPane.showMessageDialog(component, "Could not save file: " + ex.getMessage());
			return false;
		}
	}
}
