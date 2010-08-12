package org.geomajas.plugin.geocoder.gwt.example.client.pages;

import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

/**
 * Basic implementation for a tab. The tabs are added bottom left.
 * 
 * @author Jan De Moerloose
 */
public abstract class AbstractTab extends Tab {

	protected final MapWidget map;

	protected VLayout mainLayout;

	AbstractTab(String title, MapWidget map) {
		super(title);
		this.map = map;
		mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		setPane(mainLayout);
	}

	public abstract void initialize();

	public MapWidget getMap() {
		return map;
	}
}
