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
package org.geomajas.gwt.client.widget.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.gwt.client.widget.AttributeListGrid;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.CurrencyAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.ImageUrlAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.layer.feature.attribute.ShortAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.feature.attribute.UrlAttribute;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Default implementation of one-to-many form item. The item is represented by a button ("...") which opens a window
 * with a list grid and detail form for performing CRUD operations on the individual attribute values of the many
 * collection.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DefaultOneToManyItem implements OneToManyItem<CanvasItem> {

	private IButton editButton;

	private AttributeListGrid masterGrid;

	private FeatureForm<DynamicForm> detailForm;

	private Window window;

	private AssociationValue selectedValue;

	private CanvasItem canvasItem;

	private FeatureInfo featureInfo;

	/**
	 * Return the actual form item.
	 * 
	 * @return the actual form item
	 */
	public CanvasItem getItem() {
		return canvasItem;
	}

	public DefaultOneToManyItem() {
		HLayout canvas = new HLayout();
		canvas.setAutoHeight();
		canvas.setAutoWidth();
		editButton = new IButton("...");
		editButton.setWidth(50);
		canvas.addMember(editButton);
		editButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				openEditor();
			}
		});
		canvasItem.setCanvas(canvas);
	}

	public void toItem(OneToManyAttribute attribute) {
		// deep clone to allow separation of object and form state
		for (AssociationValue value : attribute.getValue()) {
			masterGrid.addValue((AssociationValue) value.clone());
		}
	}

	public void fromItem(OneToManyAttribute attribute) {
		List<AssociationValue> values = new ArrayList<AssociationValue>();
		// deep clone to allow separation of object and form state
		for (AssociationValue associationValue : masterGrid.getValues()) {
			values.add((AssociationValue) associationValue.clone());
		}
		attribute.setValue(values);
	}

	public void init(AssociationAttributeInfo attributeInfo, AttributeProvider attributeProvider) {
		featureInfo = attributeInfo.getFeature();
		window = new Window();
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);
		layout.setMargin(10);
		detailForm = new DefaultFeatureForm(featureInfo, attributeProvider);
		masterGrid = new AttributeListGrid(featureInfo);
		masterGrid.setData(new ListGridRecord[] {});
		masterGrid.setHeight(300);
		layout.addMember(masterGrid);
		detailForm.getWidget().setLayoutAlign(Alignment.CENTER);
		detailForm.getWidget().setSize("90%", "30%");
		detailForm.getWidget().setIsGroup(true);
		detailForm.getWidget().setGroupTitle("Edit");
		layout.addMember(detailForm.getWidget());

		IButton applyButton = new IButton("Apply");
		applyButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (detailForm.validate() && selectedValue != null) {
					for (Map.Entry<String, Attribute<?>> entry : selectedValue.getAllAttributes().entrySet()) {
						detailForm.fromForm(entry.getKey(), entry.getValue());
						masterGrid.updateValue(selectedValue);
					}
					if (selectedValue.getId().getValue() == null) {
						if (masterGrid.addValue(selectedValue)) {
							masterGrid.selectValue(selectedValue);
						}
					}
				}
			}
		});

		IButton newButton = new IButton("New");
		newButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				selectedValue = createInstance();
				detailForm.clear();
				for (AttributeInfo info : featureInfo.getAttributes()) {
					detailForm.toForm(info.getName(), selectedValue.getAllAttributes().get(info.getName()));
				}
			}
		});
		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(10);
		buttonLayout.addMember(applyButton);
		buttonLayout.addMember(newButton);
		buttonLayout.setAlign(Alignment.CENTER);
		layout.addMember(buttonLayout);

		window.setMembersMargin(10);
		window.addItem(layout);
		window.setWidth(500);
		window.setHeight(400);
		window.setAutoSize(true);
		window.setCanDragReposition(true);
		window.setCanDragResize(true);
		window.setTitle(getItem().getTitle());
		masterGrid.addRecordClickHandler(new RecordClickHandler() {

			public void onRecordClick(RecordClickEvent event) {
				selectedValue = masterGrid.getSelectedValue();
				detailForm.clear();
				for (AttributeInfo info : featureInfo.getAttributes()) {
					detailForm.toForm(info.getName(), selectedValue.getAllAttributes().get(info.getName()));
				}
			}
		});
	}

	protected void openEditor() {
		window.centerInPage();
		if (!window.isDrawn()) {
			window.draw();
		}
		window.show();
	}

	protected AssociationValue createInstance() {
		Map<String, Attribute<?>> attributes = new HashMap<String, Attribute<?>>();
		for (AttributeInfo attrInfo : featureInfo.getAttributes()) {
			if (attrInfo instanceof PrimitiveAttributeInfo) {
				attributes.put(attrInfo.getName(), createPrimitiveAttribute((PrimitiveAttributeInfo) attrInfo));
			} else if (attrInfo instanceof AssociationAttributeInfo) {
				AssociationAttributeInfo assocInfo = (AssociationAttributeInfo) attrInfo;
				switch (assocInfo.getType()) {
					case MANY_TO_ONE:
						attributes.put(assocInfo.getName(), new ManyToOneAttribute());
						break;
					case ONE_TO_MANY:
						OneToManyAttribute oneToMany = new OneToManyAttribute();
						oneToMany.setValue(new ArrayList<AssociationValue>());
						attributes.put(assocInfo.getName(), oneToMany);
						break;

				}
			}
		}
		AssociationValue value = new AssociationValue();
		value.setAllAttributes(attributes);
		value.setId(createPrimitiveAttribute(featureInfo.getIdentifier()));
		return value;
	}

	private PrimitiveAttribute<?> createPrimitiveAttribute(PrimitiveAttributeInfo primInfo) {
		switch (primInfo.getType()) {
			case BOOLEAN:
				return new BooleanAttribute();
			case CURRENCY:
				return new CurrencyAttribute();
			case DATE:
				return new DateAttribute();
			case DOUBLE:
				return new DoubleAttribute();
			case FLOAT:
				return new FloatAttribute();
			case IMGURL:
				return new ImageUrlAttribute();
			case INTEGER:
				return new IntegerAttribute();
			case LONG:
				return new LongAttribute();
			case SHORT:
				return new ShortAttribute();
			case STRING:
				return new StringAttribute();
			case URL:
				return new UrlAttribute();
			default:
				return new StringAttribute();

		}
	}

}
