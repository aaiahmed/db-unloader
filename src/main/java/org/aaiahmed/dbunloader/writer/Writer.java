package org.aaiahmed.dbunloader.writer;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Writer {
  void write(final String[] headers, final ResultSet resultSet, final File file)
      throws IOException, SQLException;
}
