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
package org.geomajas.plugin.admin.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.geomajas.plugin.admin.AdminException;
import org.geomajas.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link ContextConfiguratorService} based on obtaining a {@link BeanDefinitionRegistry} from the
 * application context. This is possible for subclasses of <code>GenericApplicationContext</code> and
 * <code>AbstractRefreshableApplicationContext</code>, which covers more or less all implementations of
 * <code>ApplicationContext</code>.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class ContextConfiguratorServiceImpl implements ContextConfiguratorService {

	private static final String PROTOTYPE_PREFIX = "Proto";

	private static final String REWIRE_CONFIG_XML_PATH = "org/geomajas/plugin/admin/service/rewireConfig.xml";

	private final Logger log = LoggerFactory.getLogger(ContextConfiguratorServiceImpl.class);

	@Autowired
	private ApplicationContext applicationContext;

	private BeanDefinitionRegistry registry;

	private Resource[] rewireContextLocations;

	private String[] rewireClassNames;

	private List<Class<?>> rewireClasses;

	public ContextConfiguratorServiceImpl() {
		rewireContextLocations = new Resource[] { new ClassPathResource(REWIRE_CONFIG_XML_PATH) };
		rewireClassNames = new String[] { Rewirable.class.getName(), ConfigurationService.class.getName() };
	}

	public void configureBeanDefinition(String beanName, BeanDefinition beanDefinition) throws AdminException {
		BeanDefinition oldDefinition = null;
		if (getRegistry().containsBeanDefinition(beanName)) {
			oldDefinition = getRegistry().getBeanDefinition(beanName);
		}
		try {
			getRegistry().registerBeanDefinition(beanName, beanDefinition);
			// initialize if it's a singleton
			if (!beanDefinition.isAbstract() && beanDefinition.isSingleton()) {
				applicationContext.getBean(beanName);
			}
			rewireAll();
		} catch (BeanDefinitionStoreException e) {
			restoreContext(Collections.singletonMap(beanName, oldDefinition), null);
			throw new AdminException(e, AdminException.INVALID_BEAN_DEFINITION, beanName);
		} catch (BeansException e) {
			restoreContext(Collections.singletonMap(beanName, oldDefinition), null);
			throw new AdminException(e, AdminException.BEAN_CREATION_FAILED, beanName);
		}
	}

	public void configureBeanDefinitions(List<BeanDefinitionHolder> holders) throws AdminException {
		Map<String, BeanDefinition> oldDefinitions = new HashMap<String, BeanDefinition>();
		for (BeanDefinitionHolder holder : holders) {
			if (getRegistry().containsBeanDefinition(holder.getBeanName())) {
				oldDefinitions.put(holder.getBeanName(), getRegistry().getBeanDefinition(holder.getBeanName()));
			}
		}
		try {
			for (BeanDefinitionHolder holder : holders) {
				BeanDefinition beanDefinition = holder.getBeanDefinition();
				getRegistry().registerBeanDefinition(holder.getBeanName(), holder.getBeanDefinition());
				// initialize if it's a singleton
				if (!beanDefinition.isAbstract() && beanDefinition.isSingleton()) {
					applicationContext.getBean(holder.getBeanName());
				}
			}
			rewireAll();
		} catch (BeanDefinitionStoreException e) {
			restoreContext(oldDefinitions, null);
			throw new AdminException(e, AdminException.INVALID_BEAN_DEFINITION, concatNames(holders));
		} catch (BeansException e) {
			restoreContext(oldDefinitions, null);
			throw new AdminException(e, AdminException.BEAN_CREATION_FAILED, concatNames(holders));
		}
	}

	private String concatNames(List<BeanDefinitionHolder> holders) {
		Iterator<BeanDefinitionHolder> it = holders.iterator();
		StringBuilder builder = new StringBuilder();
		while (it.hasNext()) {
			String part = it.next().getBeanName();
			builder.append(part);
			if (it.hasNext()) {
				builder.append(",");
			}
		}
		return builder.toString();
	}

	public void configureBeanDefinitions(String location) throws AdminException {
		List<String> names;
		try {
			names = extractBeanNames(location);
		} catch (BeanDefinitionStoreException e) {
			throw new AdminException(e, AdminException.INVALID_BEAN_DEFINITION_LOCATION, location);
		}
		Map<String, BeanDefinition> oldDefinitions = new HashMap<String, BeanDefinition>();
		List<String> newDefinitions = new ArrayList<String>();
		for (String name : names) {
			if (getRegistry().containsBeanDefinition(name)) {
				oldDefinitions.put(name, getRegistry().getBeanDefinition(name));
			} else {
				newDefinitions.add(name);
			}
		}
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(getRegistry());
		try {
			reader.loadBeanDefinitions(location);
			for (String name : names) {
				BeanDefinition beanDefinition = getRegistry().getBeanDefinition(name);
				if (!beanDefinition.isAbstract() && beanDefinition.isSingleton()) {
					applicationContext.getBean(name);
				}
			}
			rewireAll();
		} catch (BeanDefinitionStoreException e) {
			restoreContext(oldDefinitions, newDefinitions);
			throw new AdminException(e, AdminException.INVALID_BEAN_DEFINITION_LOCATION, location);
		} catch (BeansException e) {
			restoreContext(oldDefinitions, newDefinitions);
			throw new AdminException(e, AdminException.BEAN_CREATION_FAILED_LOCATION, location);
		}
	}

	public void removeBeanDefinition(String beanName) throws AdminException {
		try {
			getRegistry().removeBeanDefinition(beanName);
		} catch (NoSuchBeanDefinitionException e) {
			// ok for the semantics of this method
		}
		rewireAll();
	}

	public Resource[] getRewireContextLocations() {
		return rewireContextLocations;
	}

	public void setRewireContextLocations(Resource[] rewireContextLocations) {
		this.rewireContextLocations = rewireContextLocations;
	}

	public String[] getRewireClassNames() {
		return rewireClassNames;
	}

	public void setRewireClassNames(String[] rewireClassNames) {
		this.rewireClassNames = rewireClassNames;
	}

	@PostConstruct
	public void postConstruct() throws ClassNotFoundException {
		if (applicationContext instanceof GenericApplicationContext) {
			registry = (GenericApplicationContext) applicationContext;
		} else if (applicationContext instanceof ConfigurableApplicationContext) {
			ConfigurableListableBeanFactory factory = ((ConfigurableApplicationContext) applicationContext)
					.getBeanFactory();
			if (factory instanceof BeanDefinitionRegistry) {
				registry = (BeanDefinitionRegistry) factory;
			} else {
				log.warn("Bean factory is not a registry, dynamic configuration will fail");
			}
		} else {
			log.warn("Cannot dynamically configure application context of type " + applicationContext.getClass());
		}
		rewireClasses = new ArrayList<Class<?>>();
		for (String className : rewireClassNames) {
			rewireClasses.add(Class.forName(className));
		}
	}

	private BeanDefinitionRegistry getRegistry() throws AdminException {
		if (registry != null) {
			return registry;
		} else {
			throw new AdminException(AdminException.CONFIGURATION_NOT_SUPPORTED);
		}
	}

	private void restoreContext(Map<String, BeanDefinition> toAdd, List<String> toRemove) throws AdminException {
		try {
			for (String name : toAdd.keySet()) {
				BeanDefinition beanDefinition = toAdd.get(name);
				getRegistry().registerBeanDefinition(name, beanDefinition);
				if (!beanDefinition.isAbstract() && beanDefinition.isSingleton()) {
					applicationContext.getBean(name);
				}
			}
			if (toRemove != null) {
				for (String name : toRemove) {
					if (getRegistry() != null && getRegistry().containsBeanDefinition(name)) {
						getRegistry().removeBeanDefinition(name);
					}
				}
			}
			rewireAll();
		} catch (Exception e) {
			try {
				// this is probably fatal, our final resort is a refresh !
				if (applicationContext instanceof ConfigurableApplicationContext) {
					((ConfigurableApplicationContext) applicationContext).refresh();
				}
			} catch (Exception e1) {
				throw new AdminException(e, AdminException.CONTEXT_RESTORE_FAILED);
			}
		}
	}

	private List<String> extractBeanNames(String location) throws BeanDefinitionStoreException {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
		reader.loadBeanDefinitions(location);
		return Arrays.asList(registry.getBeanDefinitionNames());
	}

	private void rewireAll() throws BeanDefinitionStoreException, BeansException {
		GenericApplicationContext identityContext = new GenericApplicationContext(applicationContext);
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(identityContext);
		xmlReader.loadBeanDefinitions(getRewireContextLocations());
		Set<String> allNames = new HashSet<String>();
		for (Class<?> clazz : rewireClasses) {
			allNames.addAll(BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, clazz).keySet());
		}
		for (String name : allNames) {
			GenericBeanDefinition identityFactory = new GenericBeanDefinition();
			identityFactory.setBeanClass(IdentityFactory.class);
			identityFactory.setFactoryMethodName(IdentityFactory.FACTORY_METHOD);
			identityFactory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			ConstructorArgumentValues values = new ConstructorArgumentValues();
			values.addGenericArgumentValue(new RuntimeBeanReference(name, true));
			identityFactory.setConstructorArgumentValues(values);
			identityContext.registerBeanDefinition(name + PROTOTYPE_PREFIX, identityFactory);
		}
		identityContext.refresh();
		for (String name : allNames) {
			identityContext.getBean(name + PROTOTYPE_PREFIX);
		}
		identityContext.destroy();
	}

}
