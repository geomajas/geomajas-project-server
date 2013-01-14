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
package org.geomajas.plugin.deskmanager.domain.dto;

import java.io.Serializable;

import org.geomajas.configuration.client.ScaleInfo;

/**
 * @author Kristof Heirwegh
 */
public class LayerViewDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String label;

	private boolean defaultVisible;

	private boolean showInLegend;

	private ScaleInfo minimumScale;

	private ScaleInfo maximumScale;

	// ------------------------------------------------------------------

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ScaleInfo getMinimumScale() {
		return minimumScale;
	}

	public void setMinimumScale(ScaleInfo minimumScale) {
		this.minimumScale = minimumScale;
	}

	public ScaleInfo getMaximumScale() {
		return maximumScale;
	}

	public void setMaximumScale(ScaleInfo maximumScale) {
		this.maximumScale = maximumScale;
	}

	public boolean isDefaultVisible() {
		return defaultVisible;
	}

	public void setDefaultVisible(boolean defaultVisible) {
		this.defaultVisible = defaultVisible;
	}

	public boolean isShowInLegend() {
		return showInLegend;
	}

	public void setShowInLegend(boolean showInLegend) {
		this.showInLegend = showInLegend;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
