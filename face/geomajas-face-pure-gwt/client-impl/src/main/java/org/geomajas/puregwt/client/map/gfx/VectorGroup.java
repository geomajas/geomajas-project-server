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

import org.geomajas.puregwt.client.Geomajas;
import org.geomajas.puregwt.client.spatial.Matrix;
import org.vaadin.gwtgraphics.client.Group;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Implementation of the {@link VectorContainer} interface.
 * 
 * @author Pieter De Graef
 */
public class VectorGroup extends Group implements VectorContainer {
	
	public void transform(Matrix matrix) {
		transform(getElement(), matrix);
	}

	/**
	 * Transform this vector container and all it's children according to the given matrix.
	 */
	protected void transform(Element element, Matrix matrix) {
		if (Geomajas.hasSvgSupport()) {
			DOM.setElementAttribute(element, "transform", parseSvg(matrix));
		} else {
			// TODO Internet explorer: support scaling too...
			DOM.setStyleAttribute(element, "position", "absolute");
			DOM.setStyleAttribute(element, "left", (int) matrix.getDx() + "px");
			DOM.setStyleAttribute(element, "top", (int) matrix.getDy() + "px");
		}
	}

	/**
	 * Parse a matrix object into a string, suitable for the SVG 'transform' attribute.
	 * 
	 * @param matrix
	 *            The matrix to parse.
	 * @return The transform string.
	 */
	protected String parseSvg(Matrix matrix) {
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