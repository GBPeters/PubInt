package dbhandler;

import java.sql.*;

/**
 * Basic PostgreSQL database handler for connections to localhost:5432/pubint
 *
 * Created by gijspeters on 02-10-16.
 */
public class DBConnection {

    public static final String HOST = "localhost";
    public static final String PORT = "5432";
    public static final String NAME = "pubint";
    public static final String USER = "postgres";
    public static final String PASS = "postgres";

    private Connection connection;
    private boolean autoCommit;

    /**
     * Default constructor for a database connection
     * Autocommit is false by default
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public DBConnection() throws SQLException, ClassNotFoundException {
        setupConnection(false);
    }

    /**
     * Constructor that allows setting of autocommit
     *
     * @param autocommit
     * @throws ClassNotFoundException
     * @throws SQLException
     */
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

    /**
     * Method for executing queries with responses
     *
     * @param query A valid SQL query
     * @return
     * @throws SQLException
     */
    public ResultSet select(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }

    /**
     * Method for executing other statements without responses.
     *
     * @param query A valid SQL statement
     * @throws SQLException
     */
    public void execute(String query) throws SQLException {
        execute(query, doesAutoCommit());
    }

    /**
     * Method for executing other statements without responses.
     *
     * @param query  A valid SQL statement
     * @param commit Set to true for instant commit
     * @throws SQLException
     */
    public void execute(String query, boolean commit) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        if (commit) {
            commit();
        }
    }

    /**
     * Commit database changes
     *
     * @throws SQLException
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * Get autocommit setting
     *
     * @return boolean autocommit
     */
    public boolean doesAutoCommit() {
        return autoCommit;
    }

    /**
     * Set autocommit
     *
     * @param autoCommit true to enable autocommit
     */
    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    /**
     * Closes the database connection. DBConnection object becomes useless after close.
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        connection.close();
    }
}
