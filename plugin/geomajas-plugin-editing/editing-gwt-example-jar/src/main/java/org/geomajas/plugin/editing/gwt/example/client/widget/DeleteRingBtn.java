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

package org.geomajas.plugin.editing.gwt.example.client.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.handler.MapUpHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.gfx.GeometryRenderer;
import org.geomajas.plugin.editing.client.handler.AbstractGeometryIndexMapHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.gwt.client.handler.EditingHandlerRegistry;
import org.geomajas.plugin.editing.gwt.client.handler.EditingHandlerRegistry.GeometryHandlerFactory;
import org.geomajas.plugin.editing.gwt.example.client.event.GeometryEditResumeEvent;
import org.geomajas.plugin.editing.gwt.example.client.event.GeometryEditSuspendEvent;
import org.geomajas.plugin.editing.gwt.example.client.event.GeometryEditSuspendReason;

import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Toolbar button for deleting inner rings from a polygon.
 * 
 * @author Pieter De Graef
 */
public class DeleteRingBtn extends ToolStripButton implements GeometryEditStartHandler, GeometryEditStopHandler {

	private MenuBar menuBar;

	private GeometryEditService service;

	private GeometryHandlerFactory factory;

	public DeleteRingBtn(final MenuBar menuBar, final GeometryEditService service, final GeometryRenderer renderer) {
		this.menuBar = menuBar;
		this.service = service;
		factory = new GeometryHandlerFactory() {

			public AbstractGeometryIndexMapHandler create() {
				return new DeleteRingHandler();
			}
		};
		setDisabled(true);
		setHoverWidth(300);
		setActionType(SelectionType.CHECKBOX);
		setTooltip("Remove an interior ring from the geometry by clicking on it.");
		setIcon("[ISOMORPHIC]/geomajas/osgeo/ring-delete.png");
		setIconSize(24);
		setHeight(32);

		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (isSelected()) {
					disableVerticesAndEdges(null, service.getGeometry());

					// We change the registry by adding a handler that deletes inner rings:
					EditingHandlerRegistry.addGeometryHandlerFactory(factory);

					// Trigger a redraw to apply the change in the registry:
					renderer.redraw();

					menuBar.getEventBus()
							.fireEvent(new GeometryEditSuspendEvent(GeometryEditSuspendReason.REMOVE_RING));
				} else {
					resumeNormalBehavior();
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

	private void disableVerticesAndEdges(GeometryIndex geomIndex, Geometry geometry) {
		if (geometry.getGeometries() != null) {
			for (int i = 0; i < geometry.getGeometries().length; i++) {
				GeometryIndex childIndex = service.getIndexService().addChildren(geomIndex,
						GeometryIndexType.TYPE_GEOMETRY, i);
				disableVerticesAndEdges(childIndex, geometry.getGeometries()[i]);
			}
			// Also disable background for polygons:
			if (Geometry.POLYGON.equals(geometry.getGeometryType())) {
				GeometryIndex shellIndex = service.getIndexService().addChildren(geomIndex,
						GeometryIndexType.TYPE_GEOMETRY, 0);
				service.getIndexStateService().disable(Collections.singletonList(shellIndex));
			}
		}
		if (geometry.getCoordinates() != null) {
			List<GeometryIndex> toDisable = new ArrayList<GeometryIndex>();
			for (int i = 0; i < geometry.getCoordinates().length - 1; i++) {
				toDisable.add(service.getIndexService().addChildren(geomIndex, GeometryIndexType.TYPE_VERTEX, i));
				toDisable.add(service.getIndexService().addChildren(geomIndex, GeometryIndexType.TYPE_EDGE, i));
			}
			service.getIndexStateService().disable(toDisable);
		}
	}

	private void resumeNormalBehavior() {
		EditingHandlerRegistry.removeGeometryHandlerFactory(factory);
		service.getIndexStateService().enableAll();
		menuBar.getEventBus().fireEvent(new GeometryEditResumeEvent());
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Private handler that deletes a ring when the user clicks on it.
	 * 
	 * @author Pieter De Graef
	 */
	private class DeleteRingHandler extends AbstractGeometryIndexMapHandler implements MapUpHandler, MouseOverHandler,
			MouseOutHandler {

		public void onUp(HumanInputEvent<?> event) {
			if (service.getIndexStateService().isMarkedForDeletion(index)) {
				try {
					service.remove(Collections.singletonList(index));
				} catch (GeometryOperationFailedException e) {
					Window.alert("Error occurred while deleting the inner ring: " + e.getMessage());
				}
				resumeNormalBehavior();
				setSelected(false);
			}
		}

		public void onMouseOver(MouseOverEvent event) {
			try {
				String geometryType = service.getIndexService().getGeometryType(service.getGeometry(), index);
				if (Geometry.LINEAR_RING.equals(geometryType) || Geometry.POLYGON.equals(geometryType)
						|| Geometry.MULTI_POLYGON.equals(geometryType)) {
					if (service.getIndexService().getValue(index) > 0) {
						// Only inner rings must be marked. The outer shell can remain as-is.
						service.getIndexStateService().markForDeletionBegin(Collections.singletonList(index));
					}
				}
			} catch (GeometryIndexNotFoundException e) {
				Window.alert("Error occurred while deleting the inner ring: " + e.getMessage());
			}
		}

		public void onMouseOut(MouseOutEvent event) {
			if (service.getIndexStateService().isMarkedForDeletion(index)) {
				service.getIndexStateService().markForDeletionEnd(Collections.singletonList(index));
			}
		}
	}
}