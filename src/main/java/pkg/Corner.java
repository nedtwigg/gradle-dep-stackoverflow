package pkg;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/** Positions within a rectangle (the corners, the center of the lines, and the center). */
public enum Corner {
	// @formatter:off
	TOP_LEFT(0, 0), TOP_RIGHT(1, 0), BOTTOM_LEFT(0, 1), BOTTOM_RIGHT(1, 1), // true corners
	TOP(0.5f, 0), LEFT(0, 0.5f), BOTTOM(0.5f, 1), RIGHT(1, 0.5f), CENTER(0.5f, 0.5f); // edges and center
	// @formatter:on

	private final float x, y;

	private Corner(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/** Returns this corner's position within the given rectangle. */
	public Point getPosition(Rectangle rectangle) {
		return new Point(
				rectangle.x + (int) (x * rectangle.width),
				rectangle.y + (int) (y * rectangle.height));
	}

	/** Returns this corner's position on the given control in display coordinates. */
	public Point getPosition(Control control) {
		if (control instanceof Shell) {
			return getPosition(control.getBounds());
		} else {
			return control.getDisplay().map(control.getParent(), null, getPosition(control.getBounds()));
		}
	}

	/** Returns this corner's position on the given ToolItem in display coordinates. */
	public Point getPosition(ToolItem item) {
		ToolBar toolbar = item.getParent();
		return toolbar.getDisplay().map(toolbar, null, getPosition(item.getBounds()));
	}

	/**
	 * If you move the topLeft of `rectangle` to the returned point,
	 * then this corner will be at `position`. 
	 */
	public Point topLeftRequiredFor(Rectangle rectangle, Point position) {
		Point current = getPosition(rectangle);
		return new Point(
				rectangle.x + position.x - current.x,
				rectangle.y + position.y - current.y);
	}
}
