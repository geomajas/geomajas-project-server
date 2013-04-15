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
package org.geomajas.plugin.deskmanager.client.gwt.manager.gin;

import org.geomajas.sld.editor.expert.client.gin.ClientGinjector;

import com.google.gwt.inject.client.GinModules;

/**
 * Adds the {@link ClientModule} module to the base Ginjector.
 * 
 * @author Kristof Heirwegh
 */
@GinModules({ DeskmanagerClientModule.class })
public interface DeskmanagerClientGinjector extends ClientGinjector {

	// add your own

}