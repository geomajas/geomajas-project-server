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

import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.advancedviews.client.AdvancedViewsMessages;
import org.geomajas.widget.advancedviews.configuration.client.themes.RangeConfig;
import org.geomajas.widget.advancedviews.configuration.client.themes.ViewConfig;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Show a single button with the currently selected theme.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class ExpandingThemeWidget extends AbstractThemeWidget {

	private static final int IMAGE_SIZE = 48;

	private static final int BUTTON_SIZE = IMAGE_SIZE + 8;

	private static final String NOTHEME_ICON = "[ISOMORPHIC]/geomajas/widget/themewidget/nothemeselected.png";
	private static final String BACKGROUND_IMG = "[ISOMORPHIC]/geomajas/widget/themewidget/background.png";

	private AdvancedViewsMessages messages = GWT.create(AdvancedViewsMessages.class);

	protected IButton disabledBtn;

	protected IButton masterBtn;

	protected VLayout panel;
	
	public ExpandingThemeWidget(MapWidget mapWidget) {
		super(mapWidget);
		setWidth(BUTTON_SIZE);
		setHeight(BUTTON_SIZE);
		setMargin(5);
	}

	protected void buildWidget() {
		disabledBtn = createButton(NOTHEME_ICON, messages.expandingThemeWidgetNoThemeSelected());
		disabledBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				activateViewConfig(null);
			}
		});

		for (ViewConfig viewConfig : themeInfo.getThemeConfigs()) {
			RangeConfig rangeConfig = getRangeConfigForCurrentScale(viewConfig, mapWidget.getMapModel().getMapView()
					.getCurrentScale());

			String icon;
			if (rangeConfig != null) {
				icon = "[ISOMORPHIC]/" + rangeConfig.getIcon();
			} else {
				icon = "[ISOMORPHIC]/" + viewConfig.getIcon();
			}
			final IButton button = createButton(icon, viewConfig.getDescription());

			final ViewConfigItem item = new ViewConfigItem();
			item.setViewConfig(viewConfig);
			item.setButton(button);

			button.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					activateViewConfig(item);
				}
			});
			viewConfigItems.add(item);
		}

		masterBtn = createButton(disabledBtn.getIcon(), disabledBtn.getTooltip());
		masterBtn.setShowShadow(true);
		masterBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (panel != null && panel.isVisible()) {
					hidePanel();
				} else {
					showPanel();
				}
			}
		});

		addChild(masterBtn);
		markForRedraw();
	}

	private void showPanel() {
		if (panel == null) {
			panel = new VLayout(8);
			panel.setPadding(5);
			panel.setAutoHeight();
			panel.setAutoWidth();
			panel.setShowShadow(true);
			panel.setBackgroundImage(BACKGROUND_IMG);
			panel.hide();
			panel.draw();
		}

		// -- clear but not "clear"
		for (ViewConfigItem item : viewConfigItems) {
			if (panel.contains(item.getButton())) {
				panel.removeMember(item.getButton());
			}
		}
		if (panel.contains(disabledBtn)) {
			panel.removeMember(disabledBtn);
		}
		
		// -- add required buttons
		for (ViewConfigItem item : viewConfigItems) {
			if (!item.equals(activeViewConfig)) {
				panel.addMember(item.getButton());
			}
		}
		if (activeViewConfig != null) {
			panel.addMember(disabledBtn);
		}
		
		int left = this.getAbsoluteLeft();
		int top = this.getAbsoluteTop();
		int height = viewConfigItems.size() * BUTTON_SIZE + (viewConfigItems.size() - 1) * 10 + 10;
		panel.moveTo(left, top - height);
		panel.animateShow(AnimationEffect.FADE);
	}
	
	private void hidePanel() {
		if (panel != null) {
			panel.animateHide(AnimationEffect.FADE);
		}
	}
	
	private IButton createButton(String icon, String tooltip) {
		IButton button = new IButton();
		button.setWidth(BUTTON_SIZE);
		button.setHeight(BUTTON_SIZE);
		button.setIconWidth(IMAGE_SIZE);
		button.setIconHeight(IMAGE_SIZE);
		button.setIcon(icon);
		button.setShowDownIcon(false);
		button.setTooltip(tooltip);
		return button;
	}

	private void setButton(IButton button) {
		masterBtn.setIcon(button.getIcon());
		masterBtn.setTooltip(button.getTooltip());
	}

	protected void activateViewConfig(ViewConfigItem viewConfig) {
		super.activateViewConfig(viewConfig);
		if (null != viewConfig && null != viewConfig.getViewConfig()) {
			setButton(viewConfig.getButton());
		} else {
			setButton(disabledBtn);
		}
		hidePanel();
	}

	public void onMapViewChanged(MapViewChangedEvent event) {
		super.onMapViewChanged(event);
		if (null != activeViewConfig && !event.isSameScaleLevel()) {
			resetIcons();
		}
	}

	/**
	 * Reset all icons
	 */
	protected void resetIcons() {
		for (ViewConfigItem item : viewConfigItems) {
			RangeConfig config = getRangeConfigForCurrentScale(item.getViewConfig(), mapWidget.getMapModel()
					.getMapView().getCurrentScale());
			if (null != config && null != config.getIcon()) {
				item.getButton().setIcon("[ISOMORPHIC]/" + config.getIcon());
			}
		}

		if (activeViewConfig != null) {
			setButton(activeViewConfig.getButton());
		}
	}
}
