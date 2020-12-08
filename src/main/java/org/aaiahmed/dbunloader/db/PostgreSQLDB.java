package org.aaiahmed.dbunloader.db;

public class PostgreSQLDB extends DefaultDB {

  private final String driver = "org.postgresql.Driver";

  public PostgreSQLDB(
      final String host,
      final String port,
      final String db,
      final String user,
      final String password) {
    super(String.format("jdbc:postgresql://%1s:%2s/%3s", host, port, db), user, password);
    setDriver(driver);
  }
}
