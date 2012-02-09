package org.geomajas.sld.client.presenter;

/**
 * Provides call-back to be called when the user has interacted with the Viewer so that
 * the model data has changed since the last copyToView(model) or resetAndFocus() on the Viewer.
 *
 * @author An Buyle
 *
 */

public interface ChangeHandler {
	void execute();
}
