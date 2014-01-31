/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.internal.layer.tile;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.internal.rendering.strategy.TileUtil;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Internal representation of a tile.
 * 
 * @author Pieter De Graef
 */
public class InternalTileImpl implements InternalTile {

	private static final long serialVersionUID = 190L;

	private List<InternalFeature> features = new ArrayList<InternalFeature>();

	private List<TileCode> codes = new ArrayList<TileCode>();

	private TileCode code;

	private double tileWidth;

	private double tileHeight;

	private int screenWidth;

	private int screenHeight;

	private boolean clipped;

	private String featureContent;

	private String labelContent;

	private VectorTileContentType contentType;

	private Envelope bounds;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public InternalTileImpl(TileCode code, Envelope maxExtent, double scale) {
		super();
		this.code = code;
		init(maxExtent, scale);
	}

	public InternalTileImpl(int x, int y, int tileLevel, Envelope maxExtent, double scale) {
		this(new TileCode(tileLevel, x, y), maxExtent, scale);
	}

	// -------------------------------------------------------------------------
	// General functions:
	// -------------------------------------------------------------------------

	public void init(Envelope maxExtent, double scale) {
		double[] layerSize = TileUtil.getTileLayerSize(code, maxExtent, scale);
		tileWidth = layerSize[0];
		tileHeight = layerSize[1];
		int[] screenSize = TileUtil.getTileScreenSize(layerSize, scale);
		screenWidth = screenSize[0];
		screenHeight = screenSize[1];
		bounds = TileUtil.getTileBounds(code, maxExtent, scale);
	}

	public Envelope getBounds() {
		return bounds;
	}

	public void setBounds(Envelope bounds) {
		this.bounds = bounds;
	}

	public void addFeature(InternalFeature feature) {
		features.add(feature);
	}

	public void addCode(int level, int x, int y) {
		TileCode c = new TileCode(level, x, y);
		if (!codes.contains(c)) {
			codes.add(c);
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/** Returns the list of features that are stored in this tile. */
	public List<InternalFeature> getFeatures() {
		return features;
	}

	/**
	 * Set the list of features that are stored in this tile.
	 * 
	 * @param features
	 *            The full list of features stored and rendered in this tile.
	 */
	public void setFeatures(List<InternalFeature> features) {
		this.features = features;
	}

	/**
	 * Return the rendered content of a tile's features. Depending on the rendering method used, a different content
	 * type will be stored. Basically, the returned string will contain a string rendering (SVG/VML) or a URL.
	 */
	public String getFeatureContent() {
		return featureContent;
	}

	/**
	 * Set the rendered content of a tile's features. Depending on the rendering method used, a different content type
	 * will be stored. Basically, the returned string will contain a string rendering (SVG/VML) or a URL.
	 * 
	 * @param featureContent
	 *            The new value for the actual rendered feature content of this tile.
	 */
	public void setFeatureContent(String featureContent) {
		this.featureContent = featureContent;
	}

	/**
	 * Return the rendered content of a tile's labels. Depending on the rendering method used, a different content type
	 * will be stored. Basically, the returned string will contain a string rendering (SVG/VML) or a URL.
	 */
	public String getLabelContent() {
		return labelContent;
	}

	/**
	 * Set the rendered content of a tile's labels. Depending on the rendering method used, a different content type
	 * will be stored. Basically, the returned string will contain a string rendering (SVG/VML) or a URL.
	 * 
	 * @param labelContent
	 *            The new value for the actual rendered label content of this tile.
	 */
	public void setLabelContent(String labelContent) {
		this.labelContent = labelContent;
	}

	/** Returns the type of content for the rendered features and labels that are stored within this tile. */
	public VectorTileContentType getContentType() {
		return contentType;
	}

	/**
	 * Determine the type of content for the rendered features and labels that are stored within this tile.
	 * 
	 * @param contentType
	 *            The value for the content type.
	 */
	public void setContentType(VectorTileContentType contentType) {
		this.contentType = contentType;
	}

	/**
	 * All tiles that have at least one feature that geographically intersects with this tile are called dependent
	 * tiles. This list returns their codes.
	 * 
	 * @return Return the list of tiles that are dependent on this one.
	 */
	public List<TileCode> getCodes() {
		return codes;
	}

	/**
	 * All tiles that have at least one feature that geographically intersects with this tile are called dependent
	 * tiles.
	 * 
	 * @param codes
	 *            Set the list of tiles that are dependent on this one.
	 */
	public void setCodes(List<TileCode> codes) {
		this.codes = codes;
	}

	/**
	 * Returns the unique code for this tile. Consider this it's unique identifier within a vector layer.
	 *
	 * @return tile code
	 */
	public TileCode getCode() {
		return code;
	}

	/**
	 * Set the unique code for this tile. Consider this it's unique identifier within a vector layer.
	 * 
	 * @param code
	 *            The tile's code.
	 */
	public void setCode(TileCode code) {
		this.code = code;
	}

	/**
	 * @inheritDoc
	 */
	public double getTileWidth() {
		return tileWidth;
	}

	/**
	 * @inheritDoc
	 */
	public void setTileWidth(double tileWidth) {
		this.tileWidth = tileWidth;
	}

	/**
	 * @inheritDoc
	 */
	public double getTileHeight() {
		return tileHeight;
	}

	/**
	 * @inheritDoc
	 */
	public void setTileHeight(double tileHeight) {
		this.tileHeight = tileHeight;
	}

	/**
	 * Return the tile's width, expressed in client side pixels.
	 *
	 * @return tile width in client side pixels
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Set the tile's width, expressed in client side pixels.
	 * 
	 * @param screenWidth
	 *            The new value.
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * Return the tile's height, expressed in client side pixels.
	 *
	 * @return tile height in client side pixels
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Set the tile's height, expressed in client side pixels.
	 * 
	 * @param screenHeight
	 *            The new value.
	 */
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	/**
	 * Returns whether or not at least one feature within this tile has been clipped. This may sometimes be necessary
	 * when a feature's bounds are larger then the space allowed on the client side.
	 *
	 * @return true when at least one feature has a clipped geometry
	 */
	public boolean isClipped() {
		return clipped;
	}

	/**
	 * Set whether or not at least one feature within this tile has been clipped. This may sometimes be necessary when a
	 * feature's bounds are larger then the space allowed on the client side.
	 * 
	 * @param clipped
	 *            The new value.
	 */
	public void setClipped(boolean clipped) {
		this.clipped = clipped;
	}

	@Override
	public String toString() {
		return "InternalTileImpl{" +
				"features=" + features +
				", codes=" + codes +
				", code=" + code +
				", tileWidth=" + tileWidth +
				", tileHeight=" + tileHeight +
				", screenWidth=" + screenWidth +
				", screenHeight=" + screenHeight +
				", clipped=" + clipped +
				", featureContent='" + featureContent + '\'' +
				", labelContent='" + labelContent + '\'' +
				", contentType=" + contentType +
				", bounds=" + bounds +
				'}';
	}
}
