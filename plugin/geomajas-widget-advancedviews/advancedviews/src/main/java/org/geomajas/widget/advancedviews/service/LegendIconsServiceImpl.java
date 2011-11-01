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

package org.geomajas.widget.advancedviews.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.geomajas.configuration.CircleInfo;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.ImageInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.RectInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.ConfigurationService;
import org.geomajas.widget.advancedviews.AdvancedviewsException;
import org.geomajas.widget.advancedviews.configuration.client.LayerTreeWithLegendInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Service for providing legend icons.
 *
 * @author Kristof Heirwegh
 */
@Component
public class LegendIconsServiceImpl implements LegendIconsService {

	private final Logger log = LoggerFactory.getLogger(LegendIconsServiceImpl.class);

	private static final int DEFAULT_ICON_SIZE = 18;

	private static final Color TEXT_BACKGROUND = new Color(0, 0, 0, 128);

	 private static final String DEFAULT_RASTER_IMAGE_PATH = "images/osgeo/layer-raster.png";

	private Map<String, Integer> iconSizes;

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private ConfigurationService configurationService;

	@PostConstruct
	public void init() {
		iconSizes = new HashMap<String, Integer>();

		Map<String, ClientMapInfo> maps = appContext.getBeansOfType(ClientMapInfo.class);
		for (Entry<String, ClientMapInfo> entry : maps.entrySet()) {
			if (null != entry.getValue().getWidgetInfo() && entry.getValue().getWidgetInfo().size() > 0) {
				for (Entry<String, ClientWidgetInfo> entryWidget : entry.getValue().getWidgetInfo().entrySet()) {
					if (entryWidget.getValue() instanceof LayerTreeWithLegendInfo) {
						iconSizes.put(entryWidget.getKey(),
								((LayerTreeWithLegendInfo) entryWidget.getValue()).getIconSize());
					}
				}
			}
		}
	}

	// ----------------------------------------------------------

	public Image createLegendIcon(String widgetId, String layerId, String styleName, String featureStyleId)
			throws AdvancedviewsException {
		if (null == layerId || "".equals(layerId)) {
			throw new AdvancedviewsException(AdvancedviewsException.REQUIRED_PARAMETER_MISSING);
		}

		// get layer
		final Layer<?> l = configurationService.getLayer(layerId);
		if (null == l) {
			throw new AdvancedviewsException(AdvancedviewsException.NO_SUCH_LAYER, layerId);
		}

		// get iconsize
		final int iconSize = getIconSize(LayerTreeWithLegendInfo.IDENTIFIER);

		// get style
		if (l instanceof VectorLayer) {
			if (null == styleName || null == featureStyleId || "".equals(styleName) || "".equals(featureStyleId)) {
				throw new AdvancedviewsException(AdvancedviewsException.REQUIRED_PARAMETER_MISSING);
			}

			VectorLayer vl = (VectorLayer) l;
			NamedStyleInfo nsi = vl.getLayerInfo().getNamedStyleInfo(styleName);
			if (null == nsi) {
				throw new AdvancedviewsException(AdvancedviewsException.NO_SUCH_NAMEDSTYLE, styleName);
			}

			FeatureStyleInfo fsi = null;
			for (FeatureStyleInfo tmp : nsi.getFeatureStyles()) {
				if (featureStyleId.equals(tmp.getStyleId())) {
					fsi = tmp;
					break;
				}
			}

			if (null == fsi) {
				throw new AdvancedviewsException(AdvancedviewsException.NO_SUCH_FEATURESTYLE, featureStyleId);
			}

			return createIcon(vl.getLayerInfo().getLayerType(), fsi, iconSize, null);
		} else {
			String url = null;
			if (null != styleName && !"".equals(styleName)) {
				url = styleName;
			}
			RasterLayer rl = (RasterLayer) l;
			return createIcon(rl.getLayerInfo().getLayerType(), null, iconSize, url);
		}
	}

	// ----------------------------------------------------------

	/**
	 * @param widgetId
	 *            name of the LayertreeWithLegend widget for which you want an icon
	 */
	private int getIconSize(String widgetId) {
		if (iconSizes.containsKey(widgetId)) {
			return iconSizes.get(widgetId);
		} else {
			return DEFAULT_ICON_SIZE;
		}
	}

	private Image createIcon(LayerType type, FeatureStyleInfo fsi, int iconSize, String iconUrl)
			throws AdvancedviewsException {
		BufferedImage bi = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gr = bi.createGraphics();
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		switch (type) {
		case RASTER:
			drawRaster(gr, iconSize, (iconUrl == null ? DEFAULT_RASTER_IMAGE_PATH : iconUrl));
			break;
		case POINT:
		case MULTIPOINT:
			drawPoint(gr, fsi, iconSize);
			break;
		case LINESTRING:
		case MULTILINESTRING:
			drawLinestring(gr, fsi, iconSize);
			break;
		case POLYGON:
		case MULTIPOLYGON:
		default:
			drawPolygon(gr, fsi, iconSize);
		}
		return bi;
	}

	private void drawRaster(Graphics2D gr, int iconSize, String imagePath) throws AdvancedviewsException {
		try {
			BufferedImage img = getImage(imagePath);
			AffineTransform trans;
			if (img.getHeight() > iconSize || img.getWidth() > iconSize) {
				double sx = 1d / img.getWidth() * iconSize;
				double sy = 1d / img.getHeight() * iconSize;
				double smallest = (sx < sy ? sx : sy);
				trans = AffineTransform.getScaleInstance(smallest, smallest);
				double width = smallest * img.getWidth();
				double height = smallest * img.getHeight();
				double tx = (width < iconSize ? (0d + iconSize - width) / 2 : 0d);
				double ty = (height < iconSize ? (0d + iconSize - height) / 2 : 0d);
				trans.concatenate(AffineTransform.getTranslateInstance(tx, ty));
			} else {
				double tx = (img.getWidth() < iconSize ? (0d + iconSize - img.getWidth()) / 2 : 0d);
				double ty = (img.getHeight() < iconSize ? (0d + iconSize - img.getHeight()) / 2 : 0d);
				trans = AffineTransform.getTranslateInstance(tx, ty);
			}
			gr.transform(trans);
			gr.drawImage(img, null, 0, 0);
		} catch (IOException e) {
			log.warn("Failed creating Legend Icon for Rasterlayer. Could not find image.");
			throw new AdvancedviewsException(AdvancedviewsException.FAILED_CREATING_IMAGEICON,
					"Kon rasterlaag icoon niet vinden ?!");
		}
	}

	private void drawPolygon(Graphics2D gr, FeatureStyleInfo fsi, int iconSize) throws AdvancedviewsException {
		int x;
		int y;
		int w = iconSize - 1;
		int h = iconSize - 1;
		float[] paniponidash = toDashArray(fsi.getDashArray());
		Stroke stroke = new BasicStroke(fsi.getStrokeWidth(), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f,
				paniponidash, 0.0f);
		gr.setColor(toColor(fsi.getFillColor(), fsi.getFillOpacity()));
		int linewidth = fsi.getStrokeWidth();
		int halfLinewidth = linewidth / 2; // cut int value !!
		w -= halfLinewidth * 2;
		h -= halfLinewidth * 2;
		x = halfLinewidth;
		y = halfLinewidth;
		gr.fillRect(0, 0, iconSize, iconSize);
		gr.setColor(toColor(fsi.getStrokeColor(), fsi.getStrokeOpacity()));
		gr.setStroke(stroke);
		gr.drawRect(x, y, w, h);
	}

	private void drawLinestring(Graphics2D gr, FeatureStyleInfo fsi, int iconSize) throws AdvancedviewsException {
		int max = iconSize - 1;
		float onethird = (0f + max) / 3;
		float twothirds = onethird * 2;
		float[] paniponidash = toDashArray(fsi.getDashArray());
		Stroke stroke = new BasicStroke(fsi.getStrokeWidth(), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f,
				paniponidash, 0.0f);
		gr.setColor(toColor(fsi.getStrokeColor(), fsi.getStrokeOpacity()));
		gr.setStroke(stroke);
		Path2D s = new Path2D.Double(Path2D.WIND_EVEN_ODD, 3);
		s.moveTo(0, 0);
		s.lineTo(twothirds, onethird);
		s.lineTo(onethird, twothirds);
		s.lineTo(max, max);
		gr.draw(s);
	}

	private void drawPoint(Graphics2D gr, FeatureStyleInfo fsi, int iconSize) throws AdvancedviewsException {
		int x = 0, y = 0, w = 0, h = 0, max = iconSize - 1;
		int addSize = 0;
		float[] paniponidash = toDashArray(fsi.getDashArray());
		Stroke stroke = new BasicStroke(fsi.getStrokeWidth(), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f,
				paniponidash, 0.0f);
		gr.setColor(toColor(fsi.getFillColor(), fsi.getFillOpacity()));

		if (null != fsi.getSymbol().getCircle()) {
			CircleInfo info = fsi.getSymbol().getCircle();
			w = Math.round(info.getR() * 2);
			h = w;
			if (w > max) {
				w = max;
				h = max;
				addSize = (int) info.getR();
			}
			int linewidth = fsi.getStrokeWidth();
			int halfLinewidth = linewidth / 2; // cut int value !!
			if (w + linewidth > max) {
				w = max - halfLinewidth * 2;
				x = halfLinewidth;
				addSize = (int) info.getR();
			} else {
				x = Math.round((0f + max - w) / 2f);
			}
			if (h + linewidth > max) {
				h = max - halfLinewidth * 2;
				y = halfLinewidth;
				addSize = (int) info.getR();
			} else {
				y = Math.round((0f + max - h) / 2f);
			}

			gr.fillOval(x, y, w, h);
			gr.setColor(toColor(fsi.getStrokeColor(), fsi.getStrokeOpacity()));
			gr.setStroke(stroke);
			gr.drawOval(x, y, w, h);

		} else if (null != fsi.getSymbol().getRect()) {
			RectInfo info = fsi.getSymbol().getRect();
			w = Math.round(info.getW());
			h = Math.round(info.getH());
			int realSize = (w + h) / 2;
			if (w > max) {
				w = max;
				addSize = realSize;
			}
			if (h > max) {
				h = max;
				addSize = realSize;
			}
			int linewidth = fsi.getStrokeWidth();
			int halfLinewidth = linewidth / 2; // cut int value !!
			if (w + linewidth > max) {
				w = max - halfLinewidth * 2;
				x = halfLinewidth;
				addSize = realSize;
			} else {
				x = Math.round((0f + max - w) / 2f);
			}
			if (h + linewidth > max) {
				h = max - halfLinewidth * 2;
				y = halfLinewidth;
				addSize = realSize;
			} else {
				y = Math.round((0f + max - h) / 2f);
			}

			gr.fillRect(x, y, w, h);
			gr.setColor(toColor(fsi.getStrokeColor(), fsi.getStrokeOpacity()));
			gr.setStroke(stroke);
			gr.drawRect(x, y, w, h);

		} else if (null != fsi.getSymbol().getImage()) {
			ImageInfo info = fsi.getSymbol().getImage();
			try {
				BufferedImage img = getImage(info.getHref());
				if (null == img) {
					throw new AdvancedviewsException(AdvancedviewsException.FAILED_CREATING_IMAGEICON, info.getHref());
				}

				AffineTransform trans;
				if (img.getHeight() > iconSize || img.getWidth() > iconSize) {
					double sx = 1d / img.getWidth() * iconSize;
					double sy = 1d / img.getHeight() * iconSize;
					double smallest = (sx < sy ? sx : sy);
					trans = AffineTransform.getScaleInstance(smallest, smallest);
					double width = smallest * img.getWidth();
					double height = smallest * img.getHeight();
					double tx = (width < iconSize ? (0d + iconSize - width) / 2 : 0d);
					double ty = (height < iconSize ? (0d + iconSize - height) / 2 : 0d);
					trans.concatenate(AffineTransform.getTranslateInstance(tx, ty));
				} else {
					double tx = (img.getWidth() < iconSize ? (0d + iconSize - img.getWidth()) / 2 : 0d);
					double ty = (img.getHeight() < iconSize ? (0d + iconSize - img.getHeight()) / 2 : 0d);
					trans = AffineTransform.getTranslateInstance(tx, ty);
				}
				gr.transform(trans);
				gr.drawImage(img, null, 0, 0);
			} catch (IOException e) {
				log.warn("Failed creating Legend Icon from image: " + e.getMessage());
				throw new AdvancedviewsException(AdvancedviewsException.FAILED_CREATING_IMAGEICON, info.getHref());
			}
		} else {
			throw new AdvancedviewsException(AdvancedviewsException.REQUIRED_PARAMETER_MISSING, "Symbol StyleInfo");
		}

		if (addSize > 0) {
			drawText(gr, "" + addSize, 8, Color.WHITE, iconSize);
		}
	}

	/**
	 * 
	 * @param gr
	 * @param text
	 * @param size
	 * @param color
	 */
	private void drawText(Graphics2D gr, String text, int size, Color color, int iconSize) {
		int max = iconSize - 1;
		TextLayout tl = new TextLayout(text, gr.getFont(), gr.getFontRenderContext());
		gr.setColor(TEXT_BACKGROUND);
		gr.setStroke(new BasicStroke());
		Rectangle2D b = tl.getBounds();
		b = new Rectangle2D.Double(0, max, b.getWidth() + 1, b.getHeight() + 1);
		gr.fill(b);
		gr.setColor(color);
		gr.setFont(new Font("SansSerif", Font.PLAIN, size));
		gr.drawString(text, 1, max);
	}

	private Color toColor(String color, float opacity) {
		Color c;
		String raw = color;
		if (raw.startsWith("#")) {
			raw = raw.substring(1);
		}
		try {
			int clr = Integer.parseInt(raw, 16);
			c = new Color(clr);
		} catch (Exception e) {
			c = Color.getColor(raw, 0);
		}
		if (opacity < 1) {
			return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.round(opacity * 255));
		} else {
			return c;
		}
	}

	private float[] toDashArray(String rawdashes) throws AdvancedviewsException {
		try {
			if (null == rawdashes || "".equals(rawdashes)) {
				return null;
			} else {
				String[] raw = rawdashes.split(",");
				float[] res = new float[raw.length];
				for (int i = 0; i < raw.length; i++) {
					res[i] = Float.parseFloat(raw[i]);
				}
				return res;
			}
		} catch (Exception e) {
			throw new AdvancedviewsException(AdvancedviewsException.PARSING_DASHARRAY_FAILED, rawdashes);
		}
	}

	private BufferedImage getImage(String href) throws IOException, AdvancedviewsException {
		if (href == null || "".equals(href)) {
			return null;
		} else if (href.startsWith("http://")) {
			return ImageIO.read(new URL(href));
		} else {
			String path = href;
			if (href.startsWith("/")) {
				path = href.substring(1);
			}
			InputStream is = null;
			try {
				Resource resource = appContext.getResource(path);
				if (resource != null && resource.exists()) {
					is = resource.getInputStream();
				} else {
					// conveniencecheck so clients can use same url client- as serverside
					resource = appContext.getResource("images/" + path);
					if (resource != null && resource.exists()) {
						is = resource.getInputStream();
					} else {
						is = ClassLoader.getSystemResourceAsStream(href);
					}
				}
				if (is == null) {
					throw new AdvancedviewsException(AdvancedviewsException.IMAGE_NOT_FOUND, href);
				}
				return ImageIO.read(is);
			} finally {
				if (is != null) {
					is.close();
				}
			}
		}
	}
}
