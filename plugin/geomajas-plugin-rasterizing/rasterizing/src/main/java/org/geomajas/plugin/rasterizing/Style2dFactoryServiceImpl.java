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

package org.geomajas.plugin.rasterizing;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.rasterizing.api.LabelStyle;
import org.geomajas.plugin.rasterizing.api.Style2dFactoryService;
import org.geomajas.service.TextService;
import org.geotools.renderer.style.IconStyle2D;
import org.geotools.renderer.style.LineStyle2D;
import org.geotools.renderer.style.MarkStyle2D;
import org.geotools.renderer.style.PolygonStyle2D;
import org.geotools.renderer.style.Style2D;
import org.geotools.renderer.style.TextStyle2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Default factory that creates <code>Style2D</code> objects from a configuration
 * <code>StyleInfo</code> object.
 * </p>
 *
 * @author Pieter De Graef
 */
@Component
public class Style2dFactoryServiceImpl implements Style2dFactoryService {

	private final Logger log = LoggerFactory.getLogger(GeotoolsRasterizingService.class);
	
	private static Map<String, BufferedImage> IMAGES = new HashMap<String, BufferedImage>();

	@Autowired
	private TextService textService;

	@Autowired
	private ApplicationContext applicationContext;

	public Style2D createStyle(FeatureStyleInfo styleInfo, LayerType layerType) {
		if (layerType == LayerType.LINESTRING || layerType == LayerType.MULTILINESTRING) {
			return createLineStyle(styleInfo);
		} else if (layerType == LayerType.POLYGON || layerType == LayerType.MULTIPOLYGON) {
			return createPolygonStyle(styleInfo);
		} else if (layerType == LayerType.POINT || layerType == LayerType.MULTIPOINT) {
			if (styleInfo.getSymbol().getImage() != null) {
				return createIconStyle(styleInfo);
			} else {
				return createMarkStyle(styleInfo);
			}
		}
		return null;
	}
	
	public LabelStyle createLabelStyle(LabelStyleInfo styleInfo) {
		LabelStyle style = new LabelStyle();
		FontStyleInfo fontInfo = styleInfo.getFontStyle();
		FeatureStyleInfo backgroundInfo = styleInfo.getBackgroundStyle();
		Composite fontComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fontInfo.getOpacity());
		style.setFontComposite(fontComposite);
		Composite backgroundComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				backgroundInfo.getFillOpacity());
		style.setBackgroundComposite(backgroundComposite);
		Composite strokeComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				backgroundInfo.getStrokeOpacity());
		style.setStrokeComposite(strokeComposite);
		style.setFont(textService.getFont(styleInfo.getFontStyle()));
		style.setFontColor(createColorFromHTMLCode(styleInfo.getFontStyle().getColor()));
		style.setBackgroundColor(createColorFromHTMLCode(backgroundInfo.getFillColor()));
		style.setStrokeColor(createColorFromHTMLCode(backgroundInfo.getStrokeColor()));
		style.setStroke(new BasicStroke(backgroundInfo.getStrokeWidth()));
		return style;
	}

	/**
	 * Create a TextStyle2D object that can be used in labeling.
	 *
	 * @param styleInfo The style configuration object.
	 * @param label The text to style.
	 * @param font The font to use.
	 * @return Returns a GeoTools style object.
	 */
	public TextStyle2D createStyle(FeatureStyleInfo styleInfo, String label, Font font) {
		TextStyle2D style = new TextStyle2D();
		float opacity = styleInfo.getFillOpacity();
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
		style.setComposite(composite);
		Color fillColor = createColorFromHTMLCode(styleInfo.getFillColor());
		style.setFill(fillColor);
		style.setFont(font);
		style.setLabel(label);
		return style;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	public LineStyle2D createLineStyle(FeatureStyleInfo styleInfo) {
		LineStyle2D style = new LineStyle2D();
		setStrokeStyle(styleInfo, style);
		return style;
	}



	public PolygonStyle2D createPolygonStyle(FeatureStyleInfo styleInfo) {
		PolygonStyle2D style = new PolygonStyle2D();
		setStrokeStyle(styleInfo, style);
		setFillStyle(styleInfo, style);
		return style;
	}
	
	public Style2D createMarkStyle(FeatureStyleInfo styleInfo) {
		MarkStyle2D style = new MarkStyle2D();
		setStrokeStyle(styleInfo, style);
		setFillStyle(styleInfo, style);
		if (styleInfo.getSymbol().getCircle() != null) {
			float r = styleInfo.getSymbol().getCircle().getR();
			style.setShape(new Ellipse2D.Float(-0.5f * r, -0.5f * r, r, r));
			style.setSize((int) r);
		} else if (styleInfo.getSymbol().getRect() != null) {
			double w = styleInfo.getSymbol().getRect().getW();
			double h = styleInfo.getSymbol().getRect().getH();
			style.setShape(new Rectangle2D.Double(-0.5f * w, -0.5 * h, w, h));
			style.setSize((int) h);
		}
		return style;
	}

	public Style2D createIconStyle(FeatureStyleInfo styleInfo) {
		// is this correct or are symbols always opaque ?
		Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, styleInfo.getFillOpacity());
		IconStyle2D style = new IconStyle2D(getImageIcon(styleInfo), null, 0f, 0f, 0f, composite);
		return style;
	}
	
	private void setStrokeStyle(FeatureStyleInfo styleInfo, LineStyle2D style) {
		// Stroke color:
		Color c = createColorFromHTMLCode(styleInfo.getStrokeColor());
		style.setContour(c);

		// Stroke width and dash array:
		BasicStroke stroke;
		if (styleInfo.getDashArray() != null && styleInfo.getDashArray().length() > 0) {
			StringTokenizer tokenizer = new StringTokenizer(styleInfo.getDashArray(), ",");
			float[] dash = new float[tokenizer.countTokens()];
			int count = 0;
			boolean errors = false;
			while (tokenizer.hasMoreElements()) {
				try {
					float f = Float.parseFloat(tokenizer.nextElement().toString());
					dash[count++] = f;
				} catch (Exception e) {
					errors = true;
					break;
				}
			}
			if (errors) {
				stroke = new BasicStroke(styleInfo.getStrokeWidth());
			} else {
				stroke = new BasicStroke(styleInfo.getStrokeWidth(), BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND, 1.0f, dash, 0);
			}
		} else {
			stroke = new BasicStroke(styleInfo.getStrokeWidth());
		}
		style.setStroke(stroke);

		// Stroke opacity:
		float opacity = styleInfo.getStrokeOpacity();
		AlphaComposite strokeComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
		style.setContourComposite(strokeComposite);
	}

	private void setFillStyle(FeatureStyleInfo styleInfo, PolygonStyle2D style) {
		// Fill color:
		Color fillColor = createColorFromHTMLCode(styleInfo.getFillColor());
		style.setFill(fillColor);

		// Fill opacity:
		AlphaComposite fillComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, styleInfo.getFillOpacity());
		style.setFillComposite(fillComposite);
	}

	/**
	 * Create a Color object from a HTML color code.
	 *
	 * @param htmlCode The HTML color code. In the form of "#RRGGBB".
	 * @return Returns the correct color. In case anything went wrong, this
	 *         function will automatically return white.
	 */
	private Color createColorFromHTMLCode(String htmlCode) {
		if (htmlCode == null) {
			return Color.WHITE;
		}
		try {
			String colourCode = htmlCode;
			if (colourCode.charAt(0) == '#') {
				colourCode = colourCode.substring(1);
			}
			int r = Integer.parseInt(colourCode.substring(0, 2), 16);
			int g = Integer.parseInt(colourCode.substring(2, 4), 16);
			int b = Integer.parseInt(colourCode.substring(4, 6), 16);

			return new Color(r, g, b);
		} catch (Exception e) {
			return Color.WHITE;
		}
	}
	
	private Icon getImageIcon(FeatureStyleInfo styleInfo) {
		try {
			String href = styleInfo.getSymbol().getImage().getHref();
			BufferedImage img = IMAGES.get(href);
			if (img == null) {
				// checks absolute uri or classpath or web context
				URL url = null;
				Resource resource = applicationContext.getResource(href);
				if (resource.exists()) {
					url = resource.getURL();
					img = ImageIO.read(url);
					IMAGES.put(href, img);
				}
			}
			if (img == null) {
				log.warn("could not load symbol image");
				return createMissingIcon();
			}
			int width = styleInfo.getSymbol().getImage().getWidth();
			int height = styleInfo.getSymbol().getImage().getHeight();
			if (img.getWidth() != width || img.getHeight() != height) {
				double scaleY = (double) height / img.getHeight();
				double scaleX = (double) width / img.getWidth();
				AffineTransform scaleTx = AffineTransform.getScaleInstance(scaleX, scaleY);
				AffineTransformOp ato = new AffineTransformOp(scaleTx, AffineTransformOp.TYPE_BILINEAR);
				img = ato.filter(img, null);
			}
			return new ImageIcon(img);
		} catch (Exception e) {
			log.warn("could not transform symbol image", e);
			return createMissingIcon();
		}
	}
	
	private ImageIcon createMissingIcon() {
		return new ImageIcon(getClass().getResource("/org/geomajas/plugin/rasterizing/missing_icon.png"));
	}



}
