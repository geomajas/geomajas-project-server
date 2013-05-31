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
package org.geomajas.sld.editor.expert.example.client;

import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.sld.editor.expert.example.client.gin.SldEditorClientGinjector;

import com.gwtplatform.mvp.client.DelayedBindRegistry;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;

/**
 * Entry point of SmartGWT SLD editor.
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorPanel extends SamplePanel {

	public static final String TITLE = "ExpertSldEditor";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {
		public SamplePanel createPanel() {
			return new SldEditorPanel();
		}
	};

	@Override
	public Canvas getViewPanel() {
		// -- show some background
		HTMLFlow htmlFlow = new HTMLFlow();
		htmlFlow.setWidth100();
		htmlFlow.setHeight100();
		String contents = 
				"<div style='margin-left: 5px; font-size: 100pt; font-weight: bold; color:#DDFFDD'>GEOMAJAS</div>"
				+ "<div style='margin-left: 10px; margin-top:-70px; font-size: 50pt; color:#CCCCCC'>SLD-Editor</div>"
				+ "<div style='margin-left: 10px; margin-top:-15px; font-size: 28pt; color:#DDDDDD'>EXPERT-mode</div>";
		htmlFlow.setContents(contents);

		// -- load an sld
//		String yourXmlDataHere = "<test></test>";
//		ginjector.getSldEditorExpertPresenter().get().loadSld(yourXmlDataHere, "name", "title");
		// -- or use StyledSldLayerDescriptorInfo;
//		ginjector.getSldEditorExpertPresenter().get().loadSld(sldObject);
		
		// -- now show the SLD editor in a window
		((SldEditorClientGinjector) DelayedBindRegistry.getGinjector()).getPlaceManager().revealCurrentPlace();
		
		return htmlFlow;
	}

	@Override
	public String getDescription() {
		return SldEditorExample.MSG.sampleDescription();
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[] {"classpath:org/geomajas/sld/editor/expert/example/context/application.xml"};
	}
}
