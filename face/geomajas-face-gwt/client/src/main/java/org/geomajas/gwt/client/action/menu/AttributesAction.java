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

package org.geomajas.gwt.client.action.menu;

import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.widget.FeatureAttributeEditor;
import org.geomajas.gwt.client.widget.MapWidget;

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
		super(I18nProvider.getMenu().editAttributes(), "[ISOMORPHIC]/geomajas/osgeo/table.png");
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