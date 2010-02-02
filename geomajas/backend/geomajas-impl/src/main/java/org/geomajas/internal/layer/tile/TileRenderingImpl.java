package org.geomajas.internal.layer.tile;

import org.geomajas.layer.tile.TileRendering;

public class TileRenderingImpl implements TileRendering {

	private TileRenderMethod tileRenderMethod;

	private String featureString;

	private String labelString;

	public TileRenderingImpl(TileRenderMethod tileRenderMethod) {
		this.tileRenderMethod = tileRenderMethod;
	}

	public TileRenderMethod getTileRenderMethod() {
		return tileRenderMethod;
	}

	public String getFeatureString() {
		return featureString;
	}

	public void setFeatureString(String featureString) {
		this.featureString = featureString;
	}

	public String getLabelString() {
		return labelString;
	}

	public void setLabelString(String labelString) {
		this.labelString = labelString;
	}
}
