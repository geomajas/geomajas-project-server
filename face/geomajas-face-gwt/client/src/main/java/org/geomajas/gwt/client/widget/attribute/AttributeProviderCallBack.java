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

package org.geomajas.gwt.client.widget.attribute;

import org.geomajas.layer.feature.Attribute;

import java.util.List;

/**
 * Interface to be implemented by call-back objects of this provider.
 *
 * @author Jan De Moerloose
 */
public interface AttributeProviderCallBack {

	/**
	 * Called when the attributes have been successfully found.
	 *
	 * @param attributes list of attributes
	 */
	void onSuccess(List<Attribute<?>> attributes);

	/**
	 * Called when an error occurred while trying to provide the attributes.
	 *
	 * @param errorMessages error messages
	 */
	void onError(List<String> errorMessages);
}
