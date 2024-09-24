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

package org.subwayworld.routeservice;

/**
 * Wrapper for the result code of getRoute.
 * 
 */
public class RouteResult {

  public enum RCODE {
    NULL, OK, CITY, FROM_UNKNOWN, TO_UNKNOWN, FROM_NULL, TO_NULL, FROM_IS_TO,
    NO_ROUTE, SQL_ERROR, ALIAS_UNKNOWN, CALC_ERROR
  };

  /**
   * ResultCode for getRoute request.
   */
  private RCODE m_code;

  public RouteResult() {
    reset();
  }

  public void reset() {
    m_code = RCODE.NULL;
  }

  public void setCode(RCODE code) {
    m_code = code;
  }

  public RCODE getCode() {
    return m_code;

  }

  @Override
  public String toString() {
    return "[" + m_code + "]";
  }
}
