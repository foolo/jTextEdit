package texteditor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;
import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;

public class DocumentView extends javax.swing.JPanel {

	private class MySettingsListener extends SettingsListener {

		@Override
		void WordWrapChanged() {
			boolean wrapOn = settings.GetWordWrap();
			textEditorPane1.setLineWrap(wrapOn);
		}

		@Override
		void ShowLineNumbersChanged() {
			boolean showLineNumbers = settings.GetShowLineNumbers();
			rTextScrollPane1.setLineNumbersEnabled(showLineNumbers);
		}
	}

	private org.fife.ui.rsyntaxtextarea.TextEditorPane textEditorPane1;

	MySettingsListener mySettingsListener = new MySettingsListener();

	boolean m_untitled = true;
	final JFileChooser jFileChooser1 = new JFileChooser();
	TabHandler tabHandler;
	Settings settings;

	public DocumentView(TabHandler th, Settings s) {
		initTextEditorPane();
		initComponents();
		settings = s;
		tabHandler = th;
		textEditorPane1.setEncoding("UTF-8");
		textEditorPane1.setDirty(false);
		textEditorPane1.setAnimateBracketMatching(false);
		settings.AddListener(mySettingsListener);
		mySettingsListener.CallAll();
		System.out.println(textEditorPane1.getDropTarget());
		textEditorPane1.setDropTarget(null);
	}

	public boolean OkToReplace() {
		return m_untitled && !textEditorPane1.isDirty() && textEditorPane1.getText().isEmpty();
	}

	public void Find(SearchContext context) {
		SearchResult result = SearchEngine.find(textEditorPane1, context);
		if (!result.wasFound()) {
			if (context.getSearchForward()) {
				textEditorPane1.setCaretPosition(0);
			}
			else {
				textEditorPane1.setCaretPosition(textEditorPane1.getText().length() - 1);
			}
			SearchEngine.find(textEditorPane1, context);
		}
	}

	public void Replace(SearchContext searchContext) {
		SearchEngine.replace(textEditorPane1, searchContext);
	}

	public void ReplaceAll(SearchContext searchContext) {
		SearchEngine.replaceAll(textEditorPane1, searchContext);
	}

	public void MarkAll(SearchContext context) {
		textEditorPane1.select(textEditorPane1.getCaretPosition(), textEditorPane1.getCaretPosition());
		SearchEngine.markAll(textEditorPane1, context);
	}

	String GetFileExtension() {
		return "cpp";
	}

	void UpdateSyntaxEditingStyle() {
		String fileExt = FilenameUtils.getExtension(textEditorPane1.getFileName());
		String syntaxMime = settings.GetSyntaxForFileExtension(fileExt);
		textEditorPane1.setSyntaxEditingStyle(syntaxMime);
	}

	public boolean CanBeReloaded() {
		return textEditorPane1.isLocalAndExists();
	}

	private void SetEncoding(String encoding) {
		try {
			textEditorPane1.setEncoding(encoding);
		}
		catch (UnsupportedCharsetException ex) {
			JOptionPane.showMessageDialog(this, "Encoding not supported: " + encoding);
		}
	}

	String GetEncoding() {
		return textEditorPane1.getEncoding();
	}

	public void ReloadWithEncoding(String encoding) {
		SetEncoding(encoding);
		try {
			textEditorPane1.reload();
			tabHandler.HandleDocumentChanged(this);
		}
		catch (IOException ex) {
			Logger.getLogger(DocumentView.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void ChangeEncoding(String encoding) {
		SetEncoding(encoding);
		tabHandler.HandleDocumentChanged(this);
	}

	public boolean IsLoaded(File f) {
		FileLocation fl = FileLocation.create(f);
		return (fl.getFileFullPath().equals(textEditorPane1.getFileFullPath()));
	}

	public void LoadFile(File f) {
		try {
			String encoding = EncodingDetector.GetEncoding(f);
			System.out.println("Detected encoding: " + encoding);
			textEditorPane1.load(FileLocation.create(f), encoding);
			textEditorPane1.discardAllEdits();
			UpdateSyntaxEditingStyle();
			m_untitled = false;
		}
		catch (IOException ex) {
			Logger.getLogger(DocumentView.class.getName()).log(Level.SEVERE, null, ex);
		}
		tabHandler.HandleDocumentChanged(this);
	}

	String GetFilenameAlias() {
		String alias;
		if (m_untitled) {
			alias = "Untitled";
		}
		else {
			alias = textEditorPane1.getFileName();
		}
		if (textEditorPane1.isDirty()) {
			alias = "*" + alias;
		}
		return alias;
	}

	boolean IsUntitled() {
		return m_untitled;
	}

	String GetFileDirectory() {
		return FilenameUtils.getFullPathNoEndSeparator(textEditorPane1.getFileFullPath());
	}

	@Override
	public String toString() {
		return GetFilenameAlias();
	}

	void KeyReleased() {
		tabHandler.HandleDocumentChanged(this);
	}

	void ClearMarkings() {
		SearchEngine.markAll(textEditorPane1, new SearchContext());
	}

	public boolean SaveDoc() {
		if (m_untitled) {
			return FileSaveAs();
		}
		else {
			try {
				textEditorPane1.save();
			}
			catch (IOException ex) {
				JOptionPane.showMessageDialog(this, "Could not save file: " + ex.getMessage());
			}
			tabHandler.HandleDocumentChanged(this);
			m_untitled = false;
			return true;
		}
	}

	public boolean FileSaveAs() {
		File selectedFile = SelectFile();
		if (selectedFile != null) {
			try {
				textEditorPane1.saveAs(FileLocation.create(selectedFile));
				tabHandler.HandleDocumentChanged(this);
				UpdateSyntaxEditingStyle();
				m_untitled = false;
			}
			catch (IOException ex) {
				JOptionPane.showMessageDialog(this, "Could not save file: " + ex.getMessage());
				return false;
			}
		}
		return true;
	}

	private File SelectFile() {
		while (true) {
			int returnVal = jFileChooser1.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jFileChooser1.getSelectedFile();
				if (selectedFile.exists()) {
					String message = selectedFile.getName() + " already exists. Replace it?";
					int userDesicion = JOptionPane.showConfirmDialog(this, message, "Warning", JOptionPane.YES_NO_OPTION);
					if (userDesicion == JOptionPane.YES_OPTION) {
						return selectedFile;
					}
				}
				else {
					return selectedFile;
				}
			}
			else {
				return null;
			}
		}
	}

	boolean HandleCurrentFile() {
		if (!textEditorPane1.isDirty()) {
			return true;
		}

		String message = "Save changes to " + GetFilenameAlias() + "?";

		int userDesicion = JOptionPane.showConfirmDialog(this, message, "Warning", JOptionPane.YES_NO_CANCEL_OPTION);

		if (userDesicion == JOptionPane.YES_OPTION) {
			return SaveDoc();
		}

		if (userDesicion == JOptionPane.NO_OPTION) {
			// Just ignore the old file
			return true;
		}

		if (userDesicion == JOptionPane.CANCEL_OPTION) {
			return false;
		}
		return false;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rTextScrollPane1 = new org.fife.ui.rtextarea.RTextScrollPane(textEditorPane1);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rTextScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rTextScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

	private void initTextEditorPane() {
		textEditorPane1 = new TextEditorPane();
		textEditorPane1.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				textEditorPane1KeyReleased(evt);
			}
		});
	}

	private void textEditorPane1KeyReleased(java.awt.event.KeyEvent evt) {
		KeyReleased();
	}

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
		textEditorPane1.requestFocus();
    }//GEN-LAST:event_formComponentShown

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.fife.ui.rtextarea.RTextScrollPane rTextScrollPane1;
    // End of variables declaration//GEN-END:variables
}
