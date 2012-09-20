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
package org.geomajas.plugin.deskmanager.domain.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.client.ClientLayerInfo;

/**
 * @author Kristof Heirwegh
 */
public abstract class LayerConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PARAM_SOURCE_TYPE = "_source_type_";

	public static final String SOURCE_TYPE_WMS = "WMS";

	public static final String SOURCE_TYPE_WFS = "WFS";

	public static final String SOURCE_TYPE_SHAPE = "SHAPE";

	public static final String SOURCE_TYPE_DATABASE = "DATABASE";

	protected List<Parameter> parameters = new ArrayList<Parameter>(); // connectionparameters

	// -------------------------------------------------

	public abstract ClientLayerInfo getClientLayerInfo();

	public abstract LayerInfo getServerLayerInfo();

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public String getParameterValue(String key) {
		if (parameters != null) {
			for (Parameter p : parameters) {
				if (key.equals(p.getName())) {
					return p.getValue();
				}
			}
		}
		return null;
	}

	public Parameter getParameter(String key) {
		if (parameters != null) {
			for (Parameter p : parameters) {
				if (key.equals(p.getName())) {
					return p;
				}
			}
		}
		return null;
	}
}
