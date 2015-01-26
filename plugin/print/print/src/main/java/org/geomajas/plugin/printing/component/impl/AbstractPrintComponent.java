/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.printing.component.impl;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.printing.component.ComponentUtil;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.PageComponent;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;

import java.util.ResourceBundle;

/**
 * Basic container component for printing. Handles the size calculation, layout and rendering of its children.
 * Variant of {@link PrintComponentImpl} which uses better class name.
 *
 * @author Jan De Moerloose
 * @since 2.1.0
 *
 * @param <T> DTO object class
 *
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public class AbstractPrintComponent<T extends PrintComponentInfo> extends PrintComponentImpl<T> {

	private static final String BUNDLE_NAME = "org/geomajas/plugin/printing/PrintingMessages"; //$NON-NLS-1$

	/** No-arguments constructor. */
	public AbstractPrintComponent() {
		super();
	}

	/** Constructor with component id. */
	public AbstractPrintComponent(String id) {
		super(id);
	}

	/**
	 * Constructor with component id and layout constraints.
	 *
	 * @param id component id
	 * @param constraint constraints
	 */
	public AbstractPrintComponent(String id, LayoutConstraint constraint) {
		super(id, constraint);
	}

	//-----------------------------------
	//  methods for obtaining internationalised messages
	//-----------------------------------

	/**
	 * Returns the resource bundle for current Locale, i.e. locale set in the PageComponent.
	 * Always create a new instance, this avoids getting the incorrect locale information.
	 *
	 * @return resourcebundle for internationalized messages
	 */
	protected ResourceBundle getCurrentResourceBundle() {
		return ComponentUtil.getCurrentResourceBundle(getLocaleString());
	}

	private String getLocaleString() {
		PrintComponent<?> ancestor = getParent();

		while (null != ancestor && !(ancestor instanceof PageComponent)) {
			ancestor = ancestor.getParent();
		}
		if (null != ancestor && ancestor instanceof PageComponent) {
			return ((PageComponent) ancestor).getLocale();
		} else {
			return null;
		}
	}

}
