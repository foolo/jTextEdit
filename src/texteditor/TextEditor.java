package texteditor;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;

public final class TextEditor extends javax.swing.JFrame {

	final JFileChooser jFileChooser1 = new JFileChooser();

	public TextEditor() {
		initComponents();
		NewDocument();
	}

	public void NewDocument() {
		DocumentView document = new DocumentView(this);
		jTabbedPane1.add(document, document.GetFilenameAlias());
		jTabbedPane1.setSelectedComponent(document);
	}

	public void OpenDocument() {
		int returnVal = jFileChooser1.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = jFileChooser1.getSelectedFile();
			if (f != null) {
				DocumentView documentView = new DocumentView(this);
				jTabbedPane1.add(documentView);
				jTabbedPane1.setSelectedComponent(documentView);
				documentView.LoadFile(f);
			}
		}
	}

	public boolean CloseDocument() {
		DocumentView currentDocumentView = CurrentDocumentView();
		if (currentDocumentView != null) {
			if (currentDocumentView.HandleCurrentFile()) {
				jTabbedPane1.remove(currentDocumentView);
				return true;
			}
		}
		return false;
	}

	DocumentView CurrentDocumentView() {
		Component c = jTabbedPane1.getSelectedComponent();
		return ((DocumentView) c);
	}

	public void SaveDocument() {
		DocumentView currentDocumentView = CurrentDocumentView();
		if (currentDocumentView != null) {
			currentDocumentView.SaveDoc();
		}
	}

	public void SaveDocumentAs() {
		DocumentView currentDocumentView = CurrentDocumentView();
		if (currentDocumentView != null) {
			currentDocumentView.FileSaveAs();

		}
	}

	public void ExitProgram() {
		while (jTabbedPane1.getTabCount() > 0) {
			if (CloseDocument() == false) {
				return;
			}
		};
		dispose();
	}

	public void RefreshDocumentTab(DocumentView documentView) {
		int i = jTabbedPane1.indexOfComponent(documentView);
		jTabbedPane1.setTitleAt(i, documentView.GetFilenameAlias());
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemNew = new javax.swing.JMenuItem();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemClose = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemSaveAs = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("File");

        jMenuItemNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemNew.setText("New");
        jMenuItemNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemNew);

        jMenuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemOpen.setText("Open");
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemOpen);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemClose);

        jMenuItemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSave.setText("Save");
        jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemSave);

        jMenuItemSaveAs.setText("Save as...");
        jMenuItemSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveAsActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemSaveAs);

        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemExit);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewActionPerformed
		NewDocument();
    }//GEN-LAST:event_jMenuItemNewActionPerformed

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
		OpenDocument();
    }//GEN-LAST:event_jMenuItemOpenActionPerformed

    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
		SaveDocument();
    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    private void jMenuItemSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAsActionPerformed
		SaveDocumentAs();
    }//GEN-LAST:event_jMenuItemSaveAsActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
		ExitProgram();
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseActionPerformed
		CloseDocument();
    }//GEN-LAST:event_jMenuItemCloseActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(TextEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(TextEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(TextEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(TextEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TextEditor().setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemClose;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemNew;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemSaveAs;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
