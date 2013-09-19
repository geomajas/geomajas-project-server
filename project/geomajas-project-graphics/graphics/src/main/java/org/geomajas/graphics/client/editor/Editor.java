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
package org.geomajas.graphics.client.editor;

import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.service.GraphicsService;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Editor widget for editing object roles in a custom manner.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface Editor extends IsWidget {

	/**
	 * Does the editor support this object ?
	 * 
	 * @param object
	 * @return true if supported, false otherwise
	 */
	boolean supports(GraphicsObject object);

	/**
	 * Set the current object. The widget state should reflect the current state of the object.
	 * 
	 * @param object
	 */
	void setObject(GraphicsObject object);

	/**
	 * Set the {@link GraphicsService} to use.
	 * 
	 * @param service
	 */
	void setService(GraphicsService service);

	/**
	 * Called when the widget state should be copied to the object by executing an operation on the service.
	 * Implementors may choose not to implement this method and execute operations immediately upon user interaction.
	 */
	void onOk();

	/**
	 * Returns a label for the editor. TODO: extend with icon, tooltip..
	 * @return
	 */
	String getLabel();

	/**
	 * Checks if format of all values is correct.
	 * 
	 * @return
	 */
	boolean validate();
	
	void undo();
	
	void setIconUrl(String url);
	
	String getIconUrl();
}
