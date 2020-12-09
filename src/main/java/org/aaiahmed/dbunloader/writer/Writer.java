package org.aaiahmed.dbunloader.writer;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Writer {
  void write(final String tableName, final String[] headers, final ResultSet resultSet)
      throws IOException, SQLException;
}
