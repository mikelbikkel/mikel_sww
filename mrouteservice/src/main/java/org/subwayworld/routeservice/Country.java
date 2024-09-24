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
 * Represents a country that has cities with a metro network.
 * 
 */
public class Country implements IPublishInfo {

  private final String m_code;

  private final String m_name;

  // Make sure code and name are supplied.
  public Country() {
    this("error", "error");
  }

  public Country(String code, String name) {
    if (null == code || null == name) {
      throw new IllegalArgumentException();
    }
    m_code = code;
    m_name = name;
  }

  public String getCode() {
    return m_code;
  }

  public String getName() {
    return m_name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((m_code == null) ? 0 : m_code.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Country)) {
      return false;
    }
    Country other = (Country) obj;
    if (m_code == null) {
      if (other.m_code != null) {
        return false;
      }
    } else if (!m_code.equals(other.m_code)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "<" + m_code + "> " + m_name + "<" + m_publish + ">";
  }

  /**
   * The publish information for this continent.
   * <p>
   * Composition strategy.<br>
   * This objects encapsulates the common functionality of all things that
   * implement the IPublishInfo interface.<br>
   * This allows for re-use of the common functionality, without using a base
   * class.
   */
  private PublishInfo m_publish = new PublishInfo();

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IPublishInfo#getStatus()
   */
  @Override
  public PublishInfoStatus getStatus() {
    return m_publish.getStatus();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IPublishInfo#getMessages()
   */
  @Override
  public List<PublishMessage> getMessages() {
    return m_publish.getMessages();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IPublishInfo#addMessage(org.subwayworld.
   * routeservice.PublishMessage)
   */
  @Override
  public void addMessage(PublishMessage msg) {
    m_publish.addMessage(msg);
  }
}
