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

package org.geomajas.plugin.geocoder.puregwt.example.client;

import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.plugin.geocoder.puregwt.example.client.i18n.GeoCoderMessages;
import org.geomajas.plugin.geocoder.puregwt.example.client.sample.GeoCoderExample;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Class description.
 *
 * @author Dosi Bingov
 */
public class GeoCoderExampleJar implements EntryPoint {
	private static final  GeoCoderMessages  MESSAGES = GWT.create(GeoCoderMessages.class);

	public static final String CATEGORY_GEOCODER = "Geocoder";

	public void onModuleLoad() {
		// Register all samples:
		registerSamples();
	}
	
	public static GeoCoderMessages getMessages() {
		return MESSAGES;
	}

	private void registerSamples() {
		SamplePanelRegistry.registerCategory(CATEGORY_GEOCODER, 110);
		SamplePanelRegistry.registerFactory(CATEGORY_GEOCODER, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new GeoCoderExample();
			}

			@Override
			public String getTitle() {
				return MESSAGES.geoCoderTitle();
			}

			@Override
			public String getDescription() {
				return MESSAGES.geoCoderDesription();
			}

			public String getShortDescription() {
				return MESSAGES.geoCoderShortDescr();
			}

			public String getCategory() {
				return CATEGORY_GEOCODER;
			}
		});
	}
}
