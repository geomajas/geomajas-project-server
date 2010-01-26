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

package org.geomajas.gwt.client.map.feature.operation;

import org.geomajas.gwt.client.map.feature.Feature;

/**
 * Editing features in the GWT client, goes trough a series of operations on those features. This is the general
 * interface that defines such operations. Not only should implementing classes have an execution function to operate on
 * a feature, it must always be possible to undo the changes made to a feature.
 *
 * @author Pieter De Graef
 */
public interface FeatureOperation {

	/**
	 * General execution function of a single operation on a feature.
	 *
	 * @param feature
	 *            The feature on which to operate.
	 */
	void execute(Feature feature);

	/**
	 * Every feature operation should be able to undo the changes it has made to a feature. This method does just that.
	 *
	 * @param feature
	 *            The feature on which to operate.
	 */
	void undo(Feature feature);
}
