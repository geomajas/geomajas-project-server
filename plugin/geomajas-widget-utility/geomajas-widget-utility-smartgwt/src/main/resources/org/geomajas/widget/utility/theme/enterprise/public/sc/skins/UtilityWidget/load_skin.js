
isc.loadSkin = function(theWindow) {
	if (theWindow == null)
		theWindow = window;
	with (theWindow) {
		isc.Page.loadStyleSheet("[ISOMORPHIC]/skins/UtilityWidget/skin_styles.css", theWindow);
	}
}

// call the loadSkin routine
isc.loadSkin();