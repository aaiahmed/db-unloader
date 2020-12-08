package org.aaiahmed.dbunloader.db;

import java.sql.Connection;
import java.sql.ResultSet;

public interface DB {
    Connection connect();
    ResultSet execute();
    void commit();
    void close();
}
