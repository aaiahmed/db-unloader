package org.aaiahmed.dbunloader.db;

import java.sql.*;

public abstract class DefaultDB implements DB {

  private final String url;
  private final String user;
  private final String password;
  private String driver;
  private Connection connection;

  public DefaultDB(final String url, final String user, final String password) {
    this.url = url;
    this.user = user;
    this.password = password;
  }

  protected void setDriver(final String driver) {
    this.driver = driver;
  }

  @Override
  public void connect() throws SQLException, ClassNotFoundException {
    Class.forName(driver);
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
