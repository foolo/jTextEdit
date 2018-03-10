package texteditor;

public abstract class SettingsListener {

	void CallAll() {
		WordWrapChanged();
		ShowLineNumbersChanged();
		ShowFileBrowserChanged();
		FileBrowserRootDirChanged();
		RecentFilesChanged();
	}

	void WordWrapChanged() {
	}

	void ShowLineNumbersChanged() {
	}

	void ShowFileBrowserChanged() {
	}

	void FileBrowserRootDirChanged() {
	}

	void RecentFilesChanged() {
	}
}
