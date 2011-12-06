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

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.GeometryFunction;
import org.geomajas.plugin.editing.client.merging.GeometryMergingException;
import org.geomajas.plugin.editing.client.merging.GeometryMergingService;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingAddedEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingAddedHandler;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingRemovedEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingRemovedHandler;
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
public class ExecuteMergeBtn extends ToolStripButton implements GeometryMergingStartHandler,
		GeometryMergingStopHandler, GeometryMergingAddedHandler, GeometryMergingRemovedHandler {

	private final MapWidget mapWidget;

	private GfxGeometry gfx;

	private int count;

	public ExecuteMergeBtn(final MapWidget mapWidget, final GeometryMergingService service) {
		this.mapWidget = mapWidget;
		setDisabled(true);
		setTitle("Merge selection");
		setTooltip("Merge the selected countries!");
		setIcon("[ISOMORPHIC]/geomajas/osgeo/merge.png");

		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				try {
					service.stop(new GeometryFunction() {

						public void execute(Geometry geometry) {
							mapWidget.getMapModel().clearSelectedFeatures();
							gfx = new GfxGeometry("merged", GeometryConverter.toGwt(geometry), new ShapeStyle(
									"#CC0000", .9f, "#660000", 1.0f, 3));
							mapWidget.registerWorldPaintable(gfx);
						}
					});
				} catch (GeometryMergingException e) {
					Window.alert(e.getMessage());
				}
			}
		});

		service.addGeometryMergingStartHandler(this);
		service.addGeometryMergingStopHandler(this);

		service.addGeometryMergingAddedHandler(this);
		service.addGeometryMergingRemovedHandler(this);

		mapWidget.getMapModel().addFeatureSelectionHandler(new FeatureSelectionHandler() {

			public void onFeatureSelected(FeatureSelectedEvent event) {
			}

			public void onFeatureDeselected(FeatureDeselectedEvent event) {
			}
		});
	}

	// ------------------------------------------------------------------------
	// Handler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryMergingStop(GeometryMergingStopEvent event) {
		setDisabled(true);
	}

	public void onGeometryMergingStart(GeometryMergingStartEvent event) {
		count = 0;
		mapWidget.unregisterWorldPaintable(gfx);
		gfx = null;
	}

	public void onGeometryMergingRemoved(GeometryMergingRemovedEvent event) {
		count--;
		if (count < 2) {
			setDisabled(true);
		}
	}

	public void onGeometryMergingAdded(GeometryMergingAddedEvent event) {
		count++;
		if (count >= 2) {
			setDisabled(false);
		}
	}
}