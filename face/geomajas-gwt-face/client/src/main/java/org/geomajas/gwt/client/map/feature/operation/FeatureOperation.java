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
