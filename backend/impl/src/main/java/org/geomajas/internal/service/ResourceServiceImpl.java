/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.internal.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.ResourceService;
import org.geomajas.service.resource.ResourceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link ResourceService}.
 * 
 * @author Jan De Moerloose
 */
@Component
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ApplicationContext applicationContext;

	private List<String> rootPaths = new ArrayList<String>();
	
	@Autowired(required = false)
	private List<ResourceInfo> resourcesInfos;

	/** {@inheritDoc} */
	public Resource find(String location) throws GeomajasException {
		Resource resource = applicationContext.getResource(location);
		if (resource.exists()) {
			return resource;
		} else {
			String cpResource;
			if (location.startsWith("/")) {
				cpResource = GeomajasConstant.CLASSPATH_URL_PREFIX + location.substring(1);
			} else {
				cpResource = GeomajasConstant.CLASSPATH_URL_PREFIX + location;
			}
			resource = applicationContext.getResource(cpResource);
			if (resource.exists()) {
				return resource;
			} else {
				for (String root : rootPaths) {
					if (root.endsWith("/")) {
						resource = applicationContext.getResource(root + location);
					} else {
						resource = applicationContext.getResource(root + "/" + location);
					}
					if (resource.exists()) {
						return resource;
					}
				}
				throw new GeomajasException(ExceptionCode.RESOURCE_NOT_FOUND, location);
			}
		}
	}

	/** {@inheritDoc} */
	public List<String> getRootPaths() {
		return rootPaths;
	}

	/** {@inheritDoc} */
	public void setRootPaths(List<String> rootPaths) {
		this.rootPaths = rootPaths;
	}
	
	@PostConstruct
	protected void postConstruct() {
		if (resourcesInfos != null) {
			for (ResourceInfo resourcesInfo : resourcesInfos) {
				rootPaths.add(resourcesInfo.getRootPath());
			}
		}
	}

}
