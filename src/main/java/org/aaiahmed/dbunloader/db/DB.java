package org.aaiahmed.dbunloader.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DB {
  void connect() throws SQLException, ClassNotFoundException;
  ResultSet executeQuery(final String query, final int fetchSize) throws SQLException;
  void close() throws SQLException;
}
