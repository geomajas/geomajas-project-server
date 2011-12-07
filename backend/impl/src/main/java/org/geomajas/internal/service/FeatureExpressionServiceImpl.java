/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.internal.layer.feature.InternalFeaturePropertyAccessor;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.FeatureExpressionService;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link FeatureExpressionService}, based on Spring Expression Language (SpEL). Expressions
 * are evaluated against an evaluation context that contains the feature as the root object. The feature attributes can
 * be accessed as if they were properties of the object. Sample expressions are:
 * <ul>
 * <li><code>myAttr</code> : evaluates to the value of this primitive attribute
 * <li><code>country.code</code> : evaluates to the nested code value of the many-to-one attribute country
 * <li><code>myVar.doIt(cities)</code> : evaluates to the return value of the doIt() method of the variable myVar. See
 * {@link #setVariables(Map)} on how to pass custom variables to the context. The argument is a list of association
 * values of the one-to-many attribute cities.
 * </ul>
 * 
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class FeatureExpressionServiceImpl implements FeatureExpressionService {

	private SpelExpressionParser parser = new SpelExpressionParser();

	private List<PropertyAccessor> propertyAccessors = new ArrayList<PropertyAccessor>();

	private Map<String, Object> variables = new HashMap<String, Object>();

	public FeatureExpressionServiceImpl() throws SecurityException, NoSuchMethodException {
		propertyAccessors.add(new InternalFeaturePropertyAccessor());
	}

	public Object evaluate(String expression, InternalFeature feature) {
		StandardEvaluationContext context = new StandardEvaluationContext(feature);
		// set a new list of accessors, custom accessors first !
		List<PropertyAccessor> allAccessors = new ArrayList<PropertyAccessor>(propertyAccessors);
		allAccessors.addAll(context.getPropertyAccessors());
		context.setPropertyAccessors(allAccessors);
		// set context variables
		context.setVariables(variables);
		return parser.parseExpression(expression).getValue(context);
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables.putAll(variables);
	}

}
