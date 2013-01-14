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

package org.geomajas.plugin.editing.gwt.example.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Messages for editing plug-in example.
 *
 * @author Joachim Van der Auwera
 */
public interface EditingMessages extends Messages {

	/** @return tree group name */
	String treeGroupEditing();

	/** @return editing panel name */
	String editingPanel();

	/** @return description for editing panel */
	String editingDescription();

	/** @return multi geometry editing panel name */
	String multiGeometryPanel();

	/** @return description for multi-geometry panel */
	String multiGeometryDescription();

	/** @return merge panel name */
	String mergePanel();

	/* @return description for merge panel */
	String mergeDescription();

	/** @return split panel name */
	String splitPanel();

	/** @return description for split panel */
	String splitDescription();

}
