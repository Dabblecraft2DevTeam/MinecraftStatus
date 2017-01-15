package com.bwfcwalshy.minecraftstatus.sql;

import com.bwfcwalshy.minecraftstatus.MinecraftStatus;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.naming.Context;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <br>
 * Created by Arsen on 29.10.16..
 */
public class SQLController {

    private static final Context context = null;
    private static final MysqlDataSource dataSource;

    static {
        dataSource = new MysqlDataSource();
        dataSource.setPassword(MinecraftStatus.passwd);
        dataSource.setDatabaseName("mcstatus");
        dataSource.setPort(3306);
        dataSource.setPortNumber(3306);
        dataSource.setServerName("127.0.0.1");
        dataSource.setUser("mcstatus");
        dataSource.setURL(dataSource.getURL() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false");
        dataSource.setUrl(dataSource.getUrl() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false");
    }

    private static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void runSqlTask(SQLTask toRun) throws SQLException {
        Connection c = getConnection();
        toRun.execute(c);
        if (!c.isClosed())
            c.close();
    }
}
