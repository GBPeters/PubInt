package dbhandler;

import org.postgresql.*;
import org.postgresql.Driver;

import java.sql.*;

/**
 * Created by gijspeters on 02-10-16.
 */
public class DBConnection {

    public static final String HOST = "localhost";
    public static final String PORT = "5432";
    public static final String NAME = "tweets";
    public static final String USER = "postgres";
    public static final String PASS = "postgres";

    private Connection connection;
    private boolean autoCommit;

    public DBConnection() throws SQLException, ClassNotFoundException {
        setupConnection(false);
    }

    public DBConnection(boolean autocommit) throws ClassNotFoundException, SQLException {
        setupConnection(autocommit);
    }

    private void setupConnection(boolean autocommit) throws SQLException, ClassNotFoundException {
        Connection c = null;
        Class.forName("org.postgresql.Driver");
        //DriverManager.registerDriver(new Driver());
        c = DriverManager
                .getConnection("jdbc:postgresql://" + HOST + ":" + PORT + "/" + NAME,
                        USER, PASS);
        c.setAutoCommit(autocommit);
        setAutoCommit(autocommit);
        connection = c;
    }

    public ResultSet select(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }

    public void execute(String query) throws SQLException {
        execute(query, doesAutoCommit());
    }

    public void execute(String query, boolean commit) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        if (commit) {
            commit();
        }
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public boolean doesAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
