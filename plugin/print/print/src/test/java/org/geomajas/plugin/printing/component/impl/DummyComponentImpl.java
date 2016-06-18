/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.printing.component.impl;

import org.geomajas.plugin.printing.component.DummyComponent;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.impl.AbstractPrintComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component()
@Scope("prototype")
public class DummyComponentImpl extends AbstractPrintComponent implements DummyComponent {

	@Autowired
	VectorLayerService layerService;
	
	public DummyComponentImpl(){
		System.out.println();
	}
	public void accept(PrintComponentVisitor visitor) {
	}

	public boolean isInjected() {
		return layerService != null;
	}

}
