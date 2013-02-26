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
package org.geomajas.plugin.deskmanager.client.gwt.manager.editor;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.deskmanager.domain.dto.BaseGeodeskDto;



/**
 * Interface for the widget editors that adds geodesk context.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface BaseGeodeskWidgetEditor extends WidgetEditor {

	
	/**
	 * Set the base geodesk that this editor is working on.
	 * 
	 * @param geodesk
	 */
	void setBaseGeodesk(BaseGeodeskDto geodesk);
	
}
