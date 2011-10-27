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
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.core.Rectangle;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Show a single button with the currently selected theme.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class ExpandingThemeWidget extends AbstractThemeWidget {

	private static final int IMAGE_SIZE = 48;

	private static final int BUTTON_SIZE = IMAGE_SIZE + 1;

	private static final String NOTHEME_ICON = "[ISOMORPHIC]/geomajas/widget/themewidget/nothemeselected.png";
	private static final String BACKGROUND_IMG = "[ISOMORPHIC]/geomajas/widget/themewidget/background.png";

	private static final String DESCRIPTION_HOVER_STYLENAME = "themeWidgetDescriptionHover";
	private static final String DESCRIPTION_STYLENAME = "themeWidgetDescription";

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
		disabledBtn = createButton(NOTHEME_ICON, messages.expandingThemeWidgetNoThemeSelected(), new ClickHandler() {
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

			final ViewConfigItem item = new ViewConfigItem();
			item.setViewConfig(viewConfig);

			final IButton button = createButton(icon, viewConfig.getDescription(), new ClickHandler() {
				public void onClick(ClickEvent event) {
					activateViewConfig(item);
				}
			});

			item.setButton(button);

			viewConfigItems.add(item);
		}

		masterBtn = createButton(disabledBtn.getIcon(), messages.expandingThemeWidgetTooltip(), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (panel != null && panel.isVisible()) {
					hidePanel();
				} else {
					showPanel();
				}
			}
		});
		masterBtn.setShowShadow(true);
		setMasterButton(disabledBtn);
		addChild(masterBtn);
		markForRedraw();
	}

	private void showPanel() {
		if (panel == null) {
			panel = new VLayout(themeInfo.isShowDescription() ? 0 : 8);
			panel.setPadding(5);
			panel.setAutoHeight();
			panel.setAutoWidth();
			panel.setShowShadow(true);
			panel.setBackgroundImage(BACKGROUND_IMG);
			panel.addMouseOutHandler(new MouseOutHandler() {
				public void onMouseOut(MouseOutEvent event) {
					Rectangle rect = panel.getRect();
					int x = event.getX();
					int y = event.getY();
					if (x < rect.getLeft() || x > rect.getWidth() + rect.getLeft() || y < rect.getTop() || 
							y > rect.getTop() + rect.getHeight()) {
						panel.animateHide(AnimationEffect.FADE);
					}
				}
			});

			panel.hide();
			panel.draw();
		}

		// -- clear but not "clear"
		for (ViewConfigItem item : viewConfigItems) {
			if (panel.contains(getThemeComponent(item.getButton()))) {
				panel.removeMember(getThemeComponent(item.getButton()));
			}
		}
		if (panel.contains(getThemeComponent(disabledBtn))) {
			panel.removeMember(getThemeComponent(disabledBtn));
		}

		// -- add required buttons
		for (ViewConfigItem item : viewConfigItems) {
			if (!item.equals(activeViewConfig)) {
				panel.addMember(getThemeComponent(item.getButton()));
			}
		}
		if (activeViewConfig != null) {
			panel.addMember(getThemeComponent(disabledBtn));
		}

		int left = (themeInfo.isShowDescription() ? this.getAbsoluteLeft() - 5 : this.getAbsoluteLeft());
		int top = this.getAbsoluteTop();
		int height = viewConfigItems.size() * BUTTON_SIZE + 2 /* border */ + (viewConfigItems.size() - 1) * 10;
		height += (themeInfo.isShowDescription() ? 15 : 10);
		panel.moveTo(left, top - height);
		panel.animateShow(AnimationEffect.FADE);
	}

	private Canvas getThemeComponent(IButton button) {
		if (button instanceof DescriptionIButton) {
			return ((DescriptionIButton) button).getCanvas();
		} else {
			return button;
		}
	}

	private void hidePanel() {
		if (panel != null) {
			panel.animateHide(AnimationEffect.FADE);
		}
	}

	private IButton createButton(String icon, String tooltip, ClickHandler ch) {
		IButton button;
		if (!themeInfo.isShowDescription()) {
			button = new IButton(tooltip);
		} else {
			button = new DescriptionIButton(tooltip);
		}
		button.setWidth(BUTTON_SIZE);
		button.setHeight(BUTTON_SIZE);
		button.setIconWidth(IMAGE_SIZE);
		button.setIconHeight(IMAGE_SIZE);
		button.setIcon(icon);
		button.addClickHandler(ch);

		return button;
	}

	private void setMasterButton(IButton button) {
		masterBtn.setIcon(button.getIcon());
		/*masterBtn.setTooltip(button.getTooltip()); */ 
		masterBtn.setTooltip(messages.expandingThemeWidgetTooltip());
	}

	protected void activateViewConfig(ViewConfigItem viewConfig) {
		super.activateViewConfig(viewConfig);
		if (null != viewConfig && null != viewConfig.getViewConfig()) {
			setMasterButton(viewConfig.getButton());
		} else {
			setMasterButton(disabledBtn);
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
			setMasterButton(activeViewConfig.getButton());
		}
	}

	/**
	 * A Buttonwrapper which adds a descriptive label.
	 * 
	 * @author Kristof Heirwegh
	 */
	private class DescriptionIButton extends IButton {
		private HLayout c;
		private String label;
		private ClickHandler ch;

		public DescriptionIButton(String label) {
			super("Selecteer achtergrondlagen");
			this.label = label;
		}

		public Canvas getCanvas() {
			if (c == null) {
				c = new HLayout(5);
				c.setPadding(5);
				c.setStyleName(DESCRIPTION_STYLENAME);
				c.setLayoutAlign(VerticalAlignment.CENTER);
				Label description = new Label(label);
				description.setBackgroundColor("transparent");
				description.setWidth(themeInfo.getDescriptionWidth());
				description.setHeight(BUTTON_SIZE);
				description.setValign(VerticalAlignment.CENTER);
				c.addMember(this);
				c.addMember(description);
				MouseOverHandler movh = new MouseOverHandler() {
					public void onMouseOver(MouseOverEvent event) {
						c.setStyleName(DESCRIPTION_HOVER_STYLENAME);
					}
				};
				MouseOutHandler mouh = new MouseOutHandler() {
					public void onMouseOut(MouseOutEvent event) {
						c.setStyleName(DESCRIPTION_STYLENAME);
					}
				};
				c.addMouseOutHandler(mouh);
				c.addMouseOverHandler(movh);
				c.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						ch.onClick(event);
					}
				});
			}
			return c;
		}

		@Override
		public HandlerRegistration addClickHandler(ClickHandler handler) {
			ch = handler;
			return super.addClickHandler(handler);
		}
	}
}
