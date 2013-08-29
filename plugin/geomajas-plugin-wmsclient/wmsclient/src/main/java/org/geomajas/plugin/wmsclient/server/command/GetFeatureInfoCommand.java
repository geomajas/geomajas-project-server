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

package org.geomajas.plugin.wmsclient.server.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.geomajas.command.Command;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.geometry.conversion.jts.GeometryConverterService;
import org.geomajas.geometry.conversion.jts.JtsConversionException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.ShortAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.plugin.wmsclient.client.service.WmsService.GetFeatureInfoFormat;
import org.geomajas.plugin.wmsclient.server.command.dto.GetFeatureInfoRequest;
import org.geomajas.plugin.wmsclient.server.command.dto.GetFeatureInfoResponse;
import org.geotools.GML;
import org.geotools.GML.Version;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Command that executes a WMS GetFeatureInfo request.
 * 
 * @author Pieter De Graef
 * @author An Buyle
 */
@Component
public class GetFeatureInfoCommand implements Command<GetFeatureInfoRequest, GetFeatureInfoResponse> {

	private final Logger log = LoggerFactory.getLogger(GetFeatureInfoCommand.class);
	
	private static final String PARAM_FORMAT = "info_format";

	public void execute(GetFeatureInfoRequest request, GetFeatureInfoResponse response) throws Exception {
		URL url = new URL(request.getUrl());
		GML gml;

		GetFeatureInfoFormat format = getFormatFromUrl(request.getUrl());
		List<AbstractReadOnlyAttributeInfo> attributeInfos = new ArrayList<AbstractReadOnlyAttributeInfo>();
		switch (format) {
			case GML2:
				gml = new GML(Version.GML2);
				response.setFeatures(getFeaturesFromUrl(url, gml, attributeInfos));
				response.setAttributeDescriptors(attributeInfos);
				break;
			case GML3:
				gml = new GML(Version.GML3);
				response.setFeatures(getFeaturesFromUrl(url, gml, attributeInfos));
				response.setAttributeDescriptors(attributeInfos);
				break;
			default:
				String content = readUrl(url);
				response.setWmsResponse(content);
		}
	}

	public GetFeatureInfoResponse getEmptyCommandResponse() {
		return new GetFeatureInfoResponse();
	}

	private List<Feature> getFeaturesFromUrl(URL url, GML gml, List<AbstractReadOnlyAttributeInfo> attributeInfos)
			throws IOException, SAXException, ParserConfigurationException {

		List<Feature> dtoFeatures = new ArrayList<Feature>();
		FeatureCollection<?, SimpleFeature> collection = gml.decodeFeatureCollection(url.openStream());
		FeatureIterator<SimpleFeature> it = collection.features();
		if (it.hasNext()) {
			
				SimpleFeature feature = it.next();
				attributeInfos.clear();
				for (AttributeDescriptor desc : feature.getType().getAttributeDescriptors()) {
					attributeInfos.add(toAttributeInfo(desc));
				}
				try {	
					dtoFeatures.add(toDto(feature, attributeInfos));
				} catch (Exception e) {
			}
		}
		while (it.hasNext()) {
			SimpleFeature feature = it.next();
			try {
				dtoFeatures.add(toDto(feature, attributeInfos));
			} catch (Exception e) {
				continue;
			}
		}
		return dtoFeatures;
	}

	private AbstractReadOnlyAttributeInfo toAttributeInfo(AttributeDescriptor desc) throws IOException {
		Class<?> binding = desc.getType().getBinding();
		if (binding == null) {
			throw new IOException("No attribute binding found...");
		}
		String name = desc.getLocalName();
		AbstractReadOnlyAttributeInfo attributeInfo;
		if (Integer.class.equals(binding)) {
			attributeInfo = new PrimitiveAttributeInfo(name, name, PrimitiveType.INTEGER);
		} else if (Short.class.equals(binding)) {
			attributeInfo = new PrimitiveAttributeInfo(name, name, PrimitiveType.SHORT);
		} else if (Long.class.equals(binding)) {
			attributeInfo = new PrimitiveAttributeInfo(name, name, PrimitiveType.LONG);			
		} else if (Float.class.equals(binding)) {
			attributeInfo = new PrimitiveAttributeInfo(name, name, PrimitiveType.FLOAT);
		} else if (Double.class.equals(binding)) {
			attributeInfo = new PrimitiveAttributeInfo(name, name, PrimitiveType.DOUBLE);
		} else if (BigDecimal.class.equals(binding)) {
			attributeInfo = new PrimitiveAttributeInfo(name, name, PrimitiveType.DOUBLE);
		} else if (Boolean.class.equals(binding)) {
			attributeInfo = new PrimitiveAttributeInfo(name, name, PrimitiveType.BOOLEAN);
		} else {
			attributeInfo = new PrimitiveAttributeInfo(name, name, PrimitiveType.STRING);
		}
		return attributeInfo;
	}
	
	@SuppressWarnings("incomplete-switch")
	private Attribute<?> toPrimitive(Object value, PrimitiveType primitiveType) {

		Attribute<?> attribute = null;

		try {
			
			switch (primitiveType) {
				case BOOLEAN:
					attribute = new BooleanAttribute((Boolean) convertToClass(value, Boolean.class));
					break;
				case DOUBLE:
					attribute = new DoubleAttribute((Double) convertToClass(value, Double.class));
					break;
				case FLOAT:
					attribute = new FloatAttribute((Float) convertToClass(value, Float.class));
					break;
				case INTEGER:
					attribute = new IntegerAttribute((Integer) convertToClass(value, Integer.class));
					break;
				case LONG:
					attribute = new LongAttribute((Long) convertToClass(value, Long.class));
					break;
				case SHORT:
					attribute = new ShortAttribute((Short) convertToClass(value, Short.class));
					break;
				case STRING:
					attribute = new StringAttribute(value == null ? null : value.toString());
					break;
				default:
					//Unexpected!
					log.warn("toPrimitive() for unsupported type " + primitiveType);
					break;
			}
		
		} catch (NumberFormatException e) {
			switch (primitiveType) {
				case DOUBLE:
					attribute = new DoubleAttribute(null);
					break;
				case FLOAT:
					attribute = new FloatAttribute(null);
					break;
				case INTEGER:
					attribute = new IntegerAttribute(null);
					break;
				case LONG:
					attribute = new LongAttribute(null);
					break;
				case SHORT:
					attribute = new ShortAttribute(null);
			}
		}
		
		return attribute;
	}

	@SuppressWarnings("rawtypes")
	private Feature toDto(SimpleFeature feature,
					List<AbstractReadOnlyAttributeInfo> attributeInfos) throws IllegalArgumentException {
		if (feature == null) {
			throw new IllegalArgumentException("");
		}
		Feature dto = new Feature(feature.getID());

		HashMap<String, Attribute> attributeMap = new HashMap<String, Attribute>();

		for (AbstractReadOnlyAttributeInfo desc : attributeInfos) {
			Object obj = feature.getAttribute(desc.getName());
			Attribute<?> value = null;
			if (desc instanceof PrimitiveAttributeInfo) {
				value = toPrimitive(obj, ((PrimitiveAttributeInfo) desc).getType());
				attributeMap.put(desc.getName(), value);
			}
			
		}

		dto.setAttributes(attributeMap);
		dto.setId(feature.getID());

		dto.setUpdatable(false);
		dto.setDeletable(false);

		Object defaultGeometry = feature.getDefaultGeometry();
		if (defaultGeometry instanceof Geometry) {
			Geometry geometry = (Geometry) defaultGeometry;
			try {
				dto.setGeometry(GeometryConverterService.fromJts(geometry));
			} catch (JtsConversionException e) {
				// OK then, no geometry for you...
			}
		}

		return dto;
	}

//	private Attribute<?> toPrimitive(Object value, AttributeType type) {
//
//		if (type.getBinding().equals(String.class)) {
//			return new StringAttribute(value == null ? null : value.toString());
//		}
//		// Number attributes
//		try {
//			if ((Integer.class).equals(type.getBinding())) {
//				return new IntegerAttribute((Integer) convertToClass(value, Integer.class));
//			}
//			if ((Short.class).equals(type.getBinding())) {
//				return new ShortAttribute((Short) convertToClass(value, Short.class));
//			} else if (Long.class.equals(type.getBinding())) {
//				return new LongAttribute((Long) convertToClass(value, Long.class));
//			} else if (Float.class.equals(type.getBinding())) {
//				return new FloatAttribute((Float) convertToClass(value, Float.class));
//			} else if (Double.class.equals(type.getBinding())) {
//				return new DoubleAttribute((Double) convertToClass(value, Double.class));
//			} else if (BigDecimal.class.equals(type.getBinding())) {
//				return new DoubleAttribute((Double) convertToClass(Double.valueOf(value.toString()), Double.class));
//			}
//		} catch (NumberFormatException e) {
//			// Fallback to String
//			return new StringAttribute(value.toString());
//		}
//
//		if (Boolean.class.equals(type.getBinding())) {
//			return new BooleanAttribute((Boolean) convertToClass(value, Boolean.class));
//		}
//
//		return new StringAttribute(value == null ? null : value.toString());
//
//	}

	private Object convertToClass(Object object, Class<?> c) {
		if (object == null) {
			return null;
		} else if (c.isInstance(object)) {
			return object;
		} else {
			return fromString(object.toString(), c);
		}
	}

	private Object fromString(String str, Class<?> c) {
		if (c.equals(Integer.class)) {
			return Integer.parseInt(str);
		} else if (c.equals(Short.class)) {
			return Short.parseShort(str);
		} else if (c.equals(Long.class)) {
			return Long.parseLong(str);
		} else if (c.equals(Float.class)) {
			return Float.parseFloat(str);
		} else if (c.equals(Boolean.class)) {
			return Boolean.valueOf(str);
		}

		return null;
	}

	private GetFeatureInfoFormat getFormatFromUrl(String url) {
		try {
			int index = url.toLowerCase().indexOf(PARAM_FORMAT) + PARAM_FORMAT.length() + 1;
			String format = url.substring(index);
			index = format.indexOf('&');
			if (index > 0) {
				format = format.substring(0, index);
			}
			for (GetFeatureInfoFormat enumValue : GetFeatureInfoFormat.values()) {
				if (enumValue.toString().equalsIgnoreCase(format)) {
					return enumValue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return GetFeatureInfoFormat.HTML;
	}

	private String readUrl(URL url) throws Exception {
		URLConnection connection = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		StringBuilder response = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}
}