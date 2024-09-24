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

import java.util.List;

/**
 * Represents publishing information for an object from the subwayworld.
 * <p>
 * The information indicates if an object is new or updated. And it contains the
 * collection of information messages that have been published.
 */
public interface IPublishInfo {

  /**
   * Returns the publication status of the object.
   * 
   * @return the publication status of the object.
   */
  PublishInfoStatus getStatus();

  /**
   * Returns the information messages for an object.
   * 
   * @return
   */
  public List<PublishMessage> getMessages();

  /**
   * Adds an information message.
   * 
   * @param msg
   *          the information message
   */
  public void addMessage(PublishMessage msg);

}
