dojo.provide("geomajas.widget.ActivityDiv");
/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.requireLocalization("geomajas.widget", "activityDiv");

dojo.declare("geomajas.widget.ActivityDiv", [dijit._Widget, dijit._Templated], {

	startText : "Fetching data!",
	stopText : "Fetching done.",
	locked : false,
	showCount : false,
	timeOut : 60000, // 1 minute

	templateString : '<div class="activityDiv"></div>',

	/**
	 * Initialization function for this widget.
	 * @param dispatcher Reference to a CommandDispatcher object, required
	 *                   for analyzing when command are being send, and when
	 *                   not so.
	 */
	init : function (dispatcher) {
		var widgetLocale = dojo.i18n.getLocalization("geomajas.widget", "activityDiv");
		this.startText = widgetLocale.startText;
		this.stopText = widgetLocale.stopText;

		this.dispatcher = dispatcher;
		this.count = 0;
		dojo.addClass(this.domNode, "activityDivStop");

		this.callbackHandle = dojo.connect(this.dispatcher, "onCallback", dojo.hitch(this,"onCallBack"));
		this.executeHandle = dojo.connect(this.dispatcher, "execute", dojo.hitch(this,"onExecute"));
		this.dispatcher.setActivityDiv(this);
	},

	/**
	 * Destructor function. Cleans up the connections to the CommandDispatcher
	 * before destroying itself.
	 */
	destroy : function (finalize) {
		dojo.disconnect(this.callbackHandle);
		dojo.disconnect(this.executeHandle);
		this.inherited(arguments);
	},

	/**
	 * This function is executed after each command execution.
	 */
	onExecute : function (command) {
		if (!command.isSync()) {
			this.increment();
		}
	},

	/**
	 * This function is executed after each command callback.
	 */
	onCallBack : function () {
		this.decrement();
	},

	/**
	 * Executed when at least one command is busy. It alters the widget's
	 * appearance.
	 */
	startAnimation : function () {
		if (!this.locked) {
			var textNode = document.createTextNode(this.startText);
			if (this.domNode.firstChild) {
				this.domNode.removeChild(this.domNode.firstChild);
			}
			dojo.removeClass(this.domNode, "activityDivStop");
			dojo.addClass(this.domNode, "activityDivStart");
			this.domNode.appendChild(textNode);
		}
	},

	/**
	 * Executed when at no commands are busy. It alters the widget's
	 * appearance.
	 */
	stopAnimation : function () {
		if (!this.locked) {
			this.count = 0;
			var textNode = document.createTextNode(this.stopText);
			if (this.domNode.firstChild) {
				this.domNode.removeChild(this.domNode.firstChild);
			}
			dojo.removeClass(this.domNode, "activityDivStart");
			dojo.addClass(this.domNode, "activityDivStop");
			this.domNode.appendChild(textNode);
		}
	},
	
	lock : function () {
		this.locked = true;
	},
	
	unlock : function () {
		this.locked = false;
	},

	/**
	 * allow other activity than json-commands to keep state as busy
	 */
	increment : function () {
		this.count++;
		if (this.count == 1) {
			this.startAnimation();
		}
		if (this.showCount == true && this.count > 0) {
			var text = this.domNode.firstChild;
			if (text)
				text.data = this.startText + " (" + this.count + ")";
		}
		if (this.timer) clearTimeout(this.timer);
		this.timer = setTimeout(dojo.hitch(this,"_waitingTooLong"), this.timeOut);
	},

	/**
	 * make sure in & decrements level out
	 */
	decrement : function () {
		if (this.count > 0) this.count--;
		if (this.count == 0) {
			this.stopAnimation();
		}
		if (this.showCount == true && this.count > 0) {
			var text = this.domNode.firstChild;
			if (text)
				text.data = this.startText + " (" + this.count + ")";
		}
	},

	_waitingTooLong : function () {
		log.warn("ActivityDiv: Command timed out.");
		this.count = 1;
		this.decrement();
	}

});
