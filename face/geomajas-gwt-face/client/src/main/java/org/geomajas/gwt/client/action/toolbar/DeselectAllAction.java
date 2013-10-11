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

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Tool which deselects all selected features.
 * 
 * @author Jan De Moerloose
 */
public class DeselectAllAction extends ToolbarAction implements ConfigurableAction {

	private MapWidget mapWidget;

	public DeselectAllAction(MapWidget mapWidget) {
		super(WidgetLayout.iconSelectedDelete, I18nProvider.getToolbar().deselectAllTitle(), I18nProvider
				.getToolbar().deselectAllTooltip());
		this.mapWidget = mapWidget;
	}

	public void onClick(ClickEvent event) {
		mapWidget.getMapModel().clearSelectedFeatures();
	}

	public void configure(String key, String value) {
	}

}
