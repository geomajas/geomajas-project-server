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
package org.geomajas.test.client.exporter;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.google.gwt.user.client.DOM;

/**
 * Exportable extension of MapWidget. Usage is:
 * 	<code><pre>
 *  var map = new org.geomajas.MapWidget("map", "app");
 *	map.setHtmlElementId("map");
 *	map.setWidth("400px");
 *	map.setHeight("300px");
 *	map.draw();
 *	</pre></code>
 *
 * 
 * @author Jan De Moerloose
 *
 */
@Export
@ExportPackage("org.geomajas")
public class MapWidget extends org.geomajas.gwt.client.widget.MapWidget implements Exportable {

	/**
	 * Constructs a map.
	 * @param id map id
	 * @param applicationId application id
	 */
	public MapWidget(String id, String applicationId) {
		super(id, applicationId);
	}

	/**
	 * Couples this map to an existing HTML element (div or span).
	 * @param id id of the element
	 */
	public void setHtmlElementId(String id) {
		super.setHtmlElement(DOM.getElementById(id));
	}

	/**
	 * Sets the width of the map.
	 */
	public void setWidth(String width) {
		super.setWidth(width);
	}

	/**
	 * Sets the height of the map.
	 */
	public void setHeight(String height) {
		super.setHeight(height);
	}

	/**
	 * Draws the map.
	 */
	public void draw() {
		super.draw();
	}

}
