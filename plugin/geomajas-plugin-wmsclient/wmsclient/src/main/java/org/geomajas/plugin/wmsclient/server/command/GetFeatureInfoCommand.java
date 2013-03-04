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
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.geomajas.command.Command;
import org.geomajas.geometry.conversion.jts.GeometryConverterService;
import org.geomajas.geometry.conversion.jts.JtsConversionException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.Feature;
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
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Command that executes a WMS GetFeatureInfo request.
 * 
 * @author Pieter De Graef
 */
@Component
public class GetFeatureInfoCommand implements Command<GetFeatureInfoRequest, GetFeatureInfoResponse> {

	private static final String PARAM_FORMAT = "info_format";

	public void execute(GetFeatureInfoRequest request, GetFeatureInfoResponse response) throws Exception {
		URL url = new URL(request.getUrl());
		GML gml;

		GetFeatureInfoFormat format = getFormatFromUrl(request.getUrl());
		switch (format) {
			case GML2:
				gml = new GML(Version.GML2);
				response.setFeatures(getFeaturesFromUrl(url, gml));
				break;
			case GML3:
				gml = new GML(Version.GML3);
				response.setFeatures(getFeaturesFromUrl(url, gml));
				break;
			default:
				String content = readUrl(url);
				response.setWmsResponse(content);
		}
	}

	public GetFeatureInfoResponse getEmptyCommandResponse() {
		return new GetFeatureInfoResponse();
	}

	private List<Feature> getFeaturesFromUrl(URL url, GML gml) throws IOException, SAXException,
			ParserConfigurationException {
		List<Feature> dtoFeatures = new ArrayList<Feature>();
		FeatureCollection<?, SimpleFeature> collection = gml.decodeFeatureCollection(url.openStream());
		FeatureIterator<SimpleFeature> it = collection.features();
		while (it.hasNext()) {
			try {
				SimpleFeature feature = it.next();
				dtoFeatures.add(toDto(feature));
			} catch (Exception e) {
				continue;
			}
		}
		return dtoFeatures;
	}

	@SuppressWarnings("rawtypes")
	private Feature toDto(SimpleFeature feature) throws IllegalArgumentException {
		if (feature == null) {
			throw new IllegalArgumentException("");
		}
		Feature dto = new Feature(feature.getID());

		HashMap<String, Attribute> attributes = new HashMap<String, Attribute>();

		for (AttributeDescriptor desc : feature.getType().getAttributeDescriptors()) {
			Object obj = feature.getAttribute(desc.getName());
			if (null != obj) {
				attributes.put(desc.getLocalName(), new StringAttribute(obj.toString()));
			}
		}
		dto.setAttributes(attributes);
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