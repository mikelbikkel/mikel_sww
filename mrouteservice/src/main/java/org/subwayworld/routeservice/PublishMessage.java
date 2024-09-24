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

import java.util.Date;

/**
 * Represents one information message for an object.
 * 
 */
public class PublishMessage {

  /**
   * The publication date of the message.
   */
  private Date m_date;

  /**
   * The text of the message.
   */
  private String m_message;

  /**
   * The type of the message.
   */
  private PublishInfoStatus m_mtype;

  /**
   * Code of the object that this message belongs to.
   */
  private String m_code;

  /**
   * Name of the object that this message belongs to.
   */
  private String m_name;

  public PublishMessage(Date d, String message, String mtype, String code,
      String name) {
    m_date = d;
    m_message = message;
    if ("PUBLISH_NEW".equals(mtype)) {
      m_mtype = PublishInfoStatus.INFO_NEW;
    } else if ("PUBLISH_UPDATE".equals(mtype)) {
      m_mtype = PublishInfoStatus.INFO_UPDATE;
    } else {
      assert false;
      m_mtype = PublishInfoStatus.INFO_SAME;
    }
    m_code = code;
    m_name = name;
  }

  /**
   * Returns the date when the message was published. <br>
   * The date is a timestamp with both date and time information.
   * 
   * @return the date when the message was published.
   */
  public Date getDate() {
    return m_date;
  }

  /**
   * Returns the type of the message.
   * 
   * @return the type of the message.
   */
  public PublishInfoStatus getMessageType() {
    return m_mtype;
  }

  /**
   * Returns the type of the message.
   * 
   * @return the type of the message.
   */
  public String getMessageTypeString() {
    return m_mtype.toString();
  }

  /**
   * Returns the text of the message.
   * 
   * @return the text of the message.
   */
  public String getMessage() {
    return m_message;
  }

  /**
   * Returns the name of the object that this message belongs to.
   * 
   * @return the name of the object that this message belongs to.
   */
  public String getOwnerName() {
    return m_name;
  }

  /**
   * Returns the code of the object that this message belongs to.
   * 
   * @return the code of the object that this message belongs to.
   */
  public String getOwnerCode() {
    return m_code;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return m_mtype + " -[" + m_message + "]";
  }
}
