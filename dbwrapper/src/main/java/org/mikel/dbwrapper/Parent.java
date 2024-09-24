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
 * This annotation provides the information to map a parent object to a foreign
 * key column in the database.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Parent {
  /**
   * Returns the write-names of the foreign key columns for INSERT, UPDATE and
   * DELETE statements.
   * <p>
   * These names is always used for INSERT, DELETE and UPDATE statements.
   * <p>
   * There is no name for SELECT statements, because the foreign key column is
   * not a Java member. The Java parent object is the Java equivalent of the
   * foreign key column
   * 
   * @return the write-names of the foreign key columns.
   */
  String[] childColumns();

  /**
   * Returns the write-names of the referenced, parent, columns.
   * 
   * @return the write-names of the referenced, parent, columns.
   */
  String[] parentColumns();

  /**
   * Returns true if this column is part of database writes (insert, update).
   * 
   * @return true if this column is included with database writes (insert,
   *         update). False if this column must not be written to the database.
   */
  boolean writeInclude() default true;

}
