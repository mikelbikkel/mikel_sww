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
/**
 * 
 */
package org.subwayworld.metrogen;

/**
 * 
 */
public class MetroException extends Exception {

  private String m_errorcode;

  /**
   * 
   */
  private static final long serialVersionUID = -7386492536174099517L;

  /**
   * 
   */
  public MetroException() {
  }

  /**
   * @param message
   */
  public MetroException(String message) {
    super(message);
  }

  /**
   * @param message
   */
  public MetroException(String errorcode, String message) {
    super(message);
    m_errorcode = errorcode;
  }

  /**
   * @param message
   */
  public MetroException(String errorcode, String message, Throwable cause) {
    super(cause);
    m_errorcode = errorcode;
  }

  /**
   * @param cause
   */
  public MetroException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public MetroException(String message, Throwable cause) {
    super(message, cause);
  }

  public String getErrorcode() {
    return m_errorcode;
  }

}
