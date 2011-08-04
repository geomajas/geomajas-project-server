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
package org.geomajas.gwt.client.widget;

import java.util.HashMap;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Layout behaving like a deck of cards. Only one card visible at a time.
 * 
 * @author Jan De Moerloose
 */
//@Api or should this move to plugin-widget-utility
public class CardLayout extends VLayout {

	private HashMap<String, Canvas> cards = new HashMap<String, Canvas>();

	private Canvas currentCard;

	/**
	 * Add a card to the deck and associate it with the specified key.
	 * 
	 * @param key key associated to the card
	 * @param card the card
	 */
	public void addCard(String key, Canvas card) {
		addMember(card);
		cards.put(key, card);
		showCard(key);
	}

	/**
	 * Show the card associated with the specified key.
	 * 
	 * @param key key associated to the card that should be shown
	 */
	public void showCard(String key) {
		if (cards.containsKey(key)) {
			if (null != currentCard) {
				currentCard.hide();
			}
			currentCard = cards.get(key);
			currentCard.show();
		}
	}

	/**
	 * Returns the current card.
	 * 
	 * @return the current card.
	 */
	public Canvas getCurrentCard() {
		return currentCard;
	}
}