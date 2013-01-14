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

import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.controller.MeasureDistanceController;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Measure distance action.
 * 
 * @author Pieter De Graef
 */
public class MeasureModalAction extends ToolbarModalAction {

	private MapWidget mapWidget;

	public MeasureModalAction(MapWidget mapWidget) {
		super(WidgetLayout.iconMeasureLength, I18nProvider.getToolbar().measureSelectTitle(),
				I18nProvider.getToolbar().measureSelectTooltip());
		this.mapWidget = mapWidget;
	}

	public void onSelect(ClickEvent event) {
		mapWidget.setController(new MeasureDistanceController(mapWidget));
	}

	public void onDeselect(ClickEvent event) {
		mapWidget.setController(null);
	}
}
