/*  Copyright (C) 2003-2015 JabRef contributors.
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.sf.jabref.exporter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Set;
import java.util.TreeSet;

/**
 * Writer that extends OutputStreamWriter, but also checks if the chosen
 * encoding supports all text that is written. Currently only a boolean value is
 * stored to remember whether everything has gone well or not.
 */
public class VerifyingWriter extends OutputStreamWriter {

    private final CharsetEncoder encoder;
    private final Set<Character> problemCharacters = new TreeSet<>();
    private boolean couldEncodeAll = true;


    public VerifyingWriter(OutputStream out, Charset encoding) {
        super(out, encoding);
        encoder = encoding.newEncoder();
    }

    @Override
    public void write(String str) throws IOException {
        super.write(str);
        if (!encoder.canEncode(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (!encoder.canEncode(str.charAt(i))) {
                    problemCharacters.add(str.charAt(i));
                }
            }
            couldEncodeAll = false;
        }
    }

    public boolean couldEncodeAll() {
        return couldEncodeAll;
    }

    public String getProblemCharacters() {
        StringBuilder chars = new StringBuilder();
        for (Character ch : problemCharacters) {
            chars.append(ch.charValue());
        }
        return chars.toString();
    }
}
