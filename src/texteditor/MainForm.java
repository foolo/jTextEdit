package texteditor;

import java.awt.Component;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.MOVE;

public final class MainForm extends javax.swing.JFrame {

	ArrayList<DocumentView> documentStack = new ArrayList<>();

	final JFileChooser jFileChooser1 = new JFileChooser();

	Settings settings = new Settings();

	DocumentSwitcher documentSwitcher = null;

	private final TransferHandler handler;

	private void PassCtrlTabToSwitcher(Frame frame, boolean forward) {
		if (documentSwitcher == null) {
			documentSwitcher = new DocumentSwitcher(frame, forward, documentStack);
			documentSwitcher.setVisible(true);
			documentSwitcher = null;
		}
		else {
			documentSwitcher.Navigate(forward);
		}
	}

	void initializeGlobalKeys() {
		final Frame thisFrame = this;
		KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {

				if (e.getID() == KeyEvent.KEY_RELEASED) {

					if (e.getKeyCode() == KeyEvent.VK_TAB) {
						if (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
							PassCtrlTabToSwitcher(thisFrame, true);
						}
						else if (e.getModifiersEx() == (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) {
							PassCtrlTabToSwitcher(thisFrame, false);
						}
					}

					else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
						if (documentSwitcher != null) {
							DocumentView selectedDocumentView = documentSwitcher.GetSelectedDocument();
							documentSwitcher.dispose();
							jTabbedPane1.setSelectedComponent(selectedDocumentView);
						}
					}

					else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						HandleEscapePressed();
					}

				}
				return false;
			}
		};
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
	}

	void initFromSettings() {

		boolean wrapOn = settings.GetWordWrap();
		jCheckBoxMenuItemWordWrap.setSelected(wrapOn);

		boolean showLineNumbers = settings.GetShowLineNumbers();
		jCheckBoxMenuItemShowLineNumbers.setSelected(showLineNumbers);

		Rectangle bounds = settings.GetMainFormBounds();
		setBounds(bounds);
		int newState = 0;
		if (settings.GetIsMaximizedHorizontal()) {
			newState |= JFrame.MAXIMIZED_HORIZ;
		}
		if (settings.GetIsMaximizedVertical()) {
			newState |= JFrame.MAXIMIZED_VERT;
		}
		setExtendedState(newState);
	}

	public MainForm() {
		this.handler = new TransferHandler() {
			@Override
			public boolean canImport(TransferHandler.TransferSupport support) {
				if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					return false;
				}
				if ((support.getSourceDropActions() & MOVE) != MOVE) {
					return false;
				}
				support.setDropAction(MOVE);
				return true;
			}

			@Override
			public boolean importData(TransferHandler.TransferSupport support) {
				if (!canImport(support)) {
					return false;
				}
				Transferable t = support.getTransferable();
				try {
					java.util.List<File> files = (java.util.List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
					for (File file : files) {
						DoOpen(file);
					}
				}
				catch (UnsupportedFlavorException | IOException e) {
					return false;
				}
				return true;
			}
		};
		initComponents();
		initFromSettings();
		initializeGlobalKeys();
		searchPanel1.setVisible(false);
		searchPanel1.SetMainForm(this);
		jFileChooser1.setMultiSelectionEnabled(true);
		FileNew();
		setTransferHandler(handler);
	}

	void FileNew() {
		DocumentView document = new DocumentView(this, settings);
		jTabbedPane1.add(document, document.GetFilenameAlias());
		jTabbedPane1.setSelectedComponent(document);
		HandleDocumentChanged(document);
	}

	public @Override
	void toFront() {
		int extendedState = super.getExtendedState() & ~JFrame.ICONIFIED;
		super.setExtendedState(extendedState);
		super.setAlwaysOnTop(true);
		super.toFront();
		super.requestFocus();
		super.setAlwaysOnTop(false);
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
		toFront();
	}

	void FileOpen() {
		jFileChooser1.setCurrentDirectory(settings.GetOpenDirectory());
		int returnVal = jFileChooser1.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			for (File f : jFileChooser1.getSelectedFiles()) {
				DoOpen(f);
				settings.SetOpenDirectory(jFileChooser1.getCurrentDirectory());
			}
		}
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
				DoExit();
			}
			return true;
		}
		return false;
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

		setTitle(windowTitle);
	}

	public void HandleDocumentChanged(DocumentView documentView) {
		SetTabAndWindowTitle(documentView);
		if (searchPanel1.isVisible()) {
			searchPanel1.MarkAll();
		}
	}

	void HandleEscapePressed() {
		searchPanel1.setVisible(false);
		CurrentDocumentView().ClearMarkings();
	}

	void FormBoundsChanged() {
		boolean isMaximized = ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH);
		if (!isMaximized) {
			settings.SetMainFormBounds(getBounds());
		}
	}

	void WindowStateChanged(int newState) {
		settings.SetIsMaximizedVertical((newState & JFrame.MAXIMIZED_VERT) != 0);
		settings.SetIsMaximizedHorizontal((newState & JFrame.MAXIMIZED_HORIZ) != 0);
	}

	void TabChanged() {
		DocumentView newTab = CurrentDocumentView();
		if (documentStack.contains(newTab)) {
			documentStack.remove(newTab);
		}

		// Null check needed here. It might be the last tab that is closed.
		if (newTab == null) {
			return;
		}

		documentStack.add(0, newTab);
		SetTabAndWindowTitle(newTab);
	}

	void UpdateWordWrap() {
		settings.SetWordWrap(jCheckBoxMenuItemWordWrap.isSelected());
	}

	void UpdateShowLineNumbers() {
		settings.SetShowLineNumbers(jCheckBoxMenuItemShowLineNumbers.isSelected());
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
        jCheckBoxMenuItemShowLineNumbers = new javax.swing.JCheckBoxMenuItem();
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
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });

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

        jCheckBoxMenuItemShowLineNumbers.setSelected(true);
        jCheckBoxMenuItemShowLineNumbers.setText("Show line numbers");
        jCheckBoxMenuItemShowLineNumbers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemShowLineNumbersActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItemShowLineNumbers);

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
			FileClose();
		}
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void jMenuItemReloadWithEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReloadWithEncodingActionPerformed
		ReloadWithDifferentEncoding();
    }//GEN-LAST:event_jMenuItemReloadWithEncodingActionPerformed

    private void jMenuItemChangeEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemChangeEncodingActionPerformed
		ChangeEncoding();
    }//GEN-LAST:event_jMenuItemChangeEncodingActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
		FormBoundsChanged(); // improvement: only when resizing ends
    }//GEN-LAST:event_formComponentResized

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
		FormBoundsChanged(); // improvement: only when movement ends
    }//GEN-LAST:event_formComponentMoved

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
		TabChanged();
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
		WindowStateChanged(evt.getNewState());
    }//GEN-LAST:event_formWindowStateChanged

    private void jCheckBoxMenuItemShowLineNumbersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemShowLineNumbersActionPerformed
		UpdateShowLineNumbers();
    }//GEN-LAST:event_jCheckBoxMenuItemShowLineNumbersActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemShowLineNumbers;
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
