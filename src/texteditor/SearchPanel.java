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
		searchContext.setSearchForward(true);
		searchContext.setSearchSelectionOnly(false);
		searchContext.setWholeWord(jCheckBoxWholeWords.isSelected());

		TriggerSearch();
	}

	void TriggerSearch() {
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
        jRadioButtonCurrentDocument = new javax.swing.JRadioButton();
        jRadioButtonAllDocuments = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

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

        buttonGroupSearchScope.add(jRadioButtonCurrentDocument);
        jRadioButtonCurrentDocument.setText("Current document");

        buttonGroupSearchScope.add(jRadioButtonAllDocuments);
        jRadioButtonAllDocuments.setText("All open documents");

        jLabel1.setText("Search for");

        jLabel2.setText("Replace with");

        jButton1.setText("up");

        jButton2.setText("down");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldReplace, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBoxMatchCase)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxUseRegex))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBoxWholeWords)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addGap(4, 4, 4)
                        .addComponent(jButton2)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonCurrentDocument)
                    .addComponent(jRadioButtonAllDocuments))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextFieldReplace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 5, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonCurrentDocument)
                    .addComponent(jCheckBoxUseRegex)
                    .addComponent(jCheckBoxMatchCase))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxWholeWords)
                    .addComponent(jRadioButtonAllDocuments)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(11, 11, 11))
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupSearchScope;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBoxMatchCase;
    private javax.swing.JCheckBox jCheckBoxUseRegex;
    private javax.swing.JCheckBox jCheckBoxWholeWords;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JRadioButton jRadioButtonAllDocuments;
    private javax.swing.JRadioButton jRadioButtonCurrentDocument;
    private javax.swing.JTextField jTextFieldReplace;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables
}
