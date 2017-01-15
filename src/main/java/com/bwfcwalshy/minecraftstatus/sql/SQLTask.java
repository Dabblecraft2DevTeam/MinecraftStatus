package com.bwfcwalshy.minecraftstatus.sql;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLTask {
    void execute(Connection c) throws SQLException;
}
