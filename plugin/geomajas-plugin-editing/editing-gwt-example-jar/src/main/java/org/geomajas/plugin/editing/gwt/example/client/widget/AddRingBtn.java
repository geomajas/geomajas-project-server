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

package org.geomajas.plugin.editing.gwt.example.client.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.gwt.example.client.event.GeometryEditResumeEvent;
import org.geomajas.plugin.editing.gwt.example.client.event.GeometryEditSuspendEvent;
import org.geomajas.plugin.editing.gwt.example.client.event.GeometryEditSuspendResumeHandler;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Toolbar button for adding inner rings to a polygon.
 * 
 * @author Pieter De Graef
 */
public class AddRingBtn extends ToolStripButton implements GeometryEditStartHandler, GeometryEditStopHandler,
		GeometryEditSuspendResumeHandler {

	private GeometryEditService service;

	public AddRingBtn(final GeometryEditService service) {
		this.service = service;
		setDisabled(true);
		setHoverWidth(300);
		setTooltip("Add an interior ring to the geometry. This interior ring will create a hole into the surface.");
		setIcon("[ISOMORPHIC]/geomajas/osgeo/ring-add.png");
		setIconSize(24);
		setHeight(32);

		// String imgHTML = Canvas.imgHTML("[ISOMORPHIC]/geomajas/osgeo/ring-add.png");
		// imgHTML = imgHTML.substring(0, imgHTML.length() - 2) + " width='16' height='16' />";
		// setTitle("<span>" + imgHTML + "</span>");

		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (Geometry.POLYGON.equals(service.getGeometry().getGeometryType())) {
					try {
						GeometryIndex ringIndex = service.addEmptyChild();

						// Free drawing means inserting mode. First create a new empty child, than point the insert
						// index to the child's first vertex:
						service.setInsertIndex(service.getIndexService().addChildren(ringIndex,
								GeometryIndexType.TYPE_VERTEX, 0));
						service.setEditingState(GeometryEditState.INSERTING);
					} catch (GeometryOperationFailedException e) {
						Window.alert("Error during editing: " + e.getMessage());
					}
				} else if (Geometry.MULTI_POLYGON.equals(service.getGeometry().getGeometryType())) {
					try {
						// First insert an empty ring into polygon1:
						GeometryIndex index = service.getIndexService().create(GeometryIndexType.TYPE_GEOMETRY, 0, 0);
						service.insert(Collections.singletonList(index), new ArrayList<List<Coordinate>>());

						// Free drawing means inserting mode. First create a new empty child, than point the insert
						// index to the child's first vertex:
						service.setInsertIndex(service.getIndexService().addChildren(index,
								GeometryIndexType.TYPE_VERTEX, 0));
						service.setEditingState(GeometryEditState.INSERTING);
					} catch (GeometryOperationFailedException e) {
						Window.alert("Error during editing: " + e.getMessage());
					}
				}
			}
		});

		service.addGeometryEditStartHandler(this);
		service.addGeometryEditStopHandler(this);
	}

	// ------------------------------------------------------------------------
	// GeometryEditWorkflowHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryEditStart(GeometryEditStartEvent event) {
		String geometryType = service.getGeometry().getGeometryType();
		if (Geometry.LINEAR_RING.equals(geometryType) || Geometry.POLYGON.equals(geometryType)
				|| Geometry.MULTI_POLYGON.equals(geometryType)) {
			setDisabled(false);
		}
	}

	public void onGeometryEditStop(GeometryEditStopEvent event) {
		setDisabled(true);
	}

	// ------------------------------------------------------------------------
	// GeometryEditSuspensionHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryEditSuspend(GeometryEditSuspendEvent event) {
		setDisabled(true);
	}

	public void onGeometryEditResume(GeometryEditResumeEvent event) {
		onGeometryEditStart(null);
	}
}