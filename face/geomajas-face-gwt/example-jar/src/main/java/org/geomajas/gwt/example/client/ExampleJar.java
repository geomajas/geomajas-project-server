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

package org.geomajas.gwt.example.client;

import org.geomajas.gwt.client.GeomajasGinjector;
import org.geomajas.gwt.example.base.client.ExampleBase;
import org.geomajas.gwt.example.base.client.sample.SamplePanel;
import org.geomajas.gwt.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.gwt.example.client.i18n.SampleMessages;
import org.geomajas.gwt.example.client.sample.feature.FeatureSelectionPanel;
import org.geomajas.gwt.example.client.sample.general.AlternativeControlsPanel;
import org.geomajas.gwt.example.client.sample.general.MapFillPanel;
import org.geomajas.gwt.example.client.sample.general.NavigationOptionPanel;
import org.geomajas.gwt.example.client.sample.general.ResizeMapPanel;
import org.geomajas.gwt.example.client.sample.general.ServerExceptionPanel;
import org.geomajas.gwt.example.client.sample.general.ViewPortEventPanel;
import org.geomajas.gwt.example.client.sample.layer.LayerAddRemovePanel;
import org.geomajas.gwt.example.client.sample.layer.LayerOpacityPanel;
import org.geomajas.gwt.example.client.sample.layer.LayerOrderPanel;
import org.geomajas.gwt.example.client.sample.layer.LayerRefreshPanel;
import org.geomajas.gwt.example.client.sample.layer.LayerVisibilityPanel;
import org.geomajas.gwt.example.client.sample.listener.ListenerPanel;
import org.geomajas.gwt.example.client.sample.rendering.CanvasRenderingPanel;
import org.geomajas.gwt.example.client.sample.rendering.DrawingInteractionPanel;
import org.geomajas.gwt.example.client.sample.rendering.FixedSizeWorldSpaceRenderingPanel;
import org.geomajas.gwt.example.client.sample.rendering.ScreenSpaceRenderingPanel;
import org.geomajas.gwt.example.client.sample.rendering.WorldSpaceRenderingPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point and main class for the GWT client example application.
 * 
 * @author Pieter De Graef
 */
public class ExampleJar implements EntryPoint {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String CATEGORY_GENERAL = "General Samples";

	public static final String CATEGORY_LAYER = "Layer functionalities";

	public static final String CATEGORY_FEATURE = "Feature functionalities";

	public static final String CATEGORY_RENDERING = "Drawing on the map";

	public void onModuleLoad() {
		// Register all samples:
		registerGeneralSamples();
		registerLayerSamples();
		registerFeatureSamples();
		registerRenderingSamples();
		registerListenerSample();
	}

	public static SampleMessages getMessages() {
		return MESSAGES;
	}

	public static GeomajasGinjector getInjector() {
		return (GeomajasGinjector) ExampleBase.getInjector();
	}

	private void registerGeneralSamples() {
		SamplePanelRegistry.registerCategory(CATEGORY_GENERAL, 100);
		SamplePanelRegistry.registerFactory(CATEGORY_GENERAL, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new NavigationOptionPanel();
			}

			public String getTitle() {
				return MESSAGES.generalNavOptionTitle();
			}

			public String getShortDescription() {
				return MESSAGES.generalNavOptionShort();
			}

			public String getDescription() {
				return MESSAGES.generalNavOptionDescription();
			}

			public String getCategory() {
				return CATEGORY_GENERAL;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_GENERAL, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new ResizeMapPanel();
			}

			public String getTitle() {
				return MESSAGES.generalResizeMapTitle();
			}

			public String getShortDescription() {
				return MESSAGES.generalResizeMapShort();
			}

			public String getDescription() {
				return MESSAGES.generalResizeMapDescription();
			}

			public String getCategory() {
				return CATEGORY_GENERAL;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_GENERAL, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new MapFillPanel();
			}

			public String getTitle() {
				return MESSAGES.generalMapFillTitle();
			}

			public String getShortDescription() {
				return MESSAGES.generalMapFillShort();
			}

			public String getDescription() {
				return MESSAGES.generalMapFillDescription();
			}

			public String getCategory() {
				return CATEGORY_GENERAL;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_GENERAL, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new ViewPortEventPanel();
			}

			public String getTitle() {
				return MESSAGES.generalVpEventTitle();
			}

			public String getShortDescription() {
				return MESSAGES.generalVpEventShort();
			}

			public String getDescription() {
				return MESSAGES.generalVpEventDescription();
			}

			public String getCategory() {
				return CATEGORY_GENERAL;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_GENERAL, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new AlternativeControlsPanel();
			}

			public String getTitle() {
				return MESSAGES.generalControlsTitle();
			}

			public String getShortDescription() {
				return MESSAGES.generalControlsShort();
			}

			public String getDescription() {
				return MESSAGES.generalControlsDescription();
			}

			public String getCategory() {
				return CATEGORY_GENERAL;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_GENERAL, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new ServerExceptionPanel();
			}

			public String getTitle() {
				return MESSAGES.generalServerExceptionTitle();
			}

			public String getShortDescription() {
				return MESSAGES.generalServerExceptionShort();
			}

			public String getDescription() {
				return MESSAGES.generalServerExceptionDescription();
			}

			public String getCategory() {
				return CATEGORY_GENERAL;
			}
		});
	}

	private void registerLayerSamples() {
		SamplePanelRegistry.registerCategory(CATEGORY_LAYER, 99);
		SamplePanelRegistry.registerFactory(CATEGORY_LAYER, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new LayerAddRemovePanel();
			}

			public String getTitle() {
				return MESSAGES.layerAddRemoveTitle();
			}

			public String getShortDescription() {
				return MESSAGES.layerAddRemoveShort();
			}

			public String getDescription() {
				return MESSAGES.layerAddRemoveDescription();
			}

			public String getCategory() {
				return CATEGORY_LAYER;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_LAYER, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new LayerOrderPanel();
			}

			public String getTitle() {
				return MESSAGES.layerOrderTitle();
			}

			public String getShortDescription() {
				return MESSAGES.layerOrderShort();
			}

			public String getDescription() {
				return MESSAGES.layerOrderDescription();
			}

			public String getCategory() {
				return CATEGORY_LAYER;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_LAYER, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new LayerVisibilityPanel();
			}

			public String getTitle() {
				return MESSAGES.layerVisibilityTitle();
			}

			public String getShortDescription() {
				return MESSAGES.layerVisibilityShort();
			}

			public String getDescription() {
				return MESSAGES.layerVisibilityDescription();
			}

			public String getCategory() {
				return CATEGORY_LAYER;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_LAYER, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new LayerRefreshPanel();
			}

			public String getTitle() {
				return MESSAGES.layerRefreshTitle();
			}

			public String getShortDescription() {
				return MESSAGES.layerRefreshShort();
			}

			public String getDescription() {
				return MESSAGES.layerRefreshDescription();
			}

			public String getCategory() {
				return CATEGORY_LAYER;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_LAYER, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new LayerOpacityPanel();
			}

			public String getTitle() {
				return MESSAGES.layerOpacityTitle();
			}

			public String getShortDescription() {
				return MESSAGES.layerOpacityShort();
			}

			public String getDescription() {
				return MESSAGES.layerOpacityDescription();
			}

			public String getCategory() {
				return CATEGORY_LAYER;
			}
		});
	}

	private void registerFeatureSamples() {
		SamplePanelRegistry.registerCategory(CATEGORY_FEATURE, 98);
		SamplePanelRegistry.registerFactory(CATEGORY_FEATURE, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new FeatureSelectionPanel();
			}

			public String getTitle() {
				return MESSAGES.featureSelectionTitle();
			}

			public String getShortDescription() {
				return MESSAGES.featureSelectionShort();
			}

			public String getDescription() {
				return MESSAGES.featureSelectionDescription();
			}

			public String getCategory() {
				return CATEGORY_FEATURE;
			}
		});
	}

	private void registerRenderingSamples() {
		SamplePanelRegistry.registerCategory(CATEGORY_RENDERING, 97);
		SamplePanelRegistry.registerFactory(CATEGORY_RENDERING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new DrawingInteractionPanel();
			}

			public String getTitle() {
				return MESSAGES.renderingInteractionTitle();
			}

			public String getShortDescription() {
				return MESSAGES.renderingInteractionShort();
			}

			public String getDescription() {
				return MESSAGES.renderingInteractionDescription();
			}

			public String getCategory() {
				return CATEGORY_RENDERING;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_RENDERING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new ScreenSpaceRenderingPanel();
			}

			public String getTitle() {
				return MESSAGES.renderingScreenSpaceTitle();
			}

			public String getShortDescription() {
				return MESSAGES.renderingScreenSpaceShort();
			}

			public String getDescription() {
				return MESSAGES.renderingScreenSpaceDescription();
			}

			public String getCategory() {
				return CATEGORY_RENDERING;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_RENDERING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new WorldSpaceRenderingPanel();
			}

			public String getTitle() {
				return MESSAGES.renderingWorldSpaceTitle();
			}

			public String getShortDescription() {
				return MESSAGES.renderingWorldSpaceShort();
			}

			public String getDescription() {
				return MESSAGES.renderingWorldSpaceDescription();
			}

			public String getCategory() {
				return CATEGORY_RENDERING;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_RENDERING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new FixedSizeWorldSpaceRenderingPanel();
			}

			public String getTitle() {
				return MESSAGES.renderingWorldSpaceFixedTitle();
			}

			public String getShortDescription() {
				return MESSAGES.renderingWorldSpaceFixedShort();
			}

			public String getDescription() {
				return MESSAGES.renderingWorldSpaceFixedDescription();
			}

			public String getCategory() {
				return CATEGORY_RENDERING;
			}
		});
		SamplePanelRegistry.registerFactory(CATEGORY_RENDERING, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new CanvasRenderingPanel();
			}

			public String getTitle() {
				return MESSAGES.renderingCanvasTitle();
			}

			public String getShortDescription() {
				return MESSAGES.renderingCanvasShort();
			}

			public String getDescription() {
				return MESSAGES.renderingCanvasDescription();
			}

			public String getCategory() {
				return CATEGORY_RENDERING;
			}
		});
	}

	private void registerListenerSample() {
		SamplePanelRegistry.registerCategory(CATEGORY_GENERAL, 105);
		SamplePanelRegistry.registerFactory(CATEGORY_GENERAL, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new ListenerPanel();
			}

			public String getTitle() {
				return MESSAGES.generalListnerTitle();
			}

			public String getShortDescription() {
				return MESSAGES.generalListnerShort();
			}

			public String getDescription() {
				return MESSAGES.generalListnerDescription();
			}

			public String getCategory() {
				return CATEGORY_GENERAL;
			}
		});
	}
}