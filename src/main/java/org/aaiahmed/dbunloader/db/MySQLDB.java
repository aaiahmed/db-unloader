package org.aaiahmed.dbunloader.db;

public class MySQLDB extends DefaultDB {

  private final String driver = "com.mysql.jdbc.Driver";

  public MySQLDB(
      final String host,
      final String port,
      final String db,
      final String user,
      final String password) {
    super(String.format("jdbc:mysql://%1s:%2s/%3s", host, port, db), user, password);
    setDriver(driver);
  }
}
