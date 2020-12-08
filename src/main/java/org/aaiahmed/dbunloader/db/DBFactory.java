package org.aaiahmed.dbunloader.db;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class DBFactory {

  public DB getDB() {
    final Config conf = ConfigFactory.load();
    final String host = conf.getString("database.host");
    final String port = conf.getString("database.port");
    final String dbName = conf.getString("database.db");
    final String user = conf.getString("database.user");
    final String password = conf.getString("database.password");
    final String type = conf.getString("database.type");

    if (type.equals("postgresql")) {
      return new PostgreSQLDB(host, port, dbName, user, password);
    } else if (type.equals("mysql")) {
      return new MySQLDB(host, port, dbName, user, password);
    } else {
      throw new RuntimeException("Database type not yet implemented.");
    }
  }
}
