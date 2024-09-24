/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.subwayworld.metrogen.input;

import org.subwayworld.metrogen.MetroException;

/**
 * Utility functions for UTF-strings.
 * 
 */
public class UTFHandler {

  /**
   * Maps a unicode string to its ascii equivalent.
   * 
   * @param s
   *          the unicode string.
   * 
   * @return the ascii equivalent of the input string.
   * 
   * @throws MetroException
   *           when a unicode character cannot be mapped to an ascii equivalent.
   */
  public static String removeAccents(String s) throws MetroException {

    StringBuilder sb = new StringBuilder();
    char[] input = s.toCharArray();

    String asciiChar;
    int i = 0; // declared here to be able to use in exception
    char c = ' '; // declared here to be able to use in exception.
    try {
      for (i = 0; i < input.length; i++) {
        c = input[i];

        /*
         * Unicode blocks:
         * 
         * Basic Latin: ASCII control codes and letters [0000-007F].
         * 
         * Latin-1 Supplement: encode upper range of 8859-1 [0080-00FF].
         * 
         * Latin Extended-A: Latin characters not in Latin-1 [0100-017F].
         * 
         * Latin Ligatures: Alphabetic Presentation Forms [FB00-FB4F]
         */
        if (c >= '\u0000' && c <= '\u007F') {
          char c2 = toAsciiFromLatinBasic(c);
          sb.append(c2);
          continue;
        }

        if (c >= '\u0080' && c <= '\u00FF') {
          asciiChar = toAsciiFromLatin1(c);
          sb.append(asciiChar);
          continue;
        }

        if (c >= '\u0100' && c <= '\u017F') {
          asciiChar = toAsciiFromLatinExtendedA(c);
          sb.append(asciiChar);
          continue;
        }

        if (c >= '\uFB00' && c <= '\uFB4F') {
          asciiChar = toAsciiFromLatinLigatures(c);
          sb.append(asciiChar);
          continue;
        }
        assert false;
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException e) {
      throw new MetroException("INPUT-00001", "character " + i
          + " out of range <" + c + ">: " + s);
    }
    return sb.toString();
  }

  /**
   * Replace character in Latin Basic block with ASCII equivalent.
   * <p>
   * If no equivalent exists? If out of range?
   * 
   * @param c
   *          the character to replace
   * @return the ASCII equivalent.
   */
  private static char toAsciiFromLatinBasic(char c) {
    assert c >= '\u0000';
    assert c <= '\u007F';
    if ((c >= '\u0000' && c <= '\u001F') || ('\u007F' == c)) {
      throw new IllegalArgumentException(); // Control characters.
    }
    if (c >= '\u0020' && c <= '\u002F') {
      return '_';
    }
    if (c >= '\u003A' && c <= '\u0040') {
      return '_';
    }
    if (c >= '\u005B' && c <= '\u0060') {
      return '_';
    }
    if (c >= '\u007B' && c <= '\u007E') {
      return '_';
    }
    if (c >= '\u0030' && c <= '\u0039') {
      return c; // digits
    }
    if (c >= '\u0041' && c <= '\u005A') {
      return c; // uppercase letters
    }
    if (c >= '\u0061' && c <= '\u007A') {
      return c; // lowercase letters
    }
    assert false;
    throw new IllegalArgumentException();
  }

  /**
   * Replace character in Latin-1 block with ASCII equivalent.
   * <p>
   * If no equivalent exists? If out of range?
   * 
   * @param c
   *          the character to replace
   * @return the ASCII equivalent.
   */
  private static String toAsciiFromLatin1(char c) {
    assert c >= '\u0080';
    assert c <= '\u00FF';
    if (c >= '\u0080' && c <= '\u009F') {
      throw new IllegalArgumentException(); // Control characters.
    }
    if (c >= '\u00A0' && c <= '\u00BF') {
      return "_";
    }
    if ('\u00D7' == c || '\u00F7' == c) {
      // division and multiplication
      return "_";
    }
    switch (c) {
    case '\u00C0': // A grave (accent grave, linksboven => rechtsonder)
    case '\u00C1': // A acute (accent, linksonder => rechtsboven)
    case '\u00C2': // A circumflex (dakje)
    case '\u00C3': // A tilde (spaanse slinger erboven)
    case '\u00C4': // A diaeresis (umlaut)
    case '\u00C5': // A ring above (klein cirkeltje erboven)
      return "A";
    case '\u00C6': // AE
      return "AE";
    case '\u00C7': // C cedilla (staartje eronder)
      return "C";
    case '\u00C8': // E grave
    case '\u00C9': // E acute
    case '\u00CA': // E circumflex
    case '\u00CB': // E diaeresis
      return "E";
    case '\u00CC': // I grave
    case '\u00CD': // I acute
    case '\u00CE': // I circumflex
    case '\u00CF': // I diaeresis
      return "I";
    case '\u00D0': // ETH (D met streepje door verticale streep)
      return "D";
    case '\u00D1': // N tilde
      return "N";
    case '\u00D2': // O grave
    case '\u00D3': // O acute
    case '\u00D4': // O circumflex
    case '\u00D5': // O tilde
    case '\u00D6': // O diaeresis
    case '\u00D8': // O stroke (knackebrod O)
      return "O";
    case '\u00DF': // captial ringel ess
      return "ss";
    case '\u00DE': // capital thorn
      return "TH";
    case '\u00D9': // U grave
    case '\u00DA': // U acute
    case '\u00DB': // U circumflex
    case '\u00DC': // U diaeresis
      return "U";
    case '\u00DD': // Y acute
      return "Y";
    case '\u00E0': // a grave
    case '\u00E1': // a acute
    case '\u00E2': // a circumflex
    case '\u00E3': // a tilde
    case '\u00E4': // a diaeresis
    case '\u00E5': // a ring above
      return "a";
    case '\u00E6': // ae
      return "ae";
    case '\u00E7': // c cedilla
      return "c";
    case '\u00E8': // e grave
    case '\u00E9': // e acute
    case '\u00EA': // e circumflex
    case '\u00EB': // e diaeresis
      return "e";
    case '\u00EC': // i grave
    case '\u00ED': // i acute
    case '\u00EE': // i circumflex
    case '\u00EF': // i diaeresis
      return "i";
    case '\u00F0': // eth
      return "d";
    case '\u00F1': // n tilde
      return "n";
    case '\u00F2': // o grave
    case '\u00F3': // o acute
    case '\u00F4': // o circumflex
    case '\u00F5': // o tilde
    case '\u00F6': // o diaeresis
    case '\u00F8': // o stroke
      return "o";
    case '\u00FE': // small thorn
      return "th";
    case '\u00F9': // u grave
    case '\u00FA': // u acute
    case '\u00FB': // u circumflex
    case '\u00FC': // u diaeresis
      return "u";
    case '\u00FD': // y acute
    case '\u00FF': // y diaeresis
      return "y";
    }
    assert false;
    throw new IllegalArgumentException();
  }

  /**
   * Replace character in Latin Extended-A block with ASCII equivalent.
   * <p>
   * If no equivalent exists? If out of range?
   * 
   * @param c
   *          the character to replace
   * @return the ASCII equivalent.
   */
  private static String toAsciiFromLatinExtendedA(char c) {
    assert c >= '\u0100';
    assert c <= '\u017F';
    switch (c) {
    case '\u0100': // A macron
    case '\u0102': // A breve
    case '\u0104': // A ogonek
      return "A";
    case '\u0101': // a macron
    case '\u0103': // a breve
    case '\u0105': // a ogonek
      return "a";
    case '\u0106': // C acute
    case '\u0108': // C circumflex
    case '\u010A': // C dot above
    case '\u010C': // C caron
      return "C";
    case '\u0107': // c acute
    case '\u0109': // c circumflex
    case '\u010B': // c dot above
    case '\u010D': // c caron
      return "c";
    case '\u010E': // D caron
    case '\u0110': // D stroke
      return "D";
    case '\u010F': // d caron
    case '\u0111': // d stroke
      return "d";
    case '\u0112': // E macron
    case '\u0114': // E breve
    case '\u0116': // E dot above
    case '\u0118': // E ogonek
    case '\u011A': // E caron
      return "E";
    case '\u0113': // e macron
    case '\u0115': // e breve
    case '\u0117': // e dot above
    case '\u0119': // e ogonek
    case '\u011B': // e caron
      return "e";
    case '\u011C': // G circumflex
    case '\u011E': // G breve
    case '\u0120': // G dot above
    case '\u0122': // G cedilla
      return "G";
    case '\u011D': // g circumflex
    case '\u011F': // g breve
    case '\u0121': // g dot above
    case '\u0123': // g cedilla
      return "g";
    case '\u0124': // H circumflex
    case '\u0126': // H with stroke
      return "H";
    case '\u0125': // h circumflex
    case '\u0127': // h with stroke
      return "h";
    case '\u0128': // I tilde
    case '\u012A': // I macron
    case '\u012C': // I breve
    case '\u012E': // I ogonek
    case '\u0130': // I with dot above
      return "I";
    case '\u0129': // i tilde
    case '\u012B': // i macron
    case '\u012D': // i breve
    case '\u012F': // i ogonek
    case '\u0131': // dotless i
      return "i";
    case '\u0132': // IJ
      return "IJ";
    case '\u0133': // ij.
      return "ij";
    case '\u0134': // J circumflex
      return "J";
    case '\u0135': // j circumflex
      return "j";
    case '\u0136': // K cedilla
      return "K";
    case '\u0137': // k cedilla
    case '\u0138': // small Kra
      return "k";
    case '\u0139': // L acute
    case '\u013B': // L cedilla
    case '\u013D': // L caron
    case '\u013F': // L middle dot
    case '\u0141': // L stroke
      return "L";
    case '\u013A': // l acute
    case '\u013C': // l cedilla
    case '\u013E': // l caron
    case '\u0140': // l middle dot
    case '\u0142': // l stroke
      return "l";
    case '\u0143': // N acute
    case '\u0145': // N cedilla
    case '\u0147': // N caron
    case '\u014A': // capital Eng
      return "N";
    case '\u0144': // n acute
    case '\u0146': // n cedilla
    case '\u0148': // n caron
    case '\u0149': // n preceded by apostrophe
    case '\u014B': // small Eng
      return "n";
    case '\u014C': // O macron
    case '\u014E': // O breve
    case '\u0150': // O double acute
      return "O";
    case '\u014D': // o macron
    case '\u014F': // o breve
    case '\u0151': // o double acute
      return "o";
    case '\u0152': // OE
      return "OE";
    case '\u0153': // oe
      return "oe";
    case '\u0154': // R acute
    case '\u0156': // R cedilla
    case '\u0158': // R caron
      return "R";
    case '\u0155': // r acute
    case '\u0157': // r cedilla
    case '\u0159': // r caron
      return "r";
    case '\u015A': // S acute
    case '\u015C': // S circumflex
    case '\u015E': // S cedilla
    case '\u0160': // S caron
      return "S";
    case '\u015B': // s acute
    case '\u015D': // s circumflex
    case '\u015F': // s cedilla
    case '\u0161': // s caron
    case '\u017F': // small long s
      return "s";
    case '\u0162': // T cedilla
    case '\u0164': // T caron
    case '\u0166': // T stroke
      return "T";
    case '\u0163': // t cedilla
    case '\u0165': // t caron
    case '\u0167': // t stroke
      return "t";
    case '\u0168': // U tilde
    case '\u016A': // U macron
    case '\u016C': // U breve
    case '\u016E': // U ring above
    case '\u0170': // U double acute
    case '\u0172': // U ogonek
      return "U";
    case '\u0169': // u tilde
    case '\u016B': // u macron
    case '\u016D': // u breve
    case '\u016F': // u ring above
    case '\u0171': // u double acute
    case '\u0173': // u ogonek
      return "u";
    case '\u0174': // W circumflex
      return "W";
    case '\u0175': // w circumflex
      return "w";
    case '\u0176': // Y circumflex
    case '\u0178': // Y diaeresis
      return "Y";
    case '\u0177': // y circumflex
      return "y";
    case '\u0179': // Z acute
    case '\u017B': // Z dot above
    case '\u017D': // Z caron
      return "Z";
    case '\u017A': // z acute
    case '\u017C': // z dot above
    case '\u017E': // z caron
      return "z";
    }
    assert false;
    throw new IllegalArgumentException();
  }

  /**
   * Replace character in Latin Ligatures block with ASCII equivalent.
   * <p>
   * If no equivalent exists? If out of range?
   * 
   * @param c
   *          the character to replace
   * @return the ASCII equivalent.
   */
  private static String toAsciiFromLatinLigatures(char c) {
    assert c >= '\uFB00';
    assert c <= '\uFB4F';
    switch (c) {
    case '\uFB00': // ff
      return "ff";
    case '\uFB01': // fi. Alphabetic Presentation Forms
      return "fi";
    case '\uFB02': // fl. Alphabetic Presentation Forms
      return "fl";
    case '\uFB03': // ffi. Alphabetic Presentation Forms
      return "ffi";
    case '\uFB04': // ffl. Alphabetic Presentation Forms
      return "ffl";
    case '\uFB05': // ft. Alphabetic Presentation Forms
      return "ft";
    case '\uFB06': // st. Alphabetic Presentation Forms
      return "st";
    }
    assert false;
    throw new IllegalArgumentException();
  }
}
