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
package org.geomajas.project.graphics.example.client;

import org.vaadin.gwtgraphics.client.Group;

/**
 * Exposes getter for external transformation.
 * 
 * @author Jan De Moerloose
 * 
 */
public class TransformingGroup extends Group {

	public double getDeltaX() {
		return super.getDeltaX();
	}

	public double getDeltaY() {
		return super.getDeltaY();
	}

	public double getScaleX() {
		return super.getScaleX();
	}

	public double getScaleY() {
		return super.getScaleY();
	}

}
