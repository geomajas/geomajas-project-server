/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.i18n;

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

	String operatorContains();

	String operatorEquals();

	String operatorNotEquals();

	String operatorST();

	String operatorSE();

	String operatorBT();

	String operatorBE();
}
