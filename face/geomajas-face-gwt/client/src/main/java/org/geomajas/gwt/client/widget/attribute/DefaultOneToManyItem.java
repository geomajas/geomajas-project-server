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
package org.geomajas.gwt.client.widget.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.util.AttributeUtil;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.AttributeListGrid;
import org.geomajas.gwt.client.widget.KeepInScreenWindow;
import org.geomajas.gwt.client.widget.attribute.DefaultOneToManyItem.OneToManyLink;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
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
 */
public class DefaultOneToManyItem implements OneToManyItem<OneToManyLink> {

	private AttributeListGrid masterGrid;

	private IButton applyButton;

	private IButton newButton;

	private IButton deleteButton;

	private DefaultFeatureForm detailForm;

	private Window window;

	private AssociationValue selectedValue;

	private FeatureInfo featureInfo;

	private final OneToManyLink item;

	/**
	 * Return the actual form item.
	 * 
	 * @return the actual form item
	 */
	public OneToManyLink getItem() {
		return item;
	}

	/** No-arguments constructor. */
	public DefaultOneToManyItem() {
		item = new OneToManyLink();
	}

	/** {@inheritDoc} */
	public void toItem(OneToManyAttribute attribute) {
		// deep clone to allow separation of object and form state
		for (AssociationValue value : attribute.getValue()) {
			masterGrid.saveOrUpdateValue((AssociationValue) value.clone());
		}
	}

	/** {@inheritDoc} */
	public void fromItem(OneToManyAttribute attribute) {
		List<AssociationValue> values = new ArrayList<AssociationValue>();
		// deep clone to allow separation of object and form state
		for (AssociationValue associationValue : masterGrid.getValues()) {
			values.add((AssociationValue) associationValue.clone());
		}
		attribute.setValue(values);
	}

	/** {@inheritDoc} */
	public void clearValue() {
		detailForm.clear();
		masterGrid.clearValues();
	}

	/** {@inheritDoc} */
	public void init(AssociationAttributeInfo attributeInfo, AttributeProvider attributeProvider) {
		featureInfo = attributeInfo.getFeature();
		window = new KeepInScreenWindow();
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(WidgetLayout.marginLarge);
		layout.setMargin(WidgetLayout.marginLarge);
		detailForm = new DefaultFeatureForm(featureInfo, attributeProvider);
		masterGrid = new AttributeListGrid(featureInfo);
		masterGrid.setData(new ListGridRecord[] {});
		masterGrid.setHeight(200);
		layout.addMember(masterGrid);
		detailForm.getWidget().setLayoutAlign(Alignment.CENTER);
		detailForm.getWidget().setSize("90%", "30%");
		detailForm.getWidget().setIsGroup(true);
		detailForm.getWidget().setGroupTitle("Edit");
		layout.addMember(detailForm.getWidget());

		applyButton = new IButton(I18nProvider.getAttribute().btnApplyTitle());
		applyButton.setTooltip(I18nProvider.getAttribute().btnApplyTooltip());
		applyButton.addClickHandler(new ClickHandler() {

			/** {@inheritDoc} */
			public void onClick(ClickEvent event) {
				if (detailForm.validate() && selectedValue != null) {
					for (Map.Entry<String, Attribute<?>> entry : selectedValue.getAllAttributes().entrySet()) {
						detailForm.fromForm(entry.getKey(), entry.getValue());
						masterGrid.updateValue(selectedValue);
					}
					masterGrid.saveOrUpdateValue(selectedValue);
					masterGrid.selectValue(selectedValue);
					item.fireEvent(new ChangedEvent(item.getJsObj()));
					updateButtonState(false);
				}
			}
		});

		newButton = new IButton(I18nProvider.getAttribute().btnNewTitle());
		newButton.setTooltip(I18nProvider.getAttribute().btnNewTooltip());
		newButton.addClickHandler(new ClickHandler() {

			/** {@inheritDoc} */
			public void onClick(ClickEvent event) {
				selectedValue = createInstance();
				detailForm.clear();
				detailForm.toForm(selectedValue);
				for (AbstractAttributeInfo info : featureInfo.getAttributes()) {
					detailForm.toForm(info.getName(), selectedValue.getAllAttributes().get(info.getName()));
				}
				updateButtonState(false);
			}
		});

		deleteButton = new IButton(I18nProvider.getAttribute().btnDeleteTitle());
		deleteButton.setTooltip(I18nProvider.getAttribute().btnDeleteTooltip());
		deleteButton.addClickHandler(new ClickHandler() {

			/** {@inheritDoc} */
			public void onClick(ClickEvent event) {
				if (selectedValue != null) {
					if (masterGrid.deleteValue(selectedValue)) {
						detailForm.clear();
						selectedValue = null;
						item.fireEvent(new ChangedEvent(item.getJsObj()));
						updateButtonState(false);
					}
				}
			}
		});
		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(WidgetLayout.marginLarge);
		buttonLayout.addMember(applyButton);
		buttonLayout.addMember(newButton);
		buttonLayout.addMember(deleteButton);
		buttonLayout.setAlign(Alignment.CENTER);
		layout.addMember(buttonLayout);

		window.setMembersMargin(WidgetLayout.marginLarge);
		window.setAutoSize(true);
		window.setWidth("550");
		window.setHeight("*");
		window.setCanDragReposition(true);
		window.setCanDragResize(true);
		window.setTitle(getItem().getTitle());
		window.setOverflow(Overflow.AUTO);
		window.addItem(layout);
		masterGrid.addRecordClickHandler(new RecordClickHandler() {

			public void onRecordClick(RecordClickEvent event) {
				selectedValue = masterGrid.getSelectedValue();
				detailForm.clear();
				for (AbstractAttributeInfo info : featureInfo.getAttributes()) {
					detailForm.toForm(info.getName(), selectedValue.getAllAttributes().get(info.getName()));
				}
				updateButtonState(false);
			}
		});

		detailForm.addItemChangedHandler(new ItemChangedHandler() {

			public void onItemChanged(ItemChangedEvent event) {
				updateButtonState(true);
			}
		});

	}

	/** Show the window. */
	protected void openEditor() {
		window.centerInPage();
		/*
		if (!window.isDrawn()) {
			window.draw();
		}
		*/
		window.show();
	}

	/**
	 * Create association attribute for value.
	 *
	 * @return attribute
	 */
	protected AssociationValue createInstance() {
		Map<String, Attribute<?>> attributes = new HashMap<String, Attribute<?>>();
		for (AbstractAttributeInfo attrInfo : featureInfo.getAttributes()) {
			if (attrInfo instanceof PrimitiveAttributeInfo) {
				attributes.put(attrInfo.getName(), AttributeUtil.createEmptyPrimitiveAttribute(
						(PrimitiveAttributeInfo) attrInfo));
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
					default:
						throw new IllegalStateException("Don't know how to handle association type " +
								assocInfo.getType());
				}
			}
		}
		AssociationValue value = new AssociationValue();
		value.setAllAttributes(attributes);
		value.setId(AttributeUtil.createEmptyPrimitiveAttribute(featureInfo.getIdentifier()));
		return value;
	}

	/**
	 * {@link LinkItem} that opens the editable grid of one-to-many attributes.
	 * 
	 * @author Jan De Moerloose
	 */
	class OneToManyLink extends LinkItem {

		public OneToManyLink() {
			setTitle(I18nProvider.getAttribute().one2ManyMoreTitle());
			setLinkTitle(I18nProvider.getAttribute().one2ManyMoreTitle());
			setTooltip(I18nProvider.getAttribute().one2ManyMoreTooltip());
			addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				/** {@inheritDoc} */
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					openEditor();
				}
			});
		}

		@Override
		public void setDisabled(Boolean disabled) {
			// propagate to nested form and buttons, but don't disable ourselves or the grid !
			detailForm.setDisabled(disabled);
			if (disabled) {
				applyButton.setDisabled(true);
				newButton.setDisabled(true);
				deleteButton.setDisabled(true);
			} else {
				updateButtonState(false);
			}
		}

	}

	private void updateButtonState(boolean canApply) {
		if (canApply) {
			applyButton.setDisabled(!(selectedValue != null && detailForm.validate()));
		} else {
			applyButton.setDisabled(true);
		}
		deleteButton.setDisabled(!(selectedValue != null && masterGrid.containsValue(selectedValue)));
		newButton.setDisabled(false);
	}

}
