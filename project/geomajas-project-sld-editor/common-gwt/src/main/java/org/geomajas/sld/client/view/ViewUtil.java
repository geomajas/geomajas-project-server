package org.geomajas.sld.client.view;

public interface ViewUtil {

	interface YesNoCallback {

		void onYes();

		void onNo();

		void onCancel();
	}

	void showMessage(String message);

	void showYesNoMessage(String message, YesNoCallback callback);
}
