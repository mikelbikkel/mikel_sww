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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Inserts a database row that corresponds to an object.
 */
class DBInsert implements IDBQueryOwner {

  private List<Field> m_fields;

  private List<Field> m_pkFields;

  private final Object m_obj;

  private final String m_sql;

  private String[] m_pkNames;

  @SuppressWarnings("unused") // Make the default ctor inaccessible.
  private DBInsert() {
    m_obj = null;
    m_sql = null;
  }

  DBInsert(Object obj) throws DBException {
    m_obj = obj;
    getFieldsAndPKNames();
    m_sql = generateSQL();
  }

  private void getFieldsAndPKNames() throws DBException {
    m_fields = new ArrayList<>();
    m_pkFields = new ArrayList<>();
    List<String> pkNames = new ArrayList<>();
    Field[] fields = null;
    try {
      fields = m_obj.getClass().getDeclaredFields();
    } catch (SecurityException e) {
      throw new DBException("Reflection error", e);
    }
    for (int idx = 0; idx < fields.length; idx++) {
      Util.checkAnnotations(fields[idx]);

      Parent pr = fields[idx].getAnnotation(Parent.class);
      if (null != pr) {
        m_fields.add(fields[idx]); // Add @Parent field.
        continue;
      }

      SourceColumn sc = fields[idx].getAnnotation(SourceColumn.class);
      if (null == sc) {
        continue; // Ignore fields without @SourceColumn
      }
      PrimaryKey pk = fields[idx].getAnnotation(PrimaryKey.class);
      if (null != pk) {
        if (pk.supplied()) {
          m_fields.add(fields[idx]);
        } else {
          // A generated PK field is not added to the sql statement.
          String colName = Util.colWriteName(sc);
          pkNames.add(colName);
          m_pkFields.add(fields[idx]);
        }
        continue;
      }
      // Add non-PK field if it is not excluded from writing.
      if (sc.writeInclude()) {
        m_fields.add(fields[idx]);
      }
    } // for

    if (pkNames.isEmpty()) {
      m_pkNames = null;
    } else {
      m_pkNames = pkNames.toArray(new String[0]);
    }
  }

  private String generateSQL() throws DBException {
    List<String> colNames = Util.getColumnNames(m_fields);
    String tabName = Util.tabWriteName(m_obj);

    String sqlInsert = "insert into " + tabName + " (";
    String sqlValues = " values(";

    boolean isFirst = true;
    for (String col : colNames) {
      if (isFirst) {
        sqlInsert = sqlInsert + col;
        sqlValues = sqlValues + "?";
        isFirst = false;
      } else {
        sqlInsert = sqlInsert + ", " + col;
        sqlValues = sqlValues + ", ?";
      }
    }
    sqlInsert = sqlInsert + ")";
    sqlValues = sqlValues + ")";
    return sqlInsert + sqlValues;
  }

  String getSQL() {
    return m_sql;
  }

  String[] getGeneratedPKNames() {
    return m_pkNames;
  }

  @Override
  public void setParams(PreparedStatement stmt) throws DBException, SQLException {
    // Set parameters for the VALUES-clause.
    int colCounter = 0;
    SourceColumn sc = null;
    Parent pr = null;
    for (Field fld : m_fields) {
      sc = fld.getAnnotation(SourceColumn.class);
      pr = fld.getAnnotation(Parent.class);
      if (null != sc) {
        colCounter++;
        TypeMapping.doSetParam(m_obj, fld, colCounter, stmt);
      } else if (null != pr) {
        colCounter = Util.processParent(m_obj, fld, colCounter, stmt);
      }
    }
  }

  /**
   * Sets the Primary Key values for the object. <br>
   * The values are generated by the database.
   * 
   * @param rs
   *          the {@code ResultSet} that contains the generated keys.
   */
  void setPrimaryKeyValue(ResultSet rs) throws SQLException, DBException {
    // Move to the first record.
    boolean hasRecord = rs.next();
    if (!hasRecord) {
      return;
    }

    // Assign generated PK-values to the object fields.
    Field fld;
    SourceColumn sc;
    for (int i = 0; i < m_pkNames.length; i++) {
      fld = m_pkFields.get(i);
      sc = fld.getAnnotation(SourceColumn.class);
      TypeMapping.setFieldValue(m_obj, fld, sc, m_pkNames[i], rs);
    }
  }
}
