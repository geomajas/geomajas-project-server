/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.plugin.printing.client.action.toolbar;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.printing.client.PrintingMessages;
import org.geomajas.plugin.printing.client.action.widget.PrintPreferencesWindow;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Action to create a default PDF print of the map.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PrintAction extends ToolbarAction implements ConfigurableAction {

	private PrintingMessages messages = GWT.create(PrintingMessages.class);

	private MapWidget mapWidget;

	public PrintAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/geomajas/osgeo/print.png", null);
		this.mapWidget = mapWidget;
		setTooltip(messages.printBtnTitle());
	}
	
	public void onClick(ClickEvent event) {
		PrintPreferencesWindow window = new PrintPreferencesWindow(mapWidget);
		window.centerInPage();
		window.show();
	}


//	public void onClick(ClickEvent event) {
//		PrintGetTemplateRequest request = new PrintGetTemplateRequest();
//		DefaultTemplateBuilder builder = new DefaultTemplateBuilder();
//		builder.setApplicationId(mapWidget.getApplicationId());
//		builder.setMapModel(mapWidget.getMapModel());
//		builder.setMarginX(20);
//		builder.setMarginY(20);
//		builder.setPageHeight(595);
//		builder.setPageWidth(842);
//		builder.setTitleText(mapWidget.getMapModel().getMapInfo().getId());
//		builder.setWithArrow(true);
//		builder.setWithDate(true);
//		builder.setRasterDpi(150);
//		PrintTemplateInfo template = builder.buildTemplate();
//		request.setTemplate(template);
//		final GwtCommand command = new GwtCommand("command.print.GetTemplate");
//		command.setCommandRequest(request);
//		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {
//
//			public void execute(CommandResponse r) {
//				if (r instanceof PrintGetTemplateResponse) {
//					PrintGetTemplateResponse response = (PrintGetTemplateResponse) r;
//					GWT.log("" + response.getDocumentId());
//					UrlExtender url = new UrlExtender(GWT.getHostPageBaseURL());
//					url.addPath("d/printing").addParameter("documentId", response.getDocumentId());
//					url.addParameter("download", "0").addParameter("name", "test.pdf");
//					url.addParameter("userToken", command.getUserToken());
//					Window.open(url.toString(), "_blank", null);
//				}
//			}
//		});
//	}


	public void configure(String key, String value) {
	}


}
