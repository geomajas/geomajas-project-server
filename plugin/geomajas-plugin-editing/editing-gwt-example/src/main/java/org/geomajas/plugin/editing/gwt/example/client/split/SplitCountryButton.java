/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.editing.gwt.example.client.split;

import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.split.GeometrySplitService;

import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Toolbar button for adding inner rings to a polygon.
 * 
 * @author Pieter De Graef
 */
public class SplitCountryButton extends ToolStripButton implements FeatureSelectionHandler {

	private final MapWidget mapWidget;

	private final GeometrySplitService service;

	private GraphicsController previousController;

	public SplitCountryButton(final GeometrySplitService service, final MapWidget mapWidget) {
		this.service = service;
		this.mapWidget = mapWidget;

		setHoverWidth(300);
		setTooltip("Select a country on the map to start splitting it.");
		setIcon("[ISOMORPHIC]/geomajas/osgeo/ring-add.png");
		setIconSize(24);
		setHeight(32);
		setActionType(SelectionType.CHECKBOX);

		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (isSelected()) {
					previousController = mapWidget.getController();
					mapWidget.setController(new SelectForSplitController(mapWidget));
				} else {
					if (previousController != null) {
						mapWidget.setController(previousController);
						previousController = null;
					}
					service.stop(null);
				}
			}
		});
		mapWidget.getMapModel().addFeatureSelectionHandler(this);
	}

	// ------------------------------------------------------------------------
	// FeatureSelectionHandler:
	// ------------------------------------------------------------------------

	public void onFeatureSelected(FeatureSelectedEvent event) {
		if (isSelected()) {
			Feature feature = event.getFeature();
			mapWidget.setController(previousController);
			service.start(GeometryConverter.toDto(feature.getGeometry()));
		}
	}

	public void onFeatureDeselected(FeatureDeselectedEvent event) {
	}
}