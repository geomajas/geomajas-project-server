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
package org.geomajas.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;
import org.geomajas.layer.feature.FeatureTransaction;

/**
 * Response object for {@link org.geomajas.command.feature.PersistTransactionCommand}.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class PersistTransactionResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private FeatureTransaction featureTransaction;

	/**
	 * Get the feature transaction.
	 *
	 * @return feature transaction
	 */
	public FeatureTransaction getFeatureTransaction() {
		return featureTransaction;
	}

	/**
	 * Set the feature transaction.
	 *
	 * @param featureTransaction feature transaction
	 */
	public void setFeatureTransaction(FeatureTransaction featureTransaction) {
		this.featureTransaction = featureTransaction;
	}

	@Override
	public String toString() {
		return "PersistTransactionResponse{" +
				"featureTransaction=" + featureTransaction +
				'}';
	}
}
