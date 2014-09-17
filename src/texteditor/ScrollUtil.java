package texteditor;

import java.awt.Rectangle;
import javax.swing.JComponent;

public class ScrollUtil {

	static double GetRelativeXScrollPosition(JComponent component) {
		int maxPos = component.getBounds().height - component.getVisibleRect().height;
		double pos = component.getVisibleRect().y;
		return pos / maxPos;
	}

	static void SetRelativeXScrollPosition(JComponent component, double relPos) {
		int maxPos = component.getBounds().height - component.getVisibleRect().height;
		Rectangle rect = component.getVisibleRect();
		rect.y = (int) (maxPos * relPos);
		component.scrollRectToVisible(rect);
	}
}
