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
package org.geomajas.extension.command.feature;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.command.Command;
import org.geomajas.extension.command.dto.PersistTransactionRequest;
import org.geomajas.extension.command.dto.PersistTransactionResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.FeatureTransaction;
import org.geomajas.layer.feature.RenderedFeature;
import org.geomajas.rendering.painter.LayerPaintContext;
import org.geomajas.rendering.painter.PaintFactory;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.DtoConverter;
import org.geomajas.service.FilterCreator;
import org.geomajas.service.GeoService;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.JTS;
import org.opengis.filter.Filter;
import org.opengis.referencing.operation.MathTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A securityException will fail all, not just the feature(s) which are not allowed.
 *
 * @author check subversion
 */
@Component()
public class PersistTransactionCommand
		implements Command<PersistTransactionRequest, PersistTransactionResponse> {

	@Autowired
	private ApplicationService runtimeParameters;

	@Autowired
	private DtoConverter converter;

	@Autowired
	private FilterCreator filterCreator;

	@Autowired
	private GeoService geoService;

	@Autowired
	private PaintFactory paintFactory;

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
		Filter filter;
		if (request.getFilter() != null) {
			filter = CQL.toFilter(request.getFilter());
		} else {
			filter = filterCreator.createTrueFilter();
		}

		String layerId = featureTransaction.getLayerId();
		VectorLayer layer = runtimeParameters.getVectorLayer(layerId);
		FeatureModel featureModel = layer.getLayerModel().getFeatureModel();

		Set<RenderedFeature> featuresToDelete = new HashSet<RenderedFeature>();
		List<RenderedFeature> featuresToCreate = new ArrayList<RenderedFeature>();

		if (featureTransaction.getOldFeatures() != null) {
			for (int i = 0; i < featureTransaction.getOldFeatures().length; i++) {
				featuresToDelete.add(converter.toFeature(featureTransaction.getOldFeatures()[i]));
			}
		}
		if (featureTransaction.getNewFeatures() != null) {
			for (int i = 0; i < featureTransaction.getNewFeatures().length; i++) {
				featuresToCreate.add(converter.toFeature(featureTransaction.getNewFeatures()[i]));
			}
		}
		featuresToDelete.removeAll(featuresToCreate);

		if (featuresToDelete.size() > 0 && !layer.getEditPermissions().isDeletingAllowed()) {
			throw new SecurityException(
					"Deleting features of layer " + layer.getLayerInfo().getLabel() + " is not permitted");
		}
		// TODO we can't check for updating or creating allowed, because they're in 1 list.

		// -- SECURITY -------------------------------------------------
		// TODO might want to check individual rights (e.g. now we fail on
		// delete even if there are no features to delete)
		for (RenderedFeature f : featuresToDelete) {
			if (!filter.evaluate(f)) {
				throw new SecurityException("U heeft niet voldoende rechten om dit feature te verwijderen: "
						+ f.getLocalId() + " - " + f.getLabel());
			}
		}
		for (RenderedFeature f : featuresToCreate) {
			if (!filter.evaluate(f)) {
				throw new SecurityException("U heeft niet voldoende rechten om dit feature aan te maken: "
						+ f.getLocalId() + " - " + f.getLabel());
			}
		}

		// -- CRUD ------------------------------------------------------

		// DELETE FEATURES
		for (RenderedFeature featureData : featuresToDelete) {
			layer.getLayerModel().delete(featureData.getLocalId());
		}

		// SAVE OR UPDATE FEATURES
		for (RenderedFeature featureData : featuresToCreate) {
			String assignedId = featureData.getLocalId();
			Object feature = null;
			if (assignedId != null) {
				// case 1: existing feature
				try {
					feature = layer.getLayerModel().read(featureData.getLocalId());
				} catch (NoSuchElementException e) {
					// ignore
				}
				// case 2: new feature with ID
				if (feature == null) {
					feature = featureModel.newInstance(assignedId);
				}
			} else {
				// case 3: new feature without ID
				feature = featureModel.newInstance();
			}
			featureModel.setAttributes(feature, featureData.getAttributes());

			if (featureData.getGeometry() != null) {
				MathTransform mapToLayer =
						geoService.findMathTransform(runtimeParameters.getCrs(request.getCrs()), layer.getCrs());
				Geometry transformed = JTS.transform(featureData.getGeometry(), mapToLayer);
				featureModel.setGeometry(feature, transformed);
			}
			feature = layer.getLayerModel().saveOrUpdate(feature);

			// Not needed for existing features, but no problem to re-set feature id
			String id = featureModel.getId(feature);
			String featureId = featureTransaction.getLayerId() + "." + id;
			featureData.setId(featureId);

			LayerPaintContext context = paintFactory.createLayerPaintContext(layer);
			featureData.setStyleDefinition(context.findStyleFilter(featureData).getStyleDefinition());

			featureData.setAttributes(featureModel.getAttributes(feature));
		}

		response.setFeatureTransaction(featureTransaction);
	}

}