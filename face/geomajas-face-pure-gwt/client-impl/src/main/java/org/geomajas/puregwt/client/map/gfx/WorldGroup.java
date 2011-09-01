/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.gfx;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.puregwt.client.spatial.Matrix;
import org.geomajas.puregwt.client.spatial.MatrixImpl;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Default implementation of the WorldContainer interface. It represents a vector group element in world space.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class WorldGroup extends VectorGroup implements WorldContainer {

	private Matrix scale = new MatrixImpl(1, 0, 0, 1, 0, 0);

	private Matrix translate = new MatrixImpl();

	private List<WorldObject> children = new ArrayList<WorldObject>();

	public WorldObject add(WorldObject worldObject) {
		worldObject.scaleToScreen(scale.getXx(), scale.getYy());
		super.add(worldObject.getVectorObject());
		children.add(worldObject);
		return worldObject;
	}

	public WorldObject insert(WorldObject worldObject, int beforeIndex) {
		worldObject.scaleToScreen(scale.getXx(), scale.getYy());
		super.insert(worldObject.getVectorObject(), beforeIndex);
		children.add(beforeIndex, worldObject);
		return worldObject;
	}

	public WorldObject remove(WorldObject worldObject) {
		super.remove(worldObject.getVectorObject());
		children.remove(worldObject);
		return worldObject;
	}

	public WorldObject bringToFront(WorldObject worldObject) {
		super.bringToFront(worldObject.getVectorObject());
		return worldObject;
	}

	public WorldObject getWorldObject(int index) {
		return children.get(index);
	}

	public int getWorldObjectCount() {
		return children.size();
	}

	@Override
	public VectorObject add(VectorObject vo) {
		return add(new VectorWorldObject(vo)).getVectorObject();
	}

	@Override
	public VectorObject insert(VectorObject vo, int beforeIndex) {
		return insert(new VectorWorldObject(vo), beforeIndex).getVectorObject();
	}

	@Override
	public VectorObject remove(VectorObject vo) {
		WorldObject wo = null;
		for (WorldObject child : children) {
			if (child.getVectorObject().equals(vo)) {
				wo = child;
			}
		}
		if (wo != null) {
			return remove(wo).getVectorObject();
		} else {
			return vo;
		}
	}

	@Override
	public void clear() {
		super.clear();
		children.clear();
	}

	public void transform(Matrix transformMatrix) {
		Matrix newScale = new MatrixImpl(transformMatrix.getXx(), 0, 0, transformMatrix.getYy(), 0, 0);
		translate = new MatrixImpl(1, 0, 0, 1, transformMatrix.getDx(), transformMatrix.getDy());
		// translate
		transform(getElement(), translate);
		// and scale (if necessary)
		if (!newScale.equals(scale)) {
			scale = newScale;
			for (WorldObject child : children) {
				child.scaleToScreen(scale.getXx(), scale.getYy());
			}
		}
	}

	/**
	 * Fully scaling {@link WorldObject} using the transform attribute.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class VectorWorldObject implements WorldObject {

		private VectorObject vectorObject;

		public VectorWorldObject(VectorObject vectorObject) {
			this.vectorObject = vectorObject;
		}

		public VectorObject getVectorObject() {
			return vectorObject;
		}

		public void scaleToScreen(double scaleX, double scaleY) {
			transform(vectorObject.getElement(), new MatrixImpl(scaleX, 0, 0, scaleY, 0, 0));
		}

	}

}