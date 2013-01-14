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
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Go back to the previous zoom level.
 * 
 * @author Joachim Van der Auwera
 */
public class ZoomPreviousAction extends ToolbarAction {

	private ZoomQueue zoomQueue;

	public ZoomPreviousAction(MapWidget mapWidget) {
		super(WidgetLayout.iconZoomLast, I18nProvider.getToolbar().zoomPreviousTitle(), I18nProvider
				.getToolbar().zoomPreviousTooltip());
		zoomQueue = ZoomQueue.getZoomQueue(mapWidget);
		zoomQueue.setZoomPreviousAction(this);
	}

	public void onClick(ClickEvent clickEvent) {
		zoomQueue.zoomPrevious();
	}
}
