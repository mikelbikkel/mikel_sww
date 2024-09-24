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

import java.util.Date;

/**
 * {@code 
 * create table app_module
 * ( id          serial primary key
 * , ts_insert   timestamp default current_timestamp
 * , app_module  varchar(100)
 * , app_name    varchar(100)
 * , seqno       integer
 * , tx_date     date
 * , amount      decimal(9,2)
 * , isOKorNull  varchar(1)
 * , isOK        varchar(1) not null default 'N'
 * , constraint tw_ok_domain check (isOK in ('Y', 'N'))
 * , constraint tw_okn_domain check (isOKorNull in ('Y', 'N'))
 * );
 * }
 */

@SourceTable("app_module")
public class AppModule {

  public static final int ID_NEW_ENTRY = -1;

  @PrimaryKey
  @SourceColumn("id")
  private int m_id;

  @SourceColumn(value = "ts_insert", sqlType = java.sql.Types.TIMESTAMP, writeInclude = false)
  private Date m_createDate;

  @SourceColumn(value = "module42", writeColumn = "app_module")
  private String m_module;

  @SourceColumn("app_name")
  private String m_name;

  @SourceColumn(value = "seqno", nullValue = "0")
  private int m_seqno;

  @SourceColumn("amount")
  private double m_amount;

  @SourceColumn("tx_date")
  private Date m_txDate;

  public AppModule() {
    m_id = ID_NEW_ENTRY;
  }

  public int getId() {
    return m_id;
  }

  public void setId(int id) {
    m_id = id;
  }

  public String getModule() {
    return m_module;
  }

  public void setModule(String module) {
    m_module = module;
  }

  public String getName() {
    return m_name;
  }

  public void setName(String name) {
    m_name = name;
  }

  public int getSeqno() {
    return m_seqno;
  }

  public void setSeqno(int seqno) {
    m_seqno = seqno;
  }

  public double getAmount() {
    return m_amount;
  }

  public void setAmount(double amount) {
    m_amount = amount;
  }

  public Date getTxDate() {
    return m_txDate;
  }

  public void setTxDate(Date txDate) {
    m_txDate = txDate;
  }

  public Date getCreateDate() {
    return m_createDate;
  }

  public void setCreateDate(Date createDate) {
    m_createDate = createDate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + m_id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof AppModule))
      return false;
    AppModule other = (AppModule) obj;
    if (m_id != other.m_id)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "TestWrapper01 [m_id=" + m_id + ", m_createDate=" + m_createDate + ", m_module="
        + m_module + ", m_name=" + m_name + ", m_seqno=" + m_seqno + ", m_amount=" + m_amount
        + ", m_txDate=" + m_txDate + "]";
  }

}
