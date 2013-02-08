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
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.domain.dto.BaseGeodeskDto;

import com.smartgwt.client.widgets.Canvas;



/**
 * Interface for the widget editors.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public interface WidgetEditor {

	
	/**
	 * Get the canvas for this editor.
	 * 
	 * @return
	 */
	Canvas getCanvas();
	
	/**
	 * Get the widget configuration that this editor is editing.
	 * 
	 * @return the widget configuration.
	 */
	ClientWidgetInfo getWidgetConfiguration();
	
	/**
	 * Set the widget configuration that this editor should edit.
	 * 
	 * @param configuration the widget configuration.
	 */
	void setWidgetConfiguration(ClientWidgetInfo configuration);
	
	/**
	 * Set the base geodesk that this editor is working on.
	 * 
	 * @param geodesk
	 */
	void setBaseGeodesk(BaseGeodeskDto geodesk);
	
	/**
	 * Set if the editor is disabled.
	 * 
	 * @param disabled
	 */
	void setDisabled(boolean disabled);
}
