package org.aaiahmed.dbunloader.writer;

import org.aaiahmed.dbunloader.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvWriterTest {

  private static final String tableName = "accounts";
  private static final String[] headers = {"id", "user"};
  private static final String currentDateTime = Utils.getCurrentDateTime();
  private static final String validPath = "output";
  private static final File actualFile = Utils.getWriterFile(validPath, tableName, currentDateTime);
  private static final String invalidPath = "/non/existent/Path";
  private static final File invalidFile =
      Utils.getWriterFile(invalidPath, tableName, currentDateTime);

  @Mock private ResultSet resultSet;
  @Mock private ResultSetMetaData resultSetMetaData;

  static CsvWriter csvWriter;

  @BeforeAll
  static void setup() {
    csvWriter = new CsvWriter();
  }

  @Test
  void getWriterFileReturnsExpectedFileForValidPath() {
    final File expectedFile =
        new File(String.format("%1s/%2s/%3s.csv", validPath, tableName, currentDateTime));
    assertEquals(expectedFile, actualFile);
  }

  @Test
  void writerThrowsIOExceptionWhenWritingToInvalidPath() {
    assertThrows(
        IOException.class,
        () -> {
          csvWriter.write(headers, resultSet, invalidFile);
        });
  }

  @Test
  void writerThrowsSQLExceptionWhenIssuesFromDatabase() throws SQLException {
    when(resultSet.getMetaData()).thenThrow(SQLException.class);
    assertThrows(
        SQLException.class,
        () -> {
          csvWriter.write(headers, resultSet, actualFile);
        });
  }

  @Test
  void writerWritesFileSuccessfullyForValidDatabaseResponse() throws SQLException, IOException {
    final File expectedFile = new File("src/test/resources/expectedFile.csv");
    final String mockColumnData = "test";
    when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
    when(resultSetMetaData.getColumnCount()).thenReturn(2);
    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet.getObject(anyInt())).thenReturn(mockColumnData);
    csvWriter.write(headers, resultSet, actualFile);
    assertTrue(actualFile.exists());
    assertTrue(FileUtils.contentEquals(expectedFile, actualFile));
  }

  @AfterAll
  static void cleanUp() {
    actualFile.delete();
  }
}
