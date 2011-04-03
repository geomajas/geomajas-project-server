/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.advancedviews.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.advancedviews.client.util.WidgetInfoHelper;
import org.geomajas.widget.advancedviews.configuration.client.ThemesInfo;
import org.geomajas.widget.advancedviews.configuration.client.themes.LayerConfig;
import org.geomajas.widget.advancedviews.configuration.client.themes.RangeConfig;
import org.geomajas.widget.advancedviews.configuration.client.themes.ViewConfig;

import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Oliver May
 *
 */
public class ThemeWidget extends Canvas implements MapViewChangedHandler {

	private static final String THEME_RADIO_GROUP = "themeRadioGroup";

	private static final int IMAGE_SIZE = 48;

	private static final int ROW_SIZE = IMAGE_SIZE + 8;

	protected MapWidget mapWidget;

	protected boolean initialized;

	protected ThemesInfo themeInfo;

	protected ViewConfigItem activeViewConfig;

	protected List<ViewConfigItem> viewConfigItems = new ArrayList<ThemeWidget.ViewConfigItem>();

	public ThemeWidget(MapWidget mapWidget) {
		super();
//		setHeight100();
		this.mapWidget = mapWidget;

		mapWidget.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				if (!initialized) {
					initialize();
				}
				initialized = true;
			}
		});
		mapWidget.getMapModel().getMapView().addMapViewChangedHandler(this);

	}

	protected void initialize() {
		themeInfo = WidgetInfoHelper.getClientWidgetInfo(ThemesInfo.IDENTIFIER, mapWidget);
		buildWidget();
	}

	protected void buildWidget() {
		VLayout vLayout = new VLayout();
		vLayout.setWidth100();
		vLayout.setMembersMargin(5);
		for (ViewConfig viewConfig : themeInfo.getThemeConfigs()) {

			RangeConfig rangeConfig = getRangeConfigForCurrentScale(viewConfig, mapWidget.getMapModel().getMapView()
					.getCurrentScale());

			HLayout layout = new HLayout();

			layout.setMembersMargin(2);

			final IButton button = new IButton();
			button.setWidth100();
			button.setHeight(ROW_SIZE);
			button.setActionType(SelectionType.RADIO);
			button.setRadioGroup(getID() + THEME_RADIO_GROUP);
			if (rangeConfig != null) {
				button.setIcon("[ISOMORPHIC]/" + rangeConfig.getIcon());
			} else {
				button.setIcon("[ISOMORPHIC]/" + viewConfig.getIcon());
			}
			button.setIconWidth(IMAGE_SIZE);
			button.setIconHeight(IMAGE_SIZE);
			button.setTitle(viewConfig.getTitle());
			button.setIconAlign("left");

			button.setTooltip(viewConfig.getDescription());
			
			final ViewConfigItem item = new ViewConfigItem();
			item.setViewConfig(viewConfig);
			item.setButton(button);

			button.addClickHandler(new ClickHandler() {
				
				public void onClick(ClickEvent event) {
					if (button.isSelected()) {
						activateViewConfig(item);
					}
				}
			});
			viewConfigItems.add(item);

			layout.addMember(button);

			vLayout.addMember(layout);

			
		}
		addChild(vLayout);
		markForRedraw();
	}

	protected RangeConfig getRangeConfigForCurrentScale(ViewConfig viewConfig, double scale) {
		for (RangeConfig config : viewConfig.getRangeConfigs()) {
			//FIXME: We convert pixelperunit, but this should be done elsewhere.
			if (config.getMaximumScale().getPixelPerUnit() == 0) {
				config.getMaximumScale().setPixelPerUnit(config.getMaximumScale().getNumerator() / 
						(config.getMaximumScale().getDenominator() * mapWidget.getPixelPerUnit()));
			}
			if (config.getMinimumScale().getPixelPerUnit() == 0) {
				config.getMinimumScale().setPixelPerUnit(config.getMinimumScale().getNumerator() / 
						(config.getMinimumScale().getDenominator() * mapWidget.getPixelPerUnit()));
			}
			double scaleMax = config.getMaximumScale().getPixelPerUnit();
			double scaleMin = config.getMinimumScale().getPixelPerUnit();
			if (scale <= scaleMax &&
					scale >= scaleMin) {
				return config;
			}
		}
		return null;
	}

	protected void activateViewConfig(ViewConfigItem viewConfig) {
			setActiveViewConfig(viewConfig);
			renderViewConfig(viewConfig.getViewConfig());
	}

	protected ViewConfigItem getActiveViewConfig() {
		return activeViewConfig;
	}

	protected void setActiveViewConfig(ViewConfigItem viewConfig) {
		this.activeViewConfig = viewConfig;
	}

	protected void renderViewConfig(ViewConfig viewConfig) {
		RangeConfig config = getRangeConfigForCurrentScale(viewConfig, 
				mapWidget.getMapModel().getMapView().getCurrentScale());
		
		for (LayerConfig layerConfig : config.getLayerConfigs()) {
			Layer<?> layer = mapWidget.getMapModel().getLayer(layerConfig.getLayer().getId());
			
			layer.setVisible(layerConfig.isVisible());
			if (layer instanceof RasterLayer) {
				((RasterLayer) layer).setOpacity(layerConfig.getTransparency());
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.geomajas.gwt.client.map.event.MapViewChangedHandler#onMapViewChanged(org.geomajas.gwt.client.map.event.
	 * MapViewChangedEvent)
	 */
	public void onMapViewChanged(MapViewChangedEvent event) {
		if (null != activeViewConfig) {
			renderViewConfig(activeViewConfig.getViewConfig());
		}
	}


	/**
	 * Model for Viewconfig.
	 * @author Oliver May
	 *
	 */
	protected class ViewConfigItem {
		protected ViewConfig viewConfig;
		protected IButton button;

		public ViewConfig getViewConfig() {
			return viewConfig;
		}

		public void setViewConfig(ViewConfig viewConfig) {
			this.viewConfig = viewConfig;
		}

		public IButton getButton() {
			return button;
		}

		public void setButton(IButton button) {
			this.button = button;
		}
	}
}
