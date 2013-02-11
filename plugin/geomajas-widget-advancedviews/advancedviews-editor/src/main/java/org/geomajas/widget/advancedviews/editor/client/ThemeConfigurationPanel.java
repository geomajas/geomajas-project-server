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
package org.geomajas.widget.advancedviews.editor.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.widget.advancedviews.configuration.client.ThemesInfo;
import org.geomajas.widget.advancedviews.configuration.client.themes.LayerConfig;
import org.geomajas.widget.advancedviews.configuration.client.themes.RangeConfig;
import org.geomajas.widget.advancedviews.configuration.client.themes.ViewConfig;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Panel to allow theme configuration.
 * 
 * @author Oliver May
 * 
 */
public class ThemeConfigurationPanel extends VLayout {

	private LayerConfigPanel layerConfigPanel;

	private RangeConfigPanel rangeConfigPanel;

	private ViewConfigPanel viewConfigPanel;

	private ThemeConfigPanel themeConfigPanel;

	private State state = new State();

	private ClientMapInfo mainMap;

	public static final int LEFT_WIDTH = 400;

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	public ThemeConfigurationPanel() {
		super(5);

		BreadCrumb breadCrumb = new BreadCrumb();

		layerConfigPanel = new LayerConfigPanel(this);
		rangeConfigPanel = new RangeConfigPanel(this);
		viewConfigPanel = new ViewConfigPanel(this);
		themeConfigPanel = new ThemeConfigPanel(this);

		addMember(breadCrumb);
		addMember(themeConfigPanel);
		addMember(viewConfigPanel);
		addMember(rangeConfigPanel);
		addMember(layerConfigPanel);

		hideAllPanels();

	}

	public State getState() {
		return state;
	}
	
	public void selectThemeConfig(ThemesInfo themesInfo) {
		hideAllPanels();
		state.setThemesInfo(themesInfo);
		themeConfigPanel.update(state);
		themeConfigPanel.show();
	}

	public void selectLayerConfig(LayerConfig layerConfig) {
		hideAllPanels();
		state.setLayerConfig(layerConfig);
		layerConfigPanel.update(state);
		layerConfigPanel.show();
	}

	public void selectRangeConfig(RangeConfig rangeConfig) {
		hideAllPanels();
		state.setRangeConfig(rangeConfig);
		rangeConfigPanel.update(state);
		rangeConfigPanel.show();
	}

	public void selectViewConfig(ViewConfig viewConfig) {
		hideAllPanels();
		state.setViewConfig(viewConfig);
		viewConfigPanel.update(state);
		viewConfigPanel.show();
	}

	public void setThemeConfig(ThemesInfo themesInfo) {
		selectThemeConfig(themesInfo);
	}

	public ThemesInfo getThemeConfig() {
		return state.getThemesInfo();
	}
	
	public void setMainMap(ClientMapInfo mainMap) {
		this.mainMap = mainMap;
	}

	public ClientMapInfo getMainMap() {
		return mainMap;
	}

	private void hideAllPanels() {
		themeConfigPanel.hide();
		viewConfigPanel.hide();
		rangeConfigPanel.hide();
		layerConfigPanel.hide();
	}

	/**
	 * Helper class to provide access to the state of the themeConfigurationPanel. By calling the setters the state will
	 * change, if a high level such as ThemesInfo is set, all deeper levels will be removed. Following level order is in
	 * place: ThemesInfo > ViewConfig > RangeConfig > LayerConfig.
	 * 
	 * @author Oliver May
	 * 
	 */
	public class State {

		private ThemesInfo themesInfo;

		private ViewConfig viewConfig;

		private RangeConfig rangeConfig;

		private LayerConfig layerConfig;

		private List<StateChangedHandler> handlers = new ArrayList<StateChangedHandler>();

		/**
		 * @return the themesInfo
		 */
		public ThemesInfo getThemesInfo() {
			return themesInfo;
		}

		/**
		 * @return the viewConfig
		 */
		public ViewConfig getViewConfig() {
			return viewConfig;
		}

		/**
		 * @return the rangeConfig
		 */
		public RangeConfig getRangeConfig() {
			return rangeConfig;
		}

		/**
		 * @return the layerConfig
		 */
		public LayerConfig getLayerConfig() {
			return layerConfig;
		}

		/**
		 * Add a state changed handler, called when the state is changed..
		 */
		public void addStateChangedHandler(StateChangedHandler handler) {
			handlers.add(handler);
		}

		/**
		 * @param themesInfo
		 *            the themesInfo to set
		 */
		private void setThemesInfo(ThemesInfo themesInfo) {
			this.themesInfo = themesInfo;
			setViewConfig(null);
		}

		/**
		 * @param viewConfig
		 *            the viewConfig to set
		 */
		private void setViewConfig(ViewConfig viewConfig) {
			this.viewConfig = viewConfig;
			setRangeConfig(null);
		}

		/**
		 * @param rangeConfig
		 *            the rangeConfig to set
		 */
		private void setRangeConfig(RangeConfig rangeConfig) {
			this.rangeConfig = rangeConfig;
			setLayerConfig(null);
		}

		/**
		 * @param layerConfig
		 *            the layerConfig to set
		 */
		private void setLayerConfig(LayerConfig layerConfig) {
			this.layerConfig = layerConfig;
			fireStateChanged();
		}

		private void fireStateChanged() {
			for (StateChangedHandler h : handlers) {
				h.onStateChange(this);
			}
		}

	}

	/**
	 * 
	 * @author Oliver May
	 * 
	 */
	public interface StateChangedHandler {

		void onStateChange(State state);
	}

	/**
	 * 
	 * @author Oliver May
	 * 
	 */
	private final class BreadCrumb extends Layout implements StateChangedHandler {

		private Label themeLink;

		private Label viewLink;

		private Label rangeLink;

		private Label layerLink;

		private Label viewLinkSpacer;

		private Label rangeLinkSpacer;

		private Label layerLinkSpacer;

		private BreadCrumb() {
			setHeight(10);
			HLayout layout = new HLayout(5);
			layout.setHeight(10);

			themeLink = new Label(MESSAGES.themeConfigBreadcrumbThemeConfig());
			themeLink.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					selectThemeConfig(state.getThemesInfo());
				}
			});
			themeLink.setWidth(1);
			themeLink.setWrap(false);
			layout.addMember(themeLink);

			viewLinkSpacer = new Label(">");
			viewLinkSpacer.setWidth(1);
			layout.addMember(viewLinkSpacer);

			viewLink = new Label(MESSAGES.themeConfigBreadcrumbViewConfig());
			viewLink.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					selectViewConfig(state.getViewConfig());
				}
			});
			viewLink.setWidth(1);
			viewLink.setWrap(false);
			layout.addMember(viewLink);

			rangeLinkSpacer = new Label(">");
			rangeLinkSpacer.setWidth(1);
			layout.addMember(rangeLinkSpacer);

			rangeLink = new Label(MESSAGES.themeConfigBreadcrumbRangeConfig());
			rangeLink.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					selectRangeConfig(state.getRangeConfig());
				}
			});
			rangeLink.setWidth(1);
			rangeLink.setWrap(false);
			layout.addMember(rangeLink);

			layerLinkSpacer = new Label(">");
			layerLinkSpacer.setWidth(1);
			layout.addMember(layerLinkSpacer);

			layerLink = new Label(MESSAGES.themeConfigBreadcrumbLayerConfig());
			layerLink.setWidth(1);
			layerLink.setWrap(false);
			layout.addMember(layerLink);

			state.addStateChangedHandler(this);

			addMember(layout);
		}

		public void onStateChange(State state) {
			// if (!isVisible()) {
			// return;
			// }
			if (state.getLayerConfig() != null) {
				layerLinkSpacer.show();
				layerLink.show();
				layerLink.setContents(state.getLayerConfig().getLayer().getLabel());
			} else {
				layerLinkSpacer.hide();
				layerLink.hide();
			}

			if (state.getRangeConfig() != null) {
				rangeLinkSpacer.show();
				rangeLink.show();
				rangeLink.setContents("1:"
						+ NumberFormat.getDecimalFormat().format(
								state.getRangeConfig().getMinimumScale().getDenominator())
						+ " - "
						+ "1:"
						+ NumberFormat.getDecimalFormat().format(
								state.getRangeConfig().getMaximumScale().getDenominator()));
			} else {
				rangeLinkSpacer.hide();
				rangeLink.hide();
			}

			if (state.getViewConfig() != null) {
				viewLinkSpacer.show();
				viewLink.show();
				viewLink.setContents(state.getViewConfig().getTitle());
			} else {
				viewLinkSpacer.hide();
				viewLink.hide();
			}

			if (state.getThemesInfo() != null) {
				themeLink.show();
				themeLink.setContents(MESSAGES.themeConfigBreadcrumbThemeConfig());
				// themeLink.setStyleName("nowrap", true);
				// themeLink.setWidth("50");
			} else {
				themeLink.hide();
			}

		}
	}

}
