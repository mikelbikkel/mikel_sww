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

@SourceTable(value = "tv_episode_all", writeTable = "tv_episode")
public class TvEpisode {

  @PrimaryKey(supplied = true)
  @SourceColumn(value = "s_name")
  private String m_nameSerie;

  @PrimaryKey(supplied = true)
  @SourceColumn(value = "e_season", writeColumn = "season")
  private int m_season;

  @PrimaryKey(supplied = true)
  @SourceColumn(value = "e_episode", writeColumn = "episode")
  private int m_episode;

  @SourceColumn(value = "e_name", writeColumn = "name")
  private String m_name;

  @SourceColumn(value = "e_description", writeColumn = "description")
  private String m_description;

  public String getNameSerie() {
    return m_nameSerie;
  }

  public void setNameSerie(String nameSerie) {
    m_nameSerie = nameSerie;
  }

  public int getSeason() {
    return m_season;
  }

  public void setSeason(int seqno) {
    m_season = seqno;
  }

  public String getName() {
    return m_name;
  }

  public void setName(String name) {
    m_name = name;
  }

  public String getDescription() {
    return m_description;
  }

  public void setDescription(String description) {
    m_description = description;
  }

  public int getEpisode() {
    return m_episode;
  }

  public void setEpisode(int episode) {
    m_episode = episode;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + m_episode;
    result = prime * result + ((m_nameSerie == null) ? 0 : m_nameSerie.hashCode());
    result = prime * result + m_season;
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
    TvEpisode other = (TvEpisode) obj;
    if (m_episode != other.m_episode)
      return false;
    if (m_nameSerie == null) {
      if (other.m_nameSerie != null)
        return false;
    } else if (!m_nameSerie.equals(other.m_nameSerie))
      return false;
    if (m_season != other.m_season)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "TvEpisode [" + m_nameSerie + " <S" + m_season + " E" + m_episode + ": " + m_description
        + "]";
  }

}
