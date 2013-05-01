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

package org.geomajas.puregwt.example.client;

import org.geomajas.puregwt.client.GeomajasGinjector;
import org.geomajas.puregwt.example.client.i18n.SampleMessages;
import org.geomajas.puregwt.example.client.page.sample.SamplePage;
import org.geomajas.puregwt.example.client.resource.ShowcaseResource;
import org.geomajas.puregwt.example.client.sample.SamplePanel;
import org.geomajas.puregwt.example.client.sample.SamplePanelRegistry;
import org.geomajas.puregwt.example.client.sample.ShowcaseSampleDefinition;
import org.geomajas.puregwt.example.client.sample.feature.FeatureSelectionPanel;
import org.geomajas.puregwt.example.client.sample.general.MapFillPanel;
import org.geomajas.puregwt.example.client.sample.general.NavigationOptionPanel;
import org.geomajas.puregwt.example.client.sample.general.ResizeMapPanel;
import org.geomajas.puregwt.example.client.sample.general.ViewPortEventPanel;
import org.geomajas.puregwt.example.client.sample.layer.LayerAddRemovePanel;
import org.geomajas.puregwt.example.client.sample.layer.LayerOpacityPanel;
import org.geomajas.puregwt.example.client.sample.layer.LayerOrderPanel;
import org.geomajas.puregwt.example.client.sample.layer.LayerRefreshPanel;
import org.geomajas.puregwt.example.client.sample.layer.LayerVisibilityPanel;
import org.geomajas.puregwt.example.client.sample.rendering.DrawingInteractionPanel;
import org.geomajas.puregwt.example.client.sample.rendering.FixedSizeWorldSpaceRenderingPanel;
import org.geomajas.puregwt.example.client.sample.rendering.ScreenSpaceRenderingPanel;
import org.geomajas.puregwt.example.client.sample.rendering.WorldSpaceRenderingPanel;
import org.geomajas.puregwt.example.client.widget.ShowcaseDialogBox;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point and main class for PureGWT example application.
 * 
 * @author Pieter De Graef
 */
public class Showcase implements EntryPoint {

	public static final GeomajasGinjector GEOMAJASINJECTOR = GWT.create(GeomajasGinjector.class);

	public static final ShowcaseResource RESOURCE = GWT.create(ShowcaseResource.class);

	public static final SampleMessages MSG_SAMPLE = GWT.create(SampleMessages.class);

	public static final String CATEGORY_GENERAL = "General Samples";

	public static final String CATEGORY_LAYER = "Layer functionalities";

	public static final String CATEGORY_FEATURE = "Feature functionalities";

	public static final String CATEGORY_RENDERING = "Drawing on the map";

	public static final ShowcaseLayout LAYOUT = new ShowcaseLayout();

	public void onModuleLoad() {
		// Prepare styling:
		RESOURCE.css().ensureInjected();

		// Register all samples:
		registerGeneralSamples();
		registerLayerSamples();
		registerFeatureSamples();
		registerRenderingSamples();

		// Build the showcase GUI:
		RootLayoutPanel.get().add(LAYOUT);
	}

	public static ShowcaseLayout getLayout() {
		return LAYOUT;
	}
	
	public static void showSample(ShowcaseSampleDefinition sample) {
		ShowcaseDialogBox dialogBox = new ShowcaseDialogBox();
		dialogBox.setAnimationEnabled(true);
		dialogBox.setAutoHideEnabled(false);
		dialogBox.setModal(true);
		dialogBox.setGlassEnabled(true);
		dialogBox.setText(sample.getTitle());
		SamplePage page = new SamplePage();
		int width = Window.getClientWidth() - 200;
		int height = Window.getClientHeight() - 160;
		page.setSize(width + "px", height + "px");
		page.setSamplePanelFactory(sample);
		dialogBox.setWidget(page);
		dialogBox.center();
		dialogBox.show();
	}

	private void registerGeneralSamples() {
		SamplePanelRegistry.registerCategory(CATEGORY_GENERAL, 100);
		SamplePanelRegistry.registerFactory(CATEGORY_GENERAL, new ShowcaseSampleDefinition() {

			public SamplePanel create() {
				return new NavigationOptionPanel();
			}

			public String getTitle() {
				return MSG_SAMPLE.generalNavOptionTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.generalNavOptionShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.generalNavOptionDescription();
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
				return MSG_SAMPLE.generalResizeMapTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.generalResizeMapShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.generalResizeMapDescription();
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
				return MSG_SAMPLE.generalMapFillTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.generalMapFillShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.generalMapFillDescription();
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
				return MSG_SAMPLE.generalVpEventTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.generalVpEventShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.generalVpEventDescription();
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
				return MSG_SAMPLE.layerAddRemoveTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.layerAddRemoveShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.layerAddRemoveDescription();
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
				return MSG_SAMPLE.layerOrderTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.layerOrderShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.layerOrderDescription();
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
				return MSG_SAMPLE.layerVisibilityTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.layerVisibilityShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.layerVisibilityDescription();
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
				return MSG_SAMPLE.layerRefreshTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.layerRefreshShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.layerRefreshDescription();
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
				return MSG_SAMPLE.layerOpacityTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.layerOpacityShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.layerOpacityDescription();
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
				return MSG_SAMPLE.featureSelectionTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.featureSelectionShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.featureSelectionDescription();
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
				return MSG_SAMPLE.renderingInteractionTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.renderingInteractionShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.renderingInteractionDescription();
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
				return MSG_SAMPLE.renderingScreenSpaceTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.renderingScreenSpaceShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.renderingScreenSpaceDescription();
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
				return MSG_SAMPLE.renderingWorldSpaceTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.renderingWorldSpaceShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.renderingWorldSpaceDescription();
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
				return MSG_SAMPLE.renderingWorldSpaceFixedTitle();
			}

			public String getShortDescription() {
				return MSG_SAMPLE.renderingWorldSpaceFixedShort();
			}

			public String getDescription() {
				return MSG_SAMPLE.renderingWorldSpaceFixedDescription();
			}

			public String getCategory() {
				return CATEGORY_RENDERING;
			}
		});
	}
}