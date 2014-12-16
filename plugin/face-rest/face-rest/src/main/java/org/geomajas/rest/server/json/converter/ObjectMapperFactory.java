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
package org.geomajas.rest.server.json.converter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geomajas.command.CommandResponse;
import org.geomajas.rest.server.json.mixin.ResponseMixin;

/**
 * Factory class for {@link ObjectMapper} .
 *
 * @author  Dosi Bingov
 */
public final class ObjectMapperFactory {
	private static ObjectMapper objectMapper;

	private ObjectMapperFactory() {
	}

	public static ObjectMapper create() {
		objectMapper = new ObjectMapper();

		//use fields instead of getters and setters
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
		objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

		//add dto's specific annotations here
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.addMixInAnnotations(CommandResponse.class, ResponseMixin.class);

		return objectMapper;
	}


}
