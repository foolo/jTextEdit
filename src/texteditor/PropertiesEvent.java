package texteditor;

public interface PropertiesEvent {

	public void notify(PropertiesListener pl);

	public class DirtyEvent implements PropertiesEvent {

		DocumentView documentView;

		public DirtyEvent(DocumentView dv) {
			documentView = dv;
		}

		@Override
		public void notify(PropertiesListener pl) {
			pl.DirtyChanged(documentView);
		}
	}

	public class ContentChangedEvent implements PropertiesEvent {

		DocumentView documentView;

		public ContentChangedEvent(DocumentView dv) {
			documentView = dv;
		}

		@Override
		public void notify(PropertiesListener pl) {
			pl.ContentChanged(documentView);
		}
	}

}
