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
