/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.step;

import java.util.Random;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.VectorTile;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.plugin.rasterizing.mvc.RasterizingController;
import org.geomajas.service.DispatcherUrlService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Pipeline step which creates the image URL of the raster.
 *
 * @author Jan De Moerloose
 */
public class RasterUrlStep implements PipelineStep<GetTileContainer> {

	@Autowired
	private DispatcherUrlService dispatcherUrlService;

	private Random random = new Random();

	private static final char[] CHARACTERS = {
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
		'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
		'u', 'v', 'w', 'x', 'y', 'z',
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer response) throws GeomajasException {
		InternalTile tile = response.getTile();
		tile.setContentType(VectorTile.VectorTileContentType.URL_CONTENT);
		String featuresKey = context.getOptional(RasterizingPipelineCode.IMAGE_ID_KEY, String.class);
		if (featuresKey != null) {
			tile.setFeatureContent(buildUrl(context, featuresKey));
		}
		String labelsKey = context.getOptional(RasterizingPipelineCode.IMAGE_ID_LABEL_KEY, String.class);
		if (labelsKey != null) {
			tile.setLabelContent(buildUrl(context, labelsKey));
		}
	}
	
	private String buildUrl(PipelineContext context, String key) throws GeomajasException {
		String layerId = context.get(PipelineCode.LAYER_ID_KEY, String.class);
		StringBuilder url = new StringBuilder(200);
		String dispatcherUrl = dispatcherUrlService.getDispatcherUrl();
		// check for trailing '/'
		if (dispatcherUrl.endsWith("/")) {
			dispatcherUrl = dispatcherUrl.substring(0, dispatcherUrl.length() - 1);
		}
		url.append(dispatcherUrl);
		url.append(RasterizingController.LAYER_MAPPING);
		url.append(layerId);
		url.append("/");
		url.append(key);
		url.append(".png");
		// must add a random parameter to force reload in some browsers like FF
		// better solution would be to use a cache version id ?
		url.append("?q=");
		url.append(randomString(8));
		return url.toString();
	}

	private String randomString(int length) {
		char[] buf = new char[length];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = CHARACTERS[random.nextInt(36)];
		}
		return new String(buf);
	}

}
