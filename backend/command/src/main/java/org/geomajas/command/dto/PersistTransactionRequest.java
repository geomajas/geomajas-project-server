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

package org.geomajas.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;
import org.geomajas.layer.feature.FeatureTransaction;

/**
 * Request object for {@link org.geomajas.command.feature.PersistTransactionCommand}.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class PersistTransactionRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 * */
	public static final String COMMAND = "command.feature.PersistTransaction";

	private FeatureTransaction featureTransaction;

	private String crs;

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

	/**
	 * Get the coordinate reference space which should be used for the returned geometries.
	 *
	 * @return crs
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the coordinate reference space which should be used for the returned geometries.
	 *
	 * @param crs crs
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	@Override
	public String toString() {
		return "PersistTransactionRequest{" +
				"featureTransaction=" + featureTransaction +
				", crs='" + crs + '\'' +
				'}';
	}
}
