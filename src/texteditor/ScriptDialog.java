package texteditor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;

public class ScriptDialog extends javax.swing.JFrame {

	public ScriptDialog(TabHandler th) {
		tabHandler = th;
		initComponents();
	}

	TabHandler tabHandler;

	void executeScript() {
		DocumentView documentView = tabHandler.CurrentDocumentView();
		String inputText = documentView.getText();
		String encoding = documentView.GetEncoding();
		ScriptingContainer container = new ScriptingContainer(LocalContextScope.SINGLETHREAD);
		
		try {
			container.setInput(new ByteArrayInputStream(inputText.getBytes(encoding)));
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os, true);
			container.setOutput(ps);
			container.runScriptlet(rSyntaxTextArea1.getText());
			container.terminate();
			String outputText = os.toString(encoding);
			documentView.setText(outputText);
		}
		catch (UnsupportedEncodingException ex) {
			Logger.getLogger(ScriptDialog.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        rSyntaxTextArea1 = new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        rSyntaxTextArea1.setColumns(20);
        rSyntaxTextArea1.setRows(5);
        rSyntaxTextArea1.setText("a = STDIN.readlines\na.each do |line|\n\tputs line\nend");
        jScrollPane1.setViewportView(rSyntaxTextArea1);

        jButton3.setText("Execute");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
		executeScript();
    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea rSyntaxTextArea1;
    // End of variables declaration//GEN-END:variables
}
