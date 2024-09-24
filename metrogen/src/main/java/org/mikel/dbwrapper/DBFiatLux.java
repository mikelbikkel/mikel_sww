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
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Functionality for SELECT statements.
 * <p>
 * Creates or refreshes a Java object from a {@code RecordSet} row.
 *
 */
class DBFiatLux {

  /**
   * This set contains all the singleton parent objects. <br/>
   * The objects must implement the correct {@code equals()} and
   * {@code hashCode()} methods.
   */
  private Set<Object> m_parents;

  DBFiatLux() {
    m_parents = new HashSet<>();
  }

  /**
   * Creates an object instance with data from a row in a ResultSet.
   * 
   * A field is initialized from the database if:
   * <ul>
   * <li>the field has a {@code SourceCode} or {@code Parent} annotation.</li>
   * <li>if the {@code ResultSet} contains the read-column-name of the
   * field.</li>
   * </ul>
   * <p>
   * If either the annotation or the write column is missing, the field is not
   * refreshed.
   * 
   * @param cls
   *          the class of the object to create.
   * @param rs
   *          the resultset. The object is created from the current row.
   * @return the instantiated object.
   * @throws SQLException
   *           in case of JDBC error.
   * @throws DBException
   *           in case of other error.
   */
  <T> T resurrectObject(Class<T> cls, ResultSet rs) throws SQLException, DBException {
    T obj = null;
    try {
      obj = cls.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      throw new DBException("Cannot create object.", e);
    }
    setObjectFields(obj, rs);
    return obj;
  }

  /**
   * Refreshes an object instance with data from a row in a ResultSet.
   * <p>
   * A field is refreshed if:
   * <ul>
   * <li>the field has a {@code SourceCode} or {@code Parent} annotation.</li>
   * <li>if the {@code ResultSet} contains the read-column-name of the
   * field.</li>
   * </ul>
   * <p>
   * If either the annotation or the write column is missing, the field is not
   * refreshed.
   * 
   * @param obj
   *          the object that is to be refreshed.
   * @param rs
   *          the resultset. The object is created from the current row.
   * @throws SQLException
   *           in case of JDBC error
   * @throws DBException
   *           in case of other error.
   */
  void refreshObject(Object obj, ResultSet rs) throws SQLException, DBException {
    setObjectFields(obj, rs);
  }

  private boolean hasColumn(ResultSet rs, String colName) {
    try {
      @SuppressWarnings("unused")
      String test = rs.getString(colName);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  private void setObjectFields(Object obj, ResultSet rs) throws SQLException, DBException {
    Field[] fields = null;
    try {
      fields = obj.getClass().getDeclaredFields();
    } catch (SecurityException e) {
      throw new DBException("Reflection error", e);
    }

    String colName;
    SourceColumn sc;
    Parent pr;
    Object objParent;
    for (Field fld : fields) {
      sc = fld.getAnnotation(SourceColumn.class);
      pr = fld.getAnnotation(Parent.class);
      if (null != sc) {
        colName = Util.colReadName(sc);
        if (hasColumn(rs, colName)) {
          TypeMapping.setFieldValue(obj, fld, sc, colName, rs);
        }
      } else if (null != pr) { // Handle parent annotation.
        objParent = createParentObject(fld);
        // Use a recursive call to setObjectFields to fill the parent fields.
        setObjectFields(objParent, rs);
        objParent = transformToSingleton(objParent);
        assignParentToField(fld, obj, objParent);
      }
    }
  }

  private Object createParentObject(Field fld) throws DBException, SQLException {
    Class<?> cls = fld.getType();
    try {
      Object obj = cls.getDeclaredConstructor().newInstance();
      return obj;
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      throw new DBException(e);
    }
  }

  private void assignParentToField(Field fld, Object owner, Object value) throws DBException {
    try {
      fld.setAccessible(true);
      fld.set(owner, value);
    } catch (SecurityException | IllegalAccessException e) {
      throw new DBException("Cannot create parent object.", e);
    }
  }

  private Object transformToSingleton(Object newEntry) throws DBException {
    if (null == newEntry) {
      throw new DBException("Parent object is null.");
    }
    try {
      boolean res = m_parents.add(newEntry);
      if (res) {
        return newEntry;
      }
    } catch (UnsupportedOperationException | ClassCastException e) {
      throw new DBException("Cannot add newEntry to set: " + newEntry.toString(), e);
    }
    // obj already in the set. Find the original entry.
    for (Object existingEntry : m_parents) {
      if (existingEntry.equals(newEntry)) {
        return existingEntry;
      }
    }
    throw new DBException("Cannot find new entry: " + newEntry.toString());
  }
}
