package org.aaiahmed.dbunloader.writer;

import com.typesafe.config.Config;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Writer {
  void write(final String tableName, final Config conf, final String[] headers, final ResultSet resultSet)
      throws IOException, SQLException;
}
