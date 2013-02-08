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
package org.geomajas.plugin.deskmanager.client.gwt.common;

import org.geomajas.annotation.Api;

/**
 * Configuration of a {@link WidgetEditor}. Values returned by this interface should be the same for all instances of
 * one user Editor class.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public interface WidgetEditorFactory {

	/**
	 * Create an instance of this widget editor.
	 * 
	 * @return the widgetEditor
	 */
	WidgetEditor createEditor();
	
	/**
	 * The key by which this Widget Editor is known in the application. Most often this is the same as the
	 * {@link org.geomajas.configuration.client.ClientWidgetInfo} identifier.
	 * 
	 * @return the key
	 */
	String getKey();

	/**
	 * The name by which this Widget Editor is known in the application.
	 * 
	 * @return the name
	 */
	String getName();

}
