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

package org.geomajas.smartgwt.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * <p>
 * Localization messages for the search widget.
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface SearchMessages extends Messages {

	String labelNoLayerSelected();

	String labelLayerSelected();

	String btnSearch();

	String btnReset();

	String radioOperatorAnd();

	String radioOperatorOr();

	String gridAttributeColumn();

	String gridOperatorColumn();

	String gridValueColumn();

	String gridChooseAttribute();

	String warningNoCriteria();

	String warningInvalidCriteria();

	String operatorContains();

	String operatorEquals();

	String operatorNotEquals();

	String operatorST();

	String operatorSE();

	String operatorBT();

	String operatorBE();

	String operatorBefore();

	String operatorAfter();
}
