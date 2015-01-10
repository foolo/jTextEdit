package texteditor;

public abstract class PropertiesListener {

	void CallAll(DocumentView dv) {
		DirtyChanged(dv);
		ContentChanged(dv);
	}

	void DirtyChanged(DocumentView dv) {
	}

	void ContentChanged(DocumentView dv) {
	}
}
