package texteditor;

public abstract class SettingsListener {

	void CallAll() {
		WordWrapChanged();
		ShowLineNumbersChanged();
		RecentFilesChanged();
	}

	void WordWrapChanged() {
	}

	void ShowLineNumbersChanged() {
	}

	void RecentFilesChanged() {
	}
}
