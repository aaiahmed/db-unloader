package org.aaiahmed.dbunloader.db;

public class MySQLDB extends DefaultDB {
  private final String driver = "com.mysql.jdbc.Driver";

  public MySQLDB(String url, String user, String password) {
    super(url, user, password);
    setDriver(driver);
  }
}
