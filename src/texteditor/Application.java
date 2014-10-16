package texteditor;

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import it.sauronsoftware.junique.MessageHandler;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import org.apache.commons.codec.binary.Base64;

public class Application {

	static TextEditor textEditor = null;

	public static final Logger logger = Logger.getLogger(TextEditor.class.getName());

	static void SetLookAndFeel() {
		try {
			String osVersion = System.getProperty("os.name");
			if (osVersion.contains("Linux")) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			}
			else {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	static void InitializeLogging() {

		try {
			File temp = File.createTempFile(TextEditor.class.getSimpleName(), ".log");
			FileHandler fh = new FileHandler(temp.getAbsolutePath());
			fh.setFormatter(new MyFormatter());
			logger.setUseParentHandlers(false);
			logger.addHandler(fh);
		}
		catch (IOException ex) {
			Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	static boolean CheckInstance(String id) {
		try {
			JUnique.acquireLock(id, new MessageHandler() {
				@Override
				public String handle(String message) {
					if (textEditor != null) {
						byte[] filenameBytes = Base64.decodeBase64(message);
						String filename = new String(filenameBytes);
						textEditor.DoOpen(new File(filename));
						textEditor.toFront();
					}
					return null;
				}
			});
			return true;
		}
		catch (AlreadyLockedException e) {
			return false;
		}
	}

	static void LoadIcons() {
		ArrayList<String> filenames = new ArrayList<>();
		filenames.add("/res/icons/gnome-stock-text-indent_16x16.png");
		filenames.add("/res/icons/gnome-stock-text-indent_22x22.png");
		filenames.add("/res/icons/gnome-stock-text-indent_24x24.png");
		filenames.add("/res/icons/gnome-stock-text-indent_32x32.png");
		filenames.add("/res/icons/gnome-stock-text-indent_48x48.png");
		filenames.add("/res/icons/gnome-stock-text-indent_64x64.png");
		filenames.add("/res/icons/gnome-stock-text-indent_128x128.png");
		ArrayList<Image> images = new ArrayList<Image>();
		for (String filename : filenames) {
			URL url = textEditor.getClass().getResource(filename);
			if (url != null) {
				Image image = Toolkit.getDefaultToolkit().getImage(url);
				if (image != null) {
					images.add(image);
				}
			}
		}
		textEditor.setIconImages(images);
	}

	public static void main(final String args[]) {

		InitializeLogging();

		SetLookAndFeel();

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (CheckInstance(TextEditor.class.getName())) {
					textEditor = new TextEditor();
					textEditor.setVisible(true);
					LoadIcons();
					for (String filename : args) {
						textEditor.DoOpen(new File(filename));
					}
				}
				else {
					for (String filename : args) {
						String message = new String(Base64.encodeBase64(filename.getBytes()));
						JUnique.sendMessage(TextEditor.class.getName(), message);
					}
				}

			}
		});
	}
}
