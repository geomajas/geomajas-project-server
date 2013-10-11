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
package org.geomajas.gwt.example.server.samples;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * Process the features, change the geometry to a circle with a size depending on the feature area.
 *
 * @author Joachim Van der Auwera
 */
public class ProcessFeaturesPipelineStep implements PipelineStep<GetFeaturesContainer> {

	@Autowired
	private GeoService geoService;

	public String getId() {
		return "processFeatures";
	}

	public void execute(PipelineContext context, GetFeaturesContainer container)
			throws GeomajasException {
		List<InternalFeature> base = container.getFeatures();
		List<InternalFeature> newFeatures = new ArrayList<InternalFeature>();
		Envelope bounds = new Envelope();

		for (InternalFeature feature : base) {
			InternalFeature newFeature = feature.clone();

			Geometry geometry = newFeature.getGeometry();
			double area = geometry.getArea();
			Point centroid = geometry.getCentroid();

			geometry = geoService.createCircle(centroid, Math.sqrt(area / 12), 32);

			newFeature.setGeometry(geometry);

			bounds.expandToInclude(geometry.getEnvelopeInternal());
			newFeatures.add(newFeature);
		}
		container.setFeatures(newFeatures);
		container.setBounds(bounds);
	}
}
