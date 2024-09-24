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
package org.subwayworld.routeservice.calculation;

import org.subwayworld.routeservice.TransferType;

/**
 * Attributes of the WALK-direction.
 * <p>
 * A direction that does not exist in the database, only in the route service.
 * 
 */
class WalkDirection {

  static final TransferType TRANSFER_TYPE = TransferType.WALK;

  static final String LINE = "Walk";

  static final String ENDPOINT = "destination";

  static final String REMARK = null;

  static final int DIRECTION_INDEX = 0;

  static final String DIR_ID = "WALK_DIR";

  static final String DESCRIPTION = "Walk, towards destination";

  static final String SERVICE_TYPE = "REGULAR";

}
