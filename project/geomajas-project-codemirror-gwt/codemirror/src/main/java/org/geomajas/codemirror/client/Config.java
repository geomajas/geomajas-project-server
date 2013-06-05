/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.codemirror.client;

import org.geomajas.annotation.Api;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Simple JavascriptObject with configuration options.
 * <p>
 * There are some options defined here (as static strings), see the codemirror manual for all possible options.
 * 
 * @see http://codemirror.net/doc/manual.html
 * @author Kristof Heirwegh
 * @since 3.1.1
 */
@Api
public class Config {

	/**
	 * The starting value of the editor. Can be a string, or a document object.
	 */
	public static final String VALUE = "value"; // : string|CodeMirror.Doc

	/**
	 * The mode to use. When not given, this will default to the first mode that was loaded. It may be a string, which
	 * either simply names the mode or is a MIME type associated with the mode. Alternatively, it may be an object
	 * containing configuration options for the mode, with a name property that names the mode (for example {name:
	 * "javascript", json: true}). The demo pages for each mode contain information about what configuration parameters
	 * the mode supports. You can ask CodeMirror which modes and MIME types have been defined by inspecting the
	 * CodeMirror.modes and CodeMirror.mimeModes objects. The first maps mode names to their constructors, and the
	 * second maps MIME types to mode specs.
	 */
	public static final String MODE = "mode"; // : string|object

	/**
	 * The theme to style the editor with. You must make sure the CSS file defining the corresponding .cm-s-[name]
	 * styles is loaded (see the theme directory in the distribution). The default is "default", for which colors are
	 * included in codemirror.css. It is possible to use multiple theming classes at once—for example "foo bar" will
	 * assign both the cm-s-foo and the cm-s-bar classes to the editor.
	 */
	public static final String THEME = "theme"; // : string

	/**
	 * How many spaces a block (whatever that means in the edited language) should be indented. The default is 2.
	 */
	public static final String INDENTUNIT = "indentUnit"; // : integer

	/**
	 * Whether to use the context-sensitive indentation that the mode provides (or just indent the same as the line
	 * before). Defaults to true.
	 */
	public static final String SMARTINDENT = "smartIndent"; // : boolean

	/**
	 * The width of a tab character. Defaults to 4.
	 */
	public static final String TABSIZE = "tabSize"; // : integer

	/**
	 * Whether, when indenting, the first N*tabSize spaces should be replaced by N tabs. Default is false.
	 */
	public static final String INDENTWITHTABS = "indentWithTabs"; // : boolean

	/**
	 * Configures whether the editor should re-indent the current line when a character is typed that might change its
	 * proper indentation (only works if the mode supports indentation). Default is true.
	 */
	public static final String ELECTRICCHARS = "electricChars"; // : boolean

	/**
	 * Determines whether horizontal cursor movement through right-to-left (Arabic, Hebrew) text is visual (pressing the
	 * left arrow moves the cursor left) or logical (pressing the left arrow moves to the next lower index in the
	 * string, which is visually right in right-to-left text). The default is false on Windows, and true on other
	 * platforms.
	 */
	public static final String RTLMOVEVISUALLY = "rtlMoveVisually"; // : boolean

	/**
	 * Configures the keymap to use. The default is "default", which is the only keymap defined in codemirror.js itself.
	 * Extra keymaps are found in the keymap directory. See the section on keymaps for more information.
	 */
	public static final String KEYMAP = "keyMap"; // : string

	/**
	 * Can be used to specify extra keybindings for the editor, alongside the ones defined by keyMap. Should be either
	 * null, or a valid keymap value.
	 */
	public static final String EXTRAKEYS = "extraKeys"; // : object

	/**
	 * Whether CodeMirror should scroll or wrap for long lines. Defaults to false (scroll).
	 */
	public static final String LINEWRAPPING = "lineWrapping"; // : boolean

	/**
	 * Whether to show line numbers to the left of the editor.
	 */
	public static final String LINENUMBERS = "lineNumbers"; // : boolean

	/**
	 * At which number to start counting lines. Default is 1.
	 */
	public static final String FIRSTLINENUMBER = "firstLineNumber"; // : integer

	/**
	 * A function used to format line numbers. The function is passed the line number, and should return a string that
	 * will be shown in the gutter.
	 */
	public static final String LINENUMBERFORMATTER = "lineNumberFormatter"; // : function(line: integer) -> string

	/**
	 * Can be used to add extra gutters (beyond or instead of the line number gutter). Should be an array of CSS class
	 * names, each of which defines a width (and optionally a background), and which will be used to draw the background
	 * of the gutters. May include the CodeMirror-linenumbers class, in order to explicitly set the position of the line
	 * number gutter (it will default to be to the right of all other gutters). These class names are the keys passed to
	 * setGutterMarker.
	 */
	public static final String GUTTERS = "gutters"; // : array<string>

	/**
	 * Determines whether the gutter scrolls along with the content horizontally (false) or whether it stays fixed
	 * during horizontal scrolling (true, the default).
	 */
	public static final String FIXEDGUTTER = "fixedGutter"; // : boolean

	/**
	 * This disables editing of the editor content by the user. If the special value "nocursor" is given (instead of
	 * simply true), focusing of the editor is also disallowed.
	 */
	public static final String READONLY = "readOnly"; // : boolean|string

	/**
	 * Whether the cursor should be drawn when a selection is active. Defaults to false.
	 */
	public static final String SHOWCURSORWHENSELECTING = "showCursorWhenSelecting"; // : boolean

	/**
	 * The maximum number of undo levels that the editor stores. Defaults to 40.
	 */
	public static final String UNDODEPTH = "undoDepth"; // : integer

	/**
	 * The period of inactivity (in milliseconds) that will cause a new history event to be started when typing or
	 * deleting. Defaults to 500.
	 */
	public static final String HISTORYEVENTDELAY = "historyEventDelay"; // : integer

	/**
	 * The tab index to assign to the editor. If not given, no tab index will be assigned.
	 */
	public static final String TABINDEX = "tabindex"; // : integer

	/**
	 * Can be used to make CodeMirror focus itself on initialization. Defaults to off. When fromTextArea is used, and no
	 * explicit value is given for this option, it will be set to true when either the source textarea is focused, or it
	 * has an autofocus attribute and no other element is focused.
	 */
	public static final String AUTOFOCUS = "autofocus"; // : boolean

	// ---------------------------------------------------------------

	private JavaScriptObject container;

	/**
	 * Creates a new empty configuration object, that can be used to create a codemirror editor.
	 * 
	 * @see CodeMirrorWrapper
	 */
	@Api
	public Config() {
		container = JavaScriptObject.createObject();
	}

	// ---------------------------------------------------------------

	public JavaScriptObject asJSOject() {
		return container;
	}

	public void setJSObject(JavaScriptObject container) {
		this.container = container;
	}

	/**
	 * Set a configuration option.
	 * <p>
	 * See static strings defined on this class for possible options (note that this is a non-exclusive list as
	 * Codemirror may be extended with extra plugins which all have their own configuration option.)
	 * 
	 * @param key
	 * @param value
	 */
	@Api
	public void setOption(String key, Object value) {
		setValue(container, key, value);
	}

	/**
	 * Get a configuration option.
	 * <p>
	 * Convenience method to return option as String.
	 * <p>
	 * See static strings defined on this class for possible options (note that this is a non-exclusive list as
	 * Codemirror may be extended with extra plugins which all have their own configuration option.)
	 * 
	 * @return option value if found.
	 */
	@Api
	public String getOptionAsString(String key) {
		Object v = getValue(container, key);
		if (v != null) {
			return v.toString();
		} else {
			return null;
		}
	}

	/**
	 * Get a configuration option.
	 * <p>
	 * See static strings defined on this class for possible options (note that this is a non-exclusive list as
	 * Codemirror may be extended with extra plugins which all have their own configuration option.)
	 * 
	 * @return option value if found.
	 */
	@Api
	public Object getOption(String key) {
		return getValue(container, key);
	}

	// ---------------------------------------------------------------

	private static native void setValue(JavaScriptObject object, String key, Object value) /*-{
		object[key] = value;
	}-*/;

	private static native Object getValue(JavaScriptObject object, String key) /*-{
		return object[key];
	}-*/;

	// ---------------------------------------------------------------

	/**
	 * Creates and returns a default {@link Config} object.
	 * 
	 * @see CodeMirrorWrapper
	 * @return An empty configuration.
	 */
	public static Config getDefault() {
		Config config = new Config();
		config.setOption(LINENUMBERS, true);
		return config;
	}

	/**
	 * Creates and returns a {@link Config} object usable for XML data.
	 * 
	 * @see CodeMirrorWrapper
	 * @return Configuration
	 */
	public static Config forXml() {
		Config config = getDefault();
		config.setOption(MODE, "xml");
		config.setOption("autoCloseTags", true);
		config.setOption("collapseRange", true);
		return config;
	}
}
