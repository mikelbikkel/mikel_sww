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
/**
 * 
 */
package org.mikel.dbwrapper;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Static helper functions.
 * <p>
 * This class assumes that its callers provide non-null parameters.
 */
class Util {

  /**
   * Returns the table name used for INSERT, UPDATE and DELETE statements.
   * 
   * @param obj
   *          the object that is to be written to the database.
   * @return the table name used for INSERT, UPDATE and DELETE statements.
   * @throws DBException
   *           if the class does not have a {@code SourceTable} annotation.
   */
  static String tabWriteName(Object obj) throws DBException {
    assert (null != obj);
    SourceTable tab = obj.getClass().getAnnotation(SourceTable.class);
    if (null == tab) {
      throw new DBException("missing annotation: SourceTable");
    }
    String tabName = tab.writeTable();
    if ("".equals(tabName)) {
      tabName = tab.value();
    }
    return tabName;
  }

  /**
   * Returns the table name used for SELECT statements.
   * 
   * @param obj
   *          object that is to be initialized from the database.
   * @return the table name used for INSERT, UPDATE and DELETE statements.
   * @throws DBException
   *           if the class does not have a {@code SourceTable} annotation.
   */
  static String tabReadName(Object obj) throws DBException {
    assert (null != obj);
    SourceTable tab = obj.getClass().getAnnotation(SourceTable.class);
    if (null == tab) {
      throw new DBException("missing annotation: SourceTable");
    }
    return tab.value();
  }

  /**
   * Returns the column name used for INSERT, UPDATE and DELETE statements.
   * 
   * @param sc
   *          the {@code SourceColumn} annotation.
   * @return the column name used for INSERT, UPDATE and DELETE statements.
   */
  static String colWriteName(SourceColumn sc) {
    String colName = sc.writeColumn();
    if ("".equals(colName)) {
      colName = sc.value();
    }
    return colName;
  }

  /**
   * Returns the column name used for SELECT statements.
   * 
   * @param sc
   *          the {@code SourceColumn} annotation.
   * @return the column name used for SELECT statements, or {@code null} if
   *         {@code sc} is {@code null}.
   */
  static String colReadName(SourceColumn sc) {
    if (null == sc) {
      return null;
    }
    return sc.value();
  }

  /**
   * Checks for configuration errors in the DBWrapper annotations.
   * 
   * @param fld
   *          the field to check
   * @throws DBException
   *           if a configuration error is found.
   */
  static void checkAnnotations(Field fld) throws DBException {
    PrimaryKey pk = fld.getAnnotation(PrimaryKey.class);
    Parent pr = fld.getAnnotation(Parent.class);
    SourceColumn sc = fld.getAnnotation(SourceColumn.class);

    String name = fld.getDeclaringClass().getName() + "." + fld.getName();
    if (null != pr && null != sc) {
      throw new DBException(name + ": Parent and SourceColumn annotation cannot be combined.");
    }
    if (null != pr && null != pk && !pk.supplied()) {
      throw new DBException(name + ": Parent field cannot be a generated PK field.");
    }
    if (null != pk && !pk.supplied() && null != sc && !sc.writeInclude()) {
      throw new DBException(name + ": Supplied PK field cannot be excluded from writing.");
    }
    if (null != pr && pr.childColumns().length != pr.parentColumns().length) {
      throw new DBException(name + ": Parent must have same number of child and parent columns.");
    }
  }

  static int processParent(Object obj, Field fld, int colCounter, PreparedStatement stmt)
      throws DBException, SQLException {
    Field fldParent = null;
    Parent pr = fld.getAnnotation(Parent.class);
    if (null == pr) {
      throw new DBException("Missing Parent annotation.");
    }
    String[] ccols = pr.childColumns();
    String[] pcols = pr.parentColumns();

    try {
      fld.setAccessible(true); // illegal access exc. private field...
      Object objParent = fld.get(obj);
      for (int i = 0; i < ccols.length; i++) {
        fldParent = findParentField(objParent, pcols[i]);
        colCounter++;
        TypeMapping.doSetParam(objParent, fldParent, colCounter, stmt);
      }
    } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
      throw new DBException("Reflection error", e);
    }
    return colCounter;
  }

  private static Field findParentField(Object obj, String colNameParent) throws DBException {
    Field[] fields = obj.getClass().getDeclaredFields();
    SourceColumn sc;
    String colName;
    for (int idx = 0; idx < fields.length; idx++) {
      sc = fields[idx].getAnnotation(SourceColumn.class);
      if (null != sc) {
        colName = Util.colWriteName(sc);
        if (colName.equals(colNameParent)) {
          return fields[idx];
        }
      }
    }
    throw new DBException("Cannot find parent column: " + colNameParent);
  }

  static List<String> getColumnNames(List<Field> fields) {
    List<String> cols = new ArrayList<>();
    Parent pr;
    SourceColumn sc;
    for (Field fld : fields) {
      pr = fld.getAnnotation(Parent.class);
      if (null != pr) {
        String[] ccols = pr.childColumns();
        for (int i = 0; i < ccols.length; i++) {
          cols.add(ccols[i]);
        }
        continue; // Processed the @Parent annotation.
      }

      sc = fld.getAnnotation(SourceColumn.class);
      if (null != sc) {
        String colName = Util.colWriteName(sc);
        cols.add(colName);
      }
      // Processed the @SourceColumn annotation.
    }
    return cols;
  }
}
