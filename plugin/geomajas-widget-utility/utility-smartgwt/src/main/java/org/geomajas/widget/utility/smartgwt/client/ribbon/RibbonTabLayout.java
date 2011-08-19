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

import org.geomajas.command.dto.GetClientUserDataRequest;
import org.geomajas.command.dto.GetClientUserDataResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
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

	private static final String RIBBON_INFO_CLASS = "org.geomajas.widget.utility.server.configuration.RibbonInfo";

	private TabSet tabs;

	/**
	 * Create a ribbon bar widget using a back-end spring bean identifier and a map.
	 * 
	 * @param mapWidget
	 *            The map widget onto which many actions in this ribbon apply.
	 * @param beanId
	 *            A unique spring bean identifier for a bean of class {@link RibbonInfo}. This configuration is then
	 *            fetched and applied.
	 */
	public RibbonTabLayout(final MapWidget mapWidget, String beanId) {
		tabs = new TabSet();
		tabs.setPaneMargin(0);
		addMember(tabs);

		GetClientUserDataRequest request = new GetClientUserDataRequest();
		request.setIdentifier(beanId);
		request.setClassName(RIBBON_INFO_CLASS);
		GwtCommand command = new GwtCommand(GetClientUserDataRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback<GetClientUserDataResponse>() {

			public void execute(GetClientUserDataResponse response) {
				RibbonInfo info = (RibbonInfo) response.getInformation();
				for (RibbonBarInfo tabInfo : info.getTabs()) {
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