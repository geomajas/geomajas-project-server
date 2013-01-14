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
package org.geomajas.spring;

import org.geomajas.configuration.client.ScaleInfo;

/**
 * Bean to test ScaleInfo configuration.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ScaleInfoHolder {
	
	private ScaleInfo scaleInfo;

	public ScaleInfo getScaleInfo() {
		return scaleInfo;
	}
	
	public void setScaleInfo(ScaleInfo scaleInfo) {
		this.scaleInfo = scaleInfo;
	}
		

}
