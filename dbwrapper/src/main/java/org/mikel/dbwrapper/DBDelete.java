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

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Deletes the database row that corresponds to an object.
 *
 */
class DBDelete implements IDBQueryOwner {

  private List<Field> m_fields;

  private final Object m_obj;

  private final String m_sql;

  @SuppressWarnings("unused") // Make the default ctor unaccessible.
  private DBDelete() {
    m_sql = null;
    m_obj = null;
    m_fields = null;
  }

  DBDelete(Object obj) throws DBException {
    m_obj = obj;
    getFields();

    m_sql = generateSQL();
  }

  private void getFields() throws DBException {
    m_fields = new ArrayList<>();
    Field[] fields = null;
    try {
      fields = m_obj.getClass().getDeclaredFields();
    } catch (SecurityException e) {
      throw new DBException("Reflection error", e);
    }
    SourceColumn sc;
    PrimaryKey pk;
    for (int idx = 0; idx < fields.length; idx++) {
      Util.checkAnnotations(fields[idx]);
      sc = fields[idx].getAnnotation(SourceColumn.class);
      if (null == sc) {
        continue;
      }
      pk = fields[idx].getAnnotation(PrimaryKey.class);
      if (null == pk) {
        continue;
      }
      m_fields.add(fields[idx]);
    }
  }

  private String generateSQL() throws DBException {
    String tabName = Util.tabWriteName(m_obj);
    String sqlDelete = "delete from " + tabName + " where ";
    boolean isFirst = true;
    SourceColumn sc;
    for(Field fld : m_fields) {
      sc = fld.getAnnotation(SourceColumn.class);
      String colName = Util.colWriteName(sc);
      if (isFirst) {
        isFirst = false;
        sqlDelete = sqlDelete + colName + " = ?";
      } else {
        sqlDelete = sqlDelete + " and " + colName + " = ?";
      }
    }
    return sqlDelete;
  }

  String getSQL() {
    return m_sql;
  }

  @Override
  public void setParams(PreparedStatement stmt) throws DBException, SQLException {
    // Set parameters for the WHERE-clause.
    int pkCounter = 0;
    for(Field fld : m_fields) {
      pkCounter++;
      TypeMapping.doSetParam(m_obj, fld, pkCounter, stmt);
    }
  }
}
