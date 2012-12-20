/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.configuration.client;

/**
 * @author Oliver May
 *
 */
public interface ClientLayerInfo {

	/**
	 * Whether a layer is public.
	 * 
	 * @return true if the layer is public.
	 */
	public abstract boolean isPublic();

	/**
	 * Set whether a layer is public or not.
	 * 
	 * @param publicLayer
	 */
	public abstract void setPublic(boolean publicLayer);

	/**
	 * Get the name of a layer. This name is used in the management interface.
	 * 
	 * @return the name of the layer
	 */
	public abstract String getName();

	/**
	 * Set the name of the layer. This name is used in the management interface.
	 * 
	 * @param name
	 *            the name of the layer
	 */
	public abstract void setName(String name);

	/**
	 * Whether the layer is active.
	 * 
	 * @return true if the layer is active.
	 */
	public abstract boolean isActive();

	/**
	 * Set whether the layer is active or not.
	 * 
	 * @param active
	 */
	public abstract void setActive(boolean active);

	/**
	 * Whether the layer is a system layer. A system layer is a layer that is configured using the spring xml
	 * configuration, not added dynamically.
	 * 
	 * @return true if the layer is a system layer.
	 */
	public abstract boolean isSystemLayer();

	/**
	 * Set whether layer is a system layer. A system layer is a layer that is configured using the spring xml
	 * configuration, not added dynamically.
	 * 
	 * @param systemLayer
	 */
	public abstract void setSystemLayer(boolean systemLayer);

}