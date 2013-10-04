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

package org.geomajas.gwt2.client.widget.control.watermark;

import org.geomajas.annotation.Api;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Geomajas logo that's automatically displayed in the bottom right corner of each map. This widget is meant to be added
 * to the map's widget pane (see {@link org.geomajas.gwt2.client.map.MapPresenter#getWidgetPane()}).
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class Watermark extends SimplePanel {

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/** Create a new instance using the default {@link WatermarkResource}. */
	public Watermark() {
		this((WatermarkResource) GWT.create(WatermarkResource.class));
	}

	/**
	 * Create a new instance for the given map.
	 * 
	 * @param resource
	 *            The {@link WatermarkResource} to use.
	 */
	public Watermark(WatermarkResource resource) {
		super();
		resource.css().ensureInjected();
		setStyleName(resource.css().watermark());
	}
}