package org.aaiahmed.dbunloader;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.aaiahmed.dbunloader.db.DB;
import org.aaiahmed.dbunloader.db.DBFactory;
import org.aaiahmed.dbunloader.storage.AwsS3;
import org.aaiahmed.dbunloader.utils.Utils;
import org.aaiahmed.dbunloader.writer.Writer;
import org.aaiahmed.dbunloader.writer.WriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
  private static Logger logger = LoggerFactory.getLogger(Main.class);
  private static final String currentTimeStamp = Utils.getCurrentDateTime();

  public static void main(String[] args) {
    try {
      final Config conf = ConfigFactory.load();
      DB db = getDB();
      unloadTables(db, getTablesToUnload(conf), conf);
      db.close();
    } catch (Exception e) {
      logger.error("Unexpected error, db unload failed.", e);
    }
  }

  private static DB getDB() throws SQLException, ClassNotFoundException {
    final DBFactory dbFactory = new DBFactory();
    final DB db = dbFactory.getDB();
    db.connect();
    return db;
  }

  private static List<String> getTablesToUnload(final Config conf) {
    return conf.getStringList("database.tables");
  }

  private static void unloadTables(
      final DB db, final List<String> tablesToUnload, final Config conf) {

    ExecutorService executorService = Executors.newFixedThreadPool(conf.getInt("app.numThreads"));
    for (String tableName : tablesToUnload) {
      executorService.submit(
          () -> {
            try {
              logger.info("Table unload started for table: {}.", tableName);
              final String[] header = getTableHeader(db, tableName, conf);
              final ResultSet resultSet = getTableContent(db, tableName, conf);
              writeTableData(tableName, conf, header, resultSet);
              uploadToS3(tableName, conf);
              logger.info("Table unload completed for table: {}.", tableName);
            } catch (SQLException | IOException e) {
              logger.error("Table unload failed for table: {}.", tableName, e);
            }
          });
    }
    executorService.shutdown();
    try {
      executorService.awaitTermination(conf.getLong("app.timeoutInMinutes"), TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      logger.error(
          "Table unload did not finish in {} minutes.", conf.getLong("app.timeoutInMinutes"), e);
    }
  }

  private static String[] getTableHeader(final DB db, final String table, final Config conf)
      throws SQLException {

    logger.info("Finding header for table: {}.", table);
    final ResultSet resultSet =
        db.executeQuery(
            String.format(conf.getString("database.headerQuery"), table),
            conf.getInt("database.fetchSize"));
    List<String> columns = new LinkedList<>();
    while (resultSet.next()) {
      columns.add(resultSet.getString(1));
    }
    return columns.toArray(String[]::new);
  }

  private static ResultSet getTableContent(final DB db, final String table, final Config conf)
      throws SQLException {

    logger.info("Retrieving data from table: {}.", table);
    return db.executeQuery(
        String.format(conf.getString("database.tableQuery"), table),
        conf.getInt("database.fetchSize"));
  }

  private static void writeTableData(
      final String tableName, final Config conf, final String[] header, final ResultSet resultSet)
      throws SQLException, IOException {

    logger.info("Writing data for table: {}.", tableName);
    final String path = conf.getString("writer.path");
    final WriterFactory writerFactory = new WriterFactory();
    final Writer writer = writerFactory.getWriter();
    final File file = Utils.getWriterFile(path, tableName, currentTimeStamp);
    writer.write(header, resultSet, file);
  }

  private static void uploadToS3(final String tableName, final Config conf) {
    logger.info("Uploading file to s3 for table: {}.", tableName);
    final String clientRegion = conf.getString("storage.s3.aws_region");
    final String bucketName = conf.getString("storage.s3.bucket");
    final String fileName =
        conf.getString("writer.path")
            + "/"
            + tableName
            + "/"
            + currentTimeStamp
            + "."
            + conf.getString("writer.type");

    final String fileObjKeyName =
        String.format("%1s/%2s.%3s", tableName, currentTimeStamp, conf.getString("writer.type"));
    System.out.println(fileObjKeyName);
    System.out.println(fileName);
    AwsS3.uploadFile(clientRegion, bucketName, fileObjKeyName, fileName);
  }
}
