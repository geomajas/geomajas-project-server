/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.runtimeconfig.service.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.geomajas.plugin.runtimeconfig.service.BeanDefinitionDtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

/**
 * Base implementation of {@link org.geomajas.plugin.runtimeconfig.service.BeanFactory} for all client layer beans.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class BaseClientLayerBeanFactory extends BaseBeanFactory {

	public static final String SERVER_LAYER_ID = "serverLayerId";

	public static final String LABEL = "label";

	public static final String MAP_ID = "mapId";

	@Autowired(required = false)
	private Map<String, ClientMapInfo> maps;

	@Autowired
	private BeanDefinitionDtoConverterService dtoConverterService;

	protected BaseClientLayerBeanFactory(Class<?> clazz) {
		super(clazz);
		addToIgnoreList(MAP_ID);
	}

	@Override
	public Priority getPriority(Map<String, Object> parameters) {
		Priority priority = super.getPriority(parameters);
		priority.and(checkString(SERVER_LAYER_ID, parameters));
		priority.and(checkString(LABEL, parameters));
		priority.and(checkOptionalString(MAP_ID, parameters));
		return priority;
	}

	@Override
	public List<BeanDefinitionHolder> createBeans(Map<String, Object> parameters) throws RuntimeConfigException {
		ClientLayerInfo clientLayerInfo = createBean();
		clientLayerInfo.setId(getString(BEAN_NAME, parameters));
		clientLayerInfo.setLabel(getString(LABEL, parameters));
		clientLayerInfo.setVisible(true);
		clientLayerInfo.setServerLayerId(getString(SERVER_LAYER_ID, parameters));
		processExtraParameters(clientLayerInfo, parameters);
		BeanDefinitionHolder layerBean = new BeanDefinitionHolder(
				dtoConverterService.createBeanDefinitionByIntrospection(clientLayerInfo), clientLayerInfo.getId());
		if (checkString(MAP_ID, parameters) != Priority.NONE) {
			String mapId = (String) parameters.get(MAP_ID);
			if (!maps.containsKey(mapId)) {
				throw new RuntimeConfigException(RuntimeConfigException.BAD_PARAMETER, MAP_ID, "No such map");
			} else {
				maps.get(mapId).getLayers().add(clientLayerInfo);
				BeanDefinitionHolder mapBean = new BeanDefinitionHolder(
						dtoConverterService.createBeanDefinitionByIntrospection(maps.get(mapId)), mapId);
				return Arrays.asList(layerBean, mapBean);
			}
		} else {
			return Arrays.asList(layerBean);
		}
	}

	protected abstract ClientLayerInfo createBean();

	protected abstract void processExtraParameters(ClientLayerInfo clientLayerInfo, Map<String, Object> parameters);
}
