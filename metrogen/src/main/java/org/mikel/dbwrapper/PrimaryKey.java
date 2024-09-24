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
 * This annotation provides the information for the primary key columns.
 * <p>
 * Info is used for insert, update and delete statements.
 * <p>
 * An attribute with a {@code PrimaryKey} annotation must also have a
 * {@code SourceCode} annotation.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {
  /**
   * Returns true if the primary key value is supplied.
   * <p>
   * Optional attribute, defaults to false.<br>
   * When {@code false}, the database generates the primary key value when the
   * record is inserted. If set to {@code true}, the Java object supplies the
   * value for the primary key.
   * 
   * @return true if the primary key value is supplied, false otherwise.
   */
  boolean supplied() default false;

}
