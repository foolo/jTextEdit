package texteditor;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class FileBrowserTree extends JTree {

	@Override
	public boolean hasBeenExpanded(TreePath path) {
		return true;
	}
}
