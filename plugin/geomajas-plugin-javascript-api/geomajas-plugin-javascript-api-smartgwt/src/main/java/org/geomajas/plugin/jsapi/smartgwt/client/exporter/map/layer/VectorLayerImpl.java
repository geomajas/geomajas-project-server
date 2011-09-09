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
package org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.layer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geomajas.annotation.FutureApi;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.jsapi.map.Feature;
import org.geomajas.jsapi.map.layer.VectorLayer;
import org.geomajas.jsapi.spatial.geometry.Bbox;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Exportable facade for {@link org.geomajas.gwt.client.map.layer.VectorLayer} in javascript.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Export
@ExportPackage("org.geomajas.jsapi.map.layer")
public class VectorLayerImpl implements Exportable, VectorLayer {

	private int fetchType = GeomajasConstant.FEATURE_INCLUDE_ALL;

	private org.geomajas.gwt.client.map.layer.VectorLayer layer;

	/**
	 * TODO.
	 * 
	 * @param layer
	 * @since 1.0.0
	 */
	@FutureApi
	public VectorLayerImpl(org.geomajas.gwt.client.map.layer.VectorLayer layer) {
		this.layer = layer;
	}

	public String[] getSelectedFeatures() {
		return (String[]) layer.getSelectedFeatures().toArray(new String[getSelectedFeatures().length]);
	}

	public String getId() {
		return layer.getId();
	}

	public String getServerLayerId() {
		return layer.getServerLayerId();
	}

	public String getTitle() {
		return layer.getLabel();
	}

	public void setSelected(boolean selected) {
		layer.setSelected(selected);
	}

	public boolean isSelected() {
		return layer.isSelected();
	}

	public void setMarkedAsVisible(boolean markedAsVisible) {
		layer.setVisible(true);
	}

	public boolean isMarkedAsVisible() {
		return layer.isVisible();
	}

	public boolean isShowing() {
		return layer.isShowing();
	}

	public void setFilter(String filter) {
		layer.setFilter(filter);
	}

	public String getFilter() {
		return layer.getFilter();
	}

	public boolean isFeatureSelected(String featureId) {
		return layer.isFeatureSelected(featureId);
	}

	public void selectFeature(String featureId, final FeatureCallback callBack) {
		layer.getFeatureStore().getFeature(featureId, fetchType, new LazyLoadCallback() {

			public void execute(List<org.geomajas.gwt.client.map.feature.Feature> response) {
				if (response.size() > 0 && response.get(0).isAttributesLoaded()) {
					layer.selectFeature(response.get(0));
					Feature[] features = { fromGwtFeature(response.get(0)) };
					callBack.execute(features[0]);
				} else {
					callBack.execute((Feature) null);
				}
			}
		});
	}

	public void deSelectFeature(String featureId, final FeatureCallback callBack) {
		layer.getFeatureStore().getFeature(featureId, fetchType, new LazyLoadCallback() {

			public void execute(List<org.geomajas.gwt.client.map.feature.Feature> response) {
				if (response.size() > 0 && response.get(0).isAttributesLoaded()) {
					layer.deselectFeature(response.get(0));
					Feature[] features = { fromGwtFeature(response.get(0)) };
					callBack.execute(features[0]);
				} else {
					callBack.execute((Feature) null);
				}
			}
		});
	}

	public void clearSelectedFeatures() {
		layer.clearSelectedFeatures();
	}

	public String[] getSelectedFeatureIds() {
		return layer.getSelectedFeatures().toArray(new String[layer.getSelectedFeatures().size()]);
	}

	public void getFeature(String featureId, final FeatureCallback callBack) {
		layer.getFeatureStore().getFeature(featureId, fetchType, new LazyLoadCallback() {

			public void execute(List<org.geomajas.gwt.client.map.feature.Feature> response) {
				if (response.size() > 0 && response.get(0).isAttributesLoaded()) {
					callBack.execute(fromGwtFeature(response.get(0)));
				} else {
					callBack.execute((Feature) null);
				}
			}
		});
	}
	
	public void getFeatures(Bbox bounds, final FeaturesCallback callBack) {
		GeometryFactory gf = new GeometryFactory(layer.getMapModel().getSrid(), 
				GeometryFactory.PARAM_DEFAULT_PRECISION);

		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setLayerIds(new String[]{getServerLayerId()});

		org.geomajas.gwt.client.spatial.Bbox box = new org.geomajas.gwt.client.spatial.Bbox(
				bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		request.setLocation(GeometryConverter.toDto(gf.createPolygon(box)));
		
		request.setCrs(layer.getMapModel().getCrs());
		request.setSearchType(SearchByLocationRequest.QUERY_CONTAINS);
		request.setSearchType(SearchByLocationRequest.SEARCH_FIRST_LAYER);
		request.setFeatureIncludes(GeomajasConstant.FEATURE_INCLUDE_NONE);
		request.setFilter(layer.getServerLayerId(), layer.getFilter());

		GwtCommand commandRequest = new GwtCommand(SearchByLocationRequest.COMMAND);
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {

			public void execute(CommandResponse commandResponse) {
				if (commandResponse instanceof SearchByLocationResponse) {
					SearchByLocationResponse response = (SearchByLocationResponse) commandResponse;
					Map<String, List<org.geomajas.layer.feature.Feature>> featureMap = response.getFeatureMap();
					List<String> features = new ArrayList<String>();
					if (featureMap.get(layer.getServerLayerId()) != null) {
						for (org.geomajas.layer.feature.Feature f : featureMap.get(layer.getServerLayerId())) {
							features.add(f.getId());
						}
					}
					callBack.execute(features.toArray(new String[0]));
				}
			}
			
		});
	}

	
	
	/**
	 * Set the fetchType @see {@link GeomajasConstant} for possible values. Note that attributes are always fetched
	 * despite the value of fetchType.
	 * 
	 * @param fetchType
	 *            the data to include in a fetched feature.
	 */
	@FutureApi
	public void setFetchType(int fetchType) {
		// Make sure attributes are always fetched.
		if (fetchType % 2 == 0) {
			this.fetchType = fetchType + 1;
		} else {
			this.fetchType = fetchType;
		}

	}
	
	private Feature fromGwtFeature(org.geomajas.gwt.client.map.feature.Feature f) {
		
		org.geomajas.gwt.client.spatial.Bbox fBox = f.getGeometry().getBounds();
		
		return new Feature(f.getId(), f.getAttributes(), f.getLabel(), f.getGeometry().toWkt(),
				new Bbox(fBox.getX(), fBox.getY(), fBox.getWidth(), fBox.getHeight()), this, f.isSelected());
	}

}
