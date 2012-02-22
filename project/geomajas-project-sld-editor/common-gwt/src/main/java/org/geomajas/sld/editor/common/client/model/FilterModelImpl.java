/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.common.client.model;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.sld.editor.common.client.i18n.SldEditorMessages;
import org.geomajas.sld.expression.ExpressionInfo;
import org.geomajas.sld.expression.LiteralTypeInfo;
import org.geomajas.sld.expression.PropertyNameInfo;
import org.geomajas.sld.filter.BinaryComparisonOpTypeInfo;
import org.geomajas.sld.filter.ComparisonOpsTypeInfo;
import org.geomajas.sld.filter.FilterTypeInfo;
import org.geomajas.sld.filter.LogicOpsTypeInfo;
import org.geomajas.sld.filter.LowerBoundaryTypeInfo;
import org.geomajas.sld.filter.PropertyIsBetweenTypeInfo;
import org.geomajas.sld.filter.PropertyIsEqualToInfo;
import org.geomajas.sld.filter.PropertyIsGreaterThanInfo;
import org.geomajas.sld.filter.PropertyIsGreaterThanOrEqualToInfo;
import org.geomajas.sld.filter.PropertyIsLessThanInfo;
import org.geomajas.sld.filter.PropertyIsLessThanOrEqualToInfo;
import org.geomajas.sld.filter.PropertyIsLikeTypeInfo;
import org.geomajas.sld.filter.PropertyIsNotEqualToInfo;
import org.geomajas.sld.filter.PropertyIsNullTypeInfo;
import org.geomajas.sld.filter.UnaryLogicOpTypeInfo;
import org.geomajas.sld.filter.UpperBoundaryTypeInfo;

/**
 * Default implementation of {@link FilterModel}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class FilterModelImpl implements FilterModel {

	/** private members for filter form **/
	private static final String DEFAULT_WILD_CARD = "*";

	private static final String DEFAULT_WILD_CARD_SINGLE_CHAR = "?";

	private static final String DEFAULT_ESCAPE = "\\";

	private String propertyName;

	private String propertyValue;

	private String lowerValue;

	private String upperValue;

	private String patternMatchingWildCard = DEFAULT_WILD_CARD;

	private String patternMatchingSingleChar = DEFAULT_WILD_CARD_SINGLE_CHAR;

	private String patternMatchingEscape = DEFAULT_ESCAPE;

	private FilterTypeInfo filterTypeInfo;

	private OperatorType operatorType;

	private FilterModelState state;

	private String supportedWarning;
	
	private SldEditorMessages messages;

	public FilterModelImpl(SldEditorMessages messages) {
		this(null, messages);
		this.messages = messages;
	}

	public FilterModelImpl(FilterTypeInfo filterTypeInfo, SldEditorMessages messages) {
		this.filterTypeInfo = filterTypeInfo;
		this.messages = messages;
		parseFilter(filterTypeInfo);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String getLowerValue() {
		return lowerValue;
	}

	public void setLowerValue(String lowerValue) {
		this.lowerValue = lowerValue;
	}

	public String getUpperValue() {
		return upperValue;
	}

	public void setUpperValue(String upperValue) {
		this.upperValue = upperValue;
	}

	public void setOperatorType(OperatorType operatorType) {
		this.operatorType = operatorType;
	}

	public OperatorType getOperatorType() {
		return operatorType;
	}

	public String getPatternMatchingWildCard() {
		return patternMatchingWildCard;
	}

	public void setPatternMatchingWildCard(String patternMatchingWildCard) {
		this.patternMatchingWildCard = patternMatchingWildCard;
	}

	public String getPatternMatchingSingleChar() {
		return patternMatchingSingleChar;
	}

	public void setPatternMatchingSingleChar(String patternMatchingSingleChar) {
		this.patternMatchingSingleChar = patternMatchingSingleChar;
	}

	public String getPatternMatchingEscape() {
		return patternMatchingEscape;
	}

	public void setPatternMatchingEscape(String patternMatchingEscape) {
		this.patternMatchingEscape = patternMatchingEscape;
	}

	public FilterTypeInfo getFilterTypeInfo() {
		return filterTypeInfo;
	}

	public FilterModelState getState() {
		return state;
	}

	private void parseFilter(FilterTypeInfo filterTypeInfo) {
		if (null != filterTypeInfo) {
			if (filterTypeInfo.ifComparisonOps()) {
				ComparisonOpsTypeInfo op = filterTypeInfo.getComparisonOps();
				if (op.getClass().equals(PropertyIsLikeTypeInfo.class)) {
					operatorType = OperatorType.PROPERTY_IS_LIKE;
					propertyName = ((PropertyIsLikeTypeInfo) op).getPropertyName().getValue();
					propertyValue = ((PropertyIsLikeTypeInfo) op).getLiteral().getValue();
					patternMatchingWildCard = ((PropertyIsLikeTypeInfo) op).getWildCard();
					patternMatchingSingleChar = ((PropertyIsLikeTypeInfo) op).getSingleChar();
					patternMatchingEscape = ((PropertyIsLikeTypeInfo) op).getEscape();
				} else if (op.getClass().equals(PropertyIsEqualToInfo.class)) {
					operatorType = OperatorType.PROPERTY_IS_EQUAL_TO;
					processBinaryComparisonOp((BinaryComparisonOpTypeInfo) op);
				} else if (op.getClass().equals(PropertyIsNotEqualToInfo.class)) {
					operatorType = OperatorType.PROPERTY_IS_NOT_EQUAL_TO;
					processBinaryComparisonOp((BinaryComparisonOpTypeInfo) op);

				} else if (op.getClass().equals(PropertyIsGreaterThanInfo.class)) {
					operatorType = OperatorType.PROPERTY_IS_GREATER_THAN;
					processBinaryComparisonOp((BinaryComparisonOpTypeInfo) op);

				} else if (op.getClass().equals(PropertyIsGreaterThanOrEqualToInfo.class)) {
					operatorType = OperatorType.PROPERTY_IS_GREATER_THAN_OR_EQUAL;
					processBinaryComparisonOp((BinaryComparisonOpTypeInfo) op);

				} else if (op.getClass().equals(PropertyIsLessThanInfo.class)) {
					operatorType = OperatorType.PROPERTY_IS_LESS_THAN;
					processBinaryComparisonOp((BinaryComparisonOpTypeInfo) op);

				} else if (op.getClass().equals(PropertyIsLessThanOrEqualToInfo.class)) {
					operatorType = OperatorType.PROPERTY_IS_LESS_THAN_OR_EQUAL;
					processBinaryComparisonOp((BinaryComparisonOpTypeInfo) op);

				} else if (op.getClass().equals(PropertyIsBetweenTypeInfo.class)) {
					operatorType = OperatorType.PROPERTY_IS_BETWEEN;

					ExpressionInfo expressionInfo = ((PropertyIsBetweenTypeInfo) op).getExpression();

					if (expressionInfo.getClass().equals(PropertyNameInfo.class)) {
						propertyName = expressionInfo.getValue();
					} else {
						state = FilterModelState.UNSUPPORTED;
					}

					ExpressionInfo lowerBoundaryExpression = ((PropertyIsBetweenTypeInfo) op).getLowerBoundary()
							.getExpression();
					lowerValue = lowerBoundaryExpression.getValue();

					ExpressionInfo upperBoundaryExpression = ((PropertyIsBetweenTypeInfo) op).getUpperBoundary()
							.getExpression();
					upperValue = upperBoundaryExpression.getValue();

				} else if (op.getClass().equals(PropertyIsNullTypeInfo.class)) {
					operatorType = OperatorType.PROPERTY_IS_NULL;
					propertyName = ((PropertyIsNullTypeInfo) op).getPropertyName().getValue();
				} else {
					state = FilterModelState.UNSUPPORTED;
				}
			} else if (filterTypeInfo.ifLogicOps()) {
				LogicOpsTypeInfo op = filterTypeInfo.getLogicOps();
				/** Only the NOT operator is (partially) supported **/
				if (op.getClass().equals(UnaryLogicOpTypeInfo.class)) {
					UnaryLogicOpTypeInfo info = (UnaryLogicOpTypeInfo) op;
					if (info.ifComparisonOps()) {
						ComparisonOpsTypeInfo innerOp = info.getComparisonOps();

						if (innerOp.getClass().equals(PropertyIsNullTypeInfo.class)) {
							operatorType = OperatorType.PROPERTY_IS_NOT_NULL;
							propertyName = ((PropertyIsNullTypeInfo) innerOp).getPropertyName().getValue();
						} else if (innerOp.getClass().equals(PropertyIsLikeTypeInfo.class)) {
							operatorType = OperatorType.PROPERTY_IS_NOT_LIKE;
							propertyName = ((PropertyIsLikeTypeInfo) innerOp).getPropertyName().getValue();
							propertyValue = ((PropertyIsLikeTypeInfo) innerOp).getLiteral().getValue();
							patternMatchingWildCard = ((PropertyIsLikeTypeInfo) innerOp).getWildCard();
							patternMatchingSingleChar = ((PropertyIsLikeTypeInfo) innerOp).getSingleChar();
							patternMatchingEscape = ((PropertyIsLikeTypeInfo) innerOp).getEscape();

						} else if (innerOp.getClass().equals(PropertyIsBetweenTypeInfo.class)) {
							operatorType = OperatorType.PROPERTY_IS_NOT_BETWEEN;
							ExpressionInfo expressionInfo = ((PropertyIsBetweenTypeInfo) innerOp).getExpression();

							if (expressionInfo.getClass().equals(PropertyNameInfo.class)) {
								propertyName = expressionInfo.getValue();
							} else {
								state = FilterModelState.UNSUPPORTED;
							}

							ExpressionInfo lowerBoundaryExpression = ((PropertyIsBetweenTypeInfo) innerOp)
									.getLowerBoundary().getExpression();
							lowerValue = lowerBoundaryExpression.getValue();

							ExpressionInfo upperBoundaryExpression = ((PropertyIsBetweenTypeInfo) innerOp)
									.getUpperBoundary().getExpression();
							upperValue = upperBoundaryExpression.getValue();
						} else {
							state = FilterModelState.UNSUPPORTED;
						}
					}

				} else {
					state = FilterModelState.UNSUPPORTED;
				}
			} else {
				state = FilterModelState.UNSUPPORTED;
			}
		} else {
			state = FilterModelState.COMPLETE;
		}
		if (state == FilterModelState.UNSUPPORTED) {
			supportedWarning = messages.unsupportedFilter();
		}

	}

	private void processBinaryComparisonOp(BinaryComparisonOpTypeInfo op) {
		for (ExpressionInfo expressionInfo : op.getExpressionList()) {

			if (expressionInfo.getClass().equals(PropertyNameInfo.class)) {
				propertyName = expressionInfo.getValue();
			} else if (expressionInfo.getClass().equals(LiteralTypeInfo.class)) {
				propertyValue = expressionInfo.getValue();
			}
		}
	}

	public void synchronize() {
		if (state == FilterModelState.UNSUPPORTED) {
			// unsupported can't change
			return;
		} else if ((propertyName == null || propertyName.isEmpty())
				&& (propertyValue == null || propertyValue.isEmpty())) {
			filterTypeInfo = null;
			state = FilterModelState.COMPLETE;
		} else {
			if (filterTypeInfo == null) {
				filterTypeInfo = new FilterTypeInfo();
			}
			if (operatorType == null) {
				state = FilterModelState.INCOMPLETE;
			} else {
				state = FilterModelState.COMPLETE;
				switch (operatorType) {
					case PROPERTY_IS_BETWEEN:
						checkBetweenFilter();
						break;
					case PROPERTY_IS_EQUAL_TO:
					case PROPERTY_IS_GREATER_THAN:
					case PROPERTY_IS_GREATER_THAN_OR_EQUAL:
					case PROPERTY_IS_LESS_THAN:
					case PROPERTY_IS_LESS_THAN_OR_EQUAL:
					case PROPERTY_IS_LIKE:
					case PROPERTY_IS_NOT_EQUAL_TO:
						checkBinaryComparisonFilter();
						break;
					case PROPERTY_IS_NOT_LIKE:
						checkNotLikeFilter();
						break;
					case PROPERTY_IS_NOT_BETWEEN:
						checkNotBetweenFilter();
						break;
					case PROPERTY_IS_NOT_NULL:
						checkNotNullFilter();
						break;
					case PROPERTY_IS_NULL:
						checkNullFilter();
						break;
				}
			}
		}
	}

	private void checkNullFilter() {
		if (null == propertyName) {
			state = FilterModelState.INCOMPLETE;
		} else {
			PropertyIsNullTypeInfo propertyIsNullTypeInfo = new PropertyIsNullTypeInfo();
			// Assume filterAttributeName form field value not null (must be selected first)
			propertyIsNullTypeInfo.setPropertyName(new PropertyNameInfo(propertyName));
			filterTypeInfo.clearChoiceSelect();
			filterTypeInfo.setComparisonOps(propertyIsNullTypeInfo);
		}
	}

	/**
	 * @return true if filter object could be created/updated completely, else false
	 */

	private void checkNotNullFilter() {
		if (null == propertyName) {
			state = FilterModelState.INCOMPLETE;
		} else {
			UnaryLogicOpTypeInfo logicOps = new UnaryLogicOpTypeInfo(); /* create a NOT filter */
			PropertyIsNullTypeInfo propertyIsNullTypeInfo = new PropertyIsNullTypeInfo();

			// Assume filterAttributeName form field value not null (must be selected first)
			propertyIsNullTypeInfo.setPropertyName(new PropertyNameInfo(propertyName));

			logicOps.setComparisonOps(propertyIsNullTypeInfo);
			filterTypeInfo.clearChoiceSelect();
			filterTypeInfo.setLogicOps(logicOps);
		}
	}

	private void checkBetweenFilter() {

		PropertyIsBetweenTypeInfo info = new PropertyIsBetweenTypeInfo();
		if (null == propertyName || null == lowerValue || null == upperValue) {
			state = FilterModelState.INCOMPLETE;
		} else {
			info.setExpression(new PropertyNameInfo(propertyName));

			LowerBoundaryTypeInfo lowerBoundary = new LowerBoundaryTypeInfo();
			lowerBoundary.setExpression(new LiteralTypeInfo(lowerValue));
			info.setLowerBoundary(lowerBoundary);
			UpperBoundaryTypeInfo upperBoundary = new UpperBoundaryTypeInfo();
			upperBoundary.setExpression(new LiteralTypeInfo(upperValue));
			info.setUpperBoundary(upperBoundary);

			filterTypeInfo.clearChoiceSelect();
			filterTypeInfo.setComparisonOps(info);
		}
	}

	private void checkNotBetweenFilter() {

		PropertyIsBetweenTypeInfo info = new PropertyIsBetweenTypeInfo();

		if (null == propertyName || null == lowerValue || null == upperValue) {
			state = FilterModelState.INCOMPLETE;
		} else {
			info.setExpression(new PropertyNameInfo(propertyName));

			LowerBoundaryTypeInfo lowerBoundary = new LowerBoundaryTypeInfo();
			lowerBoundary.setExpression(new LiteralTypeInfo(lowerValue));
			info.setLowerBoundary(lowerBoundary);
			UpperBoundaryTypeInfo upperBoundary = new UpperBoundaryTypeInfo();
			upperBoundary.setExpression(new LiteralTypeInfo(upperValue));
			info.setUpperBoundary(upperBoundary);

			UnaryLogicOpTypeInfo logicOps = new UnaryLogicOpTypeInfo(); /* create a NOT filter */

			logicOps.setComparisonOps(info);
			filterTypeInfo.clearChoiceSelect();
			filterTypeInfo.setLogicOps(logicOps);
		}
	}

	private void checkNotLikeFilter() {

		if (null == propertyName || null == propertyValue) {
			state = FilterModelState.INCOMPLETE;
		} else {
			UnaryLogicOpTypeInfo logicOps = new UnaryLogicOpTypeInfo(); /* create a NOT filter */

			PropertyIsLikeTypeInfo info = new PropertyIsLikeTypeInfo();

			PropertyNameInfo propertyNameInfo = new PropertyNameInfo(propertyName);
			info.setPropertyName(propertyNameInfo);

			LiteralTypeInfo literal = new LiteralTypeInfo(propertyValue);
			info.setLiteral(literal);

			info.setWildCard(patternMatchingWildCard);
			info.setSingleChar(patternMatchingSingleChar);
			info.setEscape(patternMatchingEscape);

			logicOps.setComparisonOps(info);
			filterTypeInfo.clearChoiceSelect();
			filterTypeInfo.setLogicOps(logicOps);
		}
	}

	private void checkBinaryComparisonFilter() {

		if (null == propertyName || null == propertyValue) {
			state = FilterModelState.INCOMPLETE;
		} else {
			switch (operatorType) {
				case PROPERTY_IS_EQUAL_TO:
					PropertyIsEqualToInfo equal = new PropertyIsEqualToInfo();
					setBinaryComparisonOp(equal);
					break;
				case PROPERTY_IS_GREATER_THAN:
					PropertyIsGreaterThanInfo gt = new PropertyIsGreaterThanInfo();
					setBinaryComparisonOp(gt);
					break;
				case PROPERTY_IS_GREATER_THAN_OR_EQUAL:
					PropertyIsGreaterThanOrEqualToInfo gte = new PropertyIsGreaterThanOrEqualToInfo();
					setBinaryComparisonOp(gte);
					break;
				case PROPERTY_IS_LESS_THAN:
					PropertyIsLessThanInfo lt = new PropertyIsLessThanInfo();
					setBinaryComparisonOp(lt);
					break;
				case PROPERTY_IS_LESS_THAN_OR_EQUAL:
					PropertyIsLessThanOrEqualToInfo lte = new PropertyIsLessThanOrEqualToInfo();
					setBinaryComparisonOp(lte);
					break;
				case PROPERTY_IS_LIKE:
					PropertyIsLikeTypeInfo info = new PropertyIsLikeTypeInfo();

					PropertyNameInfo propertyNameInfo = new PropertyNameInfo(propertyName);
					info.setPropertyName(propertyNameInfo);

					LiteralTypeInfo literal = new LiteralTypeInfo(propertyValue);
					info.setLiteral(literal);

					info.setWildCard(patternMatchingWildCard);
					info.setSingleChar(patternMatchingSingleChar);
					info.setEscape(patternMatchingEscape);
					filterTypeInfo.clearChoiceSelect();
					filterTypeInfo.setComparisonOps(info);
					break;
				case PROPERTY_IS_NOT_EQUAL_TO:
					PropertyIsNotEqualToInfo ne = new PropertyIsNotEqualToInfo();
					setBinaryComparisonOp(ne);
					break;
			}
		}
	}

	private void setBinaryComparisonOp(BinaryComparisonOpTypeInfo op) {
		List<ExpressionInfo> expList = new ArrayList<ExpressionInfo>(2);

		expList.add(new PropertyNameInfo(propertyName));
		expList.add(new LiteralTypeInfo(propertyValue));

		op.setExpressionList(expList);
		filterTypeInfo.clearChoiceSelect();
		filterTypeInfo.setComparisonOps(op);
	}

	public String getSupportedWarning() {
		return null;
	}

}
