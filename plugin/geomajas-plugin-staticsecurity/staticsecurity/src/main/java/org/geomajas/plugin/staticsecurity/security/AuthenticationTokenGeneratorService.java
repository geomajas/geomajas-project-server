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

package org.geomajas.plugin.staticsecurity.security;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Generator for authentication tokens.
 * <p/>
 *  We need a universal object if which is fast to generate, has
 * a minimal (no) configuration overhead and is cross platform. In accordance with Scott Ambler's article about this
 * ("Enterprise Ready Object IDs", @link http://www.sdmagazine.com/print/documentID=11250) we use a HIGH-LOW scheme.
 * <p/>
 * The idea is to use the time of initialisation of this class as base for the
 * HIGH address. This should prove to be quite random and chances are very, very slim that a second system which needs
 * the same database will also use this timestamp as base for HIGH value. However, to decrease the likelyhood even
 * further, the HIGH value also contains a random number which does not use the time at it's seed.
 * <p/>
 * For LOW values
 * we just use a counter and we force a new HIGH value when the counter runs out. Contrary to normal practice, the LOW
 * counter does not start at zero, but at a random number. This increases the distribution of token values.
 * <p/>
 * The token consists of a 36bit time, and 30bit random number, giving a 66bit high
 * value, and a 18bit LOW value, giving a 84 bit key. This is converted to a 14 digit String using base64 encoding.
 * However, the digits are encoded back to front to make the token strings discriminate from the start.
 * <p/>
 * There are
 * some things which can be chosen in this scheme. The random part of the HIGH value could be regenerated for each token
 * (giving a HIGH/RAND/LOW scheme), or just once (as in true HIGH/LOW scheme). When going for a once generated random,
 * the question is whether you change just the time part or both time and random parts when you run out of LOW values
 * and have to generate a new HIGH as a result of that.
 * <p/>
 * Original code from org.equanda.persistence.UoidGenerator class in equanda.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class AuthenticationTokenGeneratorService {

	private static final Integer LOCK = 0; // lock to force sequential access
	private static long TIME = System.currentTimeMillis(); // time part of high value
	private static boolean INITIALISED; // true when id is initialised
	private static byte[] VALUE =
			new byte[14]; // array to build token in, normally contains high in fourth and following characters
	private static int LOW; // next value for LOW
	private static int LOW_LAST; // last value for LOW, when low==lowLast then a new high has to be
	// regenerated (test on increment, increment after use)
	private static final int LOW_MAX = 262144; // max value for LOW (==2^18)
	private static byte[] BASE64 = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z', '-', '.', 'a', 'b',
			'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z'};

	/**
	 * Get a new token.
	 *
	 * @return 14 character String
	 */
	public String get() {
		synchronized (LOCK) {
			if (!INITIALISED) {
				// generate the random number
				Random rnd = new Random();	// @todo need a different seed, this is now time based and I
				// would prefer something different, like an object address
				// get the random number, instead of getting an integer and converting that to base64 later,
				// we get a string and narrow that down to base64, use the top 6 bits of the characters
				// as they are more random than the bottom ones...
				rnd.nextBytes(VALUE);		// get some random characters
				VALUE[3] = BASE64[((VALUE[3] >> 2) & 0x3f)];
				VALUE[4] = BASE64[((VALUE[4] >> 2) & 0x3f)];
				VALUE[5] = BASE64[((VALUE[5] >> 2) & 0x3f)];
				VALUE[6] = BASE64[((VALUE[6] >> 2) & 0x3f)];
				VALUE[7] = BASE64[((VALUE[7] >> 2) & 0x3f)];

				// complete the time part in the HIGH value of the token
				// this also sets the initial low value
				completeToken(rnd);

				INITIALISED = true;
			}

			// fill in LOW value in id
			int l = LOW;
			VALUE[0] = BASE64[(l & 0x3f)];
			l >>= 6;
			VALUE[1] = BASE64[(l & 0x3f)];
			l >>= 6;
			VALUE[2] = BASE64[(l & 0x3f)];

			String res = new String(VALUE);

			// increment LOW
			LOW++;
			if (LOW == LOW_MAX) {
				LOW = 0;
			}
			if (LOW == LOW_LAST) {
				TIME = System.currentTimeMillis();
				completeToken();
			}

			return res;
		}
	}

	private void completeToken() {
		completeToken(new Random());
	}

	private void completeToken(Random rnd) {
		// fill in time part in token string
		long t = TIME;
		for (int i = 0; i < 6; i++) {
			VALUE[8 + i] = BASE64[(((int) t) & 0x3f)];
			t >>= 6;
		}

		// generate new LOW start value
		LOW = rnd.nextInt(LOW_MAX);
		LOW_LAST = LOW;
	}
}
