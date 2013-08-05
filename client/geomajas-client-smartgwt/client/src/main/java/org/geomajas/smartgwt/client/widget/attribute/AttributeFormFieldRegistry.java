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

package org.geomajas.smartgwt.client.widget.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.AbstractEditableAttributeInfo;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AssociationType;
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
import org.geomajas.smartgwt.client.map.layer.VectorLayer;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceImageFileField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FloatItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.DateRangeValidator;
import com.smartgwt.client.widgets.form.validator.FloatPrecisionValidator;
import com.smartgwt.client.widgets.form.validator.FloatRangeValidator;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.form.validator.Validator;
import org.geomajas.gwt.client.util.Log;

/**
 * <p>
 * Factory that creates {@link FormItem}s and {@link DataSourceField}s from attribute meta-data. It is also possible to
 * register custom form field definitions using {@link AbstractReadOnlyAttributeInfo#formInputType} field.
 * </p>
 * <p>
 * When defining custom implementations of the {@link FeatureFormFactory}, you are strongly encouraged to use this class
 * to create the actual fields within the forms, and to use both a {@link DataSourceField} and a {@link FormItem} for
 * each attribute you want to display in the form.<br/>
 * The form item will provide the view on the form field, while the data source field will provide the underlying data
 * control (with validators).
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.10.0
 */
@Api(allMethods = true)
public final class AttributeFormFieldRegistry {

	private static final Map<String, FormItemFactory> FORM_ITEMS;

	private static final Map<String, DataSourceFieldFactory> DATA_SOURCE_FIELDS;

	private static final Map<String, List<Validator>> FIELD_VALIDATORS;

	// Create the default types for the known attribute types:
	static {
		FORM_ITEMS = new HashMap<String, FormItemFactory>();
		DATA_SOURCE_FIELDS = new HashMap<String, DataSourceFieldFactory>();
		FIELD_VALIDATORS = new HashMap<String, List<Validator>>();

		// Type: BOOLEAN
		registerCustomFormItem(PrimitiveType.BOOLEAN.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceBooleanField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				BooleanItem item = new BooleanItem();
				item.setValue(false); // Avoid null value
				return item;
			}
		}, null);

		// TYPE: STRING
		registerCustomFormItem(PrimitiveType.STRING.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceTextField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				return new TextItem();
			}
		}, null);

		// TYPE: SHORT
		Validator shortValidator = new IntegerRangeValidator();
		((IntegerRangeValidator) shortValidator).setMin(Short.MIN_VALUE);
		((IntegerRangeValidator) shortValidator).setMax(Short.MAX_VALUE);
		registerCustomFormItem(PrimitiveType.SHORT.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceIntegerField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				return new IntegerItem();
			}
		}, Collections.singletonList(shortValidator));

		// TYPE: INTEGER
		registerCustomFormItem(PrimitiveType.INTEGER.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceIntegerField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				return new IntegerItem();
			}
		}, null);

		// TYPE: LONG
		registerCustomFormItem(PrimitiveType.LONG.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceIntegerField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				return new IntegerItem();
			}
		}, null);

		// TYPE: FLOAT
		registerCustomFormItem(PrimitiveType.FLOAT.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceFloatField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				return new FloatItem();
			}
		}, null);

		// TYPE: DOUBLE
		registerCustomFormItem(PrimitiveType.DOUBLE.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceFloatField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				return new FloatItem();
			}
		}, null);

		// TYPE: DATE
		registerCustomFormItem(PrimitiveType.DATE.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceDateField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				return new DateItem();
			}
		}, null);

		// TYPE: URL
		registerCustomFormItem(PrimitiveType.URL.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceTextField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				return new EnableToggleFormItem(new TextItem(), new LinkItem());
			}
		}, null);

		// TYPE: IMGURL
		registerCustomFormItem(PrimitiveType.IMGURL.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceImageFileField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				final Img image = new Img();
				image.setMaxHeight(200);
				image.setMaxWidth(300);
				image.setShowDisabled(false);

				CanvasItem imgItem = new CanvasItem() {

					public void setValue(String value) {
						image.setSrc(value);
					}

				};
				imgItem.setCanvas(image);
				return new EnableToggleFormItem(new TextItem(), imgItem);
			}
		}, null);

		// TYPE: CURRENCY
		Validator currencyValidator = new IsFloatValidator();
		registerCustomFormItem(PrimitiveType.CURRENCY.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceTextField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				return new TextItem();
			}
		}, Collections.singletonList(currencyValidator));

		// TYPE: MANY_TO_ONE
		registerCustomFormItem(AssociationType.MANY_TO_ONE.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				// Don't use a DataSourceEnumField, as it doesn't work together with the SelectItem's OptionDataSource.
				return new DataSourceTextField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				DefaultManyToOneItem manyToOneItem = new DefaultManyToOneItem();
				manyToOneItem.getItem().setAttribute(AssociationItem.ASSOCIATION_ITEM_ATTRIBUTE_KEY, manyToOneItem);
				return manyToOneItem.getItem();
			}
		}, null);

		// TYPE: ONE_TO_MANY
		registerCustomFormItem(AssociationType.ONE_TO_MANY.name(), new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				DefaultOneToManyItem oneToMany = new DefaultOneToManyItem();
				oneToMany.getItem().setAttribute(AssociationItem.ASSOCIATION_ITEM_ATTRIBUTE_KEY, oneToMany);
				return oneToMany.getItem();
			}
		}, null);
	}

	private AttributeFormFieldRegistry() {
		// Utility class: hide constructor.
	}

	/**
	 * <p>
	 * Register a new type of {@link FormItem} and {@link DataSourceField} for a certain kind of attribute or key. If
	 * there already was a definition registered for the given key, than it will be replaced by the new one. For all
	 * known attribute types (integer, string, date, url, float, many-to-one, ...) there are default definitions within
	 * this registry. New types can be added or these existing types can be overwritten, that's up to you.
	 * </p>
	 * <p>
	 * In order to use completely new custom defined form item types, you can always define the
	 * <code>formInputType</code> in the attribute definitions of the vector layers, and then register a form item with
	 * the same key here in this factory.
	 * </p>
	 * 
	 * @param key The key associated with the given {@link FormItemFactory} and {@link DataSourceFieldFactory}. This key
	 *        is either the name of an attribute type (i.e. <code>PrimitiveType.DATE.name()</code>) to overwrite the
	 *        default definitions, or a completely new type which can be configured in the attribute definitions with
	 *        the <code>formInputType</code> field.
	 * @param fieldType The type of {@link DataSourceFieldFactory} associated with the given key. This factory will
	 *        create the correct {@link DataSourceField} for the given key.
	 * @param editorType The type of {@link FormItemFactory} associated with the given key. This factory will create the
	 *        correct {@link FormItem} for the given key.
	 * @param validators A list of validators that can be applied to the {@link DataSourceField}. This is optional and
	 *        can be null. These validators protect the data, and can for example make sure that a user does not use any
	 *        letters while filling an integer type field.
	 */
	public static void registerCustomFormItem(String key, DataSourceFieldFactory fieldType, FormItemFactory editorType,
			List<Validator> validators) {
		if (key == null || fieldType == null || editorType == null) {
			throw new IllegalArgumentException("Cannot provide null values when registering new form items.");
		}

		DATA_SOURCE_FIELDS.put(key, fieldType);
		FORM_ITEMS.put(key, editorType);
		FIELD_VALIDATORS.put(key, null == validators ? new ArrayList<Validator>() : validators);
	}

	/**
	 * Create a new {@link DataSourceField} instance for the given attribute info. This field can provide additional
	 * validators on the field type (if they are registered), to protect the data.<br/>
	 * If the attribute info object has the <code>formInputType</code> set, than that will be used to search for the
	 * correct field type, otherwise the attribute TYPE name is used (i.e. PrimitiveType.INTEGER.name()).
	 * 
	 * @param info The actual attribute info to create a data source field for.
	 * @return The new data source field instance associated with the type of attribute.
	 */
	public static DataSourceField createDataSourceField(AbstractReadOnlyAttributeInfo info) {
		DataSourceField field = null;
		List<Validator> validators = new ArrayList<Validator>();
		if (info.getFormInputType() != null) {
			String formInputType = info.getFormInputType(); 
			DataSourceFieldFactory factory = DATA_SOURCE_FIELDS.get(formInputType);
			if (null != factory) {
				field = factory.create();
				List<Validator> fieldValidators = FIELD_VALIDATORS.get(formInputType);
				if (null != fieldValidators) {
					validators.addAll(fieldValidators);
				}
			} else {
				Log.logWarn("Cannot find data source factory for " + info.getFormInputType() + 
						", using default instead.");				
			}
		}
		if (field == null) {
			if (info instanceof PrimitiveAttributeInfo) {
				String name = ((PrimitiveAttributeInfo) info).getType().name();
				field = DATA_SOURCE_FIELDS.get(name).create();
				validators = new ArrayList<Validator>(FIELD_VALIDATORS.get(name));
			} else if (info instanceof AssociationAttributeInfo) {
				String name = ((AssociationAttributeInfo) info).getType().name();
				field = DATA_SOURCE_FIELDS.get(name).create();
				validators.addAll(FIELD_VALIDATORS.get(name));
			} else {
				throw new IllegalStateException("Don't know how to handle field " + info.getName() + ", " +
						"maybe you need to define the formInputType.");
			}
		}
		if (field != null) {
			field.setName(info.getName());
			field.setTitle(info.getLabel());
			field.setCanEdit(info.isEditable());
			field.setRequired(info instanceof AbstractEditableAttributeInfo &&
					isRequired(((AbstractEditableAttributeInfo) info).getValidator()));
			if (info instanceof PrimitiveAttributeInfo) {
				validators.addAll(convertConstraints((PrimitiveAttributeInfo) info));
			}
			if (validators.size() > 0) {
				field.setValidators(validators.toArray(new Validator[validators.size()]));
			}
			return field;
		}
		return null;
	}

	/**
	 * Create a new {@link FormItem} instance for the given attribute info (top level attribute).<br/>
	 * If the attribute info object has the <code>formInputType</code> set, than that will be used to search for the
	 * correct field type, otherwise the attribute TYPE name is used (i.e. PrimitiveType.INTEGER.name()).
	 * 
	 * @param info The actual attribute info to create a form item for
	 * @param layer The layer to create a form item for (needed to fetch association values)
	 * @return The new form item instance associated with the type of attribute.
	 */
	public static FormItem createFormItem(AbstractReadOnlyAttributeInfo info, VectorLayer layer) {
		return createFormItem(info, new DefaultAttributeProvider(layer, info.getName()));
	}

	/**
	 * Create a new {@link FormItem} instance for the given attribute info.<br/>
	 * If the attribute info object has the <code>formInputType</code> set, than that will be used to search for the
	 * correct field type, otherwise the attribute TYPE name is used (i.e. PrimitiveType.INTEGER.name()).
	 * 
	 * @param info The actual attribute info to create a form item for.
	 * @param attributeProvider The attribute value provider for association attributes
	 * @return The new form item instance associated with the type of attribute.
	 */
	public static FormItem createFormItem(AbstractReadOnlyAttributeInfo info, AttributeProvider attributeProvider) {
		FormItem formItem = null;
		if (info.getFormInputType() != null) {
			FormItemFactory factory = FORM_ITEMS.get(info.getFormInputType());
			if (null != factory) {
				formItem = factory.create();
			} else {
				Log.logWarn("Cannot find form item for " + info.getFormInputType() + ", using default instead.");
			}
		}
		if (formItem == null) {
			if (info instanceof PrimitiveAttributeInfo) {
				String name = ((PrimitiveAttributeInfo) info).getType().name();
				formItem = FORM_ITEMS.get(name).create();
			} else if (info instanceof AssociationAttributeInfo) {
				String name = ((AssociationAttributeInfo) info).getType().name();
				formItem = FORM_ITEMS.get(name).create();
			} else {
				throw new IllegalStateException("Don't know how to create form for field " + info.getName() + ", " +
						"maybe you need to define the formInputType.");
			}
		}
		if (formItem != null) {
			formItem.setName(info.getName());
			formItem.setTitle(info.getLabel());
			formItem.setValidateOnChange(true);
			formItem.setWidth("*");

			// Special treatment for associations
			if (info instanceof AssociationAttributeInfo) {
				AssociationAttributeInfo associationInfo = (AssociationAttributeInfo) info;
				String displayName = associationInfo.getFeature().getDisplayAttributeName();
				if (displayName == null) {
					displayName = associationInfo.getFeature().getAttributes().get(0).getName();
				}
				formItem.setDisplayField(displayName);
				Object o = formItem.getAttributeAsObject(AssociationItem.ASSOCIATION_ITEM_ATTRIBUTE_KEY);
				if (o instanceof OneToManyItem<?>) {
					OneToManyItem<?> item = (OneToManyItem<?>) o;
					item.init(associationInfo, attributeProvider);
				} else if (o instanceof ManyToOneItem<?>) {
					ManyToOneItem<?> item = (ManyToOneItem<?>) o;
					item.init(associationInfo, attributeProvider);
				}
			}
			return formItem;
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// Private methods concerning VALIDATORS:
	// -------------------------------------------------------------------------

	private static boolean isRequired(ValidatorInfo info) {
		if (null != info) {
			for (ConstraintInfo constraint : info.getConstraints()) {
				if (constraint instanceof NotNullConstraintInfo) {
					return true;
				}
			}
		}
		return false;
	}

	private static List<Validator> convertConstraints(PrimitiveAttributeInfo info) {
		List<Validator> validators = new ArrayList<Validator>();
		for (ConstraintInfo constraint : info.getValidator().getConstraints()) {
			Validator validator = null;
			boolean nullValidator = false;
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
				validator = createFromFuture();
			} else if (constraint instanceof MaxConstraintInfo) {
				validator = createFromMax((MaxConstraintInfo) constraint);
			} else if (constraint instanceof MinConstraintInfo) {
				validator = createFromMin((MinConstraintInfo) constraint);
			} else if (constraint instanceof PastConstraintInfo) {
				validator = createFromPast();
			} else if (constraint instanceof PatternConstraintInfo) {
				validator = createFromPattern((PatternConstraintInfo) constraint);
			} else if (constraint instanceof SizeConstraintInfo) {
				validator = createFromSize((SizeConstraintInfo) constraint, info.getType());
			} else if (constraint instanceof NotNullConstraintInfo) {
				nullValidator = true; // nothing to do
			} else {
				throw new IllegalStateException("Unknown constraint type " + constraint);
			}
			if (null != validator && !nullValidator) {
				addErrorMessage(info, validators, validator);
			}
		}
		return validators;
	}

	private static void addErrorMessage(PrimitiveAttributeInfo info, List<Validator> validators, Validator validator) {
		validator.setErrorMessage(info.getValidator().getErrorMessage());
		validators.add(validator);
	}

	private static Validator createFromDecimalMin(DecimalMinConstraintInfo decimalMin) {
		FloatRangeValidator floatMin = new FloatRangeValidator();
		floatMin.setMin(Float.parseFloat(decimalMin.getValue()));
		return floatMin;
	}

	private static Validator createFromDecimalMax(DecimalMaxConstraintInfo decimalMax) {
		FloatRangeValidator floatMax = new FloatRangeValidator();
		floatMax.setMax(Float.parseFloat(decimalMax.getValue()));
		return floatMax;
	}

	private static Validator[] createFromDigits(DigitsConstraintInfo digits, PrimitiveType type) {
		FloatPrecisionValidator floatPrecision = new FloatPrecisionValidator();
		floatPrecision.setPrecision(digits.getFractional());
		floatPrecision.setRoundToPrecision(digits.getFractional());
		IntegerRangeValidator integerDigit;
		FloatRangeValidator floatDigit;
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
			case CURRENCY:
				floatDigit = new FloatRangeValidator();
				floatDigit.setMax((int) Math.pow(10.0, digits.getInteger()) - Float.MIN_VALUE);
				validators = new Validator[] { floatPrecision, floatDigit };
				break;
			default:
				throw new IllegalStateException("Cannot createFromDigits for type " + type);
		}
		return validators;
	}

	private static Validator createFromFuture() {
		DateRangeValidator dateFuture = new DateRangeValidator();
		dateFuture.setMin(new Date());
		return dateFuture;
	}

	private static Validator createFromMax(MaxConstraintInfo max) {
		IntegerRangeValidator integerMax = new IntegerRangeValidator();
		integerMax.setMax((int) max.getValue());
		return integerMax;
	}

	private static Validator createFromMin(MinConstraintInfo min) {
		IntegerRangeValidator integerMin = new IntegerRangeValidator();
		integerMin.setMin((int) min.getValue());
		return integerMin;
	}

	private static Validator createFromPast() {
		DateRangeValidator datePast = new DateRangeValidator();
		datePast.setMax(new Date());
		return datePast;
	}

	private static Validator createFromPattern(PatternConstraintInfo pattern) {
		RegExpValidator regexp = new RegExpValidator();
		regexp.setExpression(pattern.getRegexp());
		return regexp;
	}

	private static Validator createFromSize(SizeConstraintInfo size, PrimitiveType type) {
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