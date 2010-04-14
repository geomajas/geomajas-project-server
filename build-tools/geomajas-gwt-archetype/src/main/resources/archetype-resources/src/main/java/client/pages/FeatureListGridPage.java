#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.client.pages;

import java.util.List;

import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.FeatureListGrid;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * <p>
 * This class represents a tab that displays a {@link FeatureGrid} widget together with 2 buttons to fill the grid.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class FeatureListGridPage extends AbstractTab {

	private FeatureListGrid table;

	public FeatureListGridPage(MapWidget map) {
		super("FeatureListGrid", map);

		// Create a horizontal layout for the buttons:
		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(5);
		buttonLayout.setHeight(25);

		// Create a button to show the "roads" objects into a FeatureGrid:
		IButton button1a = new IButton("Show roads");
		button1a.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				VectorLayer layer = (VectorLayer) getMap().getMapModel().getLayer("roadsLayer");
				table.setLayer(layer);
				layer.getFeatureStore().getFeatures(GeomajasConstant.FEATURE_INCLUDE_ALL, new LazyLoadCallback() {

					// Add all the features currently in the layer's FeatureStore to the grid:
					public void execute(List<Feature> response) {
						for (Feature feature : response) {
							table.addFeature(feature);
						}
					}
				});
			}
		});
		button1a.setWidth(110);
		buttonLayout.addMember(button1a);
		mainLayout.addMember(buttonLayout);

		// Create the FeatureGrid that shows alpha-numerical attributes of features:
		table = new FeatureListGrid(map.getMapModel());
		table.setEditingEnabled(true);
		mainLayout.addMember(table);
	}

	public void initialize() {
	}

	public FeatureListGrid getTable() {
		return table;
	}
}
