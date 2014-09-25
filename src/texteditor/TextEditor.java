package texteditor;

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import it.sauronsoftware.junique.MessageHandler;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.apache.commons.codec.binary.Base64;

public final class TextEditor extends javax.swing.JFrame {

	private class MySettingsListener extends SettingsListener {

		@Override
		void WordWrapChanged() {
			boolean wrapOn = settings.GetWordWrap();
			jCheckBoxMenuItemWordWrap.setSelected(wrapOn);
		}
	}

	final JFileChooser jFileChooser1 = new JFileChooser();

	Settings settings = new Settings();

	MySettingsListener mySettingsListener = new MySettingsListener();

	public TextEditor() {
		initComponents();
		FileNew();
		searchPanel1.setVisible(false);
		searchPanel1.SetMainForm(this);
		settings.AddListener(mySettingsListener);
	}

	void FileNew() {
		DocumentView document = new DocumentView(this, settings);
		jTabbedPane1.add(document, document.GetFilenameAlias());
		jTabbedPane1.setSelectedComponent(document);
	}

	void DoOpen(File f) {
		DocumentView currentDocumentView = CurrentDocumentView();
		if (currentDocumentView.IsNewAndEmpty()) {
			currentDocumentView.LoadFile(f);
		}
		else {
			DocumentView documentView = new DocumentView(this, settings);
			jTabbedPane1.add(documentView);
			jTabbedPane1.setSelectedComponent(documentView);
			documentView.LoadFile(f);
		}
	}

	void FileOpen() {
		int returnVal = jFileChooser1.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = jFileChooser1.getSelectedFile();
			if (f != null) {
				DoOpen(f);
			}
		}
	}

	boolean FileClose() {
		DocumentView currentDocumentView = CurrentDocumentView();
		if (currentDocumentView.HandleCurrentFile()) {
			jTabbedPane1.remove(currentDocumentView);
			if (jTabbedPane1.getTabCount() == 0) {
				DoExit();
			}
			return true;
		}
		return false;
	}

	DocumentView CurrentDocumentView() {
		Component c = jTabbedPane1.getSelectedComponent();
		return ((DocumentView) c);
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

	void DoExit() {
		if (jTabbedPane1.getTabCount() == 0) {
			dispose();
		}
	}

	void FileExit() {
		CloseAllTabs();
		DoExit();
	}

	void EditFind() {
		searchPanel1.setVisible(true);
	}

	public void HandleDocumentChanged(DocumentView documentView) {
		int i = jTabbedPane1.indexOfComponent(documentView);
		jTabbedPane1.setTitleAt(i, documentView.GetFilenameAlias());
		if (searchPanel1.isVisible()) {
			searchPanel1.MarkAll();
		}
	}

	public void HandleEscapePressed() {
		searchPanel1.setVisible(false);
	}

	void UpdateWordWrap() {
		settings.SetWordWrap(jCheckBoxMenuItemWordWrap.isSelected());
	}

	void ReloadWithDifferentEncoding() {
		DocumentView documentView = CurrentDocumentView();
		if (documentView.CanBeReloaded()) {

			EncodingDialog encodingDialog = new EncodingDialog(this, true);
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
		EncodingDialog encodingDialog = new EncodingDialog(this, true);
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
        searchPanel1 = new texteditor.SearchPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemNew = new javax.swing.JMenuItem();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemClose = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemSaveAs = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jCheckBoxMenuItemWordWrap = new javax.swing.JCheckBoxMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuItemFind = new javax.swing.JMenuItem();
        jMenuDocument = new javax.swing.JMenu();
        jMenuItemReloadWithEncoding = new javax.swing.JMenuItem();
        jMenuItemChangeEncoding = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jMenuFile.setText("File");

        jMenuItemNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemNew.setText("New");
        jMenuItemNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemNew);

        jMenuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemOpen.setText("Open");
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemOpen);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);

        jMenuItemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSave.setText("Save");
        jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSave);

        jMenuItemSaveAs.setText("Save as...");
        jMenuItemSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveAsActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSaveAs);

        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);

        jMenuBar1.add(jMenuFile);

        jMenu1.setText("View");

        jCheckBoxMenuItemWordWrap.setSelected(true);
        jCheckBoxMenuItemWordWrap.setText("Word wrap");
        jCheckBoxMenuItemWordWrap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemWordWrapActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItemWordWrap);

        jMenuBar1.add(jMenu1);

        jMenuEdit.setText("Edit");

        jMenuItemFind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemFind.setText("Find");
        jMenuItemFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFindActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemFind);

        jMenuBar1.add(jMenuEdit);

        jMenuDocument.setText("Document");

        jMenuItemReloadWithEncoding.setText("Reload file with different encoding...");
        jMenuItemReloadWithEncoding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReloadWithEncodingActionPerformed(evt);
            }
        });
        jMenuDocument.add(jMenuItemReloadWithEncoding);

        jMenuItemChangeEncoding.setText("Change encoding");
        jMenuItemChangeEncoding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemChangeEncodingActionPerformed(evt);
            }
        });
        jMenuDocument.add(jMenuItemChangeEncoding);

        jMenuBar1.add(jMenuDocument);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(searchPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewActionPerformed
		FileNew();
    }//GEN-LAST:event_jMenuItemNewActionPerformed

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
		FileOpen();
    }//GEN-LAST:event_jMenuItemOpenActionPerformed

    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
		FileSave();
    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    private void jMenuItemSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAsActionPerformed
		FileSaveAs();
    }//GEN-LAST:event_jMenuItemSaveAsActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
		FileExit();
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseActionPerformed
		FileClose();
    }//GEN-LAST:event_jMenuItemCloseActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
		FileExit();
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItemFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFindActionPerformed
		EditFind();
    }//GEN-LAST:event_jMenuItemFindActionPerformed

    private void jCheckBoxMenuItemWordWrapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemWordWrapActionPerformed
		UpdateWordWrap();
    }//GEN-LAST:event_jCheckBoxMenuItemWordWrapActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
		if (SwingUtilities.isMiddleMouseButton(evt)) {
			System.out.println(jTabbedPane1.getSelectedIndex());
			FileClose();
		}
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void jMenuItemReloadWithEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReloadWithEncodingActionPerformed
		ReloadWithDifferentEncoding();
    }//GEN-LAST:event_jMenuItemReloadWithEncodingActionPerformed

    private void jMenuItemChangeEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemChangeEncodingActionPerformed
		ChangeEncoding();
    }//GEN-LAST:event_jMenuItemChangeEncodingActionPerformed

	static TextEditor textEditor = null;

	public static final Logger logger = Logger.getLogger(TextEditor.class.getName());

	static void SetLookAndFeel() {
		logger.info("SystemLookAndFeelClassName: " + UIManager.getSystemLookAndFeelClassName());
		for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
			logger.info(lookAndFeelInfo.getClassName());
		}

		try {
			String osVersion = System.getProperty("os.name");
			if (osVersion.contains("Linux")) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			}
			else {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	static void InitializeLogging() {
		try {
			File temp = File.createTempFile(TextEditor.class.getSimpleName(), ".log");
			FileHandler fh = new FileHandler(temp.getAbsolutePath());
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);

		}
		catch (IOException | SecurityException ex) {
			Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	static boolean CheckInstance(String id) {
		try {
			JUnique.acquireLock(id, new MessageHandler() {
				@Override
				public String handle(String message) {
					if (textEditor != null) {
						byte[] filenameBytes = Base64.decodeBase64(message);
						String filename = new String(filenameBytes);
						textEditor.DoOpen(new File(filename));
						textEditor.toFront();
					}
					return null;
				}
			});
			return true;
		}
		catch (AlreadyLockedException e) {
			return false;
		}
	}

	public static void main(final String args[]) {

		InitializeLogging();

		SetLookAndFeel();

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (CheckInstance(TextEditor.class.getName())) {
					textEditor = new TextEditor();
					textEditor.setVisible(true);
					for (String filename : args) {
						textEditor.DoOpen(new File(filename));
					}
				}
				else {
					for (String filename : args) {
						String message = new String(Base64.encodeBase64(filename.getBytes()));
						JUnique.sendMessage(TextEditor.class.getName(), message);
					}
				}

			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemWordWrap;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuDocument;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemChangeEncoding;
    private javax.swing.JMenuItem jMenuItemClose;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemFind;
    private javax.swing.JMenuItem jMenuItemNew;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemReloadWithEncoding;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemSaveAs;
    private javax.swing.JTabbedPane jTabbedPane1;
    private texteditor.SearchPanel searchPanel1;
    // End of variables declaration//GEN-END:variables
}
