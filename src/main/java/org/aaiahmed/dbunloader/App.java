package org.aaiahmed.dbunloader;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.aaiahmed.dbunloader.db.DB;
import org.aaiahmed.dbunloader.db.DBFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class App {
  public static void main(String[] args) throws SQLException, ClassNotFoundException {

    DBFactory dbFactory = new DBFactory();
    DB db = dbFactory.getDB();
    db.connect();

    // TODO: Remove for testing
    final String query = "select table_name from information_schema.columns limit 10;";
    ResultSet resultSet = db.executeQuery(query);
    while (resultSet.next()) {
      System.out.println(resultSet.getString(1));
    }

    // TODO: Find list of tables to unload
    final Config conf = ConfigFactory.load();
    List<String> items = conf.getStringList("database.tables");
    System.out.println(items);

    // TODO: For each table, create a new subprocess to read the table and write as csv.
    // To make it dynamic, query the information_schema for columns.
    // Do we want to write parquet?
    // Create a Parquet/CSV writer.
    // Make sure it's valid csv/parquet.
    // Add logger.

    // Close db connection once all child processes finishes.
    db.close();
  }
}
