/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.object.anchor;

import org.geomajas.graphics.client.shape.MarkerShape;
import org.vaadin.gwtgraphics.client.Shape;

/**
 * Interface with style methods for an anchor marker shape.
 * 
 * @author Jan Venstermans
 * 
 */
public interface AnchorMarkerShapeStyle {

	void setAnchorPointColor(String color);
	
	String getAnchorPointColor();
	
	void setAnchorPointOpacity(double opacity);
	
	double getAnchorPointOpacity();
	
	Shape getAnchorPointShape();
	
	void setAnchorPointShape(Shape shape);
	
	MarkerShape getMarkerShape();
	
	// markershape should be added in constructor,
	// setting new marker does not remove first markerobject from vectorobject
	// solution: remove Anchored role and add new anchored role, with markershape in constructor
//	void setMarkerShape(MarkerShape markerShape);
}
