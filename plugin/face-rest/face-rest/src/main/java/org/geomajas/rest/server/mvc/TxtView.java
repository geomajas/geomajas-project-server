/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.rest.server.mvc;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rest.server.GeoToolsConverterService;
import org.geomajas.rest.server.TxtParser;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * TXT view.
 * 
 * @author Oliver May
 */
@Component("rest.server.mvc.TextView")
public class TxtView extends AbstractView {

	@Autowired
	private GeoToolsConverterService convertorService;

	private TxtParser parser = new TxtParser();

	public TxtView() {
		setContentType("text/plain");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (model.get(RestController.VECTOR_LAYER_INFO) == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "No elements where found.");
			return;
		}

		Object ff = model.get(RestController.FEATURE_COLLECTION);
		VectorLayerInfo info = (VectorLayerInfo) model.get(RestController.VECTOR_LAYER_INFO);
		List<String> attrs = (List<String>) model.get(RestController.ATTRIBUTES);
		SimpleFeatureType sft = convertorService.toSimpleFeatureType(info, attrs);
		response.setContentType(getContentType());
		if (Collection.class.isAssignableFrom(ff.getClass())) {
			Collection<InternalFeature> features = (Collection<InternalFeature>) ff;
			MemoryFeatureCollection coll = new MemoryFeatureCollection(sft);
			for (InternalFeature feature : features) {
				coll.add(convertorService.toSimpleFeature(feature, sft));
			}
			parser.write(coll, response.getOutputStream());
		} else {
			InternalFeature feature = (InternalFeature) ff;
			MemoryFeatureCollection coll = new MemoryFeatureCollection(sft);
			coll.add(convertorService.toSimpleFeature(feature, sft));
			parser.write(coll, response.getOutputStream());
		}
		response.getOutputStream().flush();
	}

}
