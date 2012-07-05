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

import java.util.List;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AssociationType;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
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

	private AssociationAttributeInfo attributeInfo;

	private AttributeProvider attributeProvider;

	public static final String ASSOCIATION_ITEM_VALUE_OBJECT_NAME = "_AssociationValueObject";

	public static final String ASSOCIATION_ITEM_VALUE_FIELD_NAME = "_AssociationValueField";

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public ManyToOneDataSource(AssociationAttributeInfo attributeInfo, AttributeProvider attributeProvider) {
		if (attributeInfo.getType() != AssociationType.MANY_TO_ONE) {
			throw new IllegalArgumentException("AttributeInfo (name=" + attributeInfo.getName()
					+ ") passed in ManyToOneDataSource is not of type MANY_TO_ONE.");
		}
		this.attributeInfo = attributeInfo;
		this.attributeProvider = attributeProvider;

		// Make sure we can use Geomajas commands for client-server communication:
		setDataFormat(DSDataFormat.CUSTOM);
		setDataProtocol(DSProtocol.CLIENTCUSTOM);
		setClientOnly(false);

		// Add id as both value text field and primary key field
		String idLabel = attributeInfo.getFeature().getIdentifier().getLabel();
		DataSourceField field;
		field = new DataSourceTextField(ASSOCIATION_ITEM_VALUE_FIELD_NAME, idLabel);
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
		attributeProvider.getAttributes(new AttributeProviderCallBack() {

			public void onSuccess(List<Attribute<?>> attributes) {
				// Add the values to the list:
				ListGridRecord[] list = new ListGridRecord[attributes.size()];
				for (int i = 0; i < attributes.size(); i++) {
					ManyToOneAttribute manyToOneAttribute = (ManyToOneAttribute) attributes.get(i);
					ListGridRecord record = new ListGridRecord();
					// set value field to id as text field !
					record.setAttribute(ASSOCIATION_ITEM_VALUE_FIELD_NAME, manyToOneAttribute.getValue().getId()
							.getValue().toString());
					record.setAttribute(ASSOCIATION_ITEM_VALUE_OBJECT_NAME, manyToOneAttribute.getValue());
					for (String name : manyToOneAttribute.getValue().getAllAttributes().keySet()) {
						Attribute<?> attribute = manyToOneAttribute.getValue().getAllAttributes().get(name);
						record.setAttribute(name, attribute.getValue());
					}
					list[i] = record;
				}
				dsResponse.setData(list);
				dsResponse.setStatus(RPCResponse.STATUS_SUCCESS);
				processResponse(requestId, dsResponse);
			}

			public void onError(List<String> errorMessages) {
				dsResponse.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, dsResponse);
			}

		});
	}
}