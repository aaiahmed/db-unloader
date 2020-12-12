package org.aaiahmed.dbunloader.writer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CsvWriter implements Writer {

  @Override
  public void write(final String[] headers, final ResultSet resultSet, final File file)
      throws IOException, SQLException {

    try (FileWriter fileWriter = new FileWriter(file);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader(headers))) {

      csvPrinter.printRecords(resultSet);
    }
  }
}
