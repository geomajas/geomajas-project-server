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
package org.geomajas.gwt.client.widget.attribute;

import org.geomajas.command.dto.SearchAttributesRequest;
import org.geomajas.command.dto.SearchAttributesResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.layer.VectorLayer;

/**
 * Default implementation of {@link AttributeProvider}. This provider calls the server with the
 * {@link SearchAttributesRequest} to fetch the possible attribute values.
 * 
 * @author Jan De Moerloose
 */
public class DefaultAttributeProvider implements AttributeProvider {

	public static final String ATTRIBUTE_PATH_SEPARATOR = ".";

	private String serverLayerId;

	private String attributePath;

	/**
	 * Constructs an {@link DefaultAttributeProvider} for the specified layer that fetches top level attributes.
	 * 
	 * @param layer the layer
	 */
	public DefaultAttributeProvider(VectorLayer layer) {
		this(layer.getLayerInfo().getServerLayerId(), null);
	}

	/**
	 * Constructs an {@link DefaultAttributeProvider} for the specified layer that fetches attributes, starting from the
	 * specified path.
	 * 
	 * @param layer the layer
	 * @param attributePath the path from which to start (should refer to an association attribute)
	 */
	public DefaultAttributeProvider(VectorLayer layer, String attributePath) {
		this(layer.getLayerInfo().getServerLayerId(), attributePath);
	}

	/**
	 * Constructs an {@link DefaultAttributeProvider} for the specified layer that fetches top level attributes.
	 * 
	 * @param serverLayerId the server layer id
	 */
	public DefaultAttributeProvider(String serverLayerId) {
		this(serverLayerId, null);
	}

	/**
	 * Constructs an {@link DefaultAttributeProvider} for the specified layer that fetches attributes, starting from the
	 * specified path.
	 * 
	 * @param serverLayerId the server layer id
	 * @param attributePath the path from which to start (should refer to an association attribute)
	 */
	public DefaultAttributeProvider(String serverLayerId, String attributePath) {
		this.serverLayerId = serverLayerId;
		this.attributePath = attributePath;
	}

	public void getAttributes(final AttributeProviderCallBack callBack) {
		GwtCommand command = new GwtCommand(SearchAttributesRequest.COMMAND);
		command.setCommandRequest(new SearchAttributesRequest(serverLayerId, attributePath));
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<SearchAttributesResponse>() {

			public void execute(SearchAttributesResponse response) {
				if (response.isError()) {
					callBack.onError(response.getErrorMessages());
				} else {
					callBack.onSuccess(response.getAttributes());
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
