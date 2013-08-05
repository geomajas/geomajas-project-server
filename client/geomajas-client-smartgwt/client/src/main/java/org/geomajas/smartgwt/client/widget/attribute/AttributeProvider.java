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
package org.geomajas.smartgwt.client.widget.attribute;

/**
 * An {@link AttributeProvider} provides all possible attribute values of an association attribute. This is usually done
 * by calling the server, so a call back mechanism is needed.
 * 
 * @author Jan De Moerloose
 */
public interface AttributeProvider {

	/**
	 * Get all attributes and provide the results to the specified call-back object.
	 * 
	 * @param callBack the call-back object
	 */
	void getAttributes(AttributeProviderCallBack callBack);

	/**
	 * Create an attribute provider for given attribute name.
	 *
	 * @param attributeName attribute name
	 * @return attribute provider
	 */
	AttributeProvider createProvider(String attributeName);

}
