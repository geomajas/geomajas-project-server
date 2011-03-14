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

package org.geomajas.puregwt.client.map;

import org.geomajas.puregwt.client.spatial.Matrix;
import org.vaadin.gwtgraphics.client.Group;

import com.google.gwt.user.client.DOM;

/**
 * Default implementation of the WorldContainer interface. It represents a vector group element in world space.
 * 
 * @author Pieter De Graef
 */
public class WorldGroup extends Group implements WorldContainer {

	private String id;

	private boolean resizeChildren = true;

	public WorldGroup(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public boolean isResizeChildren() {
		return resizeChildren;
	}

	public void setResizeChildren(boolean resizeChildren) {
		this.resizeChildren = resizeChildren;
	}

	public void transform(ViewPort viewPort) {
		// TODO SVG only atm...

		if (resizeChildren) {
			Matrix matrix = viewPort.getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
			DOM.setElementAttribute(getElement(), "transform", parse(matrix));
		} else {
			for (int i = 0; i < getVectorObjectCount(); i++) {
				// VectorObject vo = getVectorObject(i);
				// TODO implement this...
			}
		}
	}

	/**
	 * Parse a matrix object into a string, suitable for the SVG 'transform' attribute.
	 * 
	 * @param matrix
	 *            The matrix to parse.
	 * @return The transform string.
	 */
	private String parse(Matrix matrix) {
		String transform = "";
		if (matrix != null) {
			double dx = matrix.getDx();
			double dy = matrix.getDy();
			if (matrix.getXx() != 0 && matrix.getYy() != 0 && matrix.getXx() != 1 && matrix.getYy() != 1) {
				transform += "scale(" + matrix.getXx() + ", " + matrix.getYy() + ")"; // scale first
				// no space between 'scale' and '(' !!!
				dx /= matrix.getXx();
				dy /= matrix.getYy();
			}
			transform += " translate(" + (float) dx + ", " + (float) dy + ")";
			// no space between 'translate' and '(' !!!
		}
		return transform;
	}
}