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

import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.sld.editor.expert.example.client.gin.SldEditorClientGinjector;
import org.geomajas.sld.editor.expert.example.client.i18n.SampleMessages;
import org.geomajas.sld.editor.expert.example.client.sample.layer.LayerChangeStyleExpertPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

/**
 * Entry point and main class for the PureGWT Expert SLD Editor sample application.
 * 
 * @author Pieter De Graef
 * @author Kristof Heirwegh
 */
public class ExampleJar implements EntryPoint {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	private static final SldEditorClientGinjector GINJECTOR = GWT.create(SldEditorClientGinjector.class);

	public static final String CATEGORY_LAYER = "Layer functionalities";

	public void onModuleLoad() {
		DelayedBindRegistry.bind(GINJECTOR);

		// Register all samples:
		registerLayerSamples();
	}

	public static SampleMessages getMessages() {
		return MESSAGES;
	}

	public static SldEditorClientGinjector getInjector() {
		 return GINJECTOR;
	}

	private void registerLayerSamples() {
		SamplePanelRegistry.registerCategory(CATEGORY_LAYER, 99);
		SamplePanelRegistry.registerFactory(CATEGORY_LAYER, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new LayerChangeStyleExpertPanel();
			}

			public String getTitle() {
				return MESSAGES.layerChangeStyleExpertTitle();
			}

			public String getShortDescription() {
				return MESSAGES.layerChangeStyleExpertShort();
			}

			public String getDescription() {
				return MESSAGES.layerChangeStyleExpertDescription();
			}

			public String getCategory() {
				return CATEGORY_LAYER;
			}
		});
	}

}