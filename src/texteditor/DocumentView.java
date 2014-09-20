package texteditor;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;

public class DocumentView extends javax.swing.JPanel {

	File file = null;
	boolean modified = false;
	final JFileChooser jFileChooser1 = new JFileChooser();
	TextEditor textEditor;

	public DocumentView(TextEditor te) {
		initComponents();
		textEditor = te;
	}

	void SetWordWrap(boolean wrapOn) {
		rSyntaxTextArea1.setLineWrap(wrapOn);
	}

	public boolean IsNewAndEmpty() {
		return (file == null) && !modified && rSyntaxTextArea1.getText().isEmpty();
	}

	public void Find(SearchContext context) {
		SearchResult result = SearchEngine.find(rSyntaxTextArea1, context);
		if (!result.wasFound()) {
			if (context.getSearchForward()) {
				rSyntaxTextArea1.setCaretPosition(0);
			}
			else {
				rSyntaxTextArea1.setCaretPosition(rSyntaxTextArea1.getText().length() - 1);
			}
			SearchEngine.find(rSyntaxTextArea1, context);
		}
	}

	public void Replace(SearchContext searchContext) {
		SearchEngine.replace(rSyntaxTextArea1, searchContext);
	}

	public void ReplaceAll(SearchContext searchContext) {
		SearchEngine.replaceAll(rSyntaxTextArea1, searchContext);
	}

	public void MarkAll(SearchContext context) {
		rSyntaxTextArea1.select(rSyntaxTextArea1.getCaretPosition(), rSyntaxTextArea1.getCaretPosition());
		SearchEngine.markAll(rSyntaxTextArea1, context);
	}

	public void LoadFile(File f) {
		file = f;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			rSyntaxTextArea1.read(br, null);
		}
		catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
		textEditor.HandleDocumentChanged(this);
	}

	String GetFilenameAlias() {
		String alias;
		if (file == null) {
			alias = "Untitled";
		}
		else {
			alias = file.getName();
		}
		if (modified) {
			alias = alias + " (*)";
		}
		return alias;
	}

	void KeyReleased() {
		modified = true;
		textEditor.HandleDocumentChanged(this);
	}

	void EscapePressed() {
		textEditor.HandleEscapePressed();
		SearchEngine.markAll(rSyntaxTextArea1, new SearchContext());
	}

	public boolean DoSave(File fileToSave) {
		String text = rSyntaxTextArea1.getText();
		if (FileHandler.WriteFile(fileToSave, text, this)) {
			file = fileToSave;
			modified = false;
			textEditor.HandleDocumentChanged(this);
			return true;
		}
		return false;
	}

	public boolean SaveDoc() {
		if (file == null) {
			return FileSaveAs();
		}
		else {
			return DoSave(file);
		}
	}

	public boolean FileSaveAs() {
		File selectedFile = SelectFile();
		if (selectedFile != null) {
			return DoSave(selectedFile);
		}
		return false;
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
		if (!modified) {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        rSyntaxTextArea1 = new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea();

        rSyntaxTextArea1.setColumns(20);
        rSyntaxTextArea1.setRows(5);
        rSyntaxTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rSyntaxTextArea1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rSyntaxTextArea1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                rSyntaxTextArea1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(rSyntaxTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rSyntaxTextArea1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rSyntaxTextArea1KeyReleased
		System.out.println(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			EscapePressed();
		}
		else {
			KeyReleased();
		}
    }//GEN-LAST:event_rSyntaxTextArea1KeyReleased

    private void rSyntaxTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rSyntaxTextArea1KeyPressed
		//System.out.println(evt);
    }//GEN-LAST:event_rSyntaxTextArea1KeyPressed

    private void rSyntaxTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rSyntaxTextArea1KeyTyped
		System.out.println(evt);
    }//GEN-LAST:event_rSyntaxTextArea1KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea rSyntaxTextArea1;
    // End of variables declaration//GEN-END:variables
}
