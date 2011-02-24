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

package org.geomajas.widget.featureinfo.client.widget;

import java.util.List;
import java.util.Map;

import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.widget.FeatureAttributeWindow;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

/**
 * <p>
 * The <code>MultilayerFeatureInfoWindow</code> is a floating window that shows a list of features, grouped by layers.
 * </p>
 * 
 * @author Oliver May
 */
public class MultiLayerFeatureInfoWindow extends Window {

	private FeatureInfoMessages featureInfoMessages = GWT.create(FeatureInfoMessages.class);

	private MultiLayerFeaturesList featuresList;

	private MapWidget mapWidget;

	private boolean showDetailWindowInline;

	public MultiLayerFeatureInfoWindow(MapWidget mapWidget,
			Map<String, List<org.geomajas.layer.feature.Feature>> featureMap, boolean showDetailWindowInline) {
		super();
		this.mapWidget = mapWidget;
		this.showDetailWindowInline = showDetailWindowInline;
		buildWidget();
		setFeatureMap(featureMap);
	}

	private void setFeatureMap(Map<String, List<org.geomajas.layer.feature.Feature>> featureMap) {
		featuresList.setFeatures(mapWidget, featureMap);
	}

	private void buildWidget() {
		setAutoSize(true);
		setTitle(featureInfoMessages.nearbyFeaturesWindowTitle());
		setCanDragReposition(true);
		setCanDragResize(true);
		setWidth("250px");
		setMinWidth(250);

		FeatureClickHandler featureClickHandler;
		if (showDetailWindowInline) {

			// Build the sectionstack
			SectionStack featureInfoStack = new SectionStack();
			featureInfoStack.setVisibilityMode(VisibilityMode.MULTIPLE);
			featureInfoStack.setWidth100();
			featureInfoStack.setHeight100();
			featureInfoStack.setOverflow(Overflow.VISIBLE);

			// Build the detail section
			final SectionStackSection detailsSection = new SectionStackSection(
					featureInfoMessages.nearbyFeaturesDetailsSectionTitle());

			final FeatureAttributeCanvas featureAttributeCanvas = new FeatureAttributeCanvas(null/* feature */,
					false/* noedit */, mapWidget.getHeight() / 2);

			detailsSection.setExpanded(false);
			detailsSection.addItem(featureAttributeCanvas);

			// Build the list section
			final SectionStackSection listSection = new SectionStackSection(
					featureInfoMessages.nearbyFeaturesListSectionTitle());

			featuresList = new MultiLayerFeaturesList(mapWidget, new FeatureClickHandler() {

				public void onClick(Feature feat) {
					detailsSection.setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(feat.getLabel()));
					featureAttributeCanvas.setFeature(feat, mapWidget.getHeight() / 2);
					detailsSection.setExpanded(true);
				}
			});
			listSection.setExpanded(true);
			listSection.addItem(featuresList);

			featureInfoStack.addSection(listSection);
			featureInfoStack.addSection(detailsSection);
			addItem(featureInfoStack);
		} else {
			featuresList = new MultiLayerFeaturesList(mapWidget, new FeatureClickHandler() {

				// Override here to allow custom widgets
				public void onClick(Feature feature) {
					FeatureAttributeWindow window = new FeatureAttributeWindow(feature, false);
					window.setPageTop(mapWidget.getAbsoluteTop() + 25);
					window.setPageLeft(mapWidget.getAbsoluteLeft() + 25);
					window.draw();
				}
			});
			addItem(featuresList);
		}

	}
}
