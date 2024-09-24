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
 * Updates the database row that corresponds to an object.
 */
class DBUpdate implements IDBQueryOwner {

  private List<Field> m_fields;

  private List<Field> m_keys;

  private final Object m_obj;

  private final String m_sql;

  @SuppressWarnings("unused") // Make the default ctor inaccessible.
  private DBUpdate() {
    m_obj = null;
    m_sql = null;
  }

  DBUpdate(Object obj) throws DBException {
    m_obj = obj;
    getFields();
    m_sql = generateSQL();
  }

  private void getFields() throws DBException {
    m_fields = new ArrayList<>();
    m_keys = new ArrayList<>();
    Field[] fields = null;
    try {
      fields = m_obj.getClass().getDeclaredFields();
    } catch (SecurityException e) {
      throw new DBException("Reflection error", e);
    }
    SourceColumn sc;
    PrimaryKey pk;
    Parent pr;
    for (int idx = 0; idx < fields.length; idx++) {
      Util.checkAnnotations(fields[idx]);
      pr = fields[idx].getAnnotation(Parent.class);
      if (null != pr) {
        m_fields.add(fields[idx]); // Add @Parent field.
        continue;
      }

      sc = fields[idx].getAnnotation(SourceColumn.class);
      if (null == sc) {
        continue;
      }
      pk = fields[idx].getAnnotation(PrimaryKey.class);
      if (null == pk) {
        if (sc.writeInclude()) {
          m_fields.add(fields[idx]);
        }
      } else {
        m_keys.add(fields[idx]);
      }
    }
  }

  private String generateSQL() throws DBException {
    String tabName = Util.tabWriteName(m_obj);
    String sql = "update " + tabName;

    boolean isFirst = true;
    List<String> colFields = Util.getColumnNames(m_fields);
    for (String colName : colFields) {
      if (isFirst) {
        isFirst = false;
        sql = sql + " set " + colName + " = ?";
      } else {
        sql = sql + " , " + colName + " = ?";
      }
    }

    sql = sql + " where ";
    isFirst = true;
    List<String> colKeys = Util.getColumnNames(m_keys);
    for (String colName : colKeys) {
      if (isFirst) {
        isFirst = false;
        sql = sql + colName + " = ?";
      } else {
        sql = sql + " and " + colName + " = ?";
      }
    }
    return sql;
  }

  String getSQL() {
    return m_sql;
  }

  @Override
  public void setParams(PreparedStatement stmt) throws DBException, SQLException {
    // Step 1: Set parameters for the SET-clause.
    int paramCounter = 0;
    SourceColumn sc = null;
    Parent pr = null;
    for (Field fld : m_fields) {
      sc = fld.getAnnotation(SourceColumn.class);
      pr = fld.getAnnotation(Parent.class);
      if (null != sc) {
        paramCounter++;
        TypeMapping.doSetParam(m_obj, fld, paramCounter, stmt);
      } else if (null != pr) {
        paramCounter = Util.processParent(m_obj, fld, paramCounter, stmt);
      }
    }

    // Step 2: Set parameters for the WHERE-clause.
    for (Field fld : m_keys) {
      paramCounter++;
      TypeMapping.doSetParam(m_obj, fld, paramCounter, stmt);
    }
  }
}
