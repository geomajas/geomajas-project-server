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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Matrix;
import org.geomajas.geometry.service.BboxService;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Default implementation of {@link CanvasContainer}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CanvasContainerImpl implements CanvasContainer {

	/** off-screen buffer to improve rendering performance **/
	private Canvas buffer;

	/** buffer world bounds **/
	private Bbox bufferBounds;

	/** actual canvas **/
	private Canvas canvas;

	/** canvas world bounds **/
	private Bbox canvasBounds;

	private List<CanvasShape> shapes = new ArrayList<CanvasShape>();

	private Matrix matrix;

	private double bufferFactor = 3.0;

	public CanvasContainerImpl(int width, int height) {
		matrix = new Matrix(1.0, 0.0, 0.0, 1.0, 0.0, 0.0);
		// create a canvas
		canvas = Canvas.createIfSupported();
		if (canvas != null) {
			// create an off-screen buffer with bufferFactor x the size
			buffer = Canvas.createIfSupported();
			buffer.setVisible(false);
			RootPanel.get().add(buffer);
			setPixelSize(width, height);
		} else {
			throw new RuntimeException("canvas unsupported");
		}
	}

	@Override
	public void setTranslation(double deltaX, double deltaY) {
		matrix = new Matrix(matrix.getXx(), 0.0, 0.0, matrix.getYy(), deltaX, deltaY);
		updateTransform();
		if (!BboxService.contains(bufferBounds, canvasBounds)) {
			repaintBuffer();
		}
		copyBufferToCanvas();
	}

	@Override
	public void setScale(double scaleX, double scaleY) {
		matrix = new Matrix(scaleX, 0.0, 0.0, scaleY, matrix.getDx(), matrix.getDy());
		updateTransform();
		repaint();
	}
	
	public void setBufferFactor(double bufferFactor) {
		this.bufferFactor = bufferFactor;
	}

	private void updateTransform() {
		canvasBounds = toWorld(new Bbox(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight()));
	}

	private void copyBufferToCanvas() {
		clearCanvas(canvas);
		double dx = (bufferBounds.getX() - canvasBounds.getX()) * matrix.getXx();
		double dy = -(bufferFactor - 1) * canvas.getCoordinateSpaceHeight()
				+ (bufferBounds.getY() - canvasBounds.getY()) * matrix.getYy();
		canvas.getContext2d().drawImage(buffer.getCanvasElement(), dx, dy);
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
	public void repaint() {
		repaintBuffer();
		copyBufferToCanvas();
	}

	@Override
	public void setOpacity(double opacity) {
		canvas.getContext2d().setGlobalAlpha(opacity);
		copyBufferToCanvas();
	}

	@Override
	public void setPixelSize(int width, int height) {
		canvas.setWidth(width + "px");
		canvas.setCoordinateSpaceWidth(width);
		canvas.setHeight(height + "px");
		canvas.setCoordinateSpaceHeight(height);
		buffer.setWidth(bufferFactor * width + "px");
		buffer.setCoordinateSpaceWidth((int) (bufferFactor * width));
		buffer.setHeight(bufferFactor * height + "px");
		buffer.setCoordinateSpaceHeight((int) (bufferFactor * height));
		repaintBuffer();
	}

	protected void repaintBuffer() {
		clearCanvas(buffer);
		double bufferDx = matrix.getDx() + (bufferFactor - 1) * 0.5 * canvas.getCoordinateSpaceWidth();
		double bufferDy = matrix.getDy() + (bufferFactor - 1) * 0.5 * canvas.getCoordinateSpaceHeight();
		Matrix bufferMatrix = new Matrix(matrix.getXx(), 0, 0, matrix.getYy(), bufferDx, bufferDy);
		buffer.getContext2d().setTransform(matrix.getXx(), 0, 0, matrix.getYy(), bufferDx, bufferDy);
		double width = canvas.getCoordinateSpaceWidth();
		double height = canvas.getCoordinateSpaceHeight();
		bufferBounds = toWorld(new Bbox(-0.5 * (bufferFactor - 1) * width, -0.5 * (bufferFactor - 1) * height,
				bufferFactor * width, bufferFactor * height));
		for (CanvasShape shape : shapes) {
			shape.paint(buffer, bufferMatrix);
		}
	}

	private Bbox toWorld(Bbox bbox) {
		double x1 = (bbox.getX() - matrix.getDx()) / matrix.getXx();
		double y1 = (bbox.getY() - matrix.getDy()) / matrix.getYy();
		double x2 = (bbox.getMaxX() - matrix.getDx()) / matrix.getXx();
		double y2 = (bbox.getMaxY() - matrix.getDy()) / matrix.getYy();
		double x = Math.min(x1, x2);
		double y = Math.min(y1, y2);
		return new Bbox(x, y, Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	private void clearCanvas(Canvas canvas) {
		canvas.getContext2d().save();
		canvas.getContext2d().setTransform(1, 0, 0, 1, 0, 0);
		canvas.getContext2d().clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
		canvas.getContext2d().restore();
	}

}
