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
package org.geomajas.sld.editor.client;

import org.geomajas.sld.editor.client.gin.SldEditorClientGinjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
import com.smartgwt.client.widgets.Canvas;

/**
 * Entry point of SmartGWT SLD editor.
 * 
 * @author An Buyle
 * @author Jan De Moerloose
 */
public class SldEditorEntryPoint implements EntryPoint {

	private final SldEditorClientGinjector ginjector = GWT.create(SldEditorClientGinjector.class);

	public void onModuleLoad() {
		// The SLD Editor's is automatically added to the SmartGWT root panel.
		
		// This is required for Gwt-Platform proxy's generator.
		DelayedBindRegistry.bind(ginjector);

		ginjector.getPlaceManager().revealCurrentPlace();
		//SC.showConsole();
	}

	public Canvas getViewPanel() {

		return null;
	}

}
