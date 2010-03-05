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
package org.geomajas.gwt.client.action.toolbar;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.RefreshConfigurationRequest;
import org.geomajas.command.dto.RefreshConfigurationResponse;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.i18n.I18nProvider;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Reloads the server Spring configuration.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RefreshConfigurationAction extends ToolbarAction {

	public RefreshConfigurationAction() {
		super("[ISOMORPHIC]/geomajas/reload.png", I18nProvider.getToolbar().refreshConfiguration());
	}

	public void onClick(ClickEvent event) {
		Dialog d = new Dialog();
		d.setWidth(400);
		SC.askforValue("Provide the context name", "Context file name ?", "applicationContext.xml",
				new ValueCallback() {

					public void execute(String value) {
						if (value != null) {
							RefreshConfigurationRequest request = new RefreshConfigurationRequest();
							request.setConfigLocations(value.trim().split("[ \\r\\t\\n,;]+"));
							GwtCommand command = new GwtCommand("command.configuration.Refresh");
							command.setCommandRequest(request);
							GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

								public void execute(CommandResponse response) {
									RefreshConfigurationResponse r = (RefreshConfigurationResponse) response;
									String message = "Reloaded applications : ";
									List<String> names = Arrays.asList(r.getApplicationNames());
									for (Iterator<String> it = names.iterator(); it.hasNext();) {
										message += it.next();
										if (it.hasNext()) {
											message += ",";
										}
									}
									SC.warn(message, null);
								}
							});
						}
					}
				}, d);
	}

}
