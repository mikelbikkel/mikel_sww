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

@SourceTable(value = "tv_episode_all_2", writeTable = "tv_episode_2")
public class TvEpisode2 {

  @PrimaryKey
  @SourceColumn(value = "e_id", writeColumn = "id")
  private int m_id;

  @Parent(childColumns = { "s_id" }, parentColumns = { "id" })
  private TvSerie2 m_serie;

  @SourceColumn(value = "e_season", writeColumn = "season")
  private int m_season;

  @SourceColumn(value = "e_episode", writeColumn = "episode")
  private int m_episode;

  @SourceColumn(value = "e_name", writeColumn = "name")
  private String m_name;

  @SourceColumn(value = "e_description", writeColumn = "description")
  private String m_description;

  public int getId() {
    return m_id;
  }

  public void setId(int id) {
    m_id = id;
  }

  public TvSerie2 getSerie() {
    return m_serie;
  }

  public void setSerie(TvSerie2 serie) {
    m_serie = serie;
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
    result = prime * result + m_serie.getId();
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
    TvEpisode2 other = (TvEpisode2) obj;
    if (m_episode != other.m_episode)
      return false;
    if (m_serie == null) {
      if (other.m_serie != null)
        return false;
    } else if (!m_serie.equals(other.m_serie))
      return false;
    if (m_season != other.m_season)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "TvEpisode2 [" + m_id + " - " + m_serie.toString() + ". <S" + m_season + " E" + m_episode
        + ">:<" + m_name + ">:<" + m_description + ">]";
  }
}
