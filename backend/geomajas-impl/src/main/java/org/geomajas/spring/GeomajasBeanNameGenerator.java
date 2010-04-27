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

package org.geomajas.spring;

import org.geomajas.command.Command;
import org.geomajas.global.ExpectAlternatives;
import org.geomajas.service.BeanNameSimplifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Automatically apply Geomajas naming convention for spring beans. If the bean implements one interface, it is the
 * fully qualified name of the interface (without "org.geomajas."). Alternatively it is the fully qualified class name
 * (without "org.geomajas.").
 * 
 * @author Joachim Van der Auwera
 */
@Component("service.BeanNameSimplifier")
public class GeomajasBeanNameGenerator extends AnnotationBeanNameGenerator implements BeanNameGenerator,
		BeanNameSimplifier {

	private final Logger log = LoggerFactory.getLogger(GeomajasBeanNameGenerator.class);

	private static final String SKIP_PREFIX = "org.geomajas.";

	private static final String COMMAND_PACKAGE = ".command.";

	/**
	 * Remove the skipped 'org.geomajas.' prefix from bean name when present. This often allows conversion from class to
	 * bean name for configuration. For commands (when the fully qualified name contains ".command."), remove everything
	 * before the command, and try to reduce duplication of the sub package. This will reduce
	 * "org.geomajas.command.get.GetConfigurationCommand" to "command.get.configuration"
	 * 
	 * @param orgName
	 *            original name which may contain the prefix
	 * @return bean name, guaranteed without prefix
	 */
	public String simplify(String orgName) {
		String name = orgName;
		if (name.contains(COMMAND_PACKAGE)) {
			// if this is a command package -> remove everything before "command", and reduce duplication
			name = name.substring(name.indexOf(COMMAND_PACKAGE) + 1);
			if (name.endsWith("Command")) {
				name = name.substring(0, name.length() - 7);
			}
			int dotPos = name.indexOf('.', 8);
			if (dotPos > 1) {
				String test = name.substring(8, dotPos).toLowerCase();
				String rest = name.substring(dotPos + 1).toLowerCase();
				if (rest.startsWith(test)) {
					name = name.substring(0, dotPos + 1) + name.substring(dotPos + 1 + test.length());
				} else if (rest.endsWith(test)) {
					name = name.substring(0, name.length() - test.length());
				}
			}
		}
		if (name.startsWith(SKIP_PREFIX)) {
			name = name.substring(SKIP_PREFIX.length());
		}
		return name;
	}

	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		// use name provided in annotation if any
		if (definition instanceof AnnotatedBeanDefinition) {
			String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
			if (StringUtils.hasText(beanName)) { // Explicit bean name found.
				return beanName;
			}
		}
		// calculate name based on Geomajas conventions
		try {
			String name = definition.getBeanClassName();
			if (!name.contains(COMMAND_PACKAGE)) {
				// quick simplification, all commands should have class name as name (except those with "Default"
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				if (null == classLoader) {
					classLoader = this.getClass().getClassLoader();
				}
				final Class clazz = ClassUtils.forName(name, classLoader);
				Class[] interfaces = ClassUtils.getAllInterfacesForClass(clazz);
				if (interfaces.length == 1) {
					// only apply if not annotated with "ExpectAlternatives"
					final Class interfaceClass = interfaces[0];
					if (!interfaceClass.equals(Command.class) &&
							!interfaceClass.isAnnotationPresent(ExpectAlternatives.class)) {
						name = interfaceClass.getName();
					}
				}
			}
			return simplify(name);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return super.generateBeanName(definition, registry);
	}

}
