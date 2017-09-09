package texteditor;

import java.util.ArrayList;
import javax.swing.DefaultListModel;

public final class DocumentSwitcher extends javax.swing.JDialog {

	ArrayList<DocumentView> documentStack;

	DefaultListModel<DocumentView> listModel = new DefaultListModel<>();

	int currentIndex = 0;

	public DocumentSwitcher(java.awt.Frame parent, boolean forward, ArrayList<DocumentView> ds) {
		super(parent, true);
		initComponents();
		documentStack = new ArrayList<>(ds);
		for (DocumentView dv : documentStack) {
			listModel.addElement(dv);
		}
		Navigate(forward);
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jList1.setModel(listModel);
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	DocumentView GetSelectedDocument() {
		return (DocumentView) jList1.getSelectedValue();
	}

	void Navigate(boolean forward) {
		currentIndex = currentIndex + (forward ? 1 : -1);
		if (currentIndex < 0) {
			currentIndex = listModel.size() - 1;
		}
		if (currentIndex > listModel.size() - 1) {
			currentIndex = 0;
		}
		jList1.setSelectedIndex(currentIndex);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
