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
package org.subwayworld.metrogen.console;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.subwayworld.metrogen.MessageWriter;
import org.subwayworld.metrogen.MetroException;
import org.subwayworld.metrogen.MetroGenerator;
import org.subwayworld.metrogen.MetroSettings;

/**
 * Console wrapper for the segparser.
 */
public class ConsoleParser {
  /**
   * @param args
   */
  public static void main(String[] args) {
    ConsoleParser cp = new ConsoleParser();
    cp.doParse(args);
  }

  void doParse(String[] args) {
    MessageWriter mw = new ConsoleWriter("res.bundles.mgen");

    MetroSettings ms = new MetroSettings();
    List<String> fnames = parseCommandLine(args, ms, mw);

    MetroGenerator mgen = new MetroGenerator(ms, mw);
    for (String s : fnames) {
      try {
        InputStream is = new FileInputStream(s);
        mgen.go(is, s);
      } catch (FileNotFoundException | MetroException | SecurityException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Collect input from the commandline.
   * <p>
   * Set properties for the segparser and return the names of the city-files.
   * 
   * @param args
   *          the commandline arguments
   * @return the names of the city-files, or an empty list if no city-files are
   *         specified.
   */
  private List<String> parseCommandLine(String[] args, MetroSettings ms,
      MessageWriter mw) {

    if (0 == args.length) {
      mw.write("usage");
      return Collections.emptyList();
    }

    List<String> fnames = new ArrayList<>();
    for (String s : args) {
      if (s.equals("-direct")) {
        ms.dbDirect = true;
        continue;
      }

      if (!s.startsWith("-")) {
        // an argument without a minus must be a filename.
        fnames.add(s);
        continue;
      }
    }
    return fnames;
  }
}

// @SuppressWarnings("unused")
//// replaced by SegparserConsole
// private void parseCommandLine(String[] args) {
//
// if (0 == args.length) {
// String msg = "Usage: java -jar SegParser.jar [-options] city-file";
// System.out.println(msg);
// System.out.println("Options:");
// System.out.println(" -db2: generate DB2 SQL files.");
// System.out.println(" -oracle: generate Oracle SQL files.");
// System.out.println(" Default: generate MySQL SQL files.");
// System.out.println(" -latin1: input files are in Latin-1 encoding.");
// System.out.println(" Default: UTF-8 encoded input files.");
// System.out.println(" -text: generate textfile with SEGMENTs.");
// System.out.println(" -db: direct database update.");
// System.out.println(" mysql: -db:mysql:hostname:database:password");
// return;
// }
//
// for (String s : args) {
// if (s.equals("-db2")) {
// m_dbtype = DBTYPE.DBT_DB2;
// continue;
// }
// if (s.equals("-oracle")) {
// m_dbtype = DBTYPE.DBT_ORACLE;
// continue;
// }
// if (s.equals("-latin1")) {
// encoding = "ISO-8859-1";
// continue;
// }
// if (s.equals("-text")) {
// genText = true;
// continue;
// }
// if (s.startsWith("-db")) {
// dbDirect = true;
// String[] dbConnectInfo = s.split(":");
// // mysql: -db:mysql:hostname:database:password
// if ("mysql".equals(dbConnectInfo[1])) {
// connectinfo = new Properties();
// connectinfo.put("dbtype", dbConnectInfo[1]);
// connectinfo.put("hostname", dbConnectInfo[2]);
// connectinfo.put("database", dbConnectInfo[3]);
// connectinfo.put("password", dbConnectInfo[4]);
// }
// continue;
// }
// if (!s.startsWith("-")) {
// // an argument without a minus must be a filename.
// // networkFile = s;
// continue;
// }
// }
// return;
// }
