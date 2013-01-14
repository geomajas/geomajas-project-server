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

package org.geomajas.plugin.jsapi.client.map.controller;

import org.geomajas.annotation.Api;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * JavaScript exportable handler for catching mouse up or touch end events.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export
@ExportClosure
@Api(allMethods = true)
public interface UpHandler extends Exportable {

	/**
	 * Executed when a mouse up or touch end event occurred.
	 * 
	 * @param event
	 *            The mouse up or touch end event.
	 */
	void onUp(HumanInputEvent<?> event);
}