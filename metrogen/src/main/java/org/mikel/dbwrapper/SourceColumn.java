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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation provides the information for the mapping between between Java
 * object attributes and SQL database columns.
 * <p>
 * The class {@code DBFiatLux} implements the mapping.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SourceColumn {
  /**
   * Returns the default name of the column.
   * <p>
   * This name is always used for SELECT statements. For INSERT, DELETE and
   * UPDATE the column name can be overwritten with the {@code writeColumn}
   * attribute.
   * <p>
   * Rationale for override possibility: the SELECT can use an alias and hide
   * the real database column name.
   * 
   * @return the default name of the column.
   */
  String value();

  /**
   * Returns the name of the column for INSERT, UPDATE and DELETE statements.
   * <p>
   * Optional attribute. If specified, it overrules the {@code value} attribute.
   * If not specified, the {@code value} attribute is used for INSERT, UPDATE
   * and DELETE statements.
   * 
   * TODO: rename to writeName?
   * 
   * @return the name of the column for for INSERT, UPDATE and DELETE
   *         statements.
   */
  String writeColumn() default "";

  /**
   * Returns true if this column is part of database writes (insert, update).
   * 
   * @return true if this column is included with database writes (insert,
   *         update). False if this column must not be written to the database.
   */
  boolean writeInclude() default true;

  /**
   * Returns the value that is interpreted as a NULL value.
   * <p>
   * Optional attribute.<br>
   * Use for fields of a primitive Java type, for example {@code int}.
   * <p>
   * TODO: add to DOUBLE
   * 
   * @return the value that is interpreted as a NULL value.
   */
  String nullValue() default "";

  /**
   * Returns the SQL type of the column.
   * <p>
   * Optional attribute.<br>
   * If not specified the, default SQL type is used.<br>
   * Legal values are defined by {@code java.sql.Types}.
   * <p>
   * 
   * @return the SQL type of the column.
   */
  int sqlType() default java.sql.Types.NULL;
}
