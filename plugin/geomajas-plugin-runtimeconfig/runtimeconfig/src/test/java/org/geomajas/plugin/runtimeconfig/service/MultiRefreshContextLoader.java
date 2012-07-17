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
package org.geomajas.plugin.runtimeconfig.service;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.support.GenericXmlContextLoader;

public class MultiRefreshContextLoader implements ContextLoader {

	private ContextLoader delegateLoader = new GenericXmlContextLoader();

	public ApplicationContext loadContext(String... locations) throws Exception {
		return new MultiRefreshContext(locations);
	}

	public String[] processLocations(Class<?> clazz, String... locations) {
		return delegateLoader.processLocations(clazz, locations);
	}

	class MultiRefreshContext implements ConfigurableApplicationContext {

		String[] locations;

		private ConfigurableApplicationContext currentContext;

		MultiRefreshContext(String... locations) {
			this.locations = locations;
			refresh();
		}

		public boolean containsBean(String name) {
			return currentContext.containsBean(name);
		}

		public boolean containsBeanDefinition(String beanName) {
			return currentContext.containsBeanDefinition(beanName);
		}

		public boolean containsLocalBean(String name) {
			return currentContext.containsLocalBean(name);
		}

		public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
			return currentContext.findAnnotationOnBean(beanName, annotationType);
		}

		public String[] getAliases(String name) {
			return currentContext.getAliases(name);
		}

		public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
			return currentContext.getAutowireCapableBeanFactory();
		}

		public <T> T getBean(Class<T> requiredType) throws BeansException {
			return currentContext.getBean(requiredType);
		}

		public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
			return currentContext.getBean(name, requiredType);
		}

		public Object getBean(String name, Object... args) throws BeansException {
			return currentContext.getBean(name, args);
		}

		public Object getBean(String name) throws BeansException {
			return currentContext.getBean(name);
		}

		public int getBeanDefinitionCount() {
			return currentContext.getBeanDefinitionCount();
		}

		public String[] getBeanDefinitionNames() {
			return currentContext.getBeanDefinitionNames();
		}

		public String[] getBeanNamesForType(Class type, boolean includeNonSingletons, boolean allowEagerInit) {
			return currentContext.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
		}

		public String[] getBeanNamesForType(Class type) {
			return currentContext.getBeanNamesForType(type);
		}

		public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
				throws BeansException {
			return currentContext.getBeansOfType(type, includeNonSingletons, allowEagerInit);
		}

		public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
			return currentContext.getBeansOfType(type);
		}

		public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
				throws BeansException {
			return currentContext.getBeansWithAnnotation(annotationType);
		}

		public ClassLoader getClassLoader() {
			return currentContext.getClassLoader();
		}

		public String getDisplayName() {
			return currentContext.getDisplayName();
		}

		public String getId() {
			return currentContext.getId();
		}

		public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
			return currentContext.getMessage(resolvable, locale);
		}

		public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
			return currentContext.getMessage(code, args, locale);
		}

		public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
			return currentContext.getMessage(code, args, defaultMessage, locale);
		}

		public ApplicationContext getParent() {
			return currentContext.getParent();
		}

		public BeanFactory getParentBeanFactory() {
			return currentContext.getParentBeanFactory();
		}

		public Resource getResource(String location) {
			return currentContext.getResource(location);
		}

		public Resource[] getResources(String locationPattern) throws IOException {
			return currentContext.getResources(locationPattern);
		}

		public long getStartupDate() {
			return currentContext.getStartupDate();
		}

		public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
			return currentContext.getType(name);
		}

		public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
			return currentContext.isPrototype(name);
		}

		public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
			return currentContext.isSingleton(name);
		}

		public boolean isTypeMatch(String name, Class targetType) throws NoSuchBeanDefinitionException {
			return currentContext.isTypeMatch(name, targetType);
		}

		public void publishEvent(ApplicationEvent event) {
			currentContext.publishEvent(event);
		}

		public void addApplicationListener(ApplicationListener listener) {
			currentContext.addApplicationListener(listener);
		}

		public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
			currentContext.addBeanFactoryPostProcessor(beanFactoryPostProcessor);
		}

		public void close() {
			currentContext.close();
		}

		public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
			return currentContext.getBeanFactory();
		}

		public boolean isActive() {
			return currentContext.isActive();
		}

		public boolean isRunning() {
			return currentContext.isRunning();
		}

		public void refresh() throws BeansException, IllegalStateException {
			try {
				currentContext = (ConfigurableApplicationContext) delegateLoader.loadContext(locations);
				currentContext.getBeanFactory().registerSingleton("refreshContext", this);
			} catch (Exception e) {
				throw new IllegalStateException("The context could not be reloaded", e);
			}
		}

		public void registerShutdownHook() {
			currentContext.registerShutdownHook();
		}

		public void setId(String id) {
			currentContext.setId(id);
		}

		public void setParent(ApplicationContext parent) {
			currentContext.setParent(parent);
		}

		public void start() {
			currentContext.start();
		}

		public void stop() {
			currentContext.stop();
		}

	}

}
