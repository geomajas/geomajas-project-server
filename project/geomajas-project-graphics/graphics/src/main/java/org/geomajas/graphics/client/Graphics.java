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
package org.geomajas.graphics.client;

import org.geomajas.graphics.client.resource.GraphicsResource;

import com.google.gwt.core.client.EntryPoint;

/**
 * {@link EntryPoint} for the Graphics project.
 * 
 * @author Jan De Moerloose
 * 
 */
public class Graphics implements EntryPoint {

	@Override
	public void onModuleLoad() {
		GraphicsResource.INSTANCE.css().ensureInjected();
	}

}
