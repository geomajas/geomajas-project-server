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
package org.geomajas.plugin.graphicsediting.client.action;

import org.geomajas.geometry.Geometry;
import org.geomajas.graphics.client.action.Action;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.util.Interruptible;
import org.geomajas.plugin.graphicsediting.client.object.GeometryEditable;
import org.geomajas.plugin.graphicsediting.client.operation.GeometryEditOperation;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorFactory;

/**
 * Action to delete a {@link GraphicsObject}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class EditAction implements Action, GeometryEditChangeStateHandler,
		GeometryEditStopHandler, Interruptible {

	private GraphicsService service;
	
	private GraphicsObject object;

	private GeometryEditor editor;

	private GeometryEditService editService;

	private MapPresenter mapPresenter;

	private GeometryEditorFactory editorFactory;
	
	private String iconUrl;
	
	private boolean backToOriginal;

	public EditAction(MapPresenter mapPresenter,
			GeometryEditorFactory editorFactory) {
		this.mapPresenter = mapPresenter;
		this.editorFactory = editorFactory;
	}

	public boolean supports(GraphicsObject object) {
		return object.hasRole(GeometryEditable.TYPE);
	}

	public void execute(GraphicsObject object) {
		this.object = object;
		if (editService == null) {
			editService = createEditService();
			editService.addGeometryEditChangeStateHandler(this);
			editService.addGeometryEditStopHandler(this);
		}
		service.getMetaController().setActive(false);
		editService.start(this.object.getRole(GeometryEditable.TYPE).getGeometry());
	}

	@Override
	public void onGeometryEditStop(GeometryEditStopEvent event) {
		if (backToOriginal) {
			backToOriginal = false;
			return;
		}
		Geometry start = object.getRole(GeometryEditable.TYPE).getGeometry();
		service.execute(new GeometryEditOperation(object, start, event.getGeometry()));
	}

	@Override
	public void onChangeEditingState(GeometryEditChangeStateEvent event) {
		// TODO Auto-generated method stub

	}

	public GeometryEditService createEditService() {
		editor = editorFactory.create(mapPresenter);
		editor.getBaseController().setClickToStop(true);
		return editor.getEditService();
	}

	@Override
	public void setService(GraphicsService service) {
		this.service = service;
	}

	@Override
	public String getLabel() {
		return "Edit Shape";
	}

	@Override
	public void setIconUrl(String url) {
		this.iconUrl = url;

	}

	@Override
	public String getIconUrl() {
		return iconUrl;
	}
	
	public GeometryEditService getGeometryEditorService() {
		return editService;
	}
	
	public void stop() {
		editService.stop();
	}

	@Override
	public void cancel() {
		backToOriginal = true;
		editService.stop();
	}

	@Override
	public void save() {
		backToOriginal = false;
		editService.stop();
	}

	@Override
	public void pause() {
		// not used yet
	}

	@Override
	public void resume() {
		// not used yet
	}

	@Override
	public boolean isInterrupted() {
		// not used yet
		return false;
	}

	@Override
	public boolean isInProgress() {
		if (editService == null) {
			editService = createEditService();
			editService.addGeometryEditChangeStateHandler(this);
			editService.addGeometryEditStopHandler(this);
		}
		return editService.isStarted();
	}

	@Override
	public void start() {
		// do nothing		
	}
}
