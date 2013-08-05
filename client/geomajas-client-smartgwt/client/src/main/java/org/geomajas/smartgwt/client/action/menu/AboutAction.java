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

package org.geomajas.smartgwt.client.action.menu;

import com.google.gwt.core.client.GWT;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.command.dto.CopyrightRequest;
import org.geomajas.command.dto.CopyrightResponse;
import org.geomajas.global.CopyrightInfo;
import org.geomajas.smartgwt.client.Geomajas;
import org.geomajas.smartgwt.client.action.MenuAction;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.smartgwt.client.i18n.GlobalMessages;

import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import org.geomajas.smartgwt.client.util.WidgetLayout;

import java.util.Collection;

/**
 * Menu item that tells the user something about the Geomajas technology.
 * 
 * @author Pieter De Graef
 */
public class AboutAction extends MenuAction {

	private static final GlobalMessages MESSAGES = GWT.create(GlobalMessages.class);
	
	public AboutAction() {
		super(MESSAGES.aboutMenuTitle(), WidgetLayout.iconTips);
	}

	public void onClick(MenuItemClickEvent event) {
		VLayout layout = new VLayout();
		layout.setPadding(WidgetLayout.marginLarge);
		Img logo = new Img(WidgetLayout.aboutGeomajasLogo);
		layout.addMember(logo);
		HTMLFlow flow = new HTMLFlow("<h2>Geomajas " + Geomajas.getVersion() + "</h2>" + "<p>"
				+ MESSAGES.aboutCopyRight() + "</p>" + "<p>" + MESSAGES.aboutVisit()
				+ ": <a href='http://www.geomajas.org/'>http://www.geomajas.org/</a><br />"
				+ MESSAGES.sourceUrl() + ": <a href='http://ci.geomajas.org/svn/'>http://ci.geomajas.org/svn/</a> "
				+ "&amp; <a href='https://svn.geomajas.org/majas/'>https://svn.geomajas.org/majas/</a> </p>");
		layout.addMember(flow);

		final HTMLFlow copyrightWidget = new HTMLFlow("Copyright info");
		layout.addMember(copyrightWidget);
		GwtCommand commandRequest = new GwtCommand(CopyrightRequest.COMMAND);
		commandRequest.setCommandRequest(new EmptyCommandRequest());
		GwtCommandDispatcher.getInstance().execute(commandRequest, new AbstractCommandCallback<CopyrightResponse>() {

			public void execute(CopyrightResponse response) {
				Collection<CopyrightInfo> copyrights = response.getCopyrights();
				StringBuilder sb = new StringBuilder("<h2>");
				sb.append(MESSAGES.copyrightListTitle());
				sb.append("</h2><ul>");
				for (CopyrightInfo copyright : copyrights) {
					sb.append("<li>");
					sb.append(copyright.getKey());
					sb.append(" : ");
					sb.append(MESSAGES.licensedAs());
					sb.append(" ");
					sb.append(copyright.getCopyright());
					sb.append(" : <a target=\"_blank\" href=\"");
					sb.append(copyright.getLicenseUrl());
					sb.append("\">");
					sb.append(copyright.getLicenseName());
					sb.append("</a>");
					if (null != copyright.getSourceUrl()) {
						sb.append(" ");
						sb.append(MESSAGES.sourceUrl());
						sb.append(" : ");
						sb.append(" <a target=\"_blank\" href=\"");
						sb.append(copyright.getSourceUrl());
						sb.append("\">");
						sb.append(copyright.getSourceUrl());
						sb.append("</a>");
					}
					sb.append("</li>");
				}
				sb.append("</ul>");
				copyrightWidget.setContents(sb.toString());
			}
		});



		Window window = new Window();
		window.setHeaderIcon(WidgetLayout.iconGeomajas);
		window.setTitle(MESSAGES.aboutMenuTitle());
		window.setWidth(WidgetLayout.aboutGeomajasWidth);
		window.setHeight(WidgetLayout.aboutGeomajasHeight);
		window.setAutoCenter(true);
		window.setPadding(WidgetLayout.marginLarge);
		window.addItem(layout);
		window.draw();
	}
}
