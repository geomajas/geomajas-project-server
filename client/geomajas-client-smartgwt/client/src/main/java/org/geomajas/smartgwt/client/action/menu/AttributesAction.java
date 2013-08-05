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

package org.geomajas.smartgwt.client.action.menu;

import org.geomajas.smartgwt.client.action.MenuAction;
import org.geomajas.smartgwt.client.i18n.I18nProvider;
import org.geomajas.smartgwt.client.map.feature.Feature;
import org.geomajas.smartgwt.client.map.feature.FeatureTransaction;
import org.geomajas.smartgwt.client.util.WidgetLayout;
import org.geomajas.smartgwt.client.widget.FeatureAttributeEditor;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Show a window with an attribute editor widget, so the user can edit alpha-numeric attributes.
 * 
 * @author Pieter De Graef
 */
public class AttributesAction extends MenuAction {

	private MapWidget mapWidget;

	/**
	 * @param mapWidget
	 *            The map on which editing is going on.
	 */
	public AttributesAction(MapWidget mapWidget) {
		super(I18nProvider.getMenu().editAttributes(), WidgetLayout.iconTable);
		this.mapWidget = mapWidget;
	}

	/**
	 * Remove an existing ring from a Polygon or MultiPolygon at a given index.
	 * 
	 * @param event
	 *            The {@link MenuItemClickEvent} from clicking the action.
	 */
	public void onClick(MenuItemClickEvent event) {
		final FeatureTransaction ft = mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (ft != null) {
			SimpleFeatureAttributeWindow window = new SimpleFeatureAttributeWindow(ft.getNewFeatures()[0]) {

				public void onOk(Feature feature) {
					// Copy the attributes to the real feature:
					// Don't overwrite the feature itself, as the GraphicsContext expects the same object for rendering!
					ft.getNewFeatures()[0].setAttributes(feature.getAttributes());
				}

				public void onClose() {
				}
			};
			window.show();
		}
	}

	/**
	 * The widget that displays the attributes.
	 * 
	 * @author Pieter De Graef
	 */
	private class SimpleFeatureAttributeWindow extends Window {

		private FeatureAttributeEditor attributeTable;

		private IButton closeButton;

		private IButton okButton;

		public SimpleFeatureAttributeWindow(Feature feature) {
			setIsModal(true);
			centerInPage();
			setAutoSize(true);
			setCanDragReposition(true);
			setCanDragResize(true);

			VLayout layout = new VLayout();
			if (feature != null) {
				setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(feature.getLabel()));
				attributeTable = new FeatureAttributeEditor(feature.getLayer(), false);
				attributeTable.setFeature(feature);
				layout.addMember(attributeTable);
			} else {
				setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(""));
			}

			HLayout buttonLayout = new HLayout();
			buttonLayout.setAlign(Alignment.CENTER);
			buttonLayout.setMembersMargin(10);
			buttonLayout.setPadding(10);
			closeButton = new CloseButton();
			buttonLayout.addMember(closeButton);
			okButton = new OkButton();
			buttonLayout.addMember(okButton);

			layout.addMember(buttonLayout);
			layout.setWidth(450);
			addItem(layout);
		}

		public void onOk(Feature feature) {
			// Overwrite me...
		}

		public void onClose() {
			// Overwrite me...
		}

		/**
		 * Close button widget.
		 * 
		 * @author Pieter De Graef
		 */
		private class CloseButton extends IButton implements ClickHandler {

			public CloseButton() {
				super("Close");
				setWidth(60);
				addClickHandler(this);
			}

			public void onClick(ClickEvent event) {
				attributeTable.reset();
				onClose();
				SimpleFeatureAttributeWindow.this.destroy();
			}
		}

		/**
		 * OK button widget.
		 * 
		 * @author Pieter De Graef
		 */
		private class OkButton extends IButton implements ClickHandler {

			public OkButton() {
				super("Ok");
				setWidth(60);
				attributeTable.addItemChangedHandler(new ItemChangedHandler() {

					public void onItemChanged(ItemChangedEvent event) {
						if (attributeTable.validate()) {
							setDisabled(false);
						} else {
							setDisabled(true);
						}
					}
				});
				addClickHandler(this);
			}

			public void onClick(ClickEvent event) {
				onOk(attributeTable.getFeature());
				SimpleFeatureAttributeWindow.this.destroy();
			}
		}
	}
}