package texteditor;

import java.util.ArrayList;

public class PropertyDispatcher {

	private final ArrayList<PropertiesListener> listeners = new ArrayList<>();

	void AddListener(PropertiesListener pl) {
		listeners.add(pl);
	}

	void NotifyListeners(PropertiesEvent event) {
		for (PropertiesListener pl : listeners) {
			event.notify(pl);
		}
	}
}
