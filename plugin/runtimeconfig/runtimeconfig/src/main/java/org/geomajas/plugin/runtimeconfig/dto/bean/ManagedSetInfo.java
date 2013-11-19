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
package org.geomajas.plugin.runtimeconfig.dto.bean;

import java.util.HashSet;

/**
 * Data transfer object for a Spring managed set bean meta-data element.
 * 
 * @author Jan De Moerloose
 */
public class ManagedSetInfo extends HashSet<BeanMetadataElementInfo> implements BeanMetadataElementInfo {

	private static final long serialVersionUID = 100L;
}
