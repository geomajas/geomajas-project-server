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

package org.geomajas.widget.utility.gwt.client.ribbon;

import org.geomajas.gwt.client.service.ClientConfigurationService;
import org.geomajas.gwt.client.service.WidgetConfigurationCallback;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.utility.common.client.ribbon.RibbonBar;
import org.geomajas.widget.utility.configuration.RibbonBarInfo;
import org.geomajas.widget.utility.configuration.RibbonInfo;

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
							// if no custom style is set, replace smartgwt's default 'normal' with our default 'ribbon'.
							String styleName = !"normal".equals(getStyleName()) ? getStyleName() : "ribbon";
							ribbon.setStyleName(styleName);
							ribbon.setBorder("0px");
							Tab tab = new Tab(tabInfo.getTitle());
							tab.setTitleStyle(styleName + "TabTitle");
							tab.setPane(ribbon);
							tabs.addTab(tab);
							tabs.setStyleName(getStyleName() + "TabSet");
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
	 * <p>Use this method if you want to adjust the margin between groups in the bars, 
	 * <b>only present</b> in the tabs of this RibbonTabLayout.</p>
	 * <p>Give a different value to 
	 * {@link org.geomajas.widget.utility.gwt.client.util.GuwLayout#ribbonBarInternalMargin}, 
	 * if you want to alter the internal margin of <b>all</b> bars within your application.</p>  
	 * <p>If set, the value is used to override the 
	 * {@link org.geomajas.widget.utility.gwt.client.util.GuwLayout#ribbonBarInternalMargin}
	 * set in {@link RibbonBarLayout#RibbonBarLayout()}.</p>
	 */
	public void setRibbonBarMembersMargin(Integer ribbonBarMembersMargin) {
		this.ribbonBarMembersMargin = ribbonBarMembersMargin;
	}

}