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

package org.geomajas.puregwt.example.client.sample.general;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.puregwt.client.service.CommandService;
import org.geomajas.puregwt.client.service.CommandServiceImpl;
import org.geomajas.puregwt.example.client.sample.SamplePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates creation of a server side exception message.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Jan Venstermans
 */
public class ServerExceptionPanel implements SamplePanel {

	private CommandService commandService;

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, ServerExceptionPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	public Widget asWidget() {
		// Create the layout for this sample:
		Widget layout = UI_BINDER.createAndBindUi(this);
		return layout;
	}

	// ------------------------------------------------------------------------
	// Click handlers for the button:
	// ------------------------------------------------------------------------

	@UiHandler("generateExceptionButton")
	protected void onGenerateExceptionClicked(ClickEvent event) {
		EmptyCommandRequest request = new EmptyCommandRequest();
		GwtCommand command = new GwtCommand();
		command.setCommandName("command.GetSimpleExceptionCommand");
		command.setCommandRequest(request);
		commandService = new CommandServiceImpl();
		commandService.execute(command,
				new AbstractCommandCallback<CommandResponse>() {

					@Override
					public void execute(CommandResponse response) {
						// don't do anything. An Exception will been thrown at
						// server-side
					}
				});
	}
}
