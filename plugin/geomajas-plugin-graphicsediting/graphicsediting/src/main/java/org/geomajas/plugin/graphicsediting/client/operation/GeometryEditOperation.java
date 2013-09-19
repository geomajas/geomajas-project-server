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
package org.geomajas.plugin.graphicsediting.client.operation;

import org.geomajas.geometry.Geometry;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.operation.GraphicsOperation;
import org.geomajas.plugin.graphicsediting.client.object.GeometryEditable;

/**
 * Operation that edits the {@link Geometry} of an object.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeometryEditOperation implements GraphicsOperation {

	private Geometry beforeGeometry;

	private Geometry afterGeometry;

	private GraphicsObject geometryEditable;

	public GeometryEditOperation(GraphicsObject geometryEditable,
			Geometry beforeGeometry, Geometry afterGeometry) {
		this.beforeGeometry = beforeGeometry;
		this.afterGeometry = afterGeometry;
		this.geometryEditable = geometryEditable;
	}

	@Override
	public void execute() {
		asGeometryEditable().setGeometry(afterGeometry);
	}

	@Override
	public void undo() {
		asGeometryEditable().setGeometry(beforeGeometry);
	}

	private GeometryEditable asGeometryEditable() {
		return geometryEditable.getRole(GeometryEditable.TYPE);
	}

	@Override
	public GraphicsObject getObject() {
		return geometryEditable;
	}

	@Override
	public Type getType() {
		return Type.UPDATE;
	}

}