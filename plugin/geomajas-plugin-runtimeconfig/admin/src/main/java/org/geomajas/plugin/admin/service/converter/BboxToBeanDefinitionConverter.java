/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.admin.service.converter;

import javax.annotation.PostConstruct;

import org.geomajas.geometry.Bbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.stereotype.Component;

/**
 * Converts {@link Bbox} to {@link BeanDefinition}. Necessary because of redundant getter/setters.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component("BboxToBeanDefinitionConverter")
public class BboxToBeanDefinitionConverter implements Converter<Bbox, BeanDefinition> {

	@Autowired
	private ConverterRegistry converterRegistry;

	public BeanDefinition convert(Bbox source) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Bbox.class);
		builder.addPropertyValue("x", source.getX());
		builder.addPropertyValue("y", source.getY());
		builder.addPropertyValue("maxX", source.getMaxX());
		builder.addPropertyValue("maxY", source.getMaxY());
		return builder.getBeanDefinition();
	}

	@PostConstruct
	protected void register() {
		converterRegistry.addConverter(this);
	}

}
