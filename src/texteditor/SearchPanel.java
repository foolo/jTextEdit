package texteditor;

import org.fife.ui.rtextarea.SearchContext;

public class SearchPanel extends javax.swing.JPanel {

	SearchContext searchContext = new SearchContext();

	public SearchPanel() {
		initComponents();
	}

	TextEditor textEditor = null;

	public void SetMainForm(TextEditor te) {
		textEditor = te;
	}

	void UpdateSearchContext() {
		searchContext.setSearchFor(jTextFieldSearch.getText());
		searchContext.setReplaceWith(jTextFieldReplace.getText());
		searchContext.setMatchCase(jCheckBoxMatchCase.isSelected());
		searchContext.setRegularExpression(jCheckBoxUseRegex.isSelected());
		searchContext.setSearchForward(jCheckBoxBackwards.isSelected() == false);
		searchContext.setSearchSelectionOnly(false);
		searchContext.setWholeWord(jCheckBoxWholeWords.isSelected());
		MarkAll();
	}

	void FindButtonClicked() {
		UpdateSearchContext();
		textEditor.CurrentDocumentView().Find(searchContext);
	}

	void ReplaceButtonClicked() {
		UpdateSearchContext();
		textEditor.CurrentDocumentView().Replace(searchContext);
	}

	void ReplaceAllButtonClicked() {
		UpdateSearchContext();
		textEditor.CurrentDocumentView().ReplaceAll(searchContext);
	}

	void MarkAll() {
		textEditor.CurrentDocumentView().MarkAll(searchContext);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupSearchScope = new javax.swing.ButtonGroup();
        jTextFieldSearch = new javax.swing.JTextField();
        jTextFieldReplace = new javax.swing.JTextField();
        jCheckBoxMatchCase = new javax.swing.JCheckBox();
        jCheckBoxWholeWords = new javax.swing.JCheckBox();
        jCheckBoxUseRegex = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButtonFind = new javax.swing.JButton();
        jButtonReplace = new javax.swing.JButton();
        jButtonReplaceAll = new javax.swing.JButton();
        jCheckBoxBackwards = new javax.swing.JCheckBox();

        jTextFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchKeyReleased(evt);
            }
        });

        jTextFieldReplace.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldReplaceKeyReleased(evt);
            }
        });

        jCheckBoxMatchCase.setText("Case sensitive");
        jCheckBoxMatchCase.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxMatchCaseItemStateChanged(evt);
            }
        });

        jCheckBoxWholeWords.setText("Whole words");
        jCheckBoxWholeWords.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxWholeWordsItemStateChanged(evt);
            }
        });

        jCheckBoxUseRegex.setText("Use regex");
        jCheckBoxUseRegex.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxUseRegexItemStateChanged(evt);
            }
        });

        jLabel1.setText("Search for");

        jLabel2.setText("Replace with");

        jButtonFind.setText("Find");
        jButtonFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFindActionPerformed(evt);
            }
        });

        jButtonReplace.setText("Replace");
        jButtonReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReplaceActionPerformed(evt);
            }
        });

        jButtonReplaceAll.setText("Replace all");
        jButtonReplaceAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReplaceAllActionPerformed(evt);
            }
        });

        jCheckBoxBackwards.setText("Backwards");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBoxMatchCase)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBoxUseRegex)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonFind))
                            .addComponent(jCheckBoxBackwards)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextFieldReplace, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBoxWholeWords)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonReplace)
                    .addComponent(jButtonReplaceAll))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxMatchCase)
                    .addComponent(jCheckBoxUseRegex)
                    .addComponent(jButtonFind)
                    .addComponent(jButtonReplace))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBoxWholeWords)
                        .addComponent(jTextFieldReplace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCheckBoxBackwards)
                        .addComponent(jButtonReplaceAll))
                    .addComponent(jLabel2)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchKeyReleased
		UpdateSearchContext();
    }//GEN-LAST:event_jTextFieldSearchKeyReleased

    private void jCheckBoxMatchCaseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxMatchCaseItemStateChanged
		UpdateSearchContext();
    }//GEN-LAST:event_jCheckBoxMatchCaseItemStateChanged

    private void jTextFieldReplaceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldReplaceKeyReleased
		UpdateSearchContext();
    }//GEN-LAST:event_jTextFieldReplaceKeyReleased

    private void jCheckBoxWholeWordsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxWholeWordsItemStateChanged
		UpdateSearchContext();
    }//GEN-LAST:event_jCheckBoxWholeWordsItemStateChanged

    private void jCheckBoxUseRegexItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxUseRegexItemStateChanged
		UpdateSearchContext();
    }//GEN-LAST:event_jCheckBoxUseRegexItemStateChanged

    private void jButtonFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFindActionPerformed
		FindButtonClicked();
    }//GEN-LAST:event_jButtonFindActionPerformed

    private void jButtonReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReplaceActionPerformed
		ReplaceButtonClicked();
    }//GEN-LAST:event_jButtonReplaceActionPerformed

    private void jButtonReplaceAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReplaceAllActionPerformed
		ReplaceAllButtonClicked();
    }//GEN-LAST:event_jButtonReplaceAllActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupSearchScope;
    private javax.swing.JButton jButtonFind;
    private javax.swing.JButton jButtonReplace;
    private javax.swing.JButton jButtonReplaceAll;
    private javax.swing.JCheckBox jCheckBoxBackwards;
    private javax.swing.JCheckBox jCheckBoxMatchCase;
    private javax.swing.JCheckBox jCheckBoxUseRegex;
    private javax.swing.JCheckBox jCheckBoxWholeWords;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextFieldReplace;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables
}
