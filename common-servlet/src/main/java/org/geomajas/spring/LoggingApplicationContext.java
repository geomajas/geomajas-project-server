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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.AliasDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.parsing.EmptyReaderEventListener;
import org.springframework.beans.factory.parsing.ImportDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Spring context that allows to detect which bean has been defined in which context file. To use this context, add the
 * following to your web.xml:
 * 
 * <pre>
 * 
 * &lt;context-param>
 *   &lt;param-name>contextClass&lt;/param-name>
 *   &lt;param-value>org.geomajas.spring.LoggingApplicationContext&lt;/param-value>
 * &lt;/context-param>
 * </pre>
 * 
 * @author Jan De Moerloose
 * 
 */
public class LoggingApplicationContext extends XmlWebApplicationContext {

	private final Logger log = LoggerFactory.getLogger(LoggingApplicationContext.class);

	@Override
	protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader) {
		super.initBeanDefinitionReader(reader);
		reader.setEventListener(new LoggingEventListener());
	}

	/**
	 * Logging listener.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class LoggingEventListener extends EmptyReaderEventListener {

		@Override
		public void componentRegistered(ComponentDefinition componentDefinition) {
			log.info("Registered component [" + componentDefinition.getName() + "]");
			for (BeanDefinition bd : componentDefinition.getBeanDefinitions()) {
				String name = bd.getBeanClassName();
				if (bd instanceof BeanComponentDefinition) {
					name = ((BeanComponentDefinition) bd).getBeanName();
				}
				log.info("Registered bean definition: [" + name + "]" + " from " + bd.getResourceDescription());
			}
		}

		@Override
		public void aliasRegistered(AliasDefinition aliasDefinition) {
			log.info("Registered alias [" + aliasDefinition.getAlias() + "] for bean " + aliasDefinition.getBeanName());
		}

		@Override
		public void importProcessed(ImportDefinition importDefinition) {
			log.info("Processed import [" + importDefinition.getImportedResource() + "]");
		}

	}

}