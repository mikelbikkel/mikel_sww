/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.subwayworld.routeservice;

/**
 * 9 jan 2010
 * Base class.
 * Suclasses for error groups: Connection errors, data errors, input errors, ....
 *
 */
public class RSException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -6733426361876792362L;

  /**
   * 
   */
  public RSException() {
    super();
  }

  /**
   * @param message
   */
  public RSException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public RSException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public RSException(String message, Throwable cause) {
    super(message, cause);
  }

}
