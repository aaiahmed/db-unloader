package org.aaiahmed.dbunloader;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.aaiahmed.dbunloader.db.DB;
import org.aaiahmed.dbunloader.db.DBFactory;
import org.aaiahmed.dbunloader.writer.Writer;
import org.aaiahmed.dbunloader.writer.WriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Main {
  private static Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    try {
      final Config conf = ConfigFactory.load();
      DB db = getDB();
      writeTables(db, getTables(conf), conf);
      db.close();
    } catch (SQLException | IOException | ClassNotFoundException e) {
      logger.error("Table unload failed", e);
    }
  }

  private static DB getDB() throws SQLException, ClassNotFoundException {
    final DBFactory dbFactory = new DBFactory();
    final DB db = dbFactory.getDB();
    db.connect();
    return db;
  }

  private static List<String> getTables(final Config conf) {
    return conf.getStringList("database.tables");
  }

  private static void writeTables(final DB db, final List<String> tables, final Config conf)
      throws SQLException, IOException {
    for (String tableName : tables) {
      final String[] headers = getHeader(db, tableName, conf);
      final ResultSet resultSet = getTable(db, tableName, conf);
      writeOutput(tableName, headers, resultSet);
    }
  }

  private static String[] getHeader(final DB db, final String tableName, final Config conf)
      throws SQLException {
    logger.info("Finding column headers for table: {}", tableName);
    final int fetchSize = conf.getInt("database.fetchSize");
    final String headerQuery = conf.getString("database.headerQuery");
    final ResultSet resultSet = db.executeQuery(String.format(headerQuery, tableName), fetchSize);
    List<String> columns = new LinkedList<>();
    while (resultSet.next()) {
      columns.add(resultSet.getString(1));
    }
    return columns.toArray(String[]::new);
  }

  private static ResultSet getTable(final DB db, final String tableName, final Config conf)
      throws SQLException {
    logger.info("Finding data for table: {}", tableName);
    final int fetchSize = conf.getInt("database.fetchSize");
    final String tableQuery = conf.getString("database.tableQuery");
    return db.executeQuery(String.format(tableQuery, tableName), fetchSize);
  }

  private static void writeOutput(
      final String tableName, final String[] headers, final ResultSet resultSet)
      throws SQLException, IOException {
    logger.info("Writing data for table: {}", tableName);
    WriterFactory writerFactory = new WriterFactory();
    Writer writer = writerFactory.getWriter();
    writer.write(tableName, headers, resultSet);
    logger.info("Table unload completed for table: {}", tableName);
  }
}
