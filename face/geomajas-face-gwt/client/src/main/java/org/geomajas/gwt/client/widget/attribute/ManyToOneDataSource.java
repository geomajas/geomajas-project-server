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
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AssociationType;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * DataSource extension specifically designed for ManyToOne attribute FormItems. It automatically fetches possible
 * values from the Geomajas server. This DataSource is read only, which means that there is no cascading support for the
 * ManyToOne relationship through this DataSource.
 * 
 * @author Pieter De Graef
 */
public class ManyToOneDataSource extends DataSource {

	private String serverLayerId;

	private AssociationAttributeInfo attributeInfo;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public ManyToOneDataSource(String serverLayerId, AssociationAttributeInfo attributeInfo) {
		if (attributeInfo.getType() != AssociationType.MANY_TO_ONE) {
			throw new IllegalArgumentException("AttributeInfo (name=" + attributeInfo.getName()
					+ ") passed in ManyToOneDataSource is not of type MANY_TO_ONE.");
		}
		this.serverLayerId = serverLayerId;
		this.attributeInfo = attributeInfo;

		// Make sure we can use Geomajas commands for client-server communication:
		setDataFormat(DSDataFormat.CUSTOM);
		setDataProtocol(DSProtocol.CLIENTCUSTOM);
		setClientOnly(false);

		// Add id as primary key field
		DataSourceField field = new DataSourceIntegerField(AttributeFormFieldRegistry.ASSOCIATION_ITEM_ID_FIELD,
				attributeInfo.getFeature().getIdentifier().getLabel());
		field.setPrimaryKey(true);
		addField(field);

		// Add field for each attribute
		for (AttributeInfo info : attributeInfo.getFeature().getAttributes()) {
			field = new DataSourceTextField(info.getName(), info.getLabel());
			addField(field);
		}
	}

	public AssociationAttributeInfo getAttributeInfo() {
		return attributeInfo;
	}

	// ------------------------------------------------------------------------
	// Private/protected methods:
	// ------------------------------------------------------------------------

	protected Object transformRequest(DSRequest request) {
		String requestId = request.getRequestId();
		DSResponse response = new DSResponse();
		response.setAttribute("clientContext", request.getAttributeAsObject("clientContext"));

		response.setStatus(0);
		switch (request.getOperationType()) {
			case FETCH:
				executeFetch(requestId, request, response);
				break;
			case ADD:
				// Operation not implemented.
				break;
			case UPDATE:
				// Operation not implemented.
				break;
			case REMOVE:
				// Operation not implemented.
				break;
			default:
				// Operation not implemented.
				break;
		}
		return request.getData();
	}

	protected void transformResponse(DSResponse response, DSRequest request, Object data) {
		super.transformResponse(response, request, data);
	}

	protected void executeFetch(final String requestId, final DSRequest dsRequest, final DSResponse dsResponse) {
		GwtCommand command = new GwtCommand("command.feature.SearchAttributes");
		command.setCommandRequest(new SearchAttributesRequest(serverLayerId, attributeInfo.getName()));
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse response) {
				if (response.isError()) {
					dsResponse.setStatus(RPCResponse.STATUS_FAILURE);
					processResponse(requestId, dsResponse);
				} else if (response instanceof SearchAttributesResponse) {
					SearchAttributesResponse sar = (SearchAttributesResponse) response;

					// Add the values to the list:
					ListGridRecord[] list = new ListGridRecord[sar.getAttributes().size()];
					for (int i = 0; i < sar.getAttributes().size(); i++) {
						ManyToOneAttribute manyToOneAttribute = (ManyToOneAttribute) sar.getAttributes().get(i);
						ListGridRecord record = new ListGridRecord();
						record.setAttribute(AttributeFormFieldRegistry.ASSOCIATION_ITEM_ID_FIELD,
								manyToOneAttribute.getValue().getId().getValue());
						record.setAttribute(AttributeFormFieldRegistry.ASSOCIATION_ITEM_VALUE_ATTRIBUTE,
								manyToOneAttribute.getValue());
						//record.setAttribute(attributeInfo.getName(), manyToOneAttribute.getValue());
						for (String name : manyToOneAttribute.getValue().getAttributes().keySet()) {
							PrimitiveAttribute<?> attribute = manyToOneAttribute.getValue().getAttributes().get(name);
							record.setAttribute(name, attribute.getValue());
						}
						list[i] = record;
					}
					dsResponse.setData(list);
					dsResponse.setStatus(RPCResponse.STATUS_SUCCESS);
					processResponse(requestId, dsResponse);
				}
			}
		});
	}
}