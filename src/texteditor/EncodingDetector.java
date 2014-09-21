package texteditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mozilla.universalchardet.UniversalDetector;

public class EncodingDetector {

	public static String GetEncoding(File file) {
		UniversalDetector detector = new UniversalDetector(null);
		try {
			byte[] buf = new byte[4096];

			FileInputStream fileInputStream = new FileInputStream(file);

			int readBytes;
			while ((readBytes = fileInputStream.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, readBytes);
			}
			detector.dataEnd();

			String encoding = detector.getDetectedCharset();
			detector.reset();

			return encoding;
		}
		catch (FileNotFoundException ex) {
			Logger.getLogger(EncodingDetector.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
		catch (IOException ex) {
			Logger.getLogger(EncodingDetector.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
}
