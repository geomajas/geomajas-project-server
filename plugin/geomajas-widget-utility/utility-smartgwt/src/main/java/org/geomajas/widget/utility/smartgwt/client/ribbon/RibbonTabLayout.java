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

package org.geomajas.widget.utility.smartgwt.client.ribbon;

import org.geomajas.gwt.client.service.ConfigurationService;
import org.geomajas.gwt.client.service.WidgetConfigurationCallback;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.utility.client.ribbon.RibbonBar;
import org.geomajas.widget.utility.server.configuration.RibbonBarInfo;
import org.geomajas.widget.utility.server.configuration.RibbonInfo;

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

		ConfigurationService.getApplicationWidgetInfo(application, beanId,
				new WidgetConfigurationCallback<RibbonInfo>() {

					public void execute(RibbonInfo ribbonInfo) {
						for (RibbonBarInfo tabInfo : ribbonInfo.getTabs()) {
							RibbonBarLayout ribbon = new RibbonBarLayout(tabInfo, mapWidget);
							ribbon.setBorder("0px");
							Tab tab = new Tab(tabInfo.getTitle());
							tab.setPane(ribbon);
							tabs.addTab(tab);
						}
					}
				});
	}

	public RibbonBar getRibbonBar(int index) {
		return (RibbonBarLayout) tabs.getTab(index).getPane();
	}
}