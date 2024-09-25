package handlers;

import java.sql.*;

public class DatabaseHandler {
    protected static Connection connection;
    protected static Statement statement;
    protected static final String user = "postgres";
    protected static final String password = "123456778";
    protected static final String url = "jdbc:postgresql://localhost:5432/musicPlayer";

    private static void getConnection() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println("Could not connect to database\n"
            + e.getMessage());
        }
    }

    private static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Could not close the connection\n"
            + e.getMessage());
        }
    }

    private static void closeStatement() {
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Could not close the statement\n"
            + e.getMessage());
        }
    }

    public static void executeUpdate (String query) {
        getConnection();
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            closeStatement();
        } catch (SQLException e) {
            System.err.println("Could not execute the update\n"
            + e.getMessage());
        } finally {
            closeStatement();
            closeConnection();
        }
    }

    public static ResultSet getResultSet (String query) {
        getConnection();
        try {
            statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Could not get the result set\n"
            + e.getMessage());
        }
        return null;
    }

    public static void uploadTrack(String table, String name,
                                   String artists, String albums,
                                   String path) {
        int id = setUUID(path);
        String query = "INSERT INTO " + table
                + "(name, artists, albums, id_track) " +
                "VALUES (?, ?, ?, ?);";
        getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            statement = preparedStatement;
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, artists);
            preparedStatement.setString(3, albums);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Could not insert the statement\n"
            + e.getMessage());
        } finally {
            closeStatement();
            closeConnection();
        }
    }

    private static Integer setUUID (String path) {
        String uniqueTrackName =
                FileStoreHandler.uploadTrack("uploaded.tracks", path);
        getConnection();
        String query1 = "INSERT INTO uploaded_tracks (uuid_track) VALUES ('" + uniqueTrackName + "')";
        String query2 = "SELECT id_track FROM uploaded_tracks WHERE uuid_track = '" + uniqueTrackName + "'";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query1);
        } catch ( SQLException e ) {
            System.err.println("Could not insert the statement\n");
        }
        try {
            ResultSet rs = statement.executeQuery(query2);
            while (rs.next()) {
                return rs.getInt("id_track");
            }
        } catch (SQLException e) {
            System.err.println("Could not get id track from database\n"
            + e.getMessage());;
        } finally {
            closeStatement();
            closeConnection();
        }
        return null;
    }
}
