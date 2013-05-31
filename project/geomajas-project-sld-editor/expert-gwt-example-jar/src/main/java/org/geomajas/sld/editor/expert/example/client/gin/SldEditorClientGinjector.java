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
package org.geomajas.sld.editor.expert.example.client.gin;

import org.geomajas.sld.editor.expert.client.gin.ClientGinjector;

import com.google.gwt.inject.client.GinModules;

/**
 * Adds the {@link ClientModule} module to the base Ginjector.
 * 
 * @author Kristof Heirwegh
 */
@GinModules({ SldEditorClientModule.class })
public interface SldEditorClientGinjector extends ClientGinjector {

	// add your own

}