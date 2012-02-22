/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.client;

import org.geomajas.sld.client.gin.ClientGinjectorBase;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

/**
 * @author Jan De Moerloose
 */
public class SldEditor implements EntryPoint {

	private final ClientGinjectorBase ginjector = GWT.create(ClientGinjectorBase.class);

	public final void onModuleLoad() {
		// This is required for Gwt-Platform proxy's generator.
		DelayedBindRegistry.bind(ginjector);

		ginjector.getPlaceManager().revealCurrentPlace();
	}
}
