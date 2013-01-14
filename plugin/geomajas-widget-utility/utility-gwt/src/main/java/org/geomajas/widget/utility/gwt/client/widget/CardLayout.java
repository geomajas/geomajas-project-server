/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.utility.gwt.client.widget;

import java.util.HashMap;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.annotation.Api;

/**
 * Layout behaving like a deck of cards. Only one card visible at a time.
 *
 * @param <KEY_TYPE> type for the card key
 *
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api
public class CardLayout<KEY_TYPE> extends VLayout {

	private HashMap<KEY_TYPE, Canvas> cards = new HashMap<KEY_TYPE, Canvas>();

	private Canvas currentCard;

	/**
	 * Add a card to the deck and associate it with the specified key.
	 * 
	 * @param key key associated to the card
	 * @param card the card
	 * @since 1.0.0
	 */
	@Api
	public void addCard(KEY_TYPE key, Canvas card) {
		if (currentCard != null) {
			currentCard.hide();
		}
		addMember(card);
		currentCard = card;
		cards.put(key, card);
	}

	/**
	 * Show the card associated with the specified key.
	 * 
	 * @param key key associated to the card that should be shown
	 * @since 1.0.0
	 */
	@Api
	public void showCard(KEY_TYPE key) {
		Canvas newCurrent = cards.get(key);
		if (null != newCurrent) {
			if (newCurrent != currentCard && null != currentCard) {
				currentCard.hide();
			}
			currentCard = newCurrent;
			currentCard.show();
		}
	}

	/**
	 * Returns the current card.
	 * 
	 * @return the current card.
	 * @since 1.0.0
	 */
	@Api
	public Canvas getCurrentCard() {
		return currentCard;
	}
}