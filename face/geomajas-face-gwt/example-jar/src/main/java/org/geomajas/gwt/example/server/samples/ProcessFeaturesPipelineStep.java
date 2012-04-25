package org.geomajas.gwt.example.server.samples;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
