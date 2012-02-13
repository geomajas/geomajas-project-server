package org.geomajas.sld.editor.client.view;

import org.geomajas.sld.client.view.ViewUtil;

import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;

public class ViewUtilImpl implements ViewUtil {

	public void showMessage(String message) {
		SC.say(message);
	}

	public void showYesNoMessage(String message, final YesNoCallback callback) {
		SC.ask(message, new BooleanCallback() {

			public void execute(Boolean value) {
				if (value == null) {
					callback.onCancel();
				} else if (value) {
					callback.onYes();
				} else {
					callback.onNo();
				}

			}
		});
	}

}
