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
package org.geomajas.servlet.mvc.legend;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 * View responsible for the actual icon generation.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component(LegendGraphicController.LEGENDGRAPHIC_VIEW_NAME)
public class LegendGraphicView extends AbstractView {

	public static final String IMAGE_KEY = "image";

	public static final String IMAGE_FORMAT_KEY = "format";
	
	private static final Map<String, String> DEFAULT_MIME_TYPES = new HashMap<String, String>();

	static
	{
		DEFAULT_MIME_TYPES.put(".gif", "image/gif");
		DEFAULT_MIME_TYPES.put(".ico", "image/vnd.microsoft.icon");
		DEFAULT_MIME_TYPES.put(".jpeg", "image/jpeg");
		DEFAULT_MIME_TYPES.put(".jpg", "image/jpeg");
		DEFAULT_MIME_TYPES.put(".png", "image/png");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String format = (String) model.get(IMAGE_FORMAT_KEY);
		BufferedImage image = (BufferedImage) model.get(IMAGE_KEY);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 8);
		ImageIO.write(image, format, baos);
		String mimeType = DEFAULT_MIME_TYPES.get(format);
		if (mimeType != null) {
			response.setContentType(mimeType);
		} else {
			response.setContentType("image/" + format);
		}
		response.setContentLength(baos.size());
		// write the image
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
	}
}
