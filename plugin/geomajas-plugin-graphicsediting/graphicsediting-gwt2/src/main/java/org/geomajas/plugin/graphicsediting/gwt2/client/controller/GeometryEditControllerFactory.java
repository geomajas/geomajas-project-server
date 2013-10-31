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
package org.geomajas.plugin.graphicsediting.gwt2.client.controller;

import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.service.GraphicsController;
import org.geomajas.graphics.client.service.GraphicsControllerFactory;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.plugin.graphicsediting.gwt2.client.object.GeometryEditable;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorFactory;

/**
 * Factory for the {@link GeometryEditController}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeometryEditControllerFactory implements GraphicsControllerFactory {

	private GeometryEditorFactory editorFactory;

	private MapPresenter mapPresenter;

	public GeometryEditControllerFactory(GeometryEditorFactory editorFactory, MapPresenter mapPresenter) {
		this.editorFactory = editorFactory;
		this.mapPresenter = mapPresenter;
	}

	@Override
	public boolean supports(GraphicsObject object) {
		return object.hasRole(GeometryEditable.TYPE);
	}

	@Override
	public GraphicsController createController(GraphicsService graphicsService, GraphicsObject object) {
		return new GeometryEditController(object, graphicsService, editorFactory, mapPresenter);
	}

}
