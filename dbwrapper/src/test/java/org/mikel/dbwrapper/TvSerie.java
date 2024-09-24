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
package org.mikel.dbwrapper;

@SourceTable("tv_serie")
public class TvSerie {

  @PrimaryKey(supplied = true)
  @SourceColumn("name")
  private String m_name;

  @SourceColumn("description")
  private String m_description;

  @SourceColumn("producer")
  private String m_producer;

  public String getName() {
    return m_name;
  }

  public void setName(String name) {
    m_name = name;
  }

  public String getProducer() {
    return m_producer;
  }

  public void setProducer(String pr) {
    m_producer = pr;
  }

  public String getDescription() {
    return m_description;
  }

  public void setDescription(String description) {
    m_description = description;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((m_name == null) ? 0 : m_name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TvSerie other = (TvSerie) obj;
    if (m_name == null) {
      if (other.m_name != null)
        return false;
    } else if (!m_name.equals(other.m_name))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "TvSerie [" + m_name + ". " + m_description + ". By: " + m_producer + "]";
  }

}
