package texteditor;

import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Settings {

	private final Preferences prefs = Preferences.userNodeForPackage(Settings.class);

	private final ArrayList<SettingsListener> listeners = new ArrayList<>();

	void AddListener(SettingsListener settingsListener) {
		listeners.add(settingsListener);

		// Send all events
		NotifyListeners(new SettingsEvent.WordWrapEvent());
		//NotifyListeners(new SettingsEvent....
		//...
	}

	private void NotifyListeners(SettingsEvent event) {
		for (SettingsListener sl : listeners) {
			event.notify(sl);
		}
	}

	////////////////////////////////////////////////////////////////////////
	private static final String WORD_WRAP = "word_wrap";

	void SetWordWrap(boolean wordWrap) {
		prefs.putBoolean(WORD_WRAP, wordWrap);
		NotifyListeners(new SettingsEvent.WordWrapEvent());
	}

	boolean GetWordWrap() {
		return prefs.getBoolean(WORD_WRAP, false);
	}

	String GetSyntaxForFileExtension(String fileExtension) {
		switch (fileExtension) {
			case "bat":
				return "text/bat";
			case "cpp":
			case "c++":
			case "cc":
				return "text/cpp";
			case "cs":
				return "text/cs";
			case "css":
				return "text/css";
			case "json":
				return "text/json";
			case "tex":
			case "latex":
				return "text/latex";
			case "perl":
				return "text/perl";
			case "php":
				return "text/php";
			case "py":
				return "text/python";
			case "rb":
				return "text/ruby";
			case "sh":
				return "text/unix";
			case "sql":
				return "text/sql";
			case "tcl":
				return "text/tcl";
			case "vb":
				return "text/vb";
			case "xml":
				return "text/xml";
			default:
				return "text/plain";
		}
	}
}
