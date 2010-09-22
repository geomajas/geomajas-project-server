package org.geomajas.plugin.printing.component.impl;

import org.geomajas.plugin.printing.component.DummyComponent;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.impl.PrintComponentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component("DummyComponentPrototype")
@Scope("prototype")
public class DummyComponentImpl extends PrintComponentImpl implements DummyComponent {

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
