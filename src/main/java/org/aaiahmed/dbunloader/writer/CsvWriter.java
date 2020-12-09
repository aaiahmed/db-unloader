package org.aaiahmed.dbunloader.writer;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CsvWriter implements Writer {
  @Override
  public void write(final String tableName, final String[] headers, final ResultSet resultSet)
      throws IOException, SQLException {

    Config conf = ConfigFactory.load();
    final String pathName = conf.getString("writer.path");
    // TODO: Add timestamp to filename.
    final File file = new File(String.format("%1s/%2s.csv", pathName, tableName));

    try (FileWriter fileWriter = new FileWriter(file);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader(headers))) {

      csvPrinter.printRecords(resultSet);
    }
  }
}
