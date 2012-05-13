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
package org.geomajas.widget.featureinfo.configuration.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Configuration properties for custom widgets.
 * 
 * @author Kristof Heirwegh
 */
public class WidgetBuilderInfo implements ClientWidgetInfo {

	private static final long serialVersionUID = 100L;

	/**
	 * Use this identifier in your configuration files (beans).
	 */
	public static final String IDENTIFIER = "WidgetBuilderInfo";

	/**
	 * The name (key) of a builder for the custom Widget. (the type will depend
	 * on the face, eg. a Canvas builder when using GWT)
	 * <p />
	 * Do not forget to add your builder to the widget factory (in EntryPoint.onModuleLoad).
	 */
	private String builderName;

	private List<Parameter> parameters = new ArrayList<Parameter>();

	/**
	 * Get widget parameters.
	 *
	 * @return parameters
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * Set widget parameters.
	 *
	 * @param parameters parameters
	 */
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Get builder name, this needs to be registered in the WidgetFactory class.
	 *
	 * @return name of builder
	 */
	public String getBuilderName() {
		return builderName;
	}

	/**
	 * Set builder name, this needs to be registered in the WidgetFactory class.
	 *
	 * @param builderName name of builder
	 */
	public void setBuilderName(String builderName) {
		this.builderName = builderName;
	}
}
