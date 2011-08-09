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

package org.geomajas.gwt.client.map.event;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event which is passed when a feature transaction has been successfully persisted on the server.
 * 
 * @author Pieter De Graef
 * @since 1.7.0
 */
@Api(allMethods = true)
public class FeatureTransactionEvent extends GwtEvent<FeatureTransactionHandler> {

	private FeatureTransaction featureTransaction;

	public FeatureTransactionEvent(FeatureTransaction featureTransaction) {
		this.featureTransaction = featureTransaction;
	}

	public FeatureTransaction getFeatureTransaction() {
		return featureTransaction;
	}

	public Type<FeatureTransactionHandler> getAssociatedType() {
		return FeatureTransactionHandler.TYPE;
	}

	protected void dispatch(FeatureTransactionHandler featureSelectionHandler) {
		featureSelectionHandler.onTransactionSuccess(this);
	}
}
