/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.gfx.painter;

import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.DOM;

/**
 * ???
 * 
 * @author check subversion
 */
public class VectorLayerPainter implements Painter {

	private FontStyle labelStyle;

	public VectorLayerPainter() {
		this(new FontStyle("#FF0000", 10, "Courier New, Arial", "normal", "normal"));
	}

	public VectorLayerPainter(FontStyle labelStyle) {
		this.labelStyle = labelStyle;
	}

	public String getPaintableClassName() {
		return VectorLayer.class.getName();
	}

	public void paint(Paintable paintable, GraphicsContext graphics) {
		VectorLayer layer = (VectorLayer) paintable;

		// Create the needed groups in the correct order:
		graphics.drawGroup(getUniqueLayerId(layer), DOM.NS_VML); // layer.getDefaultStyle???
		graphics.drawGroup(getUniqueLayerId(layer) + ".features", DOM.NS_VML);
		graphics.drawGroup(getUniqueLayerId(layer) + ".selection", DOM.NS_VML);
		graphics.drawGroup(getUniqueLayerId(layer) + ".labels", DOM.NS_VML);

		// Draw symbol types, as these can change any time:
		// for (var i=0; i<layer.getStyles().count; i++) {
		// var style = layer.getStyles().item(i).getStyle();
		// graphics.drawShapeType({
		// style: style,
		// id: layer.getId()+"."+layer.getStyles().item(i).getId()+".style"
		// });
		// }

		// Check layer visibility:
		if (layer.isShowing()) {
			graphics.unhide(getUniqueLayerId(layer));
		} else {
			graphics.hide(getUniqueLayerId(layer));
		}

		// Check label visibility:
		if (layer.isLabeled()) {
			// TODO get label style from the layer???
			graphics.drawGroup(getUniqueLayerId(layer) + ".labels", null, null, labelStyle);
			graphics.unhide(getUniqueLayerId(layer) + ".labels");
		} else {
			graphics.hide(getUniqueLayerId(layer) + ".labels");
		}
	}

	/**
	 * Delete a <code>Paintable</code> object from the given <code>GraphicsContext</code>. It the object does not exist,
	 * nothing will be done.
	 * 
	 * @param paintable
	 *            The MapModel
	 * @param graphics
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, GraphicsContext graphics) {
		VectorLayer layer = (VectorLayer) paintable;
		graphics.deleteShape(getUniqueLayerId(layer), false);
	}

	// Getters and setters:

	/**
	 * Get the default font style used to draw labels.
	 */
	public FontStyle getLabelStyle() {
		return labelStyle;
	}

	/**
	 * Set a new default font style used for drawing labels.
	 * 
	 * @param labelStyle
	 *            The new style
	 */
	public void setLabelStyle(FontStyle labelStyle) {
		this.labelStyle = labelStyle;
	}

	// Private methods:

	private String getUniqueLayerId(VectorLayer layer) {
		return layer.getMapModel().getId() + "." + layer.getId();
	}
}
