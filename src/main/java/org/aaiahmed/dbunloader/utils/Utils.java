package org.aaiahmed.dbunloader.utils;

import java.io.File;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Utils {
  public static String getCurrentDateTime() {
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    final Instant currentTime = Instant.now();
    final OffsetDateTime offsetDateTime = currentTime.atOffset(ZoneOffset.UTC);
    return offsetDateTime.format(dateTimeFormatter);
  }

  public static File getWriterFile(final String path, final String tableName, final String timestamp) {
    final String directoryName = String.format("%1s/%2s", path, tableName);
    final File directory = new File(directoryName);
    if (!directory.exists()) {
      directory.mkdir();
    }
    return new File(String.format("%1s/%2s.csv", directoryName, timestamp));
  }
}
