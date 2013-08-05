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
package org.geomajas.smartgwt.client.action.toolbar;

import com.smartgwt.client.widgets.events.ClickEvent;
import org.geomajas.smartgwt.client.action.ToolbarModalAction;
import org.geomajas.smartgwt.client.controller.FeatureInfoController;
import org.geomajas.smartgwt.client.i18n.I18nProvider;
import org.geomajas.smartgwt.client.util.WidgetLayout;
import org.geomajas.smartgwt.client.widget.MapWidget;

/**
 * Tool which shows information about the selected feature.
 * 
 * @author Frank Wynants
 */
public class FeatureInfoModalAction extends ToolbarModalAction {

	private MapWidget mapWidget;

	private FeatureInfoController controller;

	/** Number of pixels that describes the tolerance allowed when trying to select features. */
	private int pixelTolerance = 5;

	// Constructor:

	public FeatureInfoModalAction(MapWidget mapWidget) {
		super(WidgetLayout.iconInfo, I18nProvider.getToolbar().featureInfoTitle(), I18nProvider
				.getToolbar().featureInfoTooltip());
		this.mapWidget = mapWidget;
		controller = new FeatureInfoController(mapWidget, pixelTolerance);
	}

	// ToolbarModalAction implementation:

	@Override
	public void onSelect(ClickEvent event) {
		mapWidget.setController(controller);
	}

	@Override
	public void onDeselect(ClickEvent event) {
		mapWidget.setController(null);
	}

	// Getters and setters:

	public int getPixelTolerance() {
		return pixelTolerance;
	}

	public void setPixelTolerance(int pixelTolerance) {
		this.pixelTolerance = pixelTolerance;
	}
}
