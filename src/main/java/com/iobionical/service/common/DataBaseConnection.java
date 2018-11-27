/*
 * 2018 Sami.
 */
package com.iobionical.service.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author sami
 */
public class DataBaseConnection {

    private static final String DB_CONNECTION_URI = "java:jboss/datasources/PostgresDS";
    private static final Logger logger = Logger.getLogger(DataBaseConnection.class.getName());

    /**
     * Gets connection. from container managed connection pool.
     *
     * @return Connection
     */
    public Connection getDatabaseConnection() throws SQLException, NamingException {

        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(DB_CONNECTION_URI);

        return ds.getConnection();
    }

    /**
     * Closes SQL statement.
     *
     */
    public void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "closing statement failed: " + e);
        }
    }

}
