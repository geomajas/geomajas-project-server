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
package org.geomajas.sld.editor.expert.client;

import org.geomajas.sld.editor.expert.client.gin.SldEditorClientGinjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
import com.smartgwt.client.widgets.HTMLFlow;

/**
 * Entry point of SmartGWT SLD editor.
 * 
 * @author An Buyle
 * @author Jan De Moerloose
 */
public class SldEditorEntryPoint implements EntryPoint {

	private final SldEditorClientGinjector ginjector = GWT
			.create(SldEditorClientGinjector.class);

	public void onModuleLoad() {
		DelayedBindRegistry.bind(ginjector);

		// -- show some background
		HTMLFlow htmlFlow = new HTMLFlow();
		htmlFlow.setWidth100();
		htmlFlow.setHeight100();
		String contents = 
				"<div style='margin-left: 5px; font-size: 100pt; font-weight: bold; color:#DDFFDD'>GEOMAJAS</div>"
				+ "<div style='margin-left: 10px; margin-top:-70px; font-size: 50pt; color:#CCCCCC'>SLD-Editor</div>"
				+ "<div style='margin-left: 10px; margin-top:-15px; font-size: 28pt; color:#DDDDDD'>EXPERT-mode</div>";
		htmlFlow.setContents(contents);
		htmlFlow.draw();

		// -- now show the SL editor in a window
		ginjector.getPlaceManager().revealCurrentPlace();
	}

}
