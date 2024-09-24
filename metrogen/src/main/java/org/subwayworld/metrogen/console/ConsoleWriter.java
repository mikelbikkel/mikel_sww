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
 *
 */
package org.subwayworld.metrogen.console;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.subwayworld.metrogen.MessageWriter;

/**
 * Writes messages to stdout.
 *
 */
public class ConsoleWriter implements MessageWriter {

  private final static Logger LOG = Logger.getLogger("metrogen");

  private final String m_rb;

  private final PrintWriter m_messages;

  /**
   * Creates a new writer.
   * 
   * @param rb
   *          the name of the resource bundle that contains the messages.
   * @throws NullPointerException
   *           when {@code rb} is null.
   */
  public ConsoleWriter(String rb) {
    if (null == rb) {
      throw new NullPointerException();
    }
    m_rb = rb;
    m_messages = new PrintWriter(new OutputStreamWriter(System.out));
  }

  @Override
  public void write(String msg_key, Object... args) {
    try {
      ResourceBundle rb = ResourceBundle.getBundle(m_rb, Locale.getDefault());
      String s = rb.getString(msg_key);
      if (null != args) {
        s = MessageFormat.format(s, args);
      }
      m_messages.println(s);
      m_messages.flush();
    } catch (MissingResourceException | NullPointerException
        | ClassCastException | IllegalArgumentException e) {
      if (LOG.isLoggable(Level.CONFIG)) {
        LOG.log(Level.CONFIG, m_rb, e);
      }
      return;
    }

  }

  @Override
  public void write(String msg_key) {
    write(msg_key, (Object[]) null);
  }

  @Override
  public String get(String msg_key, Object... args) {
    try {
      ResourceBundle rb = ResourceBundle.getBundle(m_rb, Locale.getDefault());
      String s = rb.getString(msg_key);
      if (null != args) {
        s = MessageFormat.format(s, args);
      }
      return s;
    } catch (MissingResourceException | NullPointerException
        | ClassCastException | IllegalArgumentException e) {
      if (LOG.isLoggable(Level.CONFIG)) {
        LOG.log(Level.CONFIG, m_rb, e);
      }
      return "error";
    }
  }

  @Override
  public String get(String msg_key) {
    return get(msg_key, (Object[]) null);
  }
}
