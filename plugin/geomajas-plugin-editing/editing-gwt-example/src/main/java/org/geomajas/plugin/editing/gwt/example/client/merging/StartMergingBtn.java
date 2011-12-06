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

package org.geomajas.plugin.editing.gwt.example.client.merging;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.merging.GeometryMergingException;
import org.geomajas.plugin.editing.client.merging.GeometryMergingService;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStartEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStartHandler;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStopEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStopHandler;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Toolbar button for adding inner rings to a polygon.
 * 
 * @author Pieter De Graef
 */
public class StartMergingBtn extends ToolStripButton implements GeometryMergingStartHandler, 
		GeometryMergingStopHandler {

	private final Map<Feature, Geometry> selectionMap = new HashMap<Feature, Geometry>();

	private final MapWidget mapWidget;

	public StartMergingBtn(final MapWidget mapWidget, final GeometryMergingService service) {
		this.mapWidget = mapWidget;
		setTitle("Start merging process");
		setHoverWidth(300);
		setTooltip("Start merging geometries. First select a few countries on the map, then press the merge button.");

		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				try {
					mapWidget.getMapModel().clearSelectedFeatures();
					mapWidget.setController(new MergeSelectionController(mapWidget));
					service.start();
				} catch (GeometryMergingException e) {
					Window.alert(e.getMessage());
				}
			}
		});

		service.addGeometryMergingStartHandler(this);
		service.addGeometryMergingStopHandler(this);

		mapWidget.getMapModel().addFeatureSelectionHandler(new FeatureSelectionHandler() {

			public void onFeatureSelected(FeatureSelectedEvent event) {
				if (service.isBusy()) {
					try {
						Geometry geometry = GeometryConverter.toDto(event.getFeature().getGeometry());
						selectionMap.put(event.getFeature(), geometry);
						service.addGeometry(geometry);
					} catch (IllegalStateException e) {
						Window.alert(e.getMessage());
					} catch (GeometryMergingException e) {
						Window.alert(e.getMessage());
					}
				}
			}

			public void onFeatureDeselected(FeatureDeselectedEvent event) {
				if (service.isBusy()) {
					try {
						service.removeGeometry(selectionMap.get(event.getFeature()));
						selectionMap.remove(event.getFeature());
					} catch (IllegalStateException e) {
						Window.alert(e.getMessage());
					} catch (GeometryMergingException e) {
						Window.alert(e.getMessage());
					}
				}
			}
		});
	}

	// ------------------------------------------------------------------------
	// GeometryEditWorkflowHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryMergingStop(GeometryMergingStopEvent event) {
		setDisabled(false);
		mapWidget.setController(null);
	}

	public void onGeometryMergingStart(GeometryMergingStartEvent event) {
		setDisabled(true);
	}
}