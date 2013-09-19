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
package org.geomajas.graphics.client.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

/**
 * Client resource bundle interface for UI resources.
 * 
 * @author Jan De Moerloose
 */
public interface GraphicsResource extends ClientBundle {

	/**
	 * Instance for use outside UIBinder.
	 */
	GraphicsResource INSTANCE = GWT.create(GraphicsResource.class);
	
	/**
	 * I18n messages.
	 */
	GraphicsMessages MESSAGES = GWT.create(GraphicsMessages.class);
	
	/**
	 * Get the css resource.
	 * 
	 * @return the css resource
	 */
	@Source("geomajas-graphics.css")
	GraphicsCssResource css();


}