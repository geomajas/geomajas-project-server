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

import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.controller.ZoomToRectangleController;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Allow zooming to the selected rectangle.
 * 
 * @author Emiel Ackermann
 */
public class ZoomToRectangleAction extends ToolbarAction {

	private MapWidget map;

	public ZoomToRectangleAction(MapWidget mapWidget) {
		super(WidgetLayout.iconZoomSelection, I18nProvider.getToolbar().zoomToRectangleTitle(),
				I18nProvider.getToolbar().zoomToRectangleTooltip());
		this.map = mapWidget;
	}

	public void onClick(ClickEvent event) {
		map.setController(new ZoomToRectangleOnceController(map));
	}

	/**
	 * Controller the zooms to a rectangle drawn by the user, and then deactivates itself again.
	 * 
	 * @author Pieter De Graef
	 */
	private class ZoomToRectangleOnceController extends ZoomToRectangleController {

		public ZoomToRectangleOnceController(MapWidget mapWidget) {
			super(mapWidget);
		}

		protected void selectRectangle(Bbox worldBounds) {
			super.selectRectangle(worldBounds);
			mapWidget.setController(null);
		}
	}

}
