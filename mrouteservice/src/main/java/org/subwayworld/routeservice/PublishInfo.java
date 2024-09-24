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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents publishing information for a subwayworld object.
 * <p>
 * Composition strategy.<br>
 * This class encapsulates the common functionality of all things that implement
 * the IPublishInfo interface.<br>
 * This allows for re-use of the common functionality.<br>
 * The alternative strategy for re-use, inheritance, would enforce all
 * implementations of the IPublishInterface to extend from a common base.
 * 
 * The information indicates if an object is new or updated. And it contains the
 * collection of information messages that have been published.
 */
class PublishInfo implements IPublishInfo {

  private boolean m_isNew;

  private boolean m_isUpdated;

  private List<PublishMessage> m_messages;

  public PublishInfo() {
    m_isNew = false;
    m_isUpdated = false;
    m_messages = null;
  }

  @Override
  public PublishInfoStatus getStatus() {
    if (m_isUpdated) {
      // update if:
      // new + update
      // update only
      return PublishInfoStatus.INFO_UPDATE;
    }
    // new if new only
    if (m_isNew) {
      return PublishInfoStatus.INFO_NEW;
    }
    return PublishInfoStatus.INFO_SAME;
  }

  @Override
  public List<PublishMessage> getMessages() {
    if (null == m_messages) {
      return Collections.emptyList();
    }
    return m_messages;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IPublishInfo#addMessage(org.subwayworld.
   * routeservice.PublishMessage)
   */
  @Override
  public void addMessage(PublishMessage msg) {
    if (null == msg) {
      return;
    }
    switch (msg.getMessageType()) {
    case INFO_NEW:
      m_isNew = true;
      break;
    case INFO_UPDATE:
      m_isUpdated = true;
      break;
	default:
		break;
    }
    if (null == m_messages) {
      m_messages = new ArrayList<PublishMessage>();
    }
    m_messages.add(msg);
    return;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "new=" + m_isNew + ", updated=" + m_isUpdated + "[" + m_messages
        + "]";
  }
}
