/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.spring;

import org.geomajas.annotation.ExpectAlternatives;
import org.geomajas.command.Command;
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

	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "REC_CATCH_EXCEPTION")
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
					classLoader = this.getClass().getClassLoader(); // NOPMD
				}
				final Class clazz = ClassUtils.forName(name, classLoader);
				Class[] interfaces = ClassUtils.getAllInterfacesForClass(clazz);
				if (interfaces.length == 1) {
					// only apply if not annotated with "ExpectAlternatives"
					final Class interfaceClass = interfaces[0];
					if (!isCobertura(interfaceClass) && useInterfaceForBeanName(interfaceClass)) {
						name = interfaceClass.getName();
					}
				} else if (interfaces.length == 2) {
					// maybe one of the two is a Cobertura class... try the other
					if (isCobertura(interfaces[0])) {
						final Class interfaceClass = interfaces[1];
						if (!isCobertura(interfaceClass) && useInterfaceForBeanName(interfaceClass)) {
							name = interfaceClass.getName();
						}
					} else if (isCobertura(interfaces[1])) {
						final Class interfaceClass = interfaces[0];
						if (!isCobertura(interfaceClass) && useInterfaceForBeanName(interfaceClass)) {
							name = interfaceClass.getName();
						}
					}
				}
			}
			return simplify(name);
		} catch (Exception e) { // NOSONAR
			log.error(e.getMessage(), e);
		}
		return super.generateBeanName(definition, registry);
	}

	private boolean useInterfaceForBeanName(Class interfaceClass) {
		return !interfaceClass.equals(Command.class) &&
				!interfaceClass.isAnnotationPresent(ExpectAlternatives.class) &&
				// backwards compatible !
				!interfaceClass.isAnnotationPresent(org.geomajas.global.ExpectAlternatives.class);
	}

	private boolean isCobertura(Class interfaceClass) {
		return "net.sourceforge.cobertura.coveragedata.HasBeenInstrumented".equals(interfaceClass.getName());
	}

}
