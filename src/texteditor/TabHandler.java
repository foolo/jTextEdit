package texteditor;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class TabHandler extends javax.swing.JPanel {

	ArrayList<DocumentView> documentStack = new ArrayList<>();

	Settings settings;
	MainForm mainForm;

	public TabHandler() {
		initComponents();
	}

	public TabHandler(MainForm mf, Settings s) {
		initComponents();
		mainForm = mf;
		settings = s;
	}

	ArrayList<DocumentView> GetDocumentStack() {
		return documentStack;
	}

	int GetTabCount() {
		return jTabbedPane1.getTabCount();
	}

	void SetSelected(DocumentView documentView) {
		jTabbedPane1.setSelectedComponent(documentView);
	}

	DocumentView CurrentDocumentView() {
		Component c = jTabbedPane1.getSelectedComponent();
		if (c != null) {
			return ((DocumentView) c);
		}
		else {
			return new DocumentView(this, settings);
		}
	}

	void New() {
		DocumentView document = new DocumentView(this, settings);
		jTabbedPane1.add(document, document.GetFilenameAlias());
		jTabbedPane1.setSelectedComponent(document);
		HandleDocumentContentChanged(document);
	}

	public void HandleDocumentContentChanged(DocumentView documentView) {

		// TODO should only be needed on dirty changed
		SetTabAndWindowTitle(documentView);

		mainForm.HandleDocumentContentChanged(documentView);
	}

	public void HandleDocumentPropertiesChanged(DocumentView documentView) {
		mainForm.HandleDocumentPropertiesChanged(documentView);
	}

	void DoOpen(File f) {
		for (Component c : jTabbedPane1.getComponents()) {
			if (((DocumentView) c).IsLoaded(f)) {
				jTabbedPane1.setSelectedComponent(c);
				return;
			}
		}

		DocumentView currentDocumentView = CurrentDocumentView();
		if (currentDocumentView.OkToReplace()) {
			currentDocumentView.LoadFile(f);
		}
		else {
			DocumentView documentView = new DocumentView(this, settings);
			jTabbedPane1.add(documentView);
			jTabbedPane1.setSelectedComponent(documentView);
			documentView.LoadFile(f);
		}

		RecentFilesCollection recentFiles = settings.GetRecentFilesCollection();
		recentFiles.AddFile(f);
		settings.SetRecentFilesCollection(recentFiles);

		mainForm.toFront();
	}

	boolean FileClose() {
		DocumentView currentDocumentView = CurrentDocumentView();
		if (currentDocumentView.HandleCurrentFile()) {
			documentStack.remove(currentDocumentView);
			ArrayList<DocumentView> documentStackBackup = new ArrayList<>(documentStack);
			jTabbedPane1.remove(currentDocumentView); // Will trigger TabChanged, which will affect documentStack
			documentStack = documentStackBackup;

			if (!documentStack.isEmpty()) {
				jTabbedPane1.setSelectedComponent(documentStack.get(0));
			}

			if (jTabbedPane1.getTabCount() == 0) {
				mainForm.DoExit();
			}
			return true;
		}
		return false;
	}

	void FileSave() {
		CurrentDocumentView().SaveDoc();
	}

	void FileSaveAs() {
		CurrentDocumentView().FileSaveAs();
	}

	void CloseAllTabs() {
		while (jTabbedPane1.getTabCount() > 0) {
			if (FileClose() == false) {
				return;
			}
		}
	}

	void SetTabAndWindowTitle(DocumentView documentView) {
		int i = jTabbedPane1.indexOfComponent(documentView);
		String filenameAlias = documentView.GetFilenameAlias();
		if (i >= 0) {
			jTabbedPane1.setTitleAt(i, filenameAlias);
		}

		String fileDir = documentView.GetFileDirectory();
		String windowTitle = filenameAlias;
		if (!documentView.IsUntitled()) {
			windowTitle += " (" + fileDir + ")";
		}
		windowTitle += " (" + documentView.GetEncoding() + ")";

		mainForm.setTitle(windowTitle);
	}

	void HandleEscape() {
		CurrentDocumentView().ClearMarkings();
	}

	void TabChanged() {
		DocumentView newTab = CurrentDocumentView();  // TODO investigate this. A new DocumentView is created when the form is closed. really needed?
		if (documentStack.contains(newTab)) {
			documentStack.remove(newTab);
		}

		// Null check needed here. It might be the last tab that is closed.
		if (newTab == null) {
			return;
		}

		documentStack.add(0, newTab);
		SetTabAndWindowTitle(newTab);
		mainForm.HandleDocumentPropertiesChanged(newTab);
	}

	void ReloadWithDifferentEncoding() {
		DocumentView documentView = CurrentDocumentView();
		if (documentView.CanBeReloaded()) {

			EncodingDialog encodingDialog = new EncodingDialog(mainForm, true);
			encodingDialog.setVisible(true);
			String encoding = encodingDialog.GetEncoding();
			if (encoding != null) {
				CurrentDocumentView().ReloadWithEncoding(encoding);
			}
		}
		else {
			JOptionPane.showMessageDialog(this, "Can not reload new file");
		}
	}

	void ChangeEncoding() {
		EncodingDialog encodingDialog = new EncodingDialog(mainForm, true);
		encodingDialog.setVisible(true);
		String encoding = encodingDialog.GetEncoding();
		if (encoding != null) {
			CurrentDocumentView().ChangeEncoding(encoding);
		}
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
		if (SwingUtilities.isMiddleMouseButton(evt)) {
			FileClose();
		}
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
		TabChanged();
    }//GEN-LAST:event_jTabbedPane1StateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
