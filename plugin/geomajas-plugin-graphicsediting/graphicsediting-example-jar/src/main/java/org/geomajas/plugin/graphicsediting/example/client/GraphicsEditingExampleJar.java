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

package org.geomajas.plugin.graphicsediting.example.client;

import org.geomajas.gwt.example.base.client.sample.SamplePanel;
import org.geomajas.gwt.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.plugin.graphicsediting.example.client.i18n.GraphicsEditingMessages;
import org.geomajas.plugin.graphicsediting.example.client.sample.GraphicsEditingExample;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Class description.
 *
 * @author Jan De Moerloose
 */
public class GraphicsEditingExampleJar implements EntryPoint {
	private static final  GraphicsEditingMessages  MESSAGES = GWT.create(GraphicsEditingMessages.class);

	public static final String CATEGORY_GRAPHICS = "Graphics";

	public void onModuleLoad() {
		// Register all samples:
		registerSamples();
	}
	
	public static GraphicsEditingMessages getMessages() {
		return MESSAGES;
	}

	private void registerSamples() {
		SamplePanelRegistry.registerCategory(CATEGORY_GRAPHICS, 120);
		SamplePanelRegistry.registerFactory(CATEGORY_GRAPHICS, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new GraphicsEditingExample();
			}

			@Override
			public String getTitle() {
				return MESSAGES.graphicsEditingTitle();
			}

			@Override
			public String getDescription() {
				return MESSAGES.graphicsEditingDescription();
			}

			public String getShortDescription() {
				return MESSAGES.graphicsEditingShortDescr();
			}

			public String getCategory() {
				return CATEGORY_GRAPHICS;
			}
		});
	}
}
