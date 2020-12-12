package org.aaiahmed.dbunloader.writer;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvWriterTest {

  private final String table = "accounts";
  private final String[] headers = {"id", "user"};

  @Mock private ResultSet resultSet;

  static CsvWriter csvWriter;

  @BeforeAll
  static void setup() {
    csvWriter = new CsvWriter();
  }

  @Test
  void getWriterFileReturnsExpectedFile() {
    final Config conf = ConfigFactory.load("valid.conf");
    final String currentDateTime = csvWriter.getCurrentDateTime();
    final File expected =
        new File(
            String.format(
                "%1s/%2s/%3s.csv", conf.getString("writer.path"), table, currentDateTime));
    final File actual = csvWriter.getWriterFile(table, conf);
    assertEquals(expected, actual);
  }

  @Test
  void writeThrowsIOException() {
    final Config conf = ConfigFactory.load("invalid.conf");
    assertThrows(
        IOException.class,
        () -> {
          csvWriter.write(table, conf, headers, resultSet);
        });
  }

  @Test
  void writeThrowsSQLException() throws SQLException {
    final Config conf = ConfigFactory.load("valid.conf");
    when(resultSet.getMetaData()).thenThrow(SQLException.class);
    assertThrows(
        SQLException.class,
        () -> {
          csvWriter.write(table, conf, headers, resultSet);
        });
  }
}
