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
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class CsvWriter implements Writer {

  @Override
  public void write(final String table, final String[] headers, final ResultSet resultSet)
      throws IOException, SQLException {

    final File file = getWriterFile(table, ConfigFactory.load());

    try (FileWriter fileWriter = new FileWriter(file);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader(headers))) {

      csvPrinter.printRecords(resultSet);
    }
  }

  private File getWriterFile(final String table, final Config conf) {
    final String directoryName = String.format("%1s/%2s", conf.getString("writer.path"), table);
    final File directory = new File(directoryName);
    if (!directory.exists()) {
      directory.mkdir();
    }
    return new File(String.format("%1s/%2s.csv", directoryName, getCurrentDateTime()));
  }

  private String getCurrentDateTime() {
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    final Instant currentTime = Instant.now();
    final OffsetDateTime offsetDateTime = currentTime.atOffset(ZoneOffset.UTC);
    return offsetDateTime.format(dateTimeFormatter);
  }
}
