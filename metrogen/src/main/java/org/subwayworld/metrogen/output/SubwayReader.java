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
package org.subwayworld.metrogen.output;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.subwayworld.metrogen.MetroException;
import org.mikel.dbwrapper.DBEnv;
import org.mikel.dbwrapper.DBException;
import org.mikel.dbwrapper.IDBQueryOwner;
import org.subwayworld.metrogen.network.MetroCity;

/**
 * A Repository that creates SWW-domain objects from the database.
 *
 */
public class SubwayReader {

  private DBEnv m_env;

  public SubwayReader(DBEnv env) {
    m_env = env;
  }

  /**
   * Gets 1 item, or null.
   * 
   * @param cls
   *          object type of the return value
   * @param qry
   *          SQL select statement
   * @param id
   *          primary key value, to use in the WHERE-clause.
   * @return Object of the specified class, initialized with values from the
   *         database.
   * @throws MetroException
   *           if object cannot be created or initialized.
   */
  private <T> T getOneItem(Class<T> cls, String qry, final String id)
      throws MetroException {
    List<T> lst = new ArrayList<>();
    // Use anonymous class to implement the callback function.
    try {
      m_env.getItems(cls, lst, qry, new IDBQueryOwner() {
        @Override
        public void setParams(PreparedStatement stmt)
            throws DBException, SQLException {
          stmt.setString(1, id);
        }
      });
    } catch (SQLException | DBException e) {
      throw new MetroException(e);
    }
    if (1 == lst.size()) {
      T res = lst.get(0);
      return res;
    }
    return null;
  }

  public MetroCity getCity(String id) throws MetroException {
    String qry = "select city_id, city_code, description from city where city_id = ?";
    return getOneItem(MetroCity.class, qry, id);
  }
}
