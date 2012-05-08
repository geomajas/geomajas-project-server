/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.widget.featureinfo.client.action.toolbar;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;
import org.geomajas.widget.featureinfo.client.controller.ShowCoordinatesController;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Shows the map/screen coordinates at mousepointer. 
 * 
 * @author Kristof Heirwegh
 *
 */
public class ShowCoordinatesModalAction extends ToolbarModalAction implements ConfigurableAction {

	private static final FeatureInfoMessages MESSAGES = GWT.create(FeatureInfoMessages.class);

	private boolean showViewCoordinates; // = false;
	private boolean showWorldCoordinates = true;

	private final MapWidget mapWidget;

	/**
	 * Constructor.
	 *
	 * @param mapWidget map widget
	 */
	public ShowCoordinatesModalAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/geomajas/osgeo/mouse_info_tool.png", null);
		this.mapWidget = mapWidget;
		this.setTitle(MESSAGES.showCoordinatesActionTitle());
		this.setTooltip(MESSAGES.showCoordinatesActionTooltip());
	}

	/** {@inheritDoc} */
	public void onSelect(ClickEvent event) {
		mapWidget.setController(new ShowCoordinatesController(mapWidget, showWorldCoordinates, showViewCoordinates));
	}

	/** {@inheritDoc} */
	public void onDeselect(ClickEvent event) {
		mapWidget.setController(null);
	}

	/** {@inheritDoc} */
	public void configure(String key, String value) {
		if ("showWorldCoordinates".equals(key)) {
			showWorldCoordinates = Boolean.parseBoolean(value);
		} else if ("showViewCoordinates".equals(key)) {
			showViewCoordinates = Boolean.parseBoolean(value);
		}
	}
}
