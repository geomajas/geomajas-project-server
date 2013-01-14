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

package org.geomajas.plugin.geocoder.client;

import com.smartgwt.client.widgets.Canvas;
import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.ToolbarCanvas;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Tool which displays a {@link GeocoderWidget}.
 *
 * @author Joachim Van der Auwera
 */
public class GeocoderAction extends ToolbarBaseAction implements ToolbarCanvas, ConfigurableAction {

	private String name = "GeocoderAction";
	private String title = "Geocoder";

	private GeocoderWidget geocoderWidget;
	private MapWidget mapWidget;

	public GeocoderAction(MapWidget mapWidget) {
		super("", ""); // dummy icon and tooltip
		this.mapWidget = mapWidget;
	}

	/** {@inheritDoc} */
	public Canvas getCanvas() {
		if (null == geocoderWidget) {
			geocoderWidget = new GeocoderWidget(mapWidget, name, title);
		}
		return geocoderWidget;
	}

	/** {@inheritDoc} */
	public void configure(String key, String value) {
		if ("name".equals(key)) {
			name = value;
		} else if ("title".equals(key)) {
			title = value;
		} else {
			Log.logError("Parameter " + key + " not recognized for GeocoderAction");
		}
	}
}
