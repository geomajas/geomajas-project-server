/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.client.gin;

import org.geomajas.sld.client.NameTokens;
import org.geomajas.sld.client.SldEditorPlaceManager;

import com.google.gwt.inject.client.AbstractGinModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;

/**
 * Client module for the common SLD editor part.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ClientModule extends AbstractGinModule {

	protected void configure() {
		// Default implementation of standard resources
		install(new DefaultModule(SldEditorPlaceManager.class));

		// Constants
		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.HOME_PAGE);

	}

}
