package texteditor;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;
import org.fife.ui.rsyntaxtextarea.FileLocation;
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
	}

	MySettingsListener mySettingsListener = new MySettingsListener();

	boolean m_untitled = true;
	final JFileChooser jFileChooser1 = new JFileChooser();
	TextEditor textEditor;
	Settings settings;

	public DocumentView(TextEditor te, Settings s) {
		initComponents();
		settings = s;
		textEditor = te;
		textEditorPane1.setAnimateBracketMatching(false);
		settings.AddListener(mySettingsListener);
	}

	public boolean IsNewAndEmpty() {
		boolean remoteOrNonExisting = !textEditorPane1.isLocalAndExists();
		return remoteOrNonExisting && !textEditorPane1.isDirty() && textEditorPane1.getText().isEmpty();
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

	void SetCharacterDecoding(String decoding) {
		textEditorPane1.setEncoding(decoding);
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

	public void ReloadWithEncoding(String encoding) {
		SetEncoding(encoding);
		try {
			textEditorPane1.reload();
		}
		catch (IOException ex) {
			Logger.getLogger(DocumentView.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void ChangeEncoding(String encoding) {
		SetEncoding(encoding);
		textEditor.HandleDocumentChanged(this);
	}

	public void LoadFile(File f) {
		try {
			String encoding = EncodingDetector.GetEncoding(f);
			System.out.println("Detected encoding: " + encoding);
			textEditorPane1.load(FileLocation.create(f), encoding);
			UpdateSyntaxEditingStyle();
			m_untitled = false;
		}
		catch (IOException ex) {
			Logger.getLogger(DocumentView.class.getName()).log(Level.SEVERE, null, ex);
		}
		textEditor.HandleDocumentChanged(this);
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
			alias = alias + " (*)";
		}
		return alias;
	}

	void KeyReleased() {
		textEditor.HandleDocumentChanged(this);
	}

	void EscapePressed() {
		textEditor.HandleEscapePressed();
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
			textEditor.HandleDocumentChanged(this);
			m_untitled = false;
			return true;
		}
	}

	public boolean FileSaveAs() {
		File selectedFile = SelectFile();
		if (selectedFile != null) {
			try {
				textEditorPane1.saveAs(FileLocation.create(selectedFile));
				textEditor.HandleDocumentChanged(this);
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

        jScrollPane2 = new javax.swing.JScrollPane();
        textEditorPane1 = new org.fife.ui.rsyntaxtextarea.TextEditorPane();

        textEditorPane1.setColumns(20);
        textEditorPane1.setRows(5);
        textEditorPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textEditorPane1KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(textEditorPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void textEditorPane1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEditorPane1KeyReleased
		if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			EscapePressed();
		}
		else {
			KeyReleased();
		}
    }//GEN-LAST:event_textEditorPane1KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private org.fife.ui.rsyntaxtextarea.TextEditorPane textEditorPane1;
    // End of variables declaration//GEN-END:variables

}
