package Communication;

import java.sql.*;

public class SQLHandler {

    private Connection connection = null;

    private Connection connect() {

        if (connection != null)
            return connection;

        try {

            connection.setAutoCommit(true);
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Opened database successfully");

            return connection;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void createTable() {

        try {

            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS Users(Username message_text primary key not null, Password char not null)");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insert() {}

    public String select() {

    }



}
