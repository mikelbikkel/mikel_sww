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
 * Factory to create and return metroService instances.
 * <p>
 * Protects clients from need to know the instantiating class and from need to
 * deal with singletons or multitons.
 * <p>
 * 
 * @see IRouteService
 * 
 */
public class RouteServiceFactory {

  /**
   * Returns an instance of the {@code RouteService} interface.
   * 
   * @param env
   *          The environment for the service.
   * @throws NullPointerException
   *           if {@code env} is {@code null}.
   * @return an instance of the {@code RouteService} interface.
   */
  public static IRouteService getService(RSEnv env) {
    if (null == env) {
      throw new NullPointerException();
    }
    IRouteService rs = new RouteService(env);
    return rs;
  }

  /**
   * Returns a mock implementation of the {@link IRouteService} interface.
   * 
   * @return a mock implementation of the RouteService interface.
   */
  public static IRouteService getMockService() {
    return new MockService();
  }
}
