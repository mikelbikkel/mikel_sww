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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * In essence, this class is a data type converter.
 * <p>
 * It converts from JDBC SQL data types to Java types, when reading data. And
 * from Java types to JDB SQL types, when writing data.<br>
 * The JDBC driver maps the JDBC SQL type to or from a database type. A database
 * type is a data type defined by a specific database. For example, the
 * PostgreSQL data type {@code DECIMAL(9,2)}.
 * <p>
 * The read scenario is covered by {@code setFieldValue}, which translates a
 * RowSet record column into a Java object instance field.
 * <p>
 * The write scenario is covered by {@code doSetParam}, which translates object
 * values into parameters for a {@code PreparedStatement}.
 * <p>
 * Both translations are guided by the {@code SourceColumn} and {@code Parent}
 * annotations.
 * <p>
 * <h3>Reading SQL {@code NULL} values.</h3><br>
 * When a database columns contains {@code NULL}, the value for the Java field
 * is <b>not</b> set. The Java field contains whatever value was set by the
 * default constructor of the Java object.
 * <p>
 * To translate a database NULL value into a specific value for a primitive
 * type, use the annotation attribute {@code nullValue}.<br>
 * For example: {@code nullValue="-1"}. This translates a NULL value in the
 * database into a -1 for the Java object field.
 * <p>
 * <h3>Writing Java {@code NULL} values.</h3><br>
 * For Java fields with primitive types, the value in the field is written into
 * the database. Java primitive types cannot be NULL. So, when the field is
 * written to the database, the field value is stored in the database.<br>
 * Java fields with Object types can be NULL. If the field is null, NULL is
 * stored in the database. If the field is not null, the field value is written
 * to the database.
 * <p>
 * To store a NULL value in the database for a primitive type, use the
 * annotation attribute {@code nullValue}. For example: {@code nullValue="0"}.
 * If the Java object field of type {@code int} has this attribute, and has the
 * value {@code 0}, the SQL NULL value is stored in the database.
 * <p>
 * <h3>The type mapping between Java types and SQL types</h3><br>
 * The default type mapping:
 * <table summary="default type mapping">
 * <tr>
 * <th>Java Type</th>
 * <th>SQL type</th>
 * <th>Reading (RowSet)</th>
 * <th>Writing (PreparedStatement)</th>
 * <th>Remark</th>
 * </tr>
 * <tr>
 * <td>int</td>
 * <td>INTEGER</td>
 * <td>getInt</td>
 * <td>setInt</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Integer</td>
 * <td>INTEGER</td>
 * <td>getInt</td>
 * <td>setInt</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>double</td>
 * <td>DOUBLE</td>
 * <td>getDouble</td>
 * <td>setDouble</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Double</td>
 * <td>DOUBLE</td>
 * <td>getDouble</td>
 * <td>setDouble</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>String</td>
 * <td>VARCHAR</td>
 * <td>getString</td>
 * <td>setString</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Date</td>
 * <td>DATE</td>
 * <td>getDate</td>
 * <td>setDate</td>
 * <td>Date information is preserved, Time information is lost.</td>
 * </tr>
 * </table>
 * <p>
 * Additional type mappings for {@code java.util.Date}:
 * <table summary="additional type mapping for java.util.Date">
 * <tr>
 * <th>Java Type</th>
 * <th>SQL type</th>
 * <th>Reading (RowSet)</th>
 * <th>Writing (PreparedStatement)</th>
 * <th>Remark</th>
 * </tr>
 * <tr>
 * <td>Date</td>
 * <td>TIMESTAMP</td>
 * <td>getTimestamp</td>
 * <td>setTimestamp</td>
 * <td>Both Date and Time information are preserved.</td>
 * </tr>
 * <tr>
 * <td>Date</td>
 * <td>TIME</td>
 * <td>getTime</td>
 * <td>setTime</td>
 * <td>Time information is preserved, Date information is lost.</td>
 * </tr>
 * </table>
 *
 */
class TypeMapping {

  /**
   * Sets the parameters for a prepared statement.
   * 
   * @param obj
   *          Object to get the parameter value from.
   * @param fld
   *          Field that contains the value.
   * @param idx
   *          index of the parameter.
   * @param stmt
   *          the prepared statement
   * @throws DBException
   *           if reflection goes wrong
   * @throws SQLException
   *           in case of JDBC error.
   */
  static void doSetParam(Object obj, Field fld, int idx, PreparedStatement stmt)
      throws DBException, SQLException {
    if (null == obj) {
      throw new DBException("Null parameter: obj");
    }
    if (null == fld) {
      throw new DBException("Null parameter: fld");
    }
    if (null == stmt) {
      throw new DBException("Null parameter: stmt");
    }
    if (idx < 1) {
      throw new DBException("Parameter idx < 1: " + idx);
    }

    try {
      SourceColumn anno = fld.getAnnotation(SourceColumn.class);
      if (null == anno) {
        throw new DBException("No SourceColumn annotation for: " + fld.getName());
      }

      try {
        fld.setAccessible(true);
      } catch (SecurityException e) {
        throw new DBException(e);
      }

      Class<?> tp = fld.getType();
      if (tp.isEnum()) {
        // For enumerations, store the name as a string value in the
        // database.
        @SuppressWarnings("rawtypes")
        Enum ef = (Enum) fld.get(obj);
        String val = ef.name();
        stmt.setString(idx, val);
      } else if (tp.equals(String.class)) {
        String s = (String) fld.get(obj);
        if (null == s) {
          stmt.setNull(idx, java.sql.Types.VARCHAR);
        } else {
          stmt.setString(idx, s);
        }
      } else if (tp.equals(int.class) || tp.equals(Integer.class)) {
        if (tp.equals(Integer.class)) {
          Object v = fld.get(obj);
          if (null == v) {
            stmt.setNull(idx, java.sql.Types.INTEGER);
            return;
          }
        }
        int i = fld.getInt(obj);
        String snull = anno.nullValue();
        if ("".equals(snull)) {
          stmt.setInt(idx, i);
        } else {
          // a NumberFormatException is handled by the
          // IllegalArgumentException
          // in the catch-block.
          int inull = Integer.parseInt(snull);
          if (i == inull) {
            stmt.setNull(idx, java.sql.Types.INTEGER);
          } else {
            stmt.setInt(idx, i);
          }
        }
      } else if (tp.equals(double.class) || tp.equals(Double.class)) {
        if (tp.equals(Double.class)) {
          Object v = fld.get(obj);
          if (null == v) {
            stmt.setNull(idx, java.sql.Types.DOUBLE);
            return;
          }
        }
        double d = fld.getDouble(obj);
        String snull = anno.nullValue();
        if ("".equals(snull)) {
          stmt.setDouble(idx, d);
        } else {
          // a NumberFormatException is handled by the
          // IllegalArgumentException
          // in the catch-block.
          double nval = Double.parseDouble(snull);
          if (0 == Double.compare(nval, d)) {
            stmt.setNull(idx, java.sql.Types.DOUBLE);
          } else {
            stmt.setDouble(idx, d);
          }
        }
      } else if (tp.equals(Date.class)) {
        int sqlType = anno.sqlType();
        Date dt = (Date) fld.get(obj);
        if (null == dt) {
          if (java.sql.Types.TIMESTAMP == sqlType) {
            stmt.setNull(idx, java.sql.Types.TIMESTAMP);
          } else if (java.sql.Types.TIME == sqlType) {
            stmt.setNull(idx, java.sql.Types.TIME);
          } else { // Use the default mapping: java.sql.DATE.
            stmt.setNull(idx, java.sql.Types.DATE);
          }
        } else {
          if (java.sql.Types.TIMESTAMP == sqlType) {
            java.sql.Timestamp ts = new java.sql.Timestamp(dt.getTime());
            stmt.setTimestamp(idx, ts);
          } else if (java.sql.Types.TIME == sqlType) {
            java.sql.Time tm = new java.sql.Time(dt.getTime());
            stmt.setTime(idx, tm);
          } else { // Use the default mapping: java.sql.DATE.
            java.sql.Date sdate = new java.sql.Date(dt.getTime());
            stmt.setDate(idx, sdate);
          }
        }
      } // TODO: add other JDBC datatypes
    } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
      throw new DBException("Cannot access field: " + fld.getName(), e);
    }
  }

  /**
   * Sets the field of an object from the column of a resultset.
   * 
   * @param obj
   *          the object.
   * @param field
   *          the field to set.
   * @param sc
   *          the SourceColumn annotation for this field.
   * @param colName
   *          the column name. The caller decides to user the read- or the
   *          write-column name.
   * @param rs
   *          the resultset.
   * @throws SQLException
   *           when a database error occurs.
   * @throws DBException
   *           when a dbwrapper error occurs.
   */
  static void setFieldValue(Object obj, Field field, SourceColumn sc, String colName, ResultSet rs)
      throws SQLException, DBException {
    if (null == sc) {
      return; // Ignore fields without SourceColumn annotation.
    }
    int sqlType = sc.sqlType();

    try {
      // field.set does not work if field is private or otherwise not
      // accessible. So force the field to be accessible.
      field.setAccessible(true);
      Class<?> tp = field.getType();
      if (tp.isEnum()) {
        String s = rs.getString(colName);
        if (!rs.wasNull()) {
          Method valueOf = field.getType().getMethod("valueOf", String.class);
          Object value = valueOf.invoke(null, s);
          field.set(obj, value);
        }
      } else if (tp.equals(String.class)) {
        String s = rs.getString(colName);
        if (!rs.wasNull()) {
          field.set(obj, s);
        }
      } else if (tp.equals(int.class) || tp.equals(Integer.class)) {
        int i = rs.getInt(colName);
        if (rs.wasNull()) {
          if (tp.equals(Integer.class)) {
            field.set(obj, null);
          } else {
            String snull = sc.nullValue();
            if (!"".equals(snull)) {
              // a NumberFormatException is handled by the
              // IllegalArgumentException
              // in the catch-block.
              int inull = Integer.parseInt(snull);
              field.set(obj, inull);
            }
          }
        } else { // rs is NOT NULL
          field.set(obj, i);
        }
      } else if (tp.equals(double.class) || tp.equals(Double.class)) {
        double d = rs.getDouble(colName);
        if (rs.wasNull()) {
          if (tp.equals(Double.class)) {
            field.set(obj, null);
          } else {
            String snull = sc.nullValue();
            if (!"".equals(snull)) {
              // a NumberFormatException is handled by the
              // IllegalArgumentException
              // in the catch-block.
              double nval = Double.parseDouble(snull);
              field.set(obj, nval);
            }
          }
        } else { // rs is NOT NULL
          field.set(obj, d);
        }
      } else if (tp.equals(Date.class)) {
        if (java.sql.Types.TIMESTAMP == sqlType) {
          java.sql.Timestamp ts = rs.getTimestamp(colName);
          if (!rs.wasNull()) {
            field.set(obj, ts);
          }
        } else if (java.sql.Types.TIME == sqlType) {
          java.sql.Time tm = rs.getTime(colName);
          if (!rs.wasNull()) {
            field.set(obj, tm);
          }
        } else { // Use the default mapping: java.sql.DATE
          java.sql.Date dt = rs.getDate(colName);
          if (!rs.wasNull()) {
            field.set(obj, dt);
          }
        }
      } // TODO: add other JDBC datatypes
    } catch (IllegalArgumentException | IllegalAccessException | SecurityException
        | NoSuchMethodException | InvocationTargetException e) {
      throw new DBException("Cannot access field: " + field.getName(), e);
    }
  }
}
