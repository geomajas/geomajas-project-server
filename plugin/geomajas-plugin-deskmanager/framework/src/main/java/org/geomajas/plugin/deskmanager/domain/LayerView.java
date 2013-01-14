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
package org.geomajas.plugin.deskmanager.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.geomajas.configuration.client.ScaleInfo;
import org.hibernate.annotations.Type;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
@Entity
@Table(name = "config_layerviews")
public class LayerView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "label")
	private String label;

	@Column(name = "default_visible")
	private boolean defaultVisible;

	@Column(name = "show_in_legend")
	private boolean showInLegend;

	@Column(name = "minimum_scale")
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	private ScaleInfo minimumScale;

	@Column(name = "maximum_scale")
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
