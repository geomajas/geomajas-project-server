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
package org.geomajas.gwt.client.widget.attribute;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchAttributesRequest;
import org.geomajas.command.dto.SearchAttributesResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;

/**
 * Default implementation of {@link AttributeProvider}. This provider calls the server with the
 * {@link SearchAttributesRequest} to fetch the possible attribute values.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DefaultAttributeProvider implements AttributeProvider {

	public static final String ATTRIBUTE_PATH_SEPARATOR = ".";

	private String serverLayerId;

	private String attributePath;

	public DefaultAttributeProvider(String serverLayerId) {
		this.serverLayerId = serverLayerId;
	}

	public DefaultAttributeProvider(String serverLayerId, String attributePath) {
		this.serverLayerId = serverLayerId;
		this.attributePath = attributePath;
	}

	public void getAttributes(final CallBack callBack) {
		GwtCommand command = new GwtCommand(SearchAttributesRequest.COMMAND);
		command.setCommandRequest(new SearchAttributesRequest(serverLayerId, attributePath));
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse response) {
				if (response.isError()) {
					callBack.onError(response.getErrorMessages());
				} else if (response instanceof SearchAttributesResponse) {
					SearchAttributesResponse sar = (SearchAttributesResponse) response;
					callBack.onSuccess(sar.getAttributes());
				}
			}
		});
	}

	public DefaultAttributeProvider createProvider(String attributeName) {
		if (attributePath == null) {
			return new DefaultAttributeProvider(serverLayerId, attributeName);
		} else {
			return new DefaultAttributeProvider(serverLayerId, attributePath + ATTRIBUTE_PATH_SEPARATOR
					+ attributeName);
		}
	}

}
