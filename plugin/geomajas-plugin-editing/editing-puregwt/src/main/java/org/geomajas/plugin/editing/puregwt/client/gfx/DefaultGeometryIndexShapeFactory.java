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

package org.geomajas.plugin.editing.puregwt.client.gfx;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.path.LineTo;
import org.vaadin.gwtgraphics.client.shape.path.MoveTo;

/**
 * Default implementation of the {@link GeometryIndexShapeFactory}. It will create a path for an edge, a path for a
 * geometry and a rectangle for a vertex.
 * 
 * @author Pieter De Graef
 */
public class DefaultGeometryIndexShapeFactory implements GeometryIndexShapeFactory {

	private static final int VERTEX_SIZE = 12;

	private static final int VERTEX_HALF_SIZE = 6;

	private final MapPresenter mapPresenter;

	private final RenderSpace targetSpace;
	
	private GfxUtil gfxUtil;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public DefaultGeometryIndexShapeFactory(MapPresenter mapPresenter, RenderSpace targetSpace, GfxUtil gfxUtil) {
		this.mapPresenter = mapPresenter;
		this.targetSpace = targetSpace;
		this.gfxUtil = gfxUtil;
	}

	// ------------------------------------------------------------------------
	// GeometryEditShapeFactory implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public Shape create(GeometryEditService editService, GeometryIndex index) throws GeometryIndexNotFoundException {
		if (index == null) {
			return createGeometry(editService, index);
		}
		switch (editService.getIndexService().getType(index)) {
			case TYPE_VERTEX:
				return createVertex(editService, index);
			case TYPE_EDGE:
				return createEdge(editService, index);
			default:
				return createGeometry(editService, index);
		}
	}

	/** {@inheritDoc} */
	public void update(Shape shape, GeometryEditService editService, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		if (index != null) {
			switch (editService.getIndexService().getType(index)) {
				case TYPE_VERTEX:
					updateVertex(shape, editService, index);
					break;
				case TYPE_EDGE:
					updateEdge(shape, editService, index);
					break;
				default:
					updateGeometry(shape, editService, index);
			}
		} else {
			updateGeometry(shape, editService, index);
		}
	}

	// ------------------------------------------------------------------------
	// Private methods for creating shapes:
	// ------------------------------------------------------------------------

	private Shape createVertex(GeometryEditService editService, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		Geometry geometry = editService.getGeometry();
		Coordinate v = editService.getIndexService().getVertex(geometry, index);
		if (!targetSpace.equals(RenderSpace.WORLD)) {
			v = mapPresenter.getViewPort().transform(v, RenderSpace.WORLD, targetSpace);
		}
		return new Rectangle(v.getX() - VERTEX_HALF_SIZE, v.getY() - VERTEX_HALF_SIZE, VERTEX_SIZE, VERTEX_SIZE);
	}

	private Shape createEdge(GeometryEditService editService, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		Geometry geometry = editService.getGeometry();
		Coordinate[] e = editService.getIndexService().getEdge(geometry, index);
		if (!targetSpace.equals(RenderSpace.WORLD)) {
			e[0] = mapPresenter.getViewPort().transform(e[0], RenderSpace.WORLD, targetSpace);
			e[1] = mapPresenter.getViewPort().transform(e[1], RenderSpace.WORLD, targetSpace);
		}
		Path edge = new Path(e[0].getX(), e[0].getY());
		edge.lineTo(e[1].getX(), e[1].getY());
		return edge;
	}

	private Shape createGeometry(GeometryEditService editService, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		Geometry geometry = editService.getGeometry();
		if (index != null) {
			geometry = editService.getIndexService().getGeometry(geometry, index);
		}
		Geometry g = geometry;
		if (!targetSpace.equals(RenderSpace.WORLD)) {
			g = mapPresenter.getViewPort().transform(g, RenderSpace.WORLD, targetSpace);
		}
		try {
			return gfxUtil.toPath(g);
		} catch (NullPointerException npe) {
			return null;
		}
	}

	// ------------------------------------------------------------------------
	// Private methods for updating shapes:
	// ------------------------------------------------------------------------

	private void updateVertex(Shape shape, GeometryEditService editService, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		if (shape instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) shape;
			Geometry geometry = editService.getGeometry();
			Coordinate v = editService.getIndexService().getVertex(geometry, index);
			if (!targetSpace.equals(RenderSpace.WORLD)) {
				v = mapPresenter.getViewPort().transform(v, RenderSpace.WORLD, targetSpace);
			}
			rectangle.setUserX(v.getX() - VERTEX_HALF_SIZE);
			rectangle.setUserY(v.getY() - VERTEX_HALF_SIZE);
		}
	}

	private void updateEdge(Shape shape, GeometryEditService editService, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		if (shape instanceof Path) {
			Path path = (Path) shape;
			Geometry geometry = editService.getGeometry();
			Coordinate[] edge = editService.getIndexService().getEdge(geometry, index);
			if (!targetSpace.equals(RenderSpace.WORLD)) {
				edge[0] = mapPresenter.getViewPort().transform(edge[0], RenderSpace.WORLD, targetSpace);
				edge[1] = mapPresenter.getViewPort().transform(edge[1], RenderSpace.WORLD, targetSpace);
			}
			path.setStep(0, new MoveTo(false, edge[0].getX(), edge[0].getY()));
			path.setStep(1, new LineTo(false, edge[1].getX(), edge[1].getY()));
		}
	}

	private void updateGeometry(Shape shape, GeometryEditService editService, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		if (shape instanceof Path) {
			Path path = (Path) shape;
			Geometry geometry = editService.getGeometry();
			if (index != null) {
				geometry = editService.getIndexService().getGeometry(geometry, index);
			}
			Geometry g = geometry;
			if (!targetSpace.equals(RenderSpace.WORLD)) {
				g = mapPresenter.getViewPort().transform(g, RenderSpace.WORLD, targetSpace);
			}

			// TODO find a better way. Now, the internal state of the path will be flawed.
			Path second = gfxUtil.toPath(g);
			for (int i = 0; i < path.getStepCount(); i++) {
				path.setStep(i, second.getStep(i));
			}
			String pathString = second.getElement().getAttribute("path");
			path.getElement().setAttribute("path", pathString);
		}
	}
}