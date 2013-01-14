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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.layer.feature.InternalFeaturePropertyAccessor;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.FeatureExpressionService;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
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
 * @author Jan De Moerloose
 */
@Component
public class FeatureExpressionServiceImpl implements FeatureExpressionService {

	private final SpelExpressionParser parser = new SpelExpressionParser();

	private final List<PropertyAccessor> propertyAccessors = new ArrayList<PropertyAccessor>();

	private final Map<String, Object> variables = new HashMap<String, Object>();

	private final Map<String, Expression> expressionCache = new ConcurrentHashMap<String, Expression>();

	/**
	 * Construct a new service instance.
	 */
	public FeatureExpressionServiceImpl() {
		propertyAccessors.add(new InternalFeaturePropertyAccessor());
	}

	/** {@inheritDoc} */
	public Object evaluate(String expression, InternalFeature feature) throws LayerException {
		StandardEvaluationContext context = new StandardEvaluationContext(feature);
		// set a new list of accessors, custom accessors first !
		List<PropertyAccessor> allAccessors = new ArrayList<PropertyAccessor>(propertyAccessors);
		allAccessors.addAll(context.getPropertyAccessors());
		context.setPropertyAccessors(allAccessors);
		// set context variables
		context.setVariables(variables);
		try {
			return getExpression(expression).getValue(context);
		} catch (ParseException e) {
			throw new LayerException(e, ExceptionCode.EXPRESSION_INVALID, expression, feature.getLayer().getId());
		} catch (EvaluationException e) {
			throw new LayerException(e, ExceptionCode.EXPRESSION_EVALUATION_FAILED, expression, feature.getLayer()
					.getId());
		}
	}

	/** {@inheritDoc} */
	public void setVariables(Map<String, Object> variables) {
		this.variables.putAll(variables);
	}

	/**
	 * Fetch the specified expression from the cache or create it if necessary.
	 * 
	 * @param expressionString the expression string
	 * @return the expression
	 * @throws ParseException oops
	 */
	private Expression getExpression(String expressionString) throws ParseException {
		if (!expressionCache.containsKey(expressionString)) {
			Expression expression;
			expression = parser.parseExpression(expressionString);
			expressionCache.put(expressionString, expression);
		}
		return expressionCache.get(expressionString);

	}

}
