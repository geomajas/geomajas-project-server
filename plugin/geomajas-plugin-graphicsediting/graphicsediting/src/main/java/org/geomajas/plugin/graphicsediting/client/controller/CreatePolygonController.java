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
package org.geomajas.plugin.graphicsediting.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Geometry;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.operation.AddOperation;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.plugin.graphicsediting.client.object.GGeometryPath;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorFactory;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Controller for Polygon objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CreatePolygonController extends AbstractGraphicsController implements GeometryEditStopHandler,
		DoubleClickHandler {

	private boolean active;

	private List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

	private GraphicsObject path;

	private VectorObjectContainer container;

	private String fillColor = "#CCFF66";

	private double fillOpacity;

	private String text;

	private MapPresenter mapPresenter;

	private GeometryEditorFactory editorFactory;

	private GeometryEditService editService;

	private GeometryEditor editor;

	public CreatePolygonController(GraphicsService graphicsService, GeometryEditorFactory editorFactory,
			MapPresenter mapPresenter) {
		super(graphicsService);
		this.mapPresenter = mapPresenter;
		this.editorFactory = editorFactory;
		container = createContainer();
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			container = createContainer();
			registrations.add(getObjectContainer().addDoubleClickHandler(this));
			startEditing();
		} else {
			for (HandlerRegistration r : registrations) {
				r.removeHandler();
			}
			registrations.clear();
			path = null;
			if (container != null) {
				removeContainer(container);
			}
			if (editService != null) {
				editService.stop();
			}
			container = null;
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
	}

	public void startEditing() {
		if (path == null) {
			Geometry polygon = new Geometry(Geometry.POLYGON, 0, -1);
			if (editService == null) {
				editService = createEditService();
				editService.addGeometryEditStopHandler(this);
			}
			editService.start(polygon);
			try {
				GeometryIndex index = editService.addEmptyChild();
				index = editService.getIndexService().addChildren(index, GeometryIndexType.TYPE_VERTEX, 0);
				editService.setEditingState(GeometryEditState.INSERTING);
				editService.setInsertIndex(index);
			} catch (GeometryOperationFailedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onGeometryEditStop(GeometryEditStopEvent event) {
		if (isActive()) {
			try {
				Geometry geom = event.getGeometry();
				path = createObject(event.getGeometry());
				execute(new AddOperation( path));
			} catch (Exception e) {
				// do nothing
			}
			path = null;
		}
	}
	
	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		editService.stop();
//		event.stopPropagation();
	}

	protected GraphicsObject createObject(Geometry geometry) {
		GGeometryPath path = new GGeometryPath(geometry, text);
		path.setFillColor(fillColor);
		path.setFillOpacity(fillOpacity);
		return path;
	}

	public GeometryEditService createEditService() {
		editor = editorFactory.create(mapPresenter);
		editor.getBaseController().setClickToStop(true);
		return editor.getEditService();
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	public void setFillOpacity(double fillOpacity) {
		this.fillOpacity = fillOpacity;
	}

	public String getFillColor() {
		return fillColor;
	}

	public double getFillOpacity() {
		return fillOpacity;
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}
}
