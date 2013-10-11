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

package org.geomajas.gwt.client.action.toolbar;

import com.smartgwt.client.widgets.Canvas;
import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.ToolbarCanvas;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.ScaleSelect;

/**
 * Tool which displays a scale select widget.
 *
 * @author Joachim Van der Auwera
 */
public class ScaleSelectAction extends ToolbarBaseAction implements ToolbarCanvas, ConfigurableAction {

	private ScaleSelect scaleSelect;

	public ScaleSelectAction(MapWidget mapWidget) {
		super("", ""); // dummy icon and tooltip
		scaleSelect = new ScaleSelect(mapWidget);
	}

	@Override
	public Canvas getCanvas() {
		return scaleSelect;
	}

	public void configure(String key, String value) {
		try {
		if ("precision".equals(key)) {
			scaleSelect.setPrecision(Integer.parseInt(value));
		} else if ("significantDigits".equals(key)) {
			scaleSelect.setSignificantDigits(Integer.parseInt(value));
		} else {
			Log.logError("Parameter " + key + " not recognized for ScaleSelectAction");
		}
		} catch (NumberFormatException nfe) {
			Log.logError("Could not parse value " + value + "for key " + key + ", should be integer.");
		}
	}
}
