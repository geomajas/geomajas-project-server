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

package org.geomajas.service;

import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.InternalFeature;

/**
 * This service evaluates feature expressions for {@link InternalFeature} objects. Expressions are textual and expressed
 * in a language that depends on the specific implementation of this service. The default implementation uses the Spring
 * Expression Language (SpEL), but other implementations may be based on eg. Freemarker/Velocity templates or even a
 * custom language. As a minimal requirement, the language should be able to evaluate simple references to primitive
 * attribute values.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 * 
 */
@Api(allMethods = true)
public interface FeatureExpressionService {

	/**
	 * Evaluates the specified expression for the specified feature. Evaluation may involve attribute access, which is
	 * subject to security limitations.
	 * 
	 * @param expression the expression to be evaluated (see class implementation)
	 * @param feature the internal feature
	 * @return the expression value
	 * @throws LayerException expression was invalid or could not be evaluated on this feature
	 */
	Object evaluate(String expression, InternalFeature feature) throws LayerException;

	/**
	 * Adds a map of variables to the expression context. The specified key will be used as the name to refer to this
	 * variable in the expression (using the conventions of the expression language for referencing it). Variables and
	 * their exposed methods should be thread-safe.
	 * 
	 * @param variables map of named variables
	 */
	void setVariables(Map<String, Object> variables);
}
