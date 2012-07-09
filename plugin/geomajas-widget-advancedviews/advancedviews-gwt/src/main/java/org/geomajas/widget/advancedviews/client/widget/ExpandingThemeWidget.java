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

import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.advancedviews.client.AdvancedViewsMessages;
import org.geomajas.widget.advancedviews.configuration.client.themes.RangeConfig;
import org.geomajas.widget.advancedviews.configuration.client.themes.ViewConfig;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.core.Rectangle;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Show a single button with the currently selected theme.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class ExpandingThemeWidget extends AbstractThemeWidget {

	private static final String NOTHEME_ICON = "[ISOMORPHIC]/geomajas/widget/themewidget/nothemeselected.png";

	private static final String BACKGROUND_IMG = "[ISOMORPHIC]/geomajas/widget/themewidget/background.png";

	private static final int IMAGE_SIZE = 48;

	private int menuWidth = 325;

	private static final AdvancedViewsMessages MESSAGES = GWT.create(AdvancedViewsMessages.class);

	protected MenuItem disabledBtn;

	protected MenuItem masterBtn;

	protected VLayout panel;

	public ExpandingThemeWidget(MapWidget mapWidget) {
		super(mapWidget);
		setPadding(5);
		setHeight(IMAGE_SIZE + 12);
		setWidth(160);
		setBackgroundImage(BACKGROUND_IMG);
		setShowShadow(true);
		setOverflow(Overflow.HIDDEN);
		setKeepInParentRect(true);
	}

	protected void buildWidget() {
		disabledBtn = new MenuItem(NOTHEME_ICON, MESSAGES.expandingThemeWidgetNoThemeSelected(), null,
				new ClickHandler() {

					public void onClick(ClickEvent event) {
						activateViewConfig(null);
					}
				});

		masterBtn = new MenuItem(disabledBtn.getIcon(), "<b>" + MESSAGES.expandingThemeWidgetTitle() + "</b>", null,
				new ClickHandler() {

					public void onClick(ClickEvent event) {
						if (panel != null && panel.isVisible()) {
							hidePanel();
						} else {
							showPanel();
						}
					}
				});
		if (themeInfo != null && themeInfo.getThemeConfigs() != null) {
			for (ViewConfig viewConfig : themeInfo.getThemeConfigs()) {
				RangeConfig rangeConfig = getRangeConfigForCurrentScale(viewConfig, mapWidget.getMapModel()
						.getMapView().getCurrentScale());

				String icon;
				if (rangeConfig != null) {
					icon = "[ISOMORPHIC]/" + rangeConfig.getIcon();
				} else {
					icon = "[ISOMORPHIC]/" + viewConfig.getIcon();
				}

				final ViewConfigItem item = new ViewConfigItem();
				item.setViewConfig(viewConfig);
				final MenuItem button = new MenuItem(icon, viewConfig.getDescription(), null, new ClickHandler() {

					public void onClick(ClickEvent event) {
						activateViewConfig(item);
					}
				});
				item.setButton(button);
				viewConfigItems.add(item);
			}
		}

		setMasterItem(disabledBtn);
		addChild(masterBtn);
		markForRedraw();
	}

	private void showPanel() {
		if (panel == null) {
			panel = new VLayout(themeInfo.isShowDescription() ? 0 : 8);
			panel.setPadding(5);
			panel.setAutoHeight();
			panel.setWidth(menuWidth);
			panel.setShowShadow(true);
			panel.setBackgroundImage(BACKGROUND_IMG);
			panel.addMouseOutHandler(new MouseOutHandler() {

				public void onMouseOut(MouseOutEvent event) {
					Rectangle rect = panel.getRect();
					int x = event.getX();
					int y = event.getY();
					if (x < rect.getLeft() || x > rect.getWidth() + rect.getLeft() || y < rect.getTop()
							|| y > rect.getTop() + rect.getHeight()) {
						panel.animateHide(AnimationEffect.FADE);
					}
				}
			});

			panel.addMember(createCategory(MESSAGES.expandingThemeWidgetTitle()));
			panel.hide();
			panel.draw();
		}

		// -- clear but not "clear"
		for (ViewConfigItem item : viewConfigItems) {
			if (panel.contains((MenuItem) item.getButton())) {
				panel.removeMember((MenuItem) item.getButton());
			}
		}

		// -- add required buttons
		for (ViewConfigItem item : viewConfigItems) {
			if (!item.equals(activeViewConfig)) {
				panel.addMember((MenuItem) item.getButton());
			}
		}

		int left = (themeInfo.isShowDescription() ? this.getAbsoluteLeft() - 5 : this.getAbsoluteLeft());
		int top = this.getAbsoluteTop();
		int height = viewConfigItems.size() * IMAGE_SIZE + 2 + (viewConfigItems.size() - 1) * 10;
		height += (themeInfo.isShowDescription() ? 15 : 10);
		panel.moveTo(left, top - height);
		panel.animateShow(AnimationEffect.FADE);
	}

	private void hidePanel() {
		if (panel != null) {
			panel.animateHide(AnimationEffect.FADE);
		}
	}

	private void setMasterItem(MenuItem item) {
		masterBtn.setIcon(item.getIcon());
		masterBtn.setTooltip(MESSAGES.expandingThemeWidgetTooltip());
	}

	protected void activateViewConfig(ViewConfigItem viewConfig) {
		super.activateViewConfig(viewConfig);
		if (null != viewConfig && null != viewConfig.getViewConfig()) {
			setMasterItem((MenuItem) viewConfig.getButton());
		} else {
			setMasterItem(disabledBtn);
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
	 * Reset all icons.
	 */
	protected void resetIcons() {
		for (ViewConfigItem item : viewConfigItems) {
			RangeConfig config = getRangeConfigForCurrentScale(item.getViewConfig(), mapWidget.getMapModel()
					.getMapView().getCurrentScale());
			if (null != config && null != config.getIcon()) {
				((MenuItem) item.getButton()).setIcon("[ISOMORPHIC]/" + config.getIcon());
			}
		}

		if (activeViewConfig != null) {
			setMasterItem((MenuItem) activeViewConfig.getButton());
		}
	}

	// ----------------------------------------------------------
	// -- builder methods
	// ----------------------------------------------------------

	public Canvas createWhiteSpace() {
		LayoutSpacer cat = new LayoutSpacer();
		cat.setHeight(5);
		return cat;
	}

	public Canvas createCategory(String title) {
		Label cat = new Label(title);
		cat.setStyleName("themeCategory");
		cat.setWidth100();
		cat.setHeight(20);
		return cat;
	}

	// ----------------------------------------------------------

	/**
	 * Individual item from the list.
	 */
	public static class MenuItem extends HLayout implements Button {

		private static final String DESCRIPTION_HOVER_STYLENAME = "themeWidgetDescriptionHover";

		private static final String DESCRIPTION_STYLENAME = "themeWidgetDescription";

		private final Img img;

		public MenuItem(String imageSrc, String title, String description, ClickHandler handler) {
			super(5);
			setPadding(5);
			setStyleName(DESCRIPTION_STYLENAME);
			setHeight(IMAGE_SIZE + 10);
			setWidth100();
			addClickHandler(handler);
			setCursor(Cursor.HAND);

			// -- image
			img = new Img(imageSrc, IMAGE_SIZE, IMAGE_SIZE);
			img.setCursor(Cursor.HAND);
			img.setImageHeight(IMAGE_SIZE);
			img.setImageWidth(IMAGE_SIZE);
			addMember(img);

			// -- description
			StringBuffer labeltext = new StringBuffer(title);
			if (description != null) {
				labeltext.append("<br />" + description);
			}
			Label desc = new Label(labeltext.toString());
			desc.setCursor(Cursor.HAND);
			desc.setBackgroundColor("transparent");
			desc.setWidth100();
			desc.setHeight100();
			desc.setBackgroundColor("transparent");
			desc.setValign(VerticalAlignment.CENTER);
			addMember(desc);

			// -- events
			MouseOverHandler movh = new MouseOverHandler() {

				public void onMouseOver(MouseOverEvent event) {
					setStyleName(DESCRIPTION_HOVER_STYLENAME);
				}
			};
			MouseOutHandler mouh = new MouseOutHandler() {

				public void onMouseOut(MouseOutEvent event) {
					setStyleName(DESCRIPTION_STYLENAME);
				}
			};

			addMouseOverHandler(movh);
			img.addMouseOverHandler(movh);
			desc.addMouseOverHandler(movh);

			addMouseOutHandler(mouh);
			img.addMouseOutHandler(mouh);
			desc.addMouseOutHandler(mouh);
		}

		public String getIcon() {
			return img.getSrc();
		}

		public void setIcon(String src) {
			img.setSrc(src);
		}
	}

	public int getMenuWidth() {
		return menuWidth;
	}

	public void setMenuWidth(int menuWidth) {
		this.menuWidth = menuWidth;
	}
}
