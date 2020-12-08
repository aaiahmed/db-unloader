package org.aaiahmed.dbunloader.db;

import java.sql.*;

public class MySQL implements DB {

  private final String url;
  private final String user;
  private final String password;
  private Connection connection;

  public MySQL(final String driver, final String url, final String user, final String password)
      throws ClassNotFoundException {
    this.url = url;
    this.user = user;
    this.password = password;
    Class.forName(driver);
  }

  @Override
  public void connect() throws SQLException {
    connection = DriverManager.getConnection(url, user, password);
    connection.setAutoCommit(false);
  }

  @Override
  public ResultSet executeQuery(final String query) throws SQLException {
    final Statement statement = connection.createStatement();
    return statement.executeQuery(query);
  }

  @Override
  public void commit() throws SQLException {
    connection.commit();
  }

  @Override
  public void close() throws SQLException {
    connection.close();
  }
}
