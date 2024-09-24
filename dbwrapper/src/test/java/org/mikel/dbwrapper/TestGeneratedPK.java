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

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGeneratedPK {

  private DBEnv m_env = null;

  private Properties readConnectInfo() throws DBException {
    final String CONFIG_FILE = "/dbconn.properties";
    Properties p = new Properties();
    try (InputStream in = this.getClass().getResourceAsStream(CONFIG_FILE)) {
      if (null == in) {
        throw new DBException("cannot find: " + CONFIG_FILE);
      }
      p.load(in);
    } catch (IOException | IllegalArgumentException e) {
      throw new DBException("cannot read: " + CONFIG_FILE, e);
    }
    return p;
  }

  @Before
  public void setUp() throws DBException {
    Properties props = readConnectInfo();
    DBEnvFactory fac = new DBEnvFactory();
    m_env = fac.createOwned(props);
  }

  @Test
  public void testInsertUpdateDelete() throws SQLException, DBException, TestWrapException {
    m_env.execStmt("delete from app_module");

    AppModule tw = new AppModule();
    tw.setSeqno(2);
    tw.setModule("Pruttel module");
    tw.setAmount(12.5);
    
    Date rd = setRaboDate("2019-10-12");
    Calendar cal = Calendar.getInstance();
    cal.setTime(rd);
    assertEquals("jaar", 2019, cal.get(Calendar.YEAR));
    assertEquals("maand", 10 - 1, cal.get(Calendar.MONTH));
    assertEquals("dag", 12, cal.get(Calendar.DAY_OF_MONTH));
    
    tw.setTxDate(rd);
    m_env.insertEntry(tw);

    String qry = "select id, ts_insert, app_module as module42, app_name, seqno, "
        + " tx_date, amount, isOKorNull, isOK from app_module";
    List<AppModule> items = new ArrayList<>();
    m_env.getItems(AppModule.class, items, qry);
    assertEquals(1, items.size());
    tw = items.get(0);
    assertEquals(2, tw.getSeqno());
    assertEquals(12.5, tw.getAmount(), 0.001);
    assertEquals("Pruttel module", tw.getModule());

    cal.setTime(tw.getTxDate());
    assertEquals("jaar", 2019, cal.get(Calendar.YEAR));
    assertEquals("maand", 10 - 1, cal.get(Calendar.MONTH)); // January = 0.
    assertEquals("dag", 12, cal.get(Calendar.DAY_OF_MONTH));
    
    int assigned_pk = tw.getId();

    tw.setModule("Testing...");
    tw.setSeqno(666);
    tw.setAmount(13.5);
    m_env.updateEntry(tw);
    items.clear();
    m_env.getItems(AppModule.class, items, qry);
    assertEquals(1, items.size());
    tw = items.get(0);
    assertEquals(assigned_pk, tw.getId());
    assertEquals(666, tw.getSeqno());
    assertEquals(13.5, tw.getAmount(), 0.001);
    assertEquals("Testing...", tw.getModule());

    cal.setTime(tw.getTxDate());
    assertEquals("jaar", 2019, cal.get(Calendar.YEAR));
    assertEquals("maand", 10 - 1, cal.get(Calendar.MONTH)); // January = 0.
    assertEquals("dag", 12, cal.get(Calendar.DAY_OF_MONTH));

    m_env.deleteEntry(tw);
    items.clear();
    m_env.getItems(AppModule.class, items, qry);
    assertTrue(items.isEmpty());
  }

  @After
  public void tearDown() {
    if (null != m_env) {
      m_env.destroy();
      m_env = null;
    }
  }

  private Date setRaboDate(String s) throws TestWrapException {
    final String RABO_DATE_FORMAT = "yyyy-MM-dd";
    try {
      if (null == s || s.trim().isEmpty()) {
        return null;
      }
      SimpleDateFormat sdf = new SimpleDateFormat(RABO_DATE_FORMAT);
      String s2 = s.trim();
      return sdf.parse(s2);
    } catch (ParseException | NullPointerException e) {
      throw new TestWrapException(e);
    }
  }
}
