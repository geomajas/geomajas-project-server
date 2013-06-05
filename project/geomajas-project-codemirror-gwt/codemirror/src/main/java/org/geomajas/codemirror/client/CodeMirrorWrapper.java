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
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.ui.TextArea;

/**
 * This class wraps the CodeMirror Javascript object.
 * 
 * @author Kristof Heirwegh
 * @since 3.1.1
 */
@Api( allMethods = true )
public class CodeMirrorWrapper {

	private JavaScriptObject codeMirrorJs;

	/**
	 * Create a new GWT object wrapping the given TextAreaElement.
	 * 
	 * @param tae
	 * @param config
	 */
	public CodeMirrorWrapper(TextAreaElement tae, Config config) {
		codeMirrorJs = fromTextAreaJs(tae, config.asJSOject());
	}

	/**
	 * Convenience method creating a new GWT object wrapping the TextAreaElement with given id.
	 * @param id
	 * @param config
	 * @return CodeMirrorWrapper
	 */
	public static CodeMirrorWrapper fromTextArea(String id, Config config) {
		return fromTextArea(TextAreaElement.as(Document.get().getElementById(id)), config);
	}

	/**
	 * Convenience method creating a new GWT object wrapping the given TextAreaElement.
	 * @param id
	 * @param config
	 * @return CodeMirrorWrapper
	 */
	public static CodeMirrorWrapper fromTextArea(TextAreaElement tae, Config config) {
		return new CodeMirrorWrapper(tae, config);
	}

	/**
	 * Convenience method creating a new GWT object wrapping the given TextArea.
	 * @param id
	 * @param config
	 * @return CodeMirrorWrapper
	 */
	public static CodeMirrorWrapper fromTextArea(TextArea ta, Config config) {
		return fromTextArea(ta.getElement().<TextAreaElement>cast(), config);
	}

	// ---------------------------------------------------------------

	/**
	 * Get the contents (= data) of this editor.
	 * @return data
	 */
	public String getContent() {
		return callStringFunction(codeMirrorJs, "getValue");
	}

	/**
	 * Set the contents (= dta) of this editor.
	 * @param content
	 */
	public void setContent(String content) {
		callFunction(codeMirrorJs, "setValue", content);
	}

	/**
	 * Call undo action on underlying editor.
	 */
	public void undo() {
		callFunction(codeMirrorJs, "undo");
	}

	/**
	 * Call redo action on underlying editor.
	 */
	public void redo() {
		callFunction(codeMirrorJs, "redo");
	}

	/**
	 * Call historySize function on underlying editor.
	 * @return historySize
	 */
	public int getHistorySize() {
		return callIntFunction(codeMirrorJs, "historySize");
	}

	/**
	 * Call clearHistory function on underlying editor.
	 */
	public void clearHistory() {
		callFunction(codeMirrorJs, "clearHistory");
	}

	/**
	 * Call clearHistory function on underlying editor.
	 */
	public void focus() {
		callFunction(codeMirrorJs, "focus");
	}

	/**
	 * Call isClean (= !dirty) function on underlying editor.
	 * @return isClean
	 */
	public boolean isClean() {
		return callBooleanFunction(codeMirrorJs, "isClean");
	}

	/**
	 * Call markClean function on underlying editor.
	 */
	public void markClean() {
		callFunction(codeMirrorJs, "markClean");
	}

	/**
	 * Set an editor option.
	 * <p>Take a look at the {@link Config} objects static strings for possible values.
	 * 
	 * @param key
	 * @param value
	 */
	public void setOption(String key, Object value) {
		callFunction(codeMirrorJs, key, value);
	}
	
	/**
	 * Get an editor option.
	 * Take a look at the {@link Config} objects static strings for possible values.
	 * 
	 * @param key
	 * @param value
	 */
	public Object getOption(String key) {
		return callFunction(codeMirrorJs, key);
	}
	
	/**
	 * Returns the internal Codemirror JavascriptObject.
	 * 
	 * @return Codemirror JavascriptObject
	 */
	public JavaScriptObject asJSOject() {
		return codeMirrorJs;
	}
	
	// ---------------------------------------------------------------

	private static native JavaScriptObject fromTextAreaJs(TextAreaElement tae, JavaScriptObject config) /*-{
		return $wnd.CodeMirror.fromTextArea(tae, config);
	}-*/;

	private static native Object callFunction(JavaScriptObject jso, String name) /*-{
		return jso[name].apply(jso);
	}-*/;

	private static native Object callFunction(JavaScriptObject jso, String name, Object arg1) /*-{
		return jso[name].apply(jso, [arg1]);
	}-*/;

	private static native boolean callBooleanFunction(JavaScriptObject jso, String name) /*-{
		return jso[name].apply(jso);
	}-*/;

	private static native String callStringFunction(JavaScriptObject jso, String name) /*-{
		return jso[name].apply(jso);
	}-*/;

	private static native int callIntFunction(JavaScriptObject jso, String name) /*-{
		return jso[name].apply(jso);
	}-*/;
}
