/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.command.staticsecurity;

/**
 * Encodes and decodes to and from Base64 notation.
 *
 * @author Robert Harder
 * @author rob@iharder.net
 * @version 2.0
 */
public final class Base64 {

	/** The equals sign (=) as a byte. */
	private static final byte EQUALS_SIGN = (byte) '=';

	/** Preferred encoding. */
	private static final String PREFERRED_ENCODING = "UTF-8";

	public static final int BITS_6 = 0x3f;
	public static final int SHIFT_1S = 6; // one time six bits
	public static final int SHIFT_2S = 12; // two times six bits
	public static final int SHIFT_3S = 18; // three times six bits
	public static final int SHIFT_1B = 8; // one byte
	public static final int SHIFT_2B = 16; // two bytes
	public static final int SHIFT_3B = 24; // three bytes


	/** The 64 valid Base64 values. */
	private static final byte[] ALPHABET;
	private static final byte[] NATIVE_ALPHABET = /* May be something funny like EBCDIC */
			{
					(byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G',
					(byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N',
					(byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U',
					(byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z',
					(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g',
					(byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n',
					(byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u',
					(byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',
					(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
					(byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '+', (byte) '/',
			};

	/** Determine which ALPHABET to use. */
	static {
		byte[] bytes;
		try {
			bytes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes(PREFERRED_ENCODING);
		} catch (java.io.UnsupportedEncodingException use) {
			bytes = NATIVE_ALPHABET; // Fall back to native encoding
		}
		ALPHABET = bytes;
	}

	/** Prevent instantiation. */
	private Base64() {
	}


/* ********  E N C O D I N G   M E T H O D S  ******** */


	/**
	 * Encodes up to three bytes of the array <var>source</var> and writes the resulting four Base64 bytes to
	 * <var>destination</var>. The source and destination arrays can be manipulated anywhere along their length by
	 * specifying <var>srcOffset</var> and <var>destOffset</var>. This method does not check to make sure your arrays
	 * are large enough to accomodate <var>srcOffset</var> + 3 for the <var>source</var> array or
	 * <var>destOffset</var> + 4 for the <var>destination</var> array. The actual number of significant bytes in your
	 * array is given by <var>numSigBytes</var>.
	 *
	 * @param source the array to convert
	 * @param srcOffset the index where conversion begins
	 * @param numSigBytes the number of significant bytes in your array
	 * @param destination the array to hold the conversion
	 * @param destOffset the index where output will be put
	 * @return the <var>destination</var> array
	 */
	private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination,
									 int destOffset) {
		//           1         2         3
		// 01234567890123456789012345678901 Bit position
		// --------000000001111111122222222 Array position from threeBytes
		// --------|    ||    ||    ||    | Six bit groups to index ALPHABET
		//          >>18  >>12  >> 6  >> 0  Right shift necessary
		//                0x3f  0x3f  0x3f  Additional AND

		// Create buffer with zero-padding if there are only one or two
		// significant bytes passed in the array.
		// We have to shift left 24 in order to flush out the 1's that appear
		// when Java treats a value as negative that is cast from a byte to an int.
		int inBuff = (numSigBytes > 0 ? ((source[srcOffset] << SHIFT_3B) >>> SHIFT_1B) : 0)
				| (numSigBytes > 1 ? ((source[srcOffset + 1] << SHIFT_3B) >>> SHIFT_2B) : 0)
				| (numSigBytes > 2 ? ((source[srcOffset + 2] << SHIFT_3B) >>> SHIFT_3B) : 0);

		switch (numSigBytes) {
			case 3: // NOSONAR
				destination[destOffset] = ALPHABET[(inBuff >>> SHIFT_3S)];
				destination[destOffset + 1] = ALPHABET[(inBuff >>> SHIFT_2S) & BITS_6];
				destination[destOffset + 2] = ALPHABET[(inBuff >>> SHIFT_1S) & BITS_6];
				destination[destOffset + 3] = ALPHABET[(inBuff) & BITS_6]; // NOSONAR
				return destination;

			case 2:
				destination[destOffset] = ALPHABET[(inBuff >>> SHIFT_3S)];
				destination[destOffset + 1] = ALPHABET[(inBuff >>> SHIFT_2S) & BITS_6];
				destination[destOffset + 2] = ALPHABET[(inBuff >>> SHIFT_1S) & BITS_6];
				destination[destOffset + 3] = EQUALS_SIGN; // NOSONAR
				return destination;

			case 1:
				destination[destOffset] = ALPHABET[(inBuff >>> SHIFT_3S)];
				destination[destOffset + 1] = ALPHABET[(inBuff >>> SHIFT_2S) & BITS_6];
				destination[destOffset + 2] = EQUALS_SIGN;
				destination[destOffset + 3] = EQUALS_SIGN; // NOSONAR
				return destination;

			default:
				return destination;
		}
	}


	/**
	 * Encodes a byte array into Base64 notation. Does not GZip-compress data.
	 *
	 * @param source The data to convert
	 * @return bas64 encoded source
	 */
	public static String encodeBytes(byte[] source) {
		return encodeBytes(source, 0, source.length);
	}

	/**
	 * Encodes a byte array into Base64 notation.
	 * <p/>
	 * Valid options:<pre>
	 *   GZIP: gzip-compresses object before encoding it.
	 *   DONT_BREAK_LINES: don't break lines at 76 characters
	 *     <i>Note: Technically, this makes your encoding non-compliant.</i>
	 * </pre>
	 * <p/>
	 * Example: <code>encodeBytes( myData, Base64.GZIP )</code> or
	 * <p/>
	 * Example: <code>encodeBytes( myData, Base64.GZIP | Base64.DONT_BREAK_LINES )</code>
	 *
	 * @param source The data to convert
	 * @param off Offset in array where conversion should begin
	 * @param len Length of data to convert
	 * @return bas64 encoded source
	 */
	public static String encodeBytes(byte[] source, int off, int len) {
			int len43 = len * 4 / 3;  // NOSONAR
			byte[] outBuff = new byte[(len43) + ((len % 3) > 0 ? 4 : 0)]; // NOSONAR Account for padding
			int d = 0;
			int e = 0;
			int len2 = len - 2;
			for ( ; d < len2 ; d += 3, e += 4) { // NOSONAR
				encode3to4(source, d + off, 3, outBuff, e); // NOSONAR
			}

			if (d < len) {
				encode3to4(source, d + off, len - d, outBuff, e);
				e += 4; // NOSONAR
			}

			// Return value according to relevant encoding.
			try {
				return new String(outBuff, 0, e, PREFERRED_ENCODING);
			} catch (java.io.UnsupportedEncodingException uue) {
				return new String(outBuff, 0, e);
			}
	}

}
