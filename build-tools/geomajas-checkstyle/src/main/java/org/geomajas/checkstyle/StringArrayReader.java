/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.checkstyle;

import java.io.IOException;
import java.io.Reader;

/**
 * A Reader that reads from an underlying String array, assuming each
 * array element corresponds to one line of text.
 * <p>
 * <strong>Note: This class is not thread safe, concurrent reads might produce
 * unexpected results! </strong> Checkstyle only works with one thread so
 * currently there is no need to introduce synchronization here.
 * </p>
 * @author <a href="mailto:lkuehne@users.sourceforge.net">Lars Kühne</a>
 *
 * This file was copied from checkstyle and is originally licensed as LGPL.
 */
final class StringArrayReader extends Reader {

	/** the underlying String array */
	private final String[] mUnderlyingArray;

	/** array containing the length of the strings. */
	private final int[] mLenghtArray;

	/** the index of the currently read String */
	private int mArrayIdx;

	/** the index of the next character to be read */
	private int mStringIdx;

	/** flag to tell whether an implicit newline has to be reported */
	private boolean mUnreportedNewline;

	/** flag to tell if the reader has been closed */
	private boolean mClosed;

	/**
	 * Creates a new StringArrayReader.
	 *
	 * @param aUnderlyingArray the underlying String array.
	 */
    StringArrayReader(String[] aUnderlyingArray)
    {
        final int length = aUnderlyingArray.length;
        mUnderlyingArray = new String[length];
        System.arraycopy(aUnderlyingArray, 0, mUnderlyingArray, 0, length);

        //additionally store the length of the strings
        //for performance optimization
        mLenghtArray = new int[length];
        for (int i = 0; i < length; i++) {
            mLenghtArray[i] = mUnderlyingArray[i].length();
        }
    }

    @Override
    public void close()
    {
        mClosed = true;
    }

    /** @return whether data is available that has not yet been read. */
    private boolean dataAvailable()
    {
        return (mUnderlyingArray.length > mArrayIdx);
    }

    @Override
    public int read(char[] aCbuf, int aOff, int aLen) throws IOException
    {
        ensureOpen();

        int retVal = 0;

        if (!mUnreportedNewline && (mUnderlyingArray.length <= mArrayIdx)) {
            return -1;
        }

        while ((retVal < aLen) && (mUnreportedNewline || dataAvailable())) {
            if (mUnreportedNewline) {
                aCbuf[aOff + retVal] = '\n';
                retVal++;
                mUnreportedNewline = false;
            }

            if ((retVal < aLen) && dataAvailable()) {
                final String currentStr = mUnderlyingArray[mArrayIdx];
                final int currentLenth = mLenghtArray[mArrayIdx];
                final int srcEnd = Math.min(currentLenth,
                                            mStringIdx + aLen - retVal);
                currentStr.getChars(mStringIdx, srcEnd, aCbuf, aOff + retVal);
                retVal += srcEnd - mStringIdx;
                mStringIdx = srcEnd;

                if (mStringIdx >= currentLenth) {
                    mArrayIdx++;
                    mStringIdx = 0;
                    mUnreportedNewline = true;
                }
            }
        }
        return retVal;
    }

    @Override
    public int read() throws IOException
    {
        if (mUnreportedNewline) {
            mUnreportedNewline = false;
            return '\n';
        }

        if ((mArrayIdx < mUnderlyingArray.length)
            && (mStringIdx < mLenghtArray[mArrayIdx]))
        {
            // this is the common case,
            // avoid char[] creation in super.read for performance
            ensureOpen();
            return mUnderlyingArray[mArrayIdx].charAt(mStringIdx++);
        }
        // don't bother duplicating the new line handling above
        // for the uncommon case
        return super.read();
    }

    /**
     * Throws an IOException if the reader has already been closed.
     *
     * @throws IOException if the stream has been closed
     */
    private void ensureOpen() throws IOException
    {
        if (mClosed) {
            throw new IOException("already closed");
        }
    }
}
