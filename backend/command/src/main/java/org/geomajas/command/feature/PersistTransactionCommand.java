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
package org.geomajas.command.feature;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.command.dto.PersistTransactionRequest;
import org.geomajas.command.dto.PersistTransactionResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.FeatureTransaction;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A securityException will fail all, not just the feature(s) which are not allowed.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api
@Component()
@Transactional(rollbackFor = { Exception.class })
public class PersistTransactionCommand implements Command<PersistTransactionRequest, PersistTransactionResponse> {

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService converter;

	@Autowired
	private VectorLayerService layerService;

	/** {@inheritDoc} */
	public PersistTransactionResponse getEmptyCommandResponse() {
		return new PersistTransactionResponse();
	}

	/** {@inheritDoc} */
	public void execute(PersistTransactionRequest request, PersistTransactionResponse response) throws Exception {
		if (null == request.getFeatureTransaction()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "featureTransaction");
		}
		if (null == request.getFeatureTransaction().getLayerId()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "featureTransaction.layerId");
		}
		if (null == request.getCrs()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}
		FeatureTransaction featureTransaction = request.getFeatureTransaction();
		if (featureTransaction == null) {
			return;
		}

		List<InternalFeature> oldFeatures = new ArrayList<InternalFeature>();
		List<InternalFeature> newFeatures = new ArrayList<InternalFeature>();
		if (featureTransaction.getOldFeatures() != null) {
			for (int i = 0; i < featureTransaction.getOldFeatures().length; i++) {
				oldFeatures.add(converter.toInternal(featureTransaction.getOldFeatures()[i]));
			}
		}
		if (featureTransaction.getNewFeatures() != null) {
			for (int i = 0; i < featureTransaction.getNewFeatures().length; i++) {
				newFeatures.add(converter.toInternal(featureTransaction.getNewFeatures()[i]));
			}
		}

		layerService.saveOrUpdate(featureTransaction.getLayerId(), geoService.getCrs(request.getCrs()), oldFeatures,
				newFeatures);

		// Apply the new set of InternalFeatures onto the transaction: (ID may be filled in now)
		if (featureTransaction.getNewFeatures() != null) {
			Feature[] resultFeatures = new Feature[newFeatures.size()];
			for (int i = 0; i < newFeatures.size(); i++) {
				InternalFeature internalFeature = newFeatures.get(i);
				resultFeatures[i] = converter.toDto(internalFeature);
			}
			featureTransaction.setNewFeatures(resultFeatures);
		}
		response.setFeatureTransaction(featureTransaction);
	}
}
