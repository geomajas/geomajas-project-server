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
package org.geomajas.widget.advancedviews.mvc;

import java.awt.image.BufferedImage;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 * @author Kristof Heirwegh
 */
@Component("legendIconsView")
public class LegendIconsView extends AbstractView {

	private static final String FORMAT = "png";

	public LegendIconsView() {
		setContentType("image/png");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BufferedImage image = (BufferedImage) model.get(LegendIconsController.IMAGE_KEY);

		// write the image
		ServletOutputStream out = response.getOutputStream();
		response.setContentType(getContentType());
		// response.setContentLength(image.); // need to write image first to know size
		ImageIO.write(image, FORMAT, out);

		out.flush();
	}
}
