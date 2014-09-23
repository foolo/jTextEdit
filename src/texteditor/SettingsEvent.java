package texteditor;

public interface SettingsEvent {

	public void notify(SettingsListener settingsListener);

	public class WordWrapEvent implements SettingsEvent {

		@Override
		public void notify(SettingsListener settingsListener) {
			settingsListener.WordWrapChanged();
		}
	}
}
