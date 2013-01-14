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
package org.geomajas.plugin.runtimeconfig.service.factory;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.GeometryAttributeInfo;
import org.geomajas.configuration.ImageInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.geotools.GeoToolsLayer;
import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.geomajas.service.GeoService;
import org.geomajas.service.ResourceService;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Default implementation of {@link org.geomajas.plugin.runtimeconfig.service.BeanFactory} for
 * {@link GeoToolsLayer} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class GeoToolsLayerBeanFactory extends BaseVectorLayerBeanFactory {

	public static final String LOCATION = "location";

	public static final String PARAMETERS = "parameters";

	public static final String URL = "url";

	@Autowired
	private ResourceService resourceService;

	private Random rand = new Random();

	@Autowired
	private GeoService geoService;

	protected GeoToolsLayerBeanFactory() {
		super(GeoToolsLayer.class);
		addToIgnoreList(LOCATION);
	}

	@Override
	public Priority getPriority(Map<String, Object> parameters) {
		Priority priority = Priority.DEFAULT;
		priority = priority.and(checkString(BEAN_NAME, parameters));
		priority = priority.and(checkEquals(CLASS_NAME, className, parameters));
		priority = priority.and(checkOptionalCollection(PARAMETERS, Parameter.class, parameters));
		priority = priority.and(checkOptionalString(LOCATION, parameters));
		priority = priority.and(checkOptionalClass(LAYER_INFO, VectorLayerInfo.class, parameters));
		if (checkString(LOCATION, parameters) == Priority.NONE
				&& checkClass(LAYER_INFO, VectorLayerInfo.class, parameters) == Priority.NONE) {
			return Priority.NONE;
		}
		return priority;
	}

	@Override
	public List<BeanDefinitionHolder> createBeans(Map<String, Object> parameters) throws RuntimeConfigException {
		Resource resource = null;
		if (checkString(LOCATION, parameters) != Priority.NONE) {
			try {
				resource = resourceService.find((String) parameters.get(LOCATION));
				parameters.put(URL, resource.getURL().toExternalForm());
			} catch (Exception e) {
				throw new RuntimeConfigException(RuntimeConfigException.BAD_PARAMETER, LOCATION, e.getMessage());
			}
		}
		if (checkClass(LAYER_INFO, VectorLayerInfo.class, parameters) == Priority.NONE) {
			// generate the layer info
			try {
				VectorLayerInfo info = generateDefaultInfo(resource.getURL());
				parameters.put(LAYER_INFO, info);
			} catch (Exception e) {
				throw new RuntimeConfigException(RuntimeConfigException.BAD_PARAMETER, LOCATION, e.getMessage());
			}
		}
		// add styles or replace them if they were passed to us
		if (checkCollection(STYLE_INFO, NamedStyleInfo.class, parameters) == Priority.NONE) {
			VectorLayerInfo info = getObject(LAYER_INFO, parameters);
			parameters.put(STYLE_INFO, info.getNamedStyleInfos());
		} else {
			VectorLayerInfo info = getObject(LAYER_INFO, parameters);
			Collection<NamedStyleInfo> styles = getCollection(STYLE_INFO, parameters);
			info.setNamedStyleInfos(new ArrayList<NamedStyleInfo>(styles));
		}
		return super.createBeans(parameters);
	}

	private VectorLayerInfo generateDefaultInfo(URL url) throws Exception {
		ShapefileDataStore store = new ShapefileDataStore(url);
		SimpleFeatureType schema = store.getFeatureSource().getSchema();
		VectorLayerInfo info = new VectorLayerInfo();
		setCRS(info, schema.getCoordinateReferenceSystem());
		setLayerType(info, schema.getGeometryDescriptor());
		setMaxExtent(info);
		setFeatureInfo(info, schema);
		setRandomStyle(info, schema);
		return info;
	}

	private void setRandomStyle(VectorLayerInfo info, SimpleFeatureType schema) {
		NamedStyleInfo styleInfo = new NamedStyleInfo();
		styleInfo.setName(schema.getTypeName() + ".default");
		FeatureStyleInfo featureStyle = null;
		switch (info.getLayerType()) {
			case MULTILINESTRING:
			case LINESTRING:
				featureStyle = createRandomLineStyle();
				break;
			case MULTIPOINT:
			case POINT:
				featureStyle = createRandomPointStyle();
				break;
			case MULTIPOLYGON:
			case POLYGON:
			default:
				featureStyle = createRandomPolygonStyle();
				break;
		}
		styleInfo.getFeatureStyles().add(featureStyle);
		// first identifying is label
		String labelName = null;
		if (info.getFeatureInfo().getIdentifier() != null) {
			labelName = info.getFeatureInfo().getIdentifier().getName();
		} else {
			labelName = info.getFeatureInfo().getAttributes().get(0).getName();
		}
		styleInfo.setLabelStyle(createRandomLabelStyle(labelName));
		info.getNamedStyleInfos().add(styleInfo);
	}

	private LabelStyleInfo createRandomLabelStyle(String attributeName) {
		LabelStyleInfo style = new LabelStyleInfo();
		style.setBackgroundStyle(createRandomPolygonStyle());
		FontStyleInfo fontStyle = new FontStyleInfo();
		fontStyle.setColor(style.getBackgroundStyle().getStrokeColor());
		fontStyle.setFamily("Verdana");
		fontStyle.setOpacity(1F);
		fontStyle.setSize(8);
		fontStyle.setWeight("normal");
		fontStyle.setStyle("normal");
		style.setFontStyle(fontStyle);
		style.setLabelAttributeName(attributeName);
		return style;
	}

	private FeatureStyleInfo createRandomPolygonStyle() {
		FeatureStyleInfo featureStyle = new FeatureStyleInfo();
		Color strokeColor = createRandomColor();
		featureStyle.setFillColor(toCss(strokeColor.brighter()));
		featureStyle.setFillOpacity(0.5F);
		featureStyle.setIndex(0);
		featureStyle.setStrokeColor(toCss(strokeColor));
		featureStyle.setStrokeOpacity(0.5F);
		featureStyle.setStrokeWidth(1);
		featureStyle.setName("default");
		return featureStyle;
	}

	private FeatureStyleInfo createRandomPointStyle() {
		FeatureStyleInfo featureStyle = new FeatureStyleInfo();
		Color strokeColor = createRandomColor();
		featureStyle.setFillColor(toCss(strokeColor.brighter()));
		featureStyle.setFillOpacity(0.5F);
		featureStyle.setIndex(0);
		featureStyle.setStrokeColor(toCss(strokeColor));
		featureStyle.setStrokeOpacity(0.5F);
		featureStyle.setStrokeWidth(1);
		SymbolInfo symbol = new SymbolInfo();
		ImageInfo image = new ImageInfo();
		image.setWidth(20);
		image.setHeight(20);
		int nr = rand.nextInt(10);
		image.setHref("myimage" + nr + ".png");
		image.setSelectionHref("myimage" + nr + "-select.png");
		symbol.setImage(image);
		featureStyle.setSymbol(symbol);
		featureStyle.setName("default");
		return featureStyle;
	}

	private FeatureStyleInfo createRandomLineStyle() {
		FeatureStyleInfo featureStyle = new FeatureStyleInfo();
		Color strokeColor = createRandomColor();
		featureStyle.setFillColor(toCss(strokeColor.brighter()));
		featureStyle.setFillOpacity(0);
		featureStyle.setIndex(0);
		featureStyle.setStrokeColor(toCss(strokeColor));
		featureStyle.setStrokeOpacity(1);
		featureStyle.setStrokeWidth(1);
		featureStyle.setName("default");
		return featureStyle;
	}

	private Color createRandomColor() {
		return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
	}

	private String toCss(Color color) {
		return "#" + Integer.toHexString((color.getRGB() & 0xffffff) | 0x1000000).substring(1);

	}

	private void setFeatureInfo(VectorLayerInfo info, SimpleFeatureType schema) {
		FeatureInfo featureInfo = new FeatureInfo();
		GeometryAttributeInfo geomInfo = new GeometryAttributeInfo();
		geomInfo.setEditable(true);
		geomInfo.setName(schema.getGeometryDescriptor().getLocalName());
		featureInfo.setGeometryType(geomInfo);
		featureInfo.setDataSourceName(schema.getTypeName());
		List<AttributeDescriptor> attrs = schema.getAttributeDescriptors();
		for (AttributeDescriptor attributeDescriptor : attrs) {
			if (attributeDescriptor != schema.getGeometryDescriptor()) {
				Class<?> binding = attributeDescriptor.getType().getBinding();
				PrimitiveAttributeInfo attrInfo = new PrimitiveAttributeInfo();
				if (binding == Boolean.class) {
					attrInfo.setType(PrimitiveType.BOOLEAN);
				} else if (binding == Short.class) {
					attrInfo.setType(PrimitiveType.SHORT);
				} else if (binding == Integer.class) {
					attrInfo.setType(PrimitiveType.INTEGER);
				} else if (binding == Long.class) {
					attrInfo.setType(PrimitiveType.LONG);
				} else if (binding == Float.class) {
					attrInfo.setType(PrimitiveType.FLOAT);
				} else if (binding == Double.class) {
					attrInfo.setType(PrimitiveType.DOUBLE);
				} else if (binding == Date.class) {
					attrInfo.setType(PrimitiveType.DATE);
				} else if (binding == String.class) {
					attrInfo.setType(PrimitiveType.STRING);
				}
				attrInfo.setEditable(true);
				attrInfo.setIdentifying(attributeDescriptor.getType().isIdentified());
				attrInfo.setLabel(attributeDescriptor.getLocalName());
				attrInfo.setName(attributeDescriptor.getLocalName());
				if (attributeDescriptor.getType().isIdentified() && featureInfo.getIdentifier() == null) {
					featureInfo.setIdentifier(attrInfo);
				} else {
					featureInfo.getAttributes().add(attrInfo);
				}
			}
		}
		info.setFeatureInfo(featureInfo);
	}

	private void setMaxExtent(VectorLayerInfo info) {
		CoordinateReferenceSystem crs;
		// hack to avoid http://jira.geomajas.org/browse/GBE-76
		if ("EPSG:4326".equals(info.getCrs())) {
			info.setMaxExtent(new Bbox(-180, -80, 360, 160));
		} else {
			try {
				crs = geoService.getCrs(info.getCrs());
				double xmin = crs.getCoordinateSystem().getAxis(0).getMinimumValue();
				double xmax = crs.getCoordinateSystem().getAxis(0).getMaximumValue();
				double ymin = crs.getCoordinateSystem().getAxis(1).getMinimumValue();
				double ymax = crs.getCoordinateSystem().getAxis(1).getMaximumValue();
				info.setMaxExtent(new Bbox(xmin, ymin, xmax - xmin, ymax - ymin));
			} catch (LayerException e) {
				info.setMaxExtent(Bbox.ALL);
			}
		}
	}

	private void setCRS(VectorLayerInfo info, CoordinateReferenceSystem crs) {
		if (crs instanceof GeographicCRS) {
			// spherical coordinate system, this is not a projection but probably should be plate carree !
			if ("GCS_WGS_1984".equals(crs.getName().getCode())) {
				info.setCrs("EPSG:4326");
			} else {
				throw new IllegalArgumentException("Unknown geographic CRS " + crs + ", expected "
						+ DefaultGeographicCRS.WGS84);
			}
		} else {
			info.setCrs("EPSG:" + geoService.getSridFromCrs(crs));
		}
	}

	private void setLayerType(VectorLayerInfo info, GeometryDescriptor geoType) {
		if (geoType.getType().getBinding() == Point.class) {
			info.setLayerType(LayerType.POINT);
		} else if (geoType.getType().getBinding() == MultiPoint.class) {
			info.setLayerType(LayerType.MULTIPOINT);
		} else if (geoType.getType().getBinding() == LineString.class) {
			info.setLayerType(LayerType.LINESTRING);
		} else if (geoType.getType().getBinding() == MultiLineString.class) {
			info.setLayerType(LayerType.MULTILINESTRING);
		} else if (geoType.getType().getBinding() == Polygon.class) {
			info.setLayerType(LayerType.POLYGON);
		} else if (geoType.getType().getBinding() == MultiPolygon.class) {
			info.setLayerType(LayerType.MULTIPOLYGON);
		}
	}

}
