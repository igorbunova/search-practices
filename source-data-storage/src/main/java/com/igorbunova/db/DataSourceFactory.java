package com.igorbunova.db;

import java.sql.SQLException;
import javax.sql.DataSource;
import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariDataSource;

/**
 * DataSourceFactory.
 */
public class DataSourceFactory {
    private DataSourceFactory() {}

    public static DataSource create(Config conf) {
        String dsClass = conf.getString("jdbc.ds");
        String host = conf.getString("jdbc.host");
        int port = conf.getInt("jdbc.port");
        String database = conf.getString("jdbc.database");
        String username = conf.getString("jdbc.username");
        String password = conf.getString("jdbc.password");
        int loginTimeOutS = conf.getInt("hikari.login.timeout");
        long connectionTimeOutMs = conf.getLong("hikari.connection.timeout");
        int poolSize = conf.getInt("hikari.pool.size");
        HikariDataSource ds = new HikariDataSource();
        Runtime.getRuntime().addShutdownHook(new Thread(ds::close));

        ds.setDataSourceClassName(dsClass);
        ds.addDataSourceProperty("serverName", host);
        ds.addDataSourceProperty("databaseName", database);
        ds.addDataSourceProperty("portNumber", port);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setConnectionTimeout(connectionTimeOutMs);
        ds.setMaximumPoolSize(poolSize);
        try {
            ds.setLoginTimeout(loginTimeOutS);
        } catch (SQLException e) {
            throw new DbException(e);
        }
        ds.setPoolName("HikariCP_" + System.currentTimeMillis());

        return ds;
    }
}
