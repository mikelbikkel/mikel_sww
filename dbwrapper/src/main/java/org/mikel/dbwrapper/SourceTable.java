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

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SourceTable {
  /**
   * Returns the default name of the table.
   * <p>
   * This name is always used for SELECT statements. For INSERT, DELETE and
   * UPDATE the table name can be overwritten with the {@code writeTable}
   * attribute.
   * <p>
   * Rationale for override possibility: the SELECT can use a view and hide the
   * real database table name.
   * 
   * @return the default name of the table.
   */
  String value();

  /**
   * Returns the name of the table for INSERT, UPDATE and DELETE statements.
   * <p>
   * Optional attribute. If specified, it overrules the {@code value} attribute.
   * If not specified, the {@code value} attribute is used for INSERT, UPDATE
   * and DELETE statements.
   * 
   * @return the name of the table for for INSERT, UPDATE and DELETE statements.
   */
  String writeTable() default "";
}
