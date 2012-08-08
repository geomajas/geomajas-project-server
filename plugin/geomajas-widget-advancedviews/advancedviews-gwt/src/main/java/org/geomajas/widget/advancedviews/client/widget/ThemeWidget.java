/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.advancedviews.client.widget;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.advancedviews.configuration.client.themes.RangeConfig;
import org.geomajas.widget.advancedviews.configuration.client.themes.ViewConfig;

import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Oliver May
 *
 */
public class ThemeWidget extends AbstractThemeWidget {

	private static final String THEME_RADIO_GROUP = "themeRadioGroup";

	private static final int IMAGE_SIZE = 48;

	private static final int ROW_SIZE = IMAGE_SIZE + 8;

	public ThemeWidget(MapWidget mapWidget) {
		super(mapWidget);
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
				button.setIcon(Geomajas.getDispatcherUrl() + rangeConfig.getIcon());
			} else {
				button.setIcon(Geomajas.getDispatcherUrl() + viewConfig.getIcon());
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
					if (null != getActiveViewConfig() && getActiveViewConfig().equals(item)) {
						button.setSelected(false);
						activateViewConfig(null);
					}
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

	/* (non-Javadoc)
	 * @see org.geomajas.gwt.client.map.event.MapViewChangedHandler#onMapViewChanged(org.geomajas.gwt.client.map.event.
	 * MapViewChangedEvent)
	 */
	public void onMapViewChanged(MapViewChangedEvent event) {
		super.onMapViewChanged(event);
		if (null != activeViewConfig && !event.isSameScaleLevel()) {
			resetIcons();
		}
	}

	/**
	 * Reset all icons.
	 */
	protected void resetIcons() {
		for (ViewConfigItem item : viewConfigItems) {
			RangeConfig config = getRangeConfigForCurrentScale(item.getViewConfig(),
					mapWidget.getMapModel().getMapView().getCurrentScale());
			if (null != config && null != config.getIcon()) {
				item.getButton().setIcon(Geomajas.getDispatcherUrl() + config.getIcon());
			}
		}
	}
}
