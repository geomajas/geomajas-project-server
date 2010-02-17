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
package org.geomajas.command.feature;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.command.dto.PersistTransactionRequest;
import org.geomajas.command.dto.PersistTransactionResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.FeatureTransaction;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.VectorLayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A securityException will fail all, not just the feature(s) which are not allowed.
 * 
 * @author check subversion
 */
@Component()
@Transactional(rollbackFor = { Exception.class })
public class PersistTransactionCommand implements Command<PersistTransactionRequest, PersistTransactionResponse> {

	@Autowired
	private ConfigurationService runtimeParameters;

	@Autowired
	private DtoConverterService converter;

	@Autowired
	private VectorLayerService layerService;

	public PersistTransactionResponse getEmptyCommandResponse() {
		return new PersistTransactionResponse();
	}

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

		layerService.saveOrUpdate(featureTransaction.getLayerId(), runtimeParameters.getCrs(request.getCrs()),
				oldFeatures, newFeatures);

		response.setFeatureTransaction(featureTransaction);
	}
}
