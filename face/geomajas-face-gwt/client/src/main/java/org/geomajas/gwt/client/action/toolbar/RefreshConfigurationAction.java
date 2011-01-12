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
 */
public class RefreshConfigurationAction extends ToolbarAction {

	public RefreshConfigurationAction() {
		super("[ISOMORPHIC]/geomajas/osgeo/reload.png", I18nProvider.getToolbar().refreshConfiguration());
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
