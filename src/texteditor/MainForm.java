package texteditor;

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
import java.util.ListIterator;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.MOVE;

public final class MainForm extends javax.swing.JFrame {

	final JFileChooser jFileChooser1 = new JFileChooser();

	Settings settings;

	PropertyDispatcher propertyDispatcher = new PropertyDispatcher();

	DocumentSwitcher documentSwitcher = null;

	private final TransferHandler handler;

	private void PassCtrlTabToSwitcher(Frame frame, boolean forward) {
		if (documentSwitcher == null) {
			documentSwitcher = new DocumentSwitcher(frame, forward, tabHandler.GetDocumentStack());
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
							tabHandler.SetSelected(selectedDocumentView);
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

	private class MySettingsListener extends SettingsListener {

		@Override
		void RecentFilesChanged() {
			UpdateOpenRecentMenu();
		}
	}
	MySettingsListener mySettingsListener = new MySettingsListener();

	public MainForm(Settings s) {
		settings = s;
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
					java.util.List<?> files = (java.util.List<?>) t.getTransferData(DataFlavor.javaFileListFlavor);
					for (Object file : files) {
						tabHandler.DoOpen((File)file);
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
		s.AddListener(mySettingsListener);
		mySettingsListener.CallAll();
		propertyDispatcher.AddListener(tabHandler.propertiesListener);
		initializeGlobalKeys();
		searchPanel1.setVisible(false);
		searchPanel1.PostAttachment(this, tabHandler);
		jFileChooser1.setMultiSelectionEnabled(true);
		tabHandler.New();
		setTransferHandler(handler);
	}

	void Open(File f) {
		tabHandler.DoOpen(f);
	}

	void UpdateOpenRecentMenu() {
		RecentFilesCollection rfc = settings.GetRecentFilesCollection();
		ArrayList<String> recentFiles = rfc.GetRecentFiles();
		jMenuOpenRecent.removeAll();
		ListIterator li = recentFiles.listIterator(recentFiles.size());
		MainForm mainForm = this;
		while (li.hasPrevious()) {
			String filename = (String) li.previous();
			final JMenuItem item = new JMenuItem(filename);
			jMenuOpenRecent.add(item);
			item.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					File f = new File(item.getText());
					if (!f.exists()) {
						RecentFilesCollection recentFiles = settings.GetRecentFilesCollection();
						recentFiles.removeFile(f);
						settings.SetRecentFilesCollection(recentFiles);
						JOptionPane.showMessageDialog(mainForm, "Could not find \"" + f + "\"");
					}
					else {
						tabHandler.DoOpen(new File(item.getText()));
					}
				}
			});
		}
		jMenuOpenRecent.addSeparator();
		jMenuOpenRecent.add(jMenuItemClearRecent);
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

	void FileOpen() {
		jFileChooser1.setCurrentDirectory(settings.GetOpenDirectory());
		int returnVal = jFileChooser1.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			for (File f : jFileChooser1.getSelectedFiles()) {
				tabHandler.DoOpen(f);
			}
			settings.SetOpenDirectory(jFileChooser1.getCurrentDirectory());
		}
	}

	void FileExit() {
		if (tabHandler.CloseAllTabs()) {
			dispose();
		}
	}

	void EditFind() {
		searchPanel1.setVisible(true);
	}

	public void UpdateMarkAllSearchHits(DocumentView documentView) {
		if (searchPanel1.isVisible()) {
			searchPanel1.MarkAll();
		}
	}

	public void HandleDocumentPropertiesChanged(DocumentView documentView) {
		statusBar1.ReflectCurrentDocument(documentView);
	}

	void HandleEscapePressed() {
		searchPanel1.setVisible(false);
		tabHandler.HandleEscape();
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

	void UpdateWordWrap() {
		settings.SetWordWrap(jCheckBoxMenuItemWordWrap.isSelected());
	}

	void UpdateShowLineNumbers() {
		settings.SetShowLineNumbers(jCheckBoxMenuItemShowLineNumbers.isSelected());
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchPanel1 = new texteditor.SearchPanel();
        tabHandler = new texteditor.TabHandler(this, settings, propertyDispatcher);
        statusBar1 = new texteditor.StatusBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemNew = new javax.swing.JMenuItem();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuOpenRecent = new javax.swing.JMenu();
        jMenuItemClearRecent = new javax.swing.JMenuItem();
        jMenuItemClose = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemSaveAs = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuView = new javax.swing.JMenu();
        jCheckBoxMenuItemWordWrap = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemShowLineNumbers = new javax.swing.JCheckBoxMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuItemFind = new javax.swing.JMenuItem();
        jMenuItemPreferences = new javax.swing.JMenuItem();
        jMenuItemScriptOperations = new javax.swing.JMenuItem();
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

        jMenuOpenRecent.setText("Open recent");

        jMenuItemClearRecent.setText("Clear recent files");
        jMenuItemClearRecent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClearRecentActionPerformed(evt);
            }
        });
        jMenuOpenRecent.add(jMenuItemClearRecent);

        jMenuFile.add(jMenuOpenRecent);

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

        jMenuView.setText("View");

        jCheckBoxMenuItemWordWrap.setSelected(true);
        jCheckBoxMenuItemWordWrap.setText("Word wrap");
        jCheckBoxMenuItemWordWrap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemWordWrapActionPerformed(evt);
            }
        });
        jMenuView.add(jCheckBoxMenuItemWordWrap);

        jCheckBoxMenuItemShowLineNumbers.setSelected(true);
        jCheckBoxMenuItemShowLineNumbers.setText("Show line numbers");
        jCheckBoxMenuItemShowLineNumbers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemShowLineNumbersActionPerformed(evt);
            }
        });
        jMenuView.add(jCheckBoxMenuItemShowLineNumbers);

        jMenuBar1.add(jMenuView);

        jMenuEdit.setText("Edit");

        jMenuItemFind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemFind.setText("Find");
        jMenuItemFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFindActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemFind);

        jMenuItemPreferences.setText("Preferences...");
        jMenuItemPreferences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPreferencesActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemPreferences);

        jMenuItemScriptOperations.setText("Script operations");
        jMenuItemScriptOperations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemScriptOperationsActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemScriptOperations);

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
                .addContainerGap(61, Short.MAX_VALUE))
            .addComponent(statusBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabHandler, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(searchPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabHandler, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewActionPerformed
		tabHandler.New();
    }//GEN-LAST:event_jMenuItemNewActionPerformed

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
		FileOpen();
    }//GEN-LAST:event_jMenuItemOpenActionPerformed

    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
		tabHandler.FileSave();
    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    private void jMenuItemSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAsActionPerformed
		tabHandler.FileSaveAs();
    }//GEN-LAST:event_jMenuItemSaveAsActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
		FileExit();
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseActionPerformed
		tabHandler.FileClose();
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

    private void jMenuItemReloadWithEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReloadWithEncodingActionPerformed
		tabHandler.ReloadWithDifferentEncoding();
    }//GEN-LAST:event_jMenuItemReloadWithEncodingActionPerformed

    private void jMenuItemChangeEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemChangeEncodingActionPerformed
		tabHandler.ChangeEncoding();
    }//GEN-LAST:event_jMenuItemChangeEncodingActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
		FormBoundsChanged(); // improvement: only when resizing ends
    }//GEN-LAST:event_formComponentResized

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
		FormBoundsChanged(); // improvement: only when movement ends
    }//GEN-LAST:event_formComponentMoved

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
		WindowStateChanged(evt.getNewState());
    }//GEN-LAST:event_formWindowStateChanged

    private void jCheckBoxMenuItemShowLineNumbersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemShowLineNumbersActionPerformed
		UpdateShowLineNumbers();
    }//GEN-LAST:event_jCheckBoxMenuItemShowLineNumbersActionPerformed

    private void jMenuItemClearRecentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemClearRecentActionPerformed
		RecentFilesCollection rfc = settings.GetRecentFilesCollection();
		rfc.Clear();
		settings.SetRecentFilesCollection(rfc);
    }//GEN-LAST:event_jMenuItemClearRecentActionPerformed

    private void jMenuItemPreferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPreferencesActionPerformed
		new PreferencesForm(this, settings).setVisible(true);
    }//GEN-LAST:event_jMenuItemPreferencesActionPerformed

    private void jMenuItemScriptOperationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemScriptOperationsActionPerformed
        ScriptDialog scriptDialog = new ScriptDialog(tabHandler);
		scriptDialog.setLocationRelativeTo(this);
		scriptDialog.setVisible(true);
    }//GEN-LAST:event_jMenuItemScriptOperationsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemShowLineNumbers;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemWordWrap;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuDocument;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemChangeEncoding;
    private javax.swing.JMenuItem jMenuItemClearRecent;
    private javax.swing.JMenuItem jMenuItemClose;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemFind;
    private javax.swing.JMenuItem jMenuItemNew;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemPreferences;
    private javax.swing.JMenuItem jMenuItemReloadWithEncoding;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemSaveAs;
    private javax.swing.JMenuItem jMenuItemScriptOperations;
    private javax.swing.JMenu jMenuOpenRecent;
    private javax.swing.JMenu jMenuView;
    private texteditor.SearchPanel searchPanel1;
    private texteditor.StatusBar statusBar1;
    private texteditor.TabHandler tabHandler;
    // End of variables declaration//GEN-END:variables
}
