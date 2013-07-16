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

package org.geomajas.puregwt.widget.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.geomajas.puregwt.example.base.client.sample.SamplePanel;
import org.geomajas.puregwt.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.puregwt.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.puregwt.widget.example.client.i18n.SampleMessages;
import org.geomajas.puregwt.widget.example.client.sample.closeabledialogbox.CloseableDialogExample;

/**
 * Entry point and main class for the widget core example application.
 * 
 * @author Pieter De Graef
 */
public class ExampleJar implements EntryPoint {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String CATEGORY_WIDGET = "Widgets";

	public void onModuleLoad() {
		// ExampleWidgetResource.INSTANCE.css().ensureInjected();
		// Register all samples:
		registerSamples();
	}

	public static SampleMessages getMessages() {
		return MESSAGES;
	}

	private void registerSamples() {
		SamplePanelRegistry.registerCategory(CATEGORY_WIDGET, 90);
		SamplePanelRegistry.registerFactory(CATEGORY_WIDGET, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return  null; // LayerLegendPanelSample();
			}

			@Override
			public String getTitle() {
				return "Legend";
			}

			@Override
			public String getDescription() {
				return "Showcase that show the layer legend view.";
			}

			public String getShortDescription() {
				return "Showcase that show the layer legend view.";
			}

			public String getCategory() {
				return CATEGORY_WIDGET;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_WIDGET, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return null;  // new LegendDropDownWidgetSample();
			}

			@Override
			public String getTitle() {
				return "LegendDropDownWidget";
			}

			@Override
			public String getDescription() {
				return "Showcase that show the layer legend view.";
			}

			public String getShortDescription() {
				return "Showcase that show the layer legend view.";
			}

			public String getCategory() {
				return CATEGORY_WIDGET;
			}
		});

		SamplePanelRegistry.registerCategory(CATEGORY_WIDGET, 100);
		SamplePanelRegistry.registerFactory(CATEGORY_WIDGET, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new CloseableDialogExample();
			}

			public String getTitle() {
				return MESSAGES.closeableDialogTitle();
			}

			public String getShortDescription() {
				return MESSAGES.closeableDialogDescrShort();
			}

			public String getDescription() {
				return MESSAGES.closeableDialogDescription();
			}

			public String getCategory() {
				return CATEGORY_WIDGET;
			}
		});
	}
}