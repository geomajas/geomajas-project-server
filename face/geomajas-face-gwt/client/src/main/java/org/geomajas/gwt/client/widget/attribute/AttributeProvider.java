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

import java.util.List;

import org.geomajas.layer.feature.Attribute;

/**
 * An {@link AttributeProvider} provides all possible attribute values of an association attribute. This is usually done
 * by calling the server, so a call back mechanism is needed.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface AttributeProvider {

	/**
	 * Get all attributes and provide the results to the specified call-back object.
	 * 
	 * @param callBack the call-back object
	 */
	void getAttributes(CallBack callBack);

	AttributeProvider createProvider(String attributeName);

	/**
	 * Interface to be implemented by call-back objects of this provider.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface CallBack {

		/**
		 * Called when the attributes have been successfully found.
		 * 
		 * @param attributes list of attributes
		 */
		void onSuccess(List<Attribute<?>> attributes);

		/**
		 * Called when an error occurred while trying to provide the attributes.
		 * 
		 * @param errorMessages
		 */
		void onError(List<String> errorMessages);
	}

}
