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

package org.geomajas.plugin.editing.puregwt.client.gfx;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;

/**
 * Default implementation for the {@link GeometryIndexStyleFactory}.
 * 
 * @author Pieter De Graef
 */
public class DefaultGeometryIndexStyleFactory implements GeometryIndexStyleFactory {

	private final StyleProvider styleProvider;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a {@link GeometryIndexStyleFactory} with the given {@link StyleProvider}. This service will be used for
	 * getting the correct styles.
	 * 
	 * @param styleProvider
	 *            The style service that will provide styles for states.
	 */
	public DefaultGeometryIndexStyleFactory(StyleProvider styleProvider) {
		this.styleProvider = styleProvider;
	}

	// ------------------------------------------------------------------------
	// GeometryIndexStyleFactory implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public FeatureStyleInfo create(GeometryEditService editService, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		if (index == null) {
			return findGeometryStyle(editService, index);
		}
		switch (editService.getIndexService().getType(index)) {
			case TYPE_VERTEX:
				return findVertexStyle(editService, index);
			case TYPE_EDGE:
				return findEdgeStyle(editService, index);
			default:
				return findGeometryStyle(editService, index);
		}
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/** Get the service that provides styles for states. */
	public StyleProvider getStyleService() {
		return styleProvider;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private FeatureStyleInfo findVertexStyle(GeometryEditService editService, GeometryIndex index) {
		if (editService.getIndexStateService().isMarkedForDeletion(index)) {
			return styleProvider.getVertexMarkForDeletionStyle();
		} else if (!editService.getIndexStateService().isEnabled(index)) {
			return styleProvider.getVertexDisabledStyle();
		} else if (editService.getIndexStateService().isSnapped(index)) {
			return styleProvider.getVertexSnappedStyle();
		}

		boolean selected = editService.getIndexStateService().isSelected(index);
		boolean highlighted = editService.getIndexStateService().isHightlighted(index);
		if (selected && highlighted) {
			return styleProvider.getVertexSelectHoverStyle();
		} else if (selected) {
			return styleProvider.getVertexSelectStyle();
		} else if (highlighted) {
			return styleProvider.getVertexHoverStyle();
		}
		return styleProvider.getVertexStyle();
	}

	private FeatureStyleInfo findEdgeStyle(GeometryEditService editService, GeometryIndex index) {
		if (editService.getIndexStateService().isMarkedForDeletion(index)) {
			return styleProvider.getEdgeMarkForDeletionStyle();
		} else if (!editService.getIndexStateService().isEnabled(index)) {
			return styleProvider.getEdgeDisabledStyle();
		}

		boolean selected = editService.getIndexStateService().isSelected(index);
		boolean highlighted = editService.getIndexStateService().isHightlighted(index);
		if (selected && highlighted) {
			return styleProvider.getEdgeSelectHoverStyle();
		} else if (selected) {
			return styleProvider.getEdgeSelectStyle();
		} else if (highlighted) {
			return styleProvider.getEdgeHoverStyle();
		}
		return styleProvider.getEdgeStyle();
	}

	private FeatureStyleInfo findGeometryStyle(GeometryEditService editService, GeometryIndex index) {
		if (index != null) {
			if (!editService.getIndexStateService().isEnabled(index)) {
				return styleProvider.getBackgroundDisabledStyle();
			} else if (editService.getIndexStateService().isMarkedForDeletion(index)) {
				return styleProvider.getBackgroundMarkedForDeletionStyle();
			}
		}
		return styleProvider.getBackgroundStyle();
	}
}