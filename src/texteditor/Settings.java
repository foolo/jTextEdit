package texteditor;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import util.Arithmetics;

public class Settings {

	private final Preferences prefs = Preferences.userNodeForPackage(Settings.class);

	private final ArrayList<SettingsListener> listeners = new ArrayList<>();

	void AddListener(SettingsListener settingsListener) {
		listeners.add(settingsListener);
	}

	private void NotifyListeners(SettingsEvent event) {
		for (SettingsListener sl : listeners) {
			event.notify(sl);
		}
	}

	////////////////////////////////////////////////////////////////////////
	private static final String WORD_WRAP = "word_wrap";
	private static final String SHOW_LINE_NUMBERS = "show_line_numbers";
	private static final String SHOW_FILE_BROWSER = "show_file_browser";
	private static final String MAIN_BOUNDS_X = "main_bounds_x";
	private static final String MAIN_BOUNDS_Y = "main_bounds_y";
	private static final String MAIN_BOUNDS_WIDTH = "main_bounds_width";
	private static final String MAIN_BOUNDS_HEIGHT = "main_bounds_height";
	private static final String IS_MAXIMIZED_H = "is_maximized_h";
	private static final String IS_MAXIMIZED_V = "is_maximized_v";
	private static final String OPEN_DIRECTORY = "open_directory";
	private static final String FILE_BROWSER_DIRECTORY = "file_browser_directory";
	private static final String RECENT_FILES = "recent_files";

	void SetRecentFilesCollection(RecentFilesCollection recentFiles) {
		prefs.putByteArray(RECENT_FILES, recentFiles.toByteArray());
		NotifyListeners(new SettingsEvent.RecentFilesChangedEvent());
	}

	void SetWordWrap(boolean wordWrap) {
		prefs.putBoolean(WORD_WRAP, wordWrap);
		NotifyListeners(new SettingsEvent.WordWrapEvent());
	}

	void SetShowLineNumbers(boolean showLineNumbers) {
		prefs.putBoolean(SHOW_LINE_NUMBERS, showLineNumbers);
		NotifyListeners(new SettingsEvent.ShowLineNumbersEvent());
	}

	void SetShowFileBrowser(boolean showFileBrowser) {
		prefs.putBoolean(SHOW_FILE_BROWSER, showFileBrowser);
		NotifyListeners(new SettingsEvent.ShowFileBrowserEvent());
	}

	RecentFilesCollection GetRecentFilesCollection() {
		byte[] b = prefs.getByteArray(RECENT_FILES, null);
		return new RecentFilesCollection(b);
	}

	boolean GetWordWrap() {
		return prefs.getBoolean(WORD_WRAP, false);
	}

	boolean GetShowLineNumbers() {
		return prefs.getBoolean(SHOW_LINE_NUMBERS, false);
	}

	boolean GetShowFileBrowser() {
		return prefs.getBoolean(SHOW_FILE_BROWSER, true);
	}

	void SetMainFormBounds(Rectangle bounds) {
		prefs.putInt(MAIN_BOUNDS_X, bounds.x);
		prefs.putInt(MAIN_BOUNDS_Y, bounds.y);
		prefs.putInt(MAIN_BOUNDS_WIDTH, bounds.width);
		prefs.putInt(MAIN_BOUNDS_HEIGHT, bounds.height);
	}

	Rectangle GetMainFormBounds() {
		int x = prefs.getInt(MAIN_BOUNDS_X, 0);
		int y = prefs.getInt(MAIN_BOUNDS_Y, 0);
		int width = prefs.getInt(MAIN_BOUNDS_WIDTH, 700);
		int height = prefs.getInt(MAIN_BOUNDS_HEIGHT, 500);

		int scr_width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int scr_height = Toolkit.getDefaultToolkit().getScreenSize().height;

		width = Arithmetics.UpperBound(width, scr_width);
		height = Arithmetics.UpperBound(height, scr_height);

		x = Arithmetics.LowerBound(x, 0);
		y = Arithmetics.LowerBound(y, 0);

		x = Arithmetics.UpperBound(x, scr_width - width);
		y = Arithmetics.UpperBound(y, scr_height - height);

		return new Rectangle(x, y, width, height);
	}

	boolean GetIsMaximizedHorizontal() {
		return prefs.getBoolean(IS_MAXIMIZED_H, false);
	}

	void SetIsMaximizedHorizontal(boolean isMaximized) {
		prefs.putBoolean(IS_MAXIMIZED_H, isMaximized);
	}

	boolean GetIsMaximizedVertical() {
		return prefs.getBoolean(IS_MAXIMIZED_V, false);
	}

	void SetIsMaximizedVertical(boolean isMaximized) {
		prefs.putBoolean(IS_MAXIMIZED_V, isMaximized);
	}

	File GetOpenDirectory() {
		String dir = prefs.get(OPEN_DIRECTORY, null);
		if (dir == null) {
			return null;
		}
		return new File(dir);
	}

	File GetFileBrowserDirectory() {
		String dir = prefs.get(FILE_BROWSER_DIRECTORY, null);
		if (dir == null) {
			dir = System.getProperty("user.home");
		}
		return new File(dir);
	}

	void SetOpenDirectory(File openDirectory) {
		prefs.put(OPEN_DIRECTORY, openDirectory.toString());
	}

	void SetFileBrowserDirectory(File fileBrowserDirectory) {
		prefs.put(FILE_BROWSER_DIRECTORY, fileBrowserDirectory.toString());
		NotifyListeners(new SettingsEvent.FileBrowserRootDirChangedEvent());
	}
}
