/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.step;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.RasterException;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.service.pipeline.PipelineContext;

/**
 * Step which writes the in-memory image to the required format. Currently, only png is supported.
 * 
 * @author Jan De Moerloose
 */
public class WriteImageStep extends AbstractRasterizingStep {

	public void execute(PipelineContext context, RasterizingContainer response) throws GeomajasException {
		if (context.containsKey(RasterizingPipelineCode.RENDERED_IMAGE)) {
			RenderedImage image = context.get(RasterizingPipelineCode.RENDERED_IMAGE, RenderedImage.class);
			ByteArrayOutputStream imageStream = new ByteArrayOutputStream(10 * 1024);
			try {
				ImageIO.write(image, "PNG", imageStream);
			} catch (IOException e) {
				throw new RasterException(RasterException.IMAGE_WRITING_FAILED, e);
			}
			byte[] imageData = imageStream.toByteArray();
			response.setImage(imageData);
		}
	}

}
