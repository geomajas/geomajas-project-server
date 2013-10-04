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

package org.geomajas.gwt2.client.gfx;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt2.client.controller.MapController;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.Strokeable;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;

import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;

/**
 * Utility class concerning custom graphics rendering on the map.
 * 
 * @author Pieter De Graef
 */
public final class GfxUtilImpl implements GfxUtil {

	@Inject
	private GfxUtilImpl() {
	}

	@Override
	public void applyStroke(VectorObject object, String strokeColor, double strokeOpacity, int strokeWidth,
			String dashArray) {
		if (object instanceof Strokeable) {
			strokeObject((Strokeable) object, strokeColor, strokeOpacity, strokeWidth, dashArray);
		} else if (object instanceof Group) {
			strokeGroup((Group) object, strokeColor, strokeOpacity, strokeWidth, dashArray);
		}
	}

	@Override
	public void applyFill(VectorObject object, String fillColor, double fillOpacity) {
		if (object instanceof Shape) {
			fillObject((Shape) object, fillColor, fillOpacity);
		} else if (object instanceof Group) {
			fillGroup((Group) object, fillColor, fillOpacity);
		}
	}

	public List<HandlerRegistration> applyController(VectorObject shape, MapController mapController) {
		List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();
		registrations.add(shape.addMouseDownHandler(mapController));
		registrations.add(shape.addMouseUpHandler(mapController));
		registrations.add(shape.addMouseMoveHandler(mapController));
		registrations.add(shape.addMouseOutHandler(mapController));
		registrations.add(shape.addMouseOverHandler(mapController));
		registrations.add(shape.addMouseWheelHandler(mapController));
		registrations.add(shape.addDoubleClickHandler(mapController));
		registrations.add(shape.addDomHandler(mapController, TouchStartEvent.getType()));
		registrations.add(shape.addDomHandler(mapController, TouchEndEvent.getType()));
		registrations.add(shape.addDomHandler(mapController, TouchMoveEvent.getType()));
		registrations.add(shape.addDomHandler(mapController, TouchCancelEvent.getType()));
		return registrations;
	}

	public VectorObject toShape(Geometry geometry) {
		if (geometry != null) {
			if (GeometryService.getNumPoints(geometry) == 0) {
				return null;
			}
			if (Geometry.POINT.equals(geometry.getGeometryType())) {
				return toShapePoint(geometry);
			} else if (Geometry.MULTI_POINT.equals(geometry.getGeometryType())) {
				return toShapeMultiPoint(geometry);
			} else {
				return new GeometryPath(geometry);
			}
		}
		return null;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private Circle toShapePoint(Geometry point) {
		if (point.getCoordinates() != null && point.getCoordinates().length == 1) {
			Coordinate first = point.getCoordinates()[0];
			Circle circle = new Circle(first.getX(), first.getY(), 5);
			circle.setFixedSize(true);
			return circle;
		}
		return null;
	}

	private Group toShapeMultiPoint(Geometry multiPoint) {
		Group group = new Group();
		if (multiPoint.getGeometries() != null && multiPoint.getGeometries().length > 0) {
			for (int i = 0; i < multiPoint.getGeometries().length; i++) {
				Geometry point = multiPoint.getGeometries()[i];
				Shape shape = toShapePoint(point);
				group.add(shape);
			}
			return group;
		}
		return null;
	}

	private void strokeGroup(Group group, String strokeColor, double strokeOpacity, int strokeWidth, String dashArray) {
		for (int i = 0; i < group.getVectorObjectCount(); i++) {
			VectorObject child = group.getVectorObject(i);
			if (child instanceof Strokeable) {
				strokeObject((Strokeable) group.getVectorObject(i), strokeColor, strokeOpacity, strokeWidth, dashArray);
			}
		}
	}

	private void strokeObject(Strokeable strokeable, String strokeColor, double strokeOpacity, int strokeWidth,
			String dashArray) {
		strokeable.setStrokeColor(strokeColor);
		strokeable.setStrokeOpacity(strokeOpacity);
		strokeable.setStrokeWidth(strokeWidth);
		strokeable.setDashArray(dashArray);
	}
	
	private void fillGroup(Group group, String fillColor, double fillOpacity) {
		for (int i = 0; i < group.getVectorObjectCount(); i++) {
			VectorObject child = group.getVectorObject(i);
			if (child instanceof Shape) {
				fillObject((Shape) group.getVectorObject(i), fillColor, fillOpacity);
			}
		}
	}

	private void fillObject(Shape shape, String fillColor, double fillOpacity) {
		shape.setFillColor(fillColor);
		shape.setFillOpacity(fillOpacity);
	}

}