/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.puregwt.client.gfx;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.Widget;

/**
 * Default implementation of {@link CanvasContainer}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CanvasContainerImpl implements CanvasContainer {

	private Canvas canvas;

	private List<CanvasShape> shapes = new ArrayList<CanvasShape>();

	private double deltaX;

	private double deltaY;

	private double scaleX;

	private double scaleY;

	public CanvasContainerImpl(int width, int height) {
		canvas = Canvas.createIfSupported();
		if (canvas != null) {
			canvas.setWidth(width + "px");
			canvas.setCoordinateSpaceWidth(width);
			canvas.setHeight(height + "px");
			canvas.setCoordinateSpaceHeight(height);
		} else {
			throw new RuntimeException("canvas unsupported");
		}
	}

	@Override
	public void setTranslation(double deltaX, double deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		setTransform();
	}

	@Override
	public void setScale(double scaleX, double scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		setTransform();
	}

	private void setTransform() {
		canvas.getContext2d().setTransform(scaleX, 0, 0, scaleY, deltaX, deltaY);
		repaint();
	}

	@Override
	public void setFixedSize(boolean fixedSize) {
	}

	@Override
	public boolean isFixedSize() {
		return false;
	}

	@Override
	public Widget asWidget() {
		return canvas;
	}

	@Override
	public void addShape(CanvasShape shape) {
		shapes.add(shape);
		repaint();
	}

	@Override
	public void addAll(List<CanvasShape> all) {
		shapes.addAll(all);
		repaint();
	}

	@Override
	public void removeShape(CanvasShape shape) {
		shapes.remove(shape);
		repaint();
	}

	@Override
	public void clear() {
		shapes.clear();
		repaint();
	}

	@Override
	public void setOpacity(double opacity) {
		canvas.getContext2d().setGlobalAlpha(opacity);
		System.out.println("opacity = " + opacity);
		repaint();
	}

	protected void repaint() {
		canvas.getContext2d().save();
		canvas.getContext2d().setTransform(1, 0, 0, 1, 0, 0);
		canvas.getContext2d().clearRect(0, 0, canvas.getOffsetWidth(), canvas.getOffsetHeight());
		canvas.getContext2d().restore();
		for (CanvasShape shape : shapes) {
			shape.paint(canvas);
		}
	}

}
