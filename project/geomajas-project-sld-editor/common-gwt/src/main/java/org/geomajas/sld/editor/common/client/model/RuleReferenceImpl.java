/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.common.client.model;

/**
 * Default implementation of {@link RuleReference} that uses the list index of the rule.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RuleReferenceImpl implements RuleReference {

	private int index;

	public RuleReferenceImpl(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

}
