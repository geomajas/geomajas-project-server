/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt.client.gfx;

import org.geomajas.geometry.Coordinate;

/**
 * Base class for {@link TransformableWidget} implementations. Subclasses should implement
 * {@link #setScreenPosition(double, double)} to position their widget on the map when scaling/panning occurs. The
 * screen position corresponds to the world position passed to the constructor
 * {@link #AbstractTransformableWidget(double, double)} or {@link #AbstractTransformableWidget(Coordinate)}.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class AbstractTransformableWidget implements TransformableWidget {

	private Coordinate worldPosition;

	private double deltaX;

	private double deltaY;

	private double scaleX;

	private double scaleY;

	private boolean fixedSize = true;

	protected AbstractTransformableWidget(double x, double y) {
		this(new Coordinate(x, y));
	}

	protected AbstractTransformableWidget(Coordinate worldPosition) {
		setWorldPosition(worldPosition);
	}

	protected void setWorldPosition(Coordinate worldPosition) {
		this.worldPosition = (Coordinate) worldPosition.clone();
	}

	public Coordinate getWorldPosition() {
		return worldPosition;
	}

	@Override
	public void setTranslation(double deltaX, double deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		render();
	}

	@Override
	public void setScale(double scaleX, double scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		render();
	}

	protected void render() {
		double left = getWorldPosition().getX() * scaleX + deltaX;
		double top = getWorldPosition().getY() * scaleY + deltaY;
		setScreenPosition(left, top);
	}

	protected abstract void setScreenPosition(double left, double top);

	@Override
	public void setFixedSize(boolean fixedSize) {
		this.fixedSize = fixedSize;
	}

	@Override
	public boolean isFixedSize() {
		return fixedSize;
	}

}
