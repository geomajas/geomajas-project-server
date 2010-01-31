/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.internal.service;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.internal.layer.feature.EditableFeature;
import org.geomajas.internal.layer.feature.RenderedFeatureImpl;
import org.geomajas.internal.layer.feature.ClippedInternalFeature;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.AttributeConverter;
import org.geomajas.service.BboxService;
import org.geomajas.service.FeatureConverter;
import org.geomajas.service.GeometryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Converter for feature objects between the server-side feature representation and the DTO version for client-server
 * communication.
 *
 * @author Pieter De Graef
 */
@Component
public class FeatureConverterImpl implements FeatureConverter {

	@Autowired
	private BboxService bboxService;

	@Autowired
	private GeometryConverter geometryConverter;

	@Autowired
	private AttributeConverter attributeConverter;

	/**
	 * Convert the server side feature to a DTO feature that can be sent to the client.
	 *
	 * @param feature The server-side feature representation.
	 * @return Returns the DTO feature.
	 */
	public Feature toDto(InternalFeature feature) {
		if (feature == null) {
			return null;
		}
		Feature dto = new Feature(feature.getId());
		FeatureInfo info = feature.getLayer().getLayerInfo().getFeatureInfo();
		dto.setAttributes(toDto(feature.getAttributes(), info));
		dto.setLabel(feature.getLabel());
		dto.setGeometry(geometryConverter.toDto(feature.getGeometry()));
		if (feature.getStyleInfo() != null) {
			dto.setStyleId((int) feature.getStyleInfo().getId());
		}

		if (feature instanceof ClippedInternalFeature) {
			ClippedInternalFeature vFeature = (ClippedInternalFeature) feature;
			dto.setClipped(vFeature.isClipped());
		} else if (feature instanceof EditableFeature) {
			EditableFeature eFeature = (EditableFeature) feature;
			dto.setEditable(eFeature.isEditable());
		}
		return dto;
	}

	/**
	 * Convert a DTO feature to a server-side feature.
	 *
	 * @param dto The DTO feature that comes from the client.
	 * @return Returns a server-side feature object.
	 */
	public InternalFeature toFeature(Feature dto) {
		if (dto == null) {
			return null;
		}
		InternalFeature feature;
		if (dto.isEditable()) {
			feature = new EditableFeature(bboxService);
			((EditableFeature) feature).setEditable(true);
		} else {
			feature = new ClippedInternalFeature(new RenderedFeatureImpl(bboxService));
			((ClippedInternalFeature) feature).setClipped(dto.isClipped());
		}
		feature.setAttributes(toFeature(dto.getAttributes()));
		feature.setId(dto.getId());
		feature.setLabel(dto.getLabel());
		feature.setGeometry(geometryConverter.toJts(dto.getGeometry()));
		return feature;
	}

	private Map<String, Attribute> toDto(Map<String, Object> attributes, FeatureInfo info) {
		HashMap<String, Attribute> map = new HashMap<String, Attribute>();
		for (AttributeInfo attributeInfo : info.getAttributes()) {
			String name = attributeInfo.getName();
			if (attributes.containsKey(name)) {
				Object attribute = attributes.get(name);
				map.put(name, attributeConverter.toDto(attribute, attributeInfo));
			}
		}
		return map;
	}

	private Map<String, Object> toFeature(Map<String, Attribute> attributes) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (String name : attributes.keySet()) {
			map.put(name, attributeConverter.toObject(attributes.get(name)));
		}
		return map;
	}

}
