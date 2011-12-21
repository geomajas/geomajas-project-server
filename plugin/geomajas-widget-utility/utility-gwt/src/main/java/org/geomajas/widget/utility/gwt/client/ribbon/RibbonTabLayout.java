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

package org.geomajas.widget.utility.gwt.client.ribbon;

import org.geomajas.gwt.client.service.ClientConfigurationService;
import org.geomajas.gwt.client.service.WidgetConfigurationCallback;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.utility.common.client.ribbon.RibbonBar;
import org.geomajas.widget.utility.configuration.RibbonBarInfo;
import org.geomajas.widget.utility.configuration.RibbonInfo;
import org.geomajas.widget.utility.gwt.client.util.GuwLayout;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * A tab layout for ribbon bars.
 * 
 * @author Pieter De Graef
 */
public class RibbonTabLayout extends VLayout {

	private TabSet tabs;
	private Integer ribbonBarMembersMargin;

	/**
	 * Create a ribbon bar widget using a back-end spring bean identifier and a map.
	 * 
	 * @param mapWidget
	 *            The map widget onto which many actions in this ribbon apply.
	 * @param application
	 *            The name of the application wherein to search for the ribbon configuration.
	 * @param beanId
	 *            A unique spring bean identifier for a bean of class {@link RibbonInfo}. This configuration is then
	 *            fetched and applied.
	 */
	public RibbonTabLayout(final MapWidget mapWidget, String application, String beanId) {
		tabs = new TabSet();
		tabs.setPaneMargin(0);
		addMember(tabs);

		ClientConfigurationService.getApplicationWidgetInfo(application, beanId,
				new WidgetConfigurationCallback<RibbonInfo>() {

					public void execute(RibbonInfo ribbonInfo) {
						for (RibbonBarInfo tabInfo : ribbonInfo.getTabs()) {
							RibbonBarLayout ribbon = new RibbonBarLayout(tabInfo, mapWidget);
							if (null != ribbonBarMembersMargin) {
								ribbon.setMembersMargin(ribbonBarMembersMargin);
							}
							ribbon.setStyleName(getStyleName()); // in case the styleName has been set already.
							ribbon.setBorder("0px");
							Tab tab = new Tab(tabInfo.getTitle());
							tab.setTitleStyle(getStyleName() + "TabTitle"); // not working
							tab.setPane(ribbon);
							tabs.addTab(tab);
							tabs.setStyleName(getStyleName()+ "TabSet"); // tabs and panes
//							tabs.setStylePrimaryName(getStyleName()+ "TabSet"); // DO NOT USE
							tabs.setPaneContainerClassName(getStyleName() + "TabContainer"); // not working
						}
					}
				});
	}

	public RibbonBar getRibbonBar(int index) {
		return (RibbonBarLayout) tabs.getTab(index).getPane();
	}
	
	@Override
	public void setStyleName(String styleName) {
		super.setStyleName(styleName);
		for (int i = 0 ; i < tabs.getNumTabs() ; i++) {
			getRibbonBar(i).asWidget().setStyleName(getStyleName());
		}
	}
	/**
	 * If set, it overrides the {@link GuwLayout#ribbonBarInternalMargin} set in {@link RibbonBarLayout#RibbonBarLayout()}. 
	 */
	public void setRibbonBarMembersMargin(Integer ribbonBarMembersMargin) {
		this.ribbonBarMembersMargin = ribbonBarMembersMargin;
	}
	
	
}