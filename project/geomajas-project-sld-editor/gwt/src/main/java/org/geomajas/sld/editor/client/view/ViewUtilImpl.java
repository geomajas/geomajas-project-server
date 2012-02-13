package org.geomajas.sld.editor.client.view;

import org.geomajas.sld.client.view.ViewUtil;

import com.smartgwt.client.util.SC;


public class ViewUtilImpl implements ViewUtil {

	public void showMessage(String message) {
		SC.say(message);
	}

}
