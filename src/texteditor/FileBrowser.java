package texteditor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class FileBrowser extends javax.swing.JPanel {

	public static class DirectoryTreeNode implements TreeNode {

		File file;

		ArrayList<DirectoryTreeNode> dirEntries = null;

		DirectoryTreeNode(File file) {
			this.file = file;
		}

		void initializeDirEntries() {
			if (dirEntries != null) {
				return;
			}
			dirEntries = new ArrayList<>();
			File[] filesArr = file.listFiles();
			if (filesArr == null) {
				return;
			}
			ArrayList<File> files = new ArrayList<>(Arrays.asList(filesArr));
			Collections.sort(files, (File f1, File f2) -> {
				if (f1.isDirectory() && !f2.isDirectory()) {
					return -1;
				}
				if (!f1.isDirectory() && f2.isDirectory()) {
					return 1;
				}
				return f1.compareTo(f2);
			});
			for (File f : files) {
				dirEntries.add(new DirectoryTreeNode(f));
			}
		}

		@Override
		public String toString() {
			return file.getName();
		}

		@Override
		public TreeNode getChildAt(int index) {
			initializeDirEntries();
			return dirEntries.get(index);
		}

		@Override
		public int getChildCount() {
			initializeDirEntries();
			return dirEntries.size();
		}

		@Override
		public TreeNode getParent() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getIndex(TreeNode node) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean getAllowsChildren() {
			return file.isDirectory();
		}

		@Override
		public boolean isLeaf() {
			return !file.isDirectory();
		}

		@Override
		public Enumeration children() {
			throw new UnsupportedOperationException();
		}
	}

	interface FileBrowserListener {

		void fileSelected(File file);

		void directoryChangeRequested(File file);
	}

	ArrayList<FileBrowserListener> fileBrowserListeners = new ArrayList<>();

	public FileBrowser() {
		initComponents();
	}

	void addFileBrowserListener(FileBrowserListener listener) {
		fileBrowserListeners.add(listener);
	}

	public void showDir(File node) {
		File path;
		try {
			path = node.getCanonicalFile();
		}
		catch (IOException ex) {
			path = node.getAbsoluteFile();
		}
		fileBrowserTree1.setModel(new DefaultTreeModel(new FileBrowser.DirectoryTreeNode(path)));
		fileBrowserTree1.setRootVisible(false);
		fileBrowserTree1.setShowsRootHandles(true);
	}

	File getParentPath() {
		DirectoryTreeNode node = (DirectoryTreeNode) fileBrowserTree1.getModel().getRoot();
		File parent = node.file.getParentFile();
		if (parent == null) {
			return node.file;
		}
		return parent;
	}

	void selectFile(File file) {
		for (FileBrowserListener l : fileBrowserListeners) {
			l.fileSelected(file);
		}
	}

	void requestDirectoryChange(File file) {
		for (FileBrowserListener l : fileBrowserListeners) {
			l.directoryChangeRequested(file);
		}
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        fileBrowserTree1 = new texteditor.FileBrowserTree();
        jButtonNavigateUp = new javax.swing.JButton();

        fileBrowserTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fileBrowserTree1MousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(fileBrowserTree1);

        jButtonNavigateUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icons/Up16.gif"))); // NOI18N
        jButtonNavigateUp.setToolTipText("");
        jButtonNavigateUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNavigateUpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButtonNavigateUp, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jButtonNavigateUp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fileBrowserTree1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileBrowserTree1MousePressed
		int selRow = fileBrowserTree1.getRowForLocation(evt.getX(), evt.getY());
		TreePath selPath = fileBrowserTree1.getPathForLocation(evt.getX(), evt.getY());
		if (selRow != -1) {
			if (evt.getClickCount() == 2) {
				DirectoryTreeNode node = (DirectoryTreeNode) selPath.getLastPathComponent();
				if (node.file.isFile()) {
					selectFile(node.file);
				}
				else if (node.file.isDirectory()) {
					requestDirectoryChange(node.file);
				}
			}
		}
    }//GEN-LAST:event_fileBrowserTree1MousePressed

    private void jButtonNavigateUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNavigateUpActionPerformed
		requestDirectoryChange(getParentPath());
    }//GEN-LAST:event_jButtonNavigateUpActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private texteditor.FileBrowserTree fileBrowserTree1;
    private javax.swing.JButton jButtonNavigateUp;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
