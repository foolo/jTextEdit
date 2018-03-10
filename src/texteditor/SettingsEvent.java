package texteditor;

public interface SettingsEvent {

	public void notify(SettingsListener settingsListener);

	public class WordWrapEvent implements SettingsEvent {

		@Override
		public void notify(SettingsListener settingsListener) {
			settingsListener.WordWrapChanged();
		}
	}

	public class ShowLineNumbersEvent implements SettingsEvent {

		@Override
		public void notify(SettingsListener settingsListener) {
			settingsListener.ShowLineNumbersChanged();
		}
	}

	public class ShowFileBrowserEvent implements SettingsEvent {

		@Override
		public void notify(SettingsListener settingsListener) {
			settingsListener.ShowFileBrowserChanged();
		}
	}

	public class FileBrowserRootDirChangedEvent implements SettingsEvent {

		@Override
		public void notify(SettingsListener settingsListener) {
			settingsListener.FileBrowserRootDirChanged();
		}
	}

	public class RecentFilesChangedEvent implements SettingsEvent {

		@Override
		public void notify(SettingsListener settingsListener) {
			settingsListener.RecentFilesChanged();
		}
	}
}
