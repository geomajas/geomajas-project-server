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
package org.geomajas.plugin.runtimeconfig.service.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

/**
 * Base implementation of {@link org.geomajas.plugin.runtimeconfig.service.BeanFactory} for all
 * {@link org.geomajas.layer.VectorLayer} beans.
 * 
 * @author Jan De Moerloose
 * 
 */
public class BaseVectorLayerBeanFactory extends BaseBeanFactory {

	public static final String LAYER_INFO = "layerInfo";

	public static final String STYLE_INFO = "styleInfo";

	protected BaseVectorLayerBeanFactory(Class<?> clazz) {
		this(clazz.getName());
	}

	protected BaseVectorLayerBeanFactory(String className) {
		super(className);
		addToIgnoreList(STYLE_INFO);
	}

	@Override
	public Priority getPriority(Map<String, Object> parameters) {
		Priority priority = super.getPriority(parameters);
		priority = priority.and(checkClass(LAYER_INFO, VectorLayerInfo.class, parameters));
		priority = priority.and(checkOptionalClass(STYLE_INFO, NamedStyleInfo.class, parameters));
		return priority;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BeanDefinitionHolder> createBeans(Map<String, Object> parameters) throws RuntimeConfigException {
		// we must have style info at this point !
		if (checkCollection(STYLE_INFO, NamedStyleInfo.class, parameters) != Priority.NONE) {
			Collection<NamedStyleInfo> styles = getCollection(STYLE_INFO, parameters);
			// add styles as bean refs			
			for (NamedStyleInfo style : styles) {
				addBeanRef(style.getName(), style, parameters);
			}
			// add styles as objects (must come first, order is important !!!!)
			List<BeanDefinitionHolder> bdh = new ArrayList<BeanDefinitionHolder>();
			for (NamedStyleInfo style : styles) {
				bdh.add(new BeanDefinitionHolder(beanDefinitionDtoConverterService
						.createBeanDefinitionByIntrospection(style), style.getName()));
			}
			// add the layer bean
			bdh.addAll(super.createBeans(parameters));
			return bdh;
		} else {
			throw new RuntimeConfigException(RuntimeConfigException.BAD_PARAMETER,
					"Missing style information for layer");
		}
	}
}
