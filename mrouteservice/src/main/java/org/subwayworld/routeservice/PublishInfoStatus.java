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
 */
package org.subwayworld.routeservice;

/**
 * Represents the publication status of an object.
 * 
 */
public enum PublishInfoStatus {

  /**
   * Status when an object is created.
   */
  INFO_NEW {
    @Override
    public String toString() {
      return "new";
    }
  },
  /**
   * Status when an object is updated.
   */
  INFO_UPDATE {
    @Override
    public String toString() {
      return "update";
    }
  },
  /**
   * Status when an object is not changed.
   */
  INFO_SAME {
    @Override
    public String toString() {
      return "same";
    }
  };
  
  public String getName() {
	    return this.toString();
	}
  
}
