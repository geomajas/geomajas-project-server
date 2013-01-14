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

package org.geomajas.gwt.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;
import org.geomajas.gwt.client.map.event.LayerFilteredEvent;
import org.geomajas.gwt.client.map.event.LayerFilteredHandler;
import org.geomajas.gwt.client.map.event.LayerLabeledEvent;
import org.geomajas.gwt.client.map.event.LayerShownEvent;
import org.geomajas.gwt.client.map.event.LayerStyleChangeEvent;
import org.geomajas.gwt.client.map.event.LayerStyleChangedHandler;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.UrlBuilder;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.UserStyleInfo;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Widget that shows the styles of the currently visible layers. For vector layers, there can be many styles. Note that
 * this widget will react automatically to the visibility status of the layers.
 * </p>
 * 
 * @author Frank Wynants
 * @author Pieter De Graef
 */
public class Legend extends VLayout {

	private List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

	private HandlerRegistration loadedRegistration;

	private MapModel mapModel;

	private boolean staticLegend;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * A legend needs to be instantiated with the MapModel that contains (or will contain) the list of layers that this
	 * legend should listen to.
	 * 
	 * @param mapModel
	 *            map model
	 * @param staticLegend
	 *			  should this Legend be statically rendered?
	 * @since 1.10.0
	 */
	public Legend(MapModel mapModel, boolean staticLegend) {
		this(mapModel);
		setMargin(WidgetLayout.marginSmall);
		this.staticLegend = staticLegend;
	}

	/**
	 * A legend needs to be instantiated with the MapModel that contains (or will contain) the list of layers that this
	 * legend should listen to.
	 * 
	 * @param mapModel
	 *            map model
	 */
	public Legend(MapModel mapModel) {
		setMembersMargin(WidgetLayout.marginSmall);
		this.mapModel = mapModel;

		loadedRegistration = mapModel.addMapModelChangedHandler(new MapModelChangedHandler() {

			public void onMapModelChanged(MapModelChangedEvent event) {
				initialize();
			}
		});

	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Render the legend. This triggers a complete redraw.
	 */
	public void render() {
		removeMembers(getMembers());
		// Then go over all layers, to draw styles:
		for (Layer<?> layer : mapModel.getLayers()) {
			if (staticLegend || layer.isShowing()) {
				// Go over every truly visible layer:
				if (layer instanceof VectorLayer) {
					ClientVectorLayerInfo layerInfo = ((VectorLayer) layer).getLayerInfo();

					// For vector layer; loop over the style definitions:
					UserStyleInfo userStyle = layerInfo.getNamedStyleInfo().getUserStyle();
					FeatureTypeStyleInfo info = userStyle.getFeatureTypeStyleList().get(0);
					for (int i = 0; i < info.getRuleList().size(); i++) {
						RuleInfo rule = info.getRuleList().get(i);
						// use title if present, name if not
						String title = (rule.getTitle() != null ? rule.getTitle() : rule.getName());
						// fall back to style name
						if (title == null) {
							title = layerInfo.getNamedStyleInfo().getName();
						}
						addVector((VectorLayer) layer, i, title);
					}
				} else if (layer instanceof RasterLayer) {
					addRaster((RasterLayer) layer);
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public MapModel getMapModel() {
		return mapModel;
	}

	/**
	 * If staticLegend is true all layers will always be shown, not just when they are visible.
	 * 
	 * @return if this is a static Legend
	 * @since 1.10.0
	 */
	public boolean isStaticLegend() {
		return staticLegend;
	}

	/**
	 * Set if this legend should be static or dynamic.
	 * 
	 * @param staticLegend should legend be static?
	 * @since 1.10.0
	 */
	public void setStaticLegend(boolean staticLegend) {
		this.staticLegend = staticLegend;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------


	private void addVector(VectorLayer layer, int ruleIndex, String title) {
		HLayout layout = new HLayout(WidgetLayout.marginSmall);
		layout.setHeight(WidgetLayout.legendVectorRowHeight);
		UrlBuilder urlBuilder = new UrlBuilder(Geomajas.getDispatcherUrl());
		urlBuilder.addPath("legendgraphic");
		urlBuilder.addPath(layer.getServerLayerId());
		urlBuilder.addPath(layer.getLayerInfo().getNamedStyleInfo().getName());
		urlBuilder.addPath(ruleIndex + ".png");
		Img icon = new Img(urlBuilder.toString(), WidgetLayout.legendRasterIconWidth,
				WidgetLayout.legendRasterIconHeight);
		icon.setLayoutAlign(Alignment.LEFT);
		layout.addMember(icon);
		Label label = new Label(title);
		label.setWrap(false);
		label.setLayoutAlign(Alignment.LEFT);
		layout.addMember(label);
		addMember(layout);
	}

	private void addRaster(RasterLayer layer) {
		HLayout layout = new HLayout(WidgetLayout.marginSmall);
		layout.setHeight(WidgetLayout.legendRasterRowHeight);
		UrlBuilder urlBuilder = new UrlBuilder(Geomajas.getDispatcherUrl());
		urlBuilder.addPath("legendgraphic");
		urlBuilder.addPath(layer.getServerLayerId() + ".png");
		Img icon =
				new Img(urlBuilder.toString(), WidgetLayout.legendRasterIconWidth, WidgetLayout.legendRasterIconHeight);
		icon.setLayoutAlign(Alignment.LEFT);
		layout.addMember(icon);
		Label label  = new Label(layer.getLabel());
		label.setWrap(false);
		label.setLayoutAlign(Alignment.LEFT);
		layout.addMember(label);		
		addMember(layout);
	}

	/** Called when the MapModel configuration has been loaded. */
	private void initialize() {
		registrations.clear();
		for (Layer<?> layer : mapModel.getLayers()) {
			registrations.add(layer.addLayerChangedHandler(new LayerChangedHandler() {

				public void onLabelChange(LayerLabeledEvent event) {
				}

				public void onVisibleChange(LayerShownEvent event) {
					render();
				}

			}));
			registrations.add(layer.addLayerStyleChangedHandler(new LayerStyleChangedHandler() {

				public void onLayerStyleChange(LayerStyleChangeEvent event) {
					render();
				}
			}));
		}
		for (final VectorLayer layer : mapModel.getVectorLayers()) {
			layer.addLayerFilteredHandler(new LayerFilteredHandler() {

				public void onFilterChange(LayerFilteredEvent event) {
					render();
				}
			});
		}
		render();
	}

	/** Remove all handlers on unload. */
	protected void onUnload() {
		if (registrations != null) {
			for (HandlerRegistration registration : registrations) {
				registration.removeHandler();
			}
		}
		loadedRegistration.removeHandler();
		super.onUnload();
	}
}