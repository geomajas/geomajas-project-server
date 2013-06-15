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

package org.geomajas.internal.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.LegendGraphicService;
import org.geomajas.service.ResourceService;
import org.geomajas.service.StyleConverterService;
import org.geomajas.service.legend.LegendGraphicMetadata;
import org.geomajas.sld.UserStyleInfo;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.renderer.lite.MetaBufferEstimator;
import org.geotools.renderer.lite.StyledShapePainter;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.visitor.RescaleStyleVisitor;
import org.geotools.util.NumberRange;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.style.RasterSymbolizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.io.WKTReader;

/**
 * Default implementation of {@link LegendGraphicService}.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 */
@Component
public class LegendGraphicServiceImpl implements LegendGraphicService {

	/**
	 * Default path of raster layer icon image.
	 */
	private static final String DEFAULT_RASTER_IMAGE_PATH = "org/geomajas/internal/image/layer-raster.png";

	@Autowired
	private StyleConverterService styleConverterService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ResourceService resourceService;

	private SLDStyleFactory styleFactory = new SLDStyleFactory();

	private StyledShapePainter shapePainter = new StyledShapePainter();

	private int defaultWidth = LegendGraphicMetadata.DEFAULT_WIDTH;

	private int defaultHeight = LegendGraphicMetadata.DEFAULT_HEIGHT;
	
	private String rasterImagePath = DEFAULT_RASTER_IMAGE_PATH;

	private final Logger log = LoggerFactory.getLogger(LegendGraphicServiceImpl.class);

	/**
	 * Get default width.
	 *
	 * @return default width
	 */
	public int getDefaultWidth() {
		return defaultWidth;
	}

	/**
	 * Set default width.
	 *
	 * @param defaultWidth default width
	 */
	public void setDefaultWidth(int defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	/**
	 * Get default height.
	 *
	 * @return default height
	 */
	public int getDefaultHeight() {
		return defaultHeight;
	}

	/**
	 * Set default height.
	 *
	 * @param defaultHeight default height
	 */
	public void setDefaultHeight(int defaultHeight) {
		this.defaultHeight = defaultHeight;
	}

	/**
	 * Get raster image path.
	 *
	 * @return raster image path
	 */
	public String getRasterImagePath() {
		return rasterImagePath;
	}

	/**
	 * Set raster image path.
	 *
	 * @param rasterImagePath raster image path
	 */
	public void setRasterImagePath(String rasterImagePath) {
		this.rasterImagePath = rasterImagePath;
	}

	/**
	 * Get legend image.
	 *
	 * @param legendMetadata the legend metadata
	 * @return rendered image
	 * @throws GeomajasException cannot render image
	 */
	public RenderedImage getLegendGraphic(LegendGraphicMetadata legendMetadata) throws GeomajasException {
		return getLegendGraphicInternal(legendMetadata);
	}
	
	private BufferedImage getLegendGraphicInternal(LegendGraphicMetadata legendMetadata) throws GeomajasException {
		Style style = null;
		VectorLayer vectorLayer = null;
		Layer<?> layer = configurationService.getLayer(legendMetadata.getLayerId());
		if (layer instanceof VectorLayer) {
			vectorLayer = (VectorLayer) layer;
		}
		UserStyleInfo userStyleInfo = legendMetadata.getUserStyle();
		if (userStyleInfo != null) {
			style = styleConverterService.convert(userStyleInfo);
		} else {
			// try to get style from layer
			String styleName = null;
			if (legendMetadata.getNamedStyle() != null) {
				styleName = legendMetadata.getNamedStyle().getName();
			}
			if (vectorLayer != null) {
				NamedStyleInfo namedStyle;
				if (styleName != null) {
					namedStyle = vectorLayer.getLayerInfo().getNamedStyleInfo(styleName);
					if (namedStyle == null) {
						throw new LayerException(ExceptionCode.STYLE_NOT_FOUND, styleName, legendMetadata.getLayerId());
					}
				} else {
					namedStyle = vectorLayer.getLayerInfo().getNamedStyleInfos().get(0);
				}
				style = styleConverterService.convert(namedStyle.getUserStyle());
			}
		}
		Rule rule = null;
		if (legendMetadata.getRule() != null) {
			rule = styleConverterService.convert(legendMetadata.getRule());
		} else if (style != null) {
			rule = style.featureTypeStyles().get(0).rules().get(0);
		}

		int width = legendMetadata.getWidth() > 0 ? legendMetadata.getWidth() : defaultWidth;
		int height = legendMetadata.getHeight() > 0 ? legendMetadata.getHeight() : defaultHeight;
		if (rule != null) {
			SimpleFeature sampleFeature = createSampleFeature(vectorLayer);
			
			MetaBufferEstimator estimator = new MetaBufferEstimator();
			estimator.visit(rule);
			double buffer = (double) estimator.getBuffer();
			double scale = Math.min(width / (buffer + 1), height / (buffer + 1));
			if (scale < 1) {
				RescaleStyleVisitor rescaler = new RescaleStyleVisitor(scale);
				rescaler.visit(rule);
				rule = (Rule) rescaler.getCopy();
			}

			Symbolizer[] symbolizers = rule.getSymbolizers();

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = image.createGraphics();
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			NumberRange<Double> scaleRange = NumberRange.create(0, Double.MAX_VALUE);

			for (Symbolizer symbolizer : symbolizers) {
				// only handle non raster/text symbolizers here
				if (!(symbolizer instanceof RasterSymbolizer) && !(symbolizer instanceof TextSymbolizer)) {
					Style2D style2d = styleFactory.createStyle(sampleFeature, symbolizer, scaleRange);
					LiteShape2 shape = getSampleShape(symbolizer, width, height);
					if (style2d != null && shape != null) {
						shapePainter.paint(graphics, shape, style2d, 1.0);
					}
				}
			}
			graphics.dispose();
			return image;
		} else if (layer instanceof RasterLayer) {
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = image.createGraphics();
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.drawImage(getImage(getRasterImagePath()), 0, 0, width, height, null);
			graphics.dispose();
			return image;
		} else {
			throw new GeomajasException(ExceptionCode.LAYER_NOT_FOUND, legendMetadata.getLayerId());
		}
	}
	
	@Override
	public RenderedImage getLegendGraphics(List<LegendGraphicMetadata> legendMetadata) throws GeomajasException {
		int width = 0;
		int height = 0;
		for (LegendGraphicMetadata lmd : legendMetadata) {
			width = Math.max(width, lmd.getWidth() > 0 ? lmd.getWidth() : defaultWidth);
			height += lmd.getHeight() > 0 ? lmd.getHeight() : defaultHeight;
		}
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		
		int y = 0;
		for (LegendGraphicMetadata lmd : legendMetadata) {
			graphics.drawImage(getLegendGraphicInternal(lmd), null, 0, y);
			y += lmd.getHeight();
		}
		graphics.dispose();
		
		return image;
		
	}

	private SimpleFeature createSampleFeature(VectorLayer vectorLayer) {
		SimpleFeatureType type;
		try {
			String geomName = "the_geom";
			if (vectorLayer != null) {
				geomName = vectorLayer.getLayerInfo().getFeatureInfo().getGeometryType().getName();
			}
			type = DataUtilities.createType("Sample", geomName + ":Geometry");
		} catch (SchemaException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return SimpleFeatureBuilder.template(type, null);
	}

	private LiteShape2 getSampleShape(Symbolizer symbolizer, int width, int height) {
		MetaBufferEstimator estimator = new MetaBufferEstimator();
		estimator.visit(symbolizer);
		// add an extra pixel to the margin (odd line widths were not always shown)
		int margin = estimator.getBuffer() + 1;
		// we define a shape in WKT of size 1 x 1 and transform it to a rectangle of (width-margin) x (height-margin),
		// to make sure we capture thick strokes
		MathTransform transform = new AffineTransform2D(width - margin, 0, 0, height - margin, 0.5 * margin,
				0.5 * margin);
		try {
			WKTReader wktReader = new WKTReader();
			if (symbolizer instanceof LineSymbolizer) {
				return new LiteShape2(wktReader.read("LINESTRING (0 0, 0.66 0.33, 0.33 0.66, 1 1)"), transform, null,
						false);
			} else if (symbolizer instanceof PolygonSymbolizer) {
				return new LiteShape2(wktReader.read("POLYGON ((0 0, 1 0, 1 1, 0 1, 0 0))"), transform, null, false);
			} else if (symbolizer instanceof PointSymbolizer) {
				return new LiteShape2(wktReader.read("POINT (0.5 0.5)"), transform, null, false);
			} else {
				return null;
			}
		} catch (Exception e) { // NOSONAR
			// should not happen
			log.warn("Could not create sample shapes", e);
			return null;
		}
	}

	private BufferedImage getImage(String href) throws GeomajasException {
		InputStream is = null;
		try {
			Resource resource = resourceService.find(href);
			if (resource != null) {
				is = resource.getInputStream();
			} else {
				// backwards compatibility
				resource = resourceService.find("images/" + href);
				if (null == resource) {
					resource = resourceService.find("image/" + href);
				}
				if (resource != null) {
					is = resource.getInputStream();
				} else {
					is = ClassLoader.getSystemResourceAsStream(href);
				}
			}
			if (is == null) {
				throw new GeomajasException(ExceptionCode.RESOURCE_NOT_FOUND, href);
			}
			return ImageIO.read(is);
		} catch (IOException io) {
			throw new GeomajasException(io, ExceptionCode.RESOURCE_NOT_FOUND, href);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

}
