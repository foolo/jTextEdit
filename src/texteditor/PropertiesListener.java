package texteditor;

public abstract class PropertiesListener {

	void CallAll(DocumentView dv) {
		DirtyChanged(dv);
	}

	void DirtyChanged(DocumentView dv) {
	}

}
