package org.aaiahmed.dbunloader.writer;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class WriterFactory {
  public Writer getWriter() {
    final Config conf = ConfigFactory.load();
    final String writerType = conf.getString("writer.type");

    if (writerType.equals("csv")) {
      return new CsvWriter();
    } else {
      throw new RuntimeException("Writer type not yet implemented.");
    }
  }
}
