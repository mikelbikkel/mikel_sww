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

package org.mikel.dbwrapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLRecoverableException;
import java.sql.SQLTransientException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;

/**
 * Abstract base class for database environment.<br>
 * Manages resources that are acquired via the application.
 * <p>
 * Container can be a servlet or a standalone java application.
 * <p>
 * Policy: When to create a new connection When to cloase a connection.
 * Scenario's: servlet context standalone java context.
 * 
 * <p>
 * <b>Limitation</b>: there can only be 1 column with a primary key annotation.
 * This implementation does not support tables with a primary key that consists
 * of multiple columns.
 * 
 */
public abstract class DBEnv {

  /**
   * Lifetime method. Same as servlet.
   */
  public abstract void destroy();

  /**
   * Creates a database connection.
   * 
   * @return database connection.
   * @throws SQLException
   */
  protected abstract Connection getDBConnection() throws SQLException;

  /**
   * @param con
   */
  protected abstract void disconnect(Connection con);

  /**
   * The database connection that is used for the active transaction.
   * <p>
   * A value of @{code null} means that there is no active transaction.
   */
  private Connection m_tx;
  private boolean m_prevAutoCommit;

  /**
   * Start a new transaction.
   * <p>
   * If a transaction is already started, this method is a no-op.
   */
  public void startTransaction() throws SQLException {
    if (null != m_tx) {
      return;
    }
    m_tx = getDBConnection();
    m_prevAutoCommit = m_tx.getAutoCommit();
    m_tx.setAutoCommit(false);
    return;
  }

  /**
   * Commit the active transaction.
   * <p>
   * If there is no active transaction, this method is a no-op.
   * 
   * @throws SQLException
   *           if the transaction cannot be committed.
   */
  public void commit() throws SQLException {
    if (null == m_tx) {
      return;
    }
    m_tx.commit();
    m_tx.setAutoCommit(m_prevAutoCommit);
    disconnect(m_tx);
    m_tx = null;
  }

  /**
   * Roll back and terminate the active transaction.
   * <p>
   * This methods does not throw exceptions. When rolling back, something has
   * gone wrong already. If in this situation the roll back fails as well, there
   * is not much we can do..... <br>
   * Except ensuring that, whatever errors have occurred, the post-condition of
   * this method is: there is no active transaction.
   */
  public void rollback() {
    if (null == m_tx) {
      return;
    }

    try {
      m_tx.rollback();
      m_tx.setAutoCommit(m_prevAutoCommit);
    } catch (SQLException e) {
      // ignore. We just want to make sure we continue with the disconnect
      // of
      // this connection.
    }

    try {
      disconnect(m_tx);
    } catch (Exception e) {
      // Do nothing. Just suppress the exception, and make sure that m_tx
      // is set
      // to null.
    } finally {
      m_tx = null;
    }
  }

  /**
   * Number of retries in case of retry-able exceptions.
   */
  private static final int NUM_RETRIES = 2;

  /**
   * A generic method to transform the result set of a query into a collection
   * of objects.
   * 
   * @param cls
   *          the Class of the generic type. Required as a parameter because
   *          it's not possible to derive the type of T from a Collection<T>.
   * @param items
   *          collection, allocated by the caller to store the items.
   * @param qry
   *          the query to initialize the items.
   * @throws SQLException
   *           in case of a database error.
   * @throws DBException
   *           if an item cannot be instantiated or initialized.
   */
  public <T> void getItems(Class<T> cls, Collection<T> items, String qry)
      throws SQLException, DBException {
    getItems(cls, items, qry, null);
  }

  /**
   * A generic method to transform the result set of a query into a collection
   * of objects.
   * 
   * @param cls
   *          the Class of the generic type. Required as a parameter because
   *          it's not possible to derive the type of T from a Collection<T>.
   * @param items
   *          collection, allocated by the caller to store the items.
   * @param qry
   *          the query to initialize the items.
   * @param owner
   *          callback function to set parameters for the query. If null, the
   *          callback function is not called.
   * @throws SQLException
   *           in case of a database error.
   * @throws DBException
   *           if an item cannot be instantiated or initialized.
   */
  public <T> void getItems(Class<T> cls, Collection<T> items, String qry, IDBQueryOwner owner)
      throws SQLException, DBException {
    T obj;

    for (int numRetries = 0; numRetries < NUM_RETRIES; numRetries++) {
      try {
        Connection dbcon = m_tx;
        if (null == m_tx) {
          dbcon = getDBConnection();
        }
        // If the Statement is closed, the ResultSet is closed as well.
        // The Statement is closed as part of the try-with-resources.
        try (PreparedStatement stmt = dbcon.prepareStatement(qry)) {
          if (null != owner) {
            owner.setParams(stmt);
          }
          ResultSet rs = stmt.executeQuery();
          DBFiatLux flx = new DBFiatLux();
          while (rs.next()) {
            obj = flx.resurrectObject(cls, rs);
            items.add(obj);
          }
        } finally {
          if (null == m_tx) {
            // Do not disconnect if a transaction is active (if m_tx
            // != null).
            // If there is no active transaction, let the DBEnv
            // decides whether
            // to close the
            // connection or not.
            disconnect(dbcon);
          }
        }
        return; // exit the for-loop in case of success.
      } catch (SQLRecoverableException | SQLTransientException e) {
        // try again.
      }
    }
  }

  /**
   * Executes an insert statement and returns generated keys.
   * <p>
   * Use this method to insert rows with a primary key that is generated by the
   * database.
   * </p>
   * 
   * @param qry
   *          the insert statement to execute.
   * @param owner
   *          callback interface
   * @throws SQLException
   *           in case of JDBC exception
   * @throws DBException
   *           in case of non-JDBC database exception.
   */
  public int[] execInsert(String qry, IDBQueryOwner owner) throws SQLException, DBException {
    for (int numRetries = 0; numRetries < NUM_RETRIES; numRetries++) {
      try {
        Connection dbcon = m_tx;
        if (null == m_tx) {
          dbcon = getDBConnection();
        }
        // If the Statement is closed, the ResultSet is closed as well.
        // The Statement is closed as part of the try-with-resources.
        try (
            PreparedStatement stmt = dbcon.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
          if (null != owner) {
            owner.setParams(stmt);
          }
          int cnt = stmt.executeUpdate();
          if (0 == cnt) {
            throw new DBException("cannot insert entry");
          }
          int[] keys = new int[cnt];
          try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            for (int idx = 0; generatedKeys.next(); idx++) {
              keys[idx] = generatedKeys.getInt(1);
            }
          }
          return keys; // exit the for-loop in case of success.
        } finally {
          if (null == m_tx) {
            // Do not disconnect if a transaction is active (if m_tx
            // != null).
            // If there is no active transaction, let the DBEnv
            // decides whether
            // to close the
            // connection or not.
            disconnect(dbcon);
          }
        }
      } catch (SQLRecoverableException | SQLTransientException e) {
        // try again.
      }
    }
    return null;
  }

  /**
   * Executes DML and DDL statements.
   * <p>
   * Use this method for DML/DDL statements that do not auto-generate keys on
   * insert.
   * </p>
   * 
   * @param qry
   *          the SQL statement to execute.
   * @param owner
   *          callback interface
   * @throws SQLException
   *           in case of JDBC exception
   * @throws DBException
   *           in case of non-JDBC database exception.
   */
  public void execStmt(String qry, IDBQueryOwner owner) throws SQLException, DBException {
    for (int numRetries = 0; numRetries < NUM_RETRIES; numRetries++) {
      try {
        Connection dbcon = m_tx;
        if (null == m_tx) {
          dbcon = getDBConnection();
        }
        // If the Statement is closed, the ResultSet is closed as well.
        // The Statement is closed as part of the try-with-resources.
        try (PreparedStatement stmt = dbcon.prepareStatement(qry)) {
          if (null != owner) {
            owner.setParams(stmt);
          }
          stmt.executeUpdate();
          return; // exit the for-loop in case of success.
        } finally {
          if (null == m_tx) {
            // Do not disconnect if a transaction is active (if m_tx
            // != null).
            // If there is no active transaction, let the DBEnv
            // decides whether
            // to close the
            // connection or not.
            disconnect(dbcon);
          }
        }
      } catch (SQLRecoverableException | SQLTransientException e) {
        // try again.
      }
    }
  }

  public void execStmt(String qry) throws SQLException, DBException {
    execStmt(qry, null);
  }

  /**
   * Executes a stored procedure.
   * <p>
   * Use this method to call stored procedures and user defined functions.<br/>
   * An example procedure call is: <code>{ ? = call upper( ? ) }</code>
   * </p>
   * 
   * @param sql
   *          the SQL statement to execute.
   * @param owner
   *          callback interface
   * @throws SQLException
   *           in case of JDBC exception
   * @throws DBException
   *           in case of non-JDBC database exception.
   */
  public void execProcedure(String sql, IDBQueryOwner owner) throws SQLException, DBException {
    for (int numRetries = 0; numRetries < NUM_RETRIES; numRetries++) {
      try {
        Connection dbcon = m_tx;
        if (null == m_tx) {
          dbcon = getDBConnection();
        }
        // The Statement is closed as part of the try-with-resources.
        try (CallableStatement stmt = dbcon.prepareCall(sql)) {
          if (null != owner) {
            owner.setParams(stmt);
          }
          stmt.execute();
          return; // exit the for-loop in case of success.
        } finally {
          if (null == m_tx) {
            // Do not disconnect if a transaction is active (if m_tx
            // != null).
            // If there is no active transaction, let the DBEnv
            // decides whether
            // to close the
            // connection or not.
            disconnect(dbcon);
          }
        }
      } catch (SQLRecoverableException | SQLTransientException e) {
        // try again.
      }
    }
  }

  public void updateEntry(Object obj) throws SQLException, DBException {
    DBUpdate dbu = new DBUpdate(obj);
    String sql = dbu.getSQL();
    for (int numRetries = 0; numRetries < NUM_RETRIES; numRetries++) {
      try {
        Connection dbcon = m_tx;
        if (null == m_tx) {
          dbcon = getDBConnection();
        }
        // The Statement is closed as part of the try-with-resources.
        try (PreparedStatement stmt = dbcon.prepareStatement(sql)) {
          dbu.setParams(stmt);
          stmt.executeUpdate();
          return; // exit the for-loop in case of success.
        } finally {
          if (null == m_tx) {
            // Do not disconnect if a transaction is active (if m_tx
            // != null).
            // If there is no active transaction, let the DBEnv
            // decides whether to close the connection or not.
            disconnect(dbcon);
          }
        }
      } catch (SQLRecoverableException | SQLTransientException e) {
        // try again.
      }
    }

  }

  public void insertEntry(Object obj) throws SQLException, DBException {
    DBInsert dbi = new DBInsert(obj);
    String sql = dbi.getSQL();
    for (int numRetries = 0; numRetries < NUM_RETRIES; numRetries++) {
      try {
        Connection dbcon = m_tx;
        if (null == m_tx) {
          dbcon = getDBConnection();
        }
        // The Statement is closed as part of the try-with-resources.
        try (PreparedStatement stmt = dbcon.prepareStatement(sql)) {
          dbi.setParams(stmt);
          stmt.executeUpdate();
          return; // exit the for-loop in case of success.
        } finally {
          if (null == m_tx) {
            // Do not disconnect if a transaction is active (if m_tx
            // != null).
            // If there is no active transaction, let the DBEnv
            // decides whether
            // to close the
            // connection or not.
            disconnect(dbcon);
          }
        }
      } catch (SQLRecoverableException | SQLTransientException e) {
        // try again.
      }
    }
  }

  public <T> void insertBatch(Collection<T> coll) throws SQLException, DBException {
    String sql = getInsert(coll);
    if (null == sql) {
      return; // empty collection.
    }
    for (int numRetries = 0; numRetries < NUM_RETRIES; numRetries++) {
      try {
        Connection dbcon = m_tx;
        if (null == m_tx) {
          dbcon = getDBConnection();
        }
        // The Statement is closed as part of the try-with-resources.
        Object obj = null;
        DBInsert dbi = null;
        try (PreparedStatement stmt = dbcon.prepareStatement(sql)) {
          Iterator<T> i = coll.iterator();
          while (i.hasNext()) {
            obj = i.next();
            dbi = new DBInsert(obj);
            dbi.setParams(stmt);
            stmt.addBatch();
          }
          stmt.executeBatch();
          return; // exit the for-loop in case of success.
        } finally {
          if (null == m_tx) {
            // Do not disconnect if a transaction is active (if m_tx
            // != null).
            // If there is no active transaction, let the DBEnv
            // decides whether
            // to close the
            // connection or not.
            disconnect(dbcon);
          }
        }
      } catch (SQLRecoverableException | SQLTransientException e) {
        // try again.
      }
    }
  }

  /**
   * Executes a batch of insert/update statements.
   * <p>
   * Adds each item in the collection to the batch, and calls the owner to set
   * the parameters.
   * </p>
   * 
   * @param qry
   *          the SQL statement to execute.
   * @param owner
   *          callback interface
   * @throws SQLException
   *           in case of JDBC exception
   * @throws DBException
   *           in case of non-JDBC database exception.
   */
  public <T> void execBatch(String qry, Collection<T> coll, BatchQueryOwner<T> owner)
      throws SQLException, DBException {
    for (int numRetries = 0; numRetries < NUM_RETRIES; numRetries++) {
      try {
        Connection dbcon = m_tx;
        if (null == m_tx) {
          dbcon = getDBConnection();
        }
        // If the Statement is closed, the ResultSet is closed as well.
        // The Statement is closed as part of the try-with-resources.
        try (PreparedStatement stmt = dbcon.prepareStatement(qry)) {
          Iterator<T> i = coll.iterator();
          while (i.hasNext()) {
            T obj = i.next();
            if (null != owner) {
              owner.setParams(stmt, obj);
            }
            stmt.addBatch();
          }
          stmt.executeBatch();
          return; // exit the for-loop in case of success.
        } finally {
          if (null == m_tx) {
            // Do not disconnect if a transaction is active (if m_tx
            // != null).
            // If there is no active transaction, let the DBEnv
            // decides whether
            // to close the
            // connection or not.
            disconnect(dbcon);
          }
        }
      } catch (SQLRecoverableException | SQLTransientException e) {
        // try again.
      }
    }

  }

  private <T> String getInsert(Collection<T> coll) throws DBException {
    Iterator<T> iter = coll.iterator();
    Object obj = null;
    if (iter.hasNext()) {
      obj = iter.next();
    } else {
      return null; // empty collection.
    }
    DBInsert dbi = new DBInsert(obj);
    return dbi.getSQL();
  }

  // It is implementation-defined as to whether getGeneratedKeys will return
  // generated values after invoking the executeBatch method.
  public void insertEntryReturnKey(Object obj) throws SQLException, DBException {
    DBInsert dbi = new DBInsert(obj);
    String sql = dbi.getSQL();
    for (int numRetries = 0; numRetries < NUM_RETRIES; numRetries++) {
      try {
        Connection dbcon = m_tx;
        if (null == m_tx) {
          dbcon = getDBConnection();
        }
        try (PreparedStatement stmt = dbcon.prepareStatement(sql, dbi.getGeneratedPKNames())) {
          // Statement.RETURN_GENERATED_KEYS)) {
          dbi.setParams(stmt);
          int cnt = stmt.executeUpdate();
          if (0 == cnt) {
            throw new DBException("cannot insert entry");
          }
          try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            dbi.setPrimaryKeyValue(generatedKeys);
          } catch (SQLFeatureNotSupportedException e) {
            return; // getGeneratedKeys() is not supported.
          }
          return; // exit the for-loop in case of success.
        } finally {
          if (null == m_tx) {
            // Do not call disconnect if a transaction is active.
            disconnect(dbcon);
          }
        }
      } catch (SQLRecoverableException | SQLTransientException e) {
        // try again.
      }
    }
  }

  public void deleteEntry(Object obj) throws SQLException, DBException {
    DBDelete dbd = new DBDelete(obj);
    String sql = dbd.getSQL();
    execStmt(sql, dbd);
  }
}
