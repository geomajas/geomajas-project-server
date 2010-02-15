/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.gwt.client.widget.attribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.validation.ConstraintInfo;
import org.geomajas.configuration.validation.DecimalMaxConstraintInfo;
import org.geomajas.configuration.validation.DecimalMinConstraintInfo;
import org.geomajas.configuration.validation.DigitsConstraintInfo;
import org.geomajas.configuration.validation.FutureConstraintInfo;
import org.geomajas.configuration.validation.MaxConstraintInfo;
import org.geomajas.configuration.validation.MinConstraintInfo;
import org.geomajas.configuration.validation.NotNullConstraintInfo;
import org.geomajas.configuration.validation.PastConstraintInfo;
import org.geomajas.configuration.validation.PatternConstraintInfo;
import org.geomajas.configuration.validation.SizeConstraintInfo;
import org.geomajas.configuration.validation.ValidatorInfo;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceImageField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceLinkField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.validator.DateRangeValidator;
import com.smartgwt.client.widgets.form.validator.FloatPrecisionValidator;
import com.smartgwt.client.widgets.form.validator.FloatRangeValidator;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.form.validator.Validator;

/**
 * The default implementation of the <code>AttributeFormFactory</code>.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class DefaultAttributeFormFactory implements AttributeFormFactory {

	/**
	 * Creates a form using the specified attribute information. This attribute form will allow for editing, and has all
	 * the necessary validators in place.
	 * 
	 * @param infos
	 *            List of attribute definitions. Normally taken from a
	 *            {@link org.geomajas.gwt.client.map.layer.VectorLayer}.
	 * @return An attribute form that allows for editing of it's values.
	 */
	public EditableAttributeForm createEditableForm(List<AttributeInfo> infos) {
		EditableAttributeForm form = new EditableAttributeForm(infos);
		DataSource source = new DataSource();
		for (AttributeInfo info : infos) {
			source.addField(createField(info));
		}
		form.setDataSource(source);
		return form;
	}

	/**
	 * Creates a form using the specified attribute information. This attribute form will not allow for editing, but
	 * instead simply displays the attribute values.
	 * 
	 * @param infos
	 *            List of attribute definitions. Normally taken from a
	 *            {@link org.geomajas.gwt.client.map.layer.VectorLayer}.
	 * @return An attribute form that does not allow editing, but simply display the values.
	 */
	public SimpleAttributeForm createSimpleForm(List<AttributeInfo> infos) {
		AttributeFormItemFactory factory = new DefaultAttributeFormItemFactory();
		SimpleAttributeForm form = new SimpleAttributeForm(infos);
		List<FormItem> fields = new ArrayList<FormItem>();
		for (AttributeInfo info : infos) {
			fields.add(factory.createFormItem(info));
		}
		form.setFields(fields);
		return form;
	}

	// -------------------------------------------------------------------------
	// Private methods for creating fields:
	// -------------------------------------------------------------------------

	protected DataSourceField createField(AttributeInfo info) {
		if (info instanceof PrimitiveAttributeInfo) {
			return createPrimitiveField((PrimitiveAttributeInfo) info);
		} else {
			return createAssociationField((AssociationAttributeInfo) info);
		}
	}

	private DataSourceField createPrimitiveField(PrimitiveAttributeInfo info) {
		List<Validator> validators = convertConstraints(info);
		DataSourceField field = null;
		boolean required = isRequired(info.getValidator());
		switch (info.getType()) {
			case BOOLEAN:
				field = new DataSourceBooleanField(info.getName(), info.getLabel());
				break;
			case SHORT:
				field = new DataSourceIntegerField(info.getName(), info.getLabel());
				IntegerRangeValidator shortValidator = new IntegerRangeValidator();
				shortValidator.setMin(Short.MIN_VALUE);
				shortValidator.setMax(Short.MAX_VALUE);
				validators.add(shortValidator);
				break;
			case INTEGER:
				field = new DataSourceIntegerField(info.getName(), info.getLabel());
				break;
			case LONG:
				// narrowing down to integer ?
				field = new DataSourceIntegerField(info.getName(), info.getLabel());
				break;
			case FLOAT:
				field = new DataSourceFloatField(info.getName(), info.getLabel());
				break;
			case DOUBLE:
				// narrowing down to float ?
				field = new DataSourceFloatField(info.getName(), info.getLabel());
				break;
			case CURRENCY:
				field = new DataSourceTextField(info.getName(), info.getLabel());
				// MaskValidator currency = new MaskValidator();
				// currency.setMask("(\\d)+(\\.\\d\\d)?");
				// currency.setTransformTo("$1.$2");
				// validators.add(currency);
				validators.add(new IsFloatValidator());
				break;
			case STRING:
				field = new DataSourceTextField(info.getName(), info.getLabel());
				break;
			case DATE:
				field = new DataSourceDateField(info.getName(), info.getLabel());
				break;
			case URL:
				field = new DataSourceLinkField(info.getName(), info.getLabel());
				break;
			case IMGURL:
				field = new DataSourceImageField(info.getName(), info.getLabel());
				break;
		}
		field.setValidators(validators.toArray(new Validator[0]));
		field.setRequired(required);
		return field;
	}

	private DataSourceField createAssociationField(AssociationAttributeInfo info) {
		DataSourceTextField field = new DataSourceTextField(info.getName(), info.getLabel(), 0, isRequired(info
				.getValidator()));
		field.setCanEdit(false);
		return field;
	}

	private boolean isRequired(ValidatorInfo info) {
		for (ConstraintInfo constraint : info.getConstraints()) {
			if (constraint instanceof NotNullConstraintInfo) {
				return true;
			}
		}
		return false;
	}

	// -------------------------------------------------------------------------
	// Private methods concerning VALIDATORS:
	// -------------------------------------------------------------------------

	private List<Validator> convertConstraints(PrimitiveAttributeInfo info) {
		List<Validator> validators = new ArrayList<Validator>();
		for (ConstraintInfo constraint : info.getValidator().getConstraints()) {
			Validator validator = null;
			if (constraint instanceof DecimalMaxConstraintInfo) {
				validator = createFromDecimalMax((DecimalMaxConstraintInfo) constraint);
			} else if (constraint instanceof DecimalMinConstraintInfo) {
				validator = createFromDecimalMin((DecimalMinConstraintInfo) constraint);
			} else if (constraint instanceof DigitsConstraintInfo) {
				Validator[] v2 = createFromDigits((DigitsConstraintInfo) constraint, info.getType());
				for (Validator v : v2) {
					addErrorMessage(info, validators, v);
				}
			} else if (constraint instanceof FutureConstraintInfo) {
				validator = createFromFuture((FutureConstraintInfo) constraint);
			} else if (constraint instanceof MaxConstraintInfo) {
				validator = createFromMax((MaxConstraintInfo) constraint);
			} else if (constraint instanceof MinConstraintInfo) {
				validator = createFromMin((MinConstraintInfo) constraint);
			} else if (constraint instanceof PastConstraintInfo) {
				validator = createFromPast((PastConstraintInfo) constraint);
			} else if (constraint instanceof PatternConstraintInfo) {
				validator = createFromPattern((PatternConstraintInfo) constraint);
			} else if (constraint instanceof SizeConstraintInfo) {
				validator = createFromSize((SizeConstraintInfo) constraint, info.getType());
			}
			if (validator != null) {
				addErrorMessage(info, validators, validator);
			}
		}
		return validators;
	}

	private void addErrorMessage(PrimitiveAttributeInfo info, List<Validator> validators, Validator validator) {
		validator.setErrorMessage(info.getValidator().getErrorMessage());
		validators.add(validator);
	}

	private Validator createFromDecimalMin(DecimalMinConstraintInfo decimalMin) {
		FloatRangeValidator floatMin = new FloatRangeValidator();
		floatMin.setMin(Float.parseFloat(decimalMin.getValue()));
		return floatMin;
	}

	private Validator createFromDecimalMax(DecimalMaxConstraintInfo decimalMax) {
		FloatRangeValidator floatMax = new FloatRangeValidator();
		floatMax.setMax(Float.parseFloat(decimalMax.getValue()));
		return floatMax;
	}

	private Validator[] createFromDigits(DigitsConstraintInfo digits, PrimitiveType type) {
		FloatPrecisionValidator floatPrecision = new FloatPrecisionValidator();
		floatPrecision.setPrecision(digits.getFractional());
		floatPrecision.setRoundToPrecision(digits.getFractional());
		IntegerRangeValidator integerDigit = null;
		FloatRangeValidator floatDigit = null;
		Validator[] validators;
		switch (type) {
			case SHORT:
			case INTEGER:
			case LONG:
				integerDigit = new IntegerRangeValidator();
				integerDigit.setMax((int) Math.pow(10.0, digits.getInteger()) - 1);
				validators = new Validator[] { floatPrecision, integerDigit };
				break;
			case FLOAT:
			case DOUBLE:
				floatDigit = new FloatRangeValidator();
				floatDigit.setMax((int) Math.pow(10.0, digits.getInteger()) - Float.MIN_VALUE);
				validators = new Validator[] { floatPrecision, floatDigit };
				break;
			default:
				validators = new Validator[] { floatPrecision };
		}
		return validators;
	}

	private Validator createFromFuture(FutureConstraintInfo future) {
		DateRangeValidator dateFuture = new DateRangeValidator();
		dateFuture.setMin(new Date());
		return dateFuture;
	}

	private Validator createFromMax(MaxConstraintInfo max) {
		IntegerRangeValidator integerMax = new IntegerRangeValidator();
		integerMax.setMax((int) max.getValue());
		return integerMax;
	}

	private Validator createFromMin(MinConstraintInfo min) {
		IntegerRangeValidator integerMin = new IntegerRangeValidator();
		integerMin.setMin((int) min.getValue());
		return integerMin;
	}

	private Validator createFromPast(PastConstraintInfo past) {
		DateRangeValidator datePast = new DateRangeValidator();
		datePast.setMax(new Date());
		return datePast;
	}

	private Validator createFromPattern(PatternConstraintInfo pattern) {
		RegExpValidator regexp = new RegExpValidator();
		regexp.setExpression(pattern.getRegexp());
		return regexp;
	}

	private Validator createFromSize(SizeConstraintInfo size, PrimitiveType type) {
		switch (type) {
			case STRING:
			case URL:
			case IMGURL:
				LengthRangeValidator lengthRange = new LengthRangeValidator();
				lengthRange.setMax(size.getMin());
				lengthRange.setMax(size.getMax());
				return lengthRange;
		}
		return null;
	}
}