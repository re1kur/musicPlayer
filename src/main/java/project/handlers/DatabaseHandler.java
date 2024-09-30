package project.handlers;

import project.classes.Composition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
Класс-обработчик методов для работы с базой данных Postgresql
 */
public class DatabaseHandler {
    protected static Connection connection;
    protected static Statement statement;
    protected static final String user = "postgres";
    protected static final String password = "123456778";
    protected static final String url =
            "jdbc:postgresql://localhost:5432/musicPlayer";

    public static void createTables() {
        getConnection();
        try {
            executeUpdate("CREATE TABLE playlists (playlist_name TEXT NOT NULL);\n" +
                    "CREATE TABLE uploaded_tracks (id_track SERIAL PRIMARY KEY NOT NULL, uuid_track TEXT NOT NULL);");
            System.out.println("Tables created");
        } catch (SQLException _) {
            System.out.println("Tables exists.");
        }
    }

    /*
    Метод для коннекта с бд
     */
    private static void getConnection() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println("Could not connect to database\n" + e.getMessage());
        }
    }

    /*
    Метод для закрытия коннекта в целях экономии памяти
     */
    private static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Could not close the connection\n" + e.getMessage());
        }
    }

    /*
    Метод для закрытия statement в целях экономии памяти
     */
    private static void closeStatement() {
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Could not close the statement\n" + e.getMessage());
        }
    }

    /*
    Метод для отправки и последующего выполнения запроса в бд
     */
    public static void executeUpdate(String query) throws SQLException {
        getConnection();
        statement = connection.createStatement();
        statement.executeUpdate(query);
        closeStatement();
        closeConnection();
    }

    /*
    Метод для получения сета из колонок по запросу из бд
     */
    public static ResultSet getResultSet(String query) {
        getConnection();
        try {
            statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Could not get the result set\n" + e.getMessage());
        }
        return null;
    }

    /*
    Метод для редактирования колонки в бд
     */
    public static void editTrack(
            String table, int id, String name, String artists, String albums) {
        String query = "UPDATE " + table + " SET name = '" + name + "', artists = '"
                + artists + "', albums = '" + albums + "'"
                + "WHERE id = " + id;
        try {
            DatabaseHandler.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Could not edit the track\n" + e.getMessage());
        } finally {
            closeStatement();
            closeConnection();
        }
    }

    /*
    Метод для загрузки колонки в бд
     */
    public static void uploadTrack(
            String table, String name, String artists, String albums, String path) {
        int id = setUUID(path);
        String query = "INSERT INTO " + table + "(name, artists, albums, id_track) "
                + "VALUES (?, ?, ?, ?);";
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
            System.out.println("Could not insert the statement\n" + e.getMessage());
        } finally {
            closeStatement();
            closeConnection();
        }
    }

    /*
    Метод для генерации уникального айди для муз.дорожки и последующая его загрузка
    в файловое хранилище
     */
    private static Integer setUUID(String path) {
        String uniqueTrackName = FileStorageHandler.uploadTrack(path);
        getConnection();
        String query1 = "INSERT INTO uploaded_tracks (uuid_track) VALUES ('"
                + uniqueTrackName + "')";
        String query2 = "SELECT id_track FROM uploaded_tracks WHERE uuid_track = '"
                + uniqueTrackName + "'";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query1);
        } catch (SQLException e) {
            System.err.println("Could not insert the statement\n");
        }
        try {
            ResultSet rs = statement.executeQuery(query2);
            while (rs.next()) {
                return rs.getInt("id_track");
            }
        } catch (SQLException e) {
            System.err.println(
                    "Could not get id track from database\n" + e.getMessage());
        } finally {
            closeStatement();
            closeConnection();
        }
        return null;
    }

    /*
    Метод для удаления плейлиста
     */
    public static void deletePlaylist(String playlist) throws SQLException {
        String query1 = "DELETE FROM playlists WHERE playlist_name = ?";
        try (PreparedStatement ps1 = connection.prepareStatement(query1)) {
            ps1.setString(1, playlist);
            ps1.executeUpdate();
            System.out.println(playlist + " deleted from playlists table.");
        }
        String query2 = "SELECT p.id_track, u.uuid_track "
                + "FROM " + playlist + " p "
                + "JOIN uploaded_tracks u ON p.id_track = u.id_track";
        List<Integer> trackIds = new ArrayList<>();
        List<String> trackUuids = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query2)) {
            while (rs.next()) {
                trackIds.add(rs.getInt("id_track"));
                trackUuids.add(rs.getString("uuid_track"));
            }
            System.out.println(
                    playlist + " got a result set with id and uuid of tracks.");
        }
        String query3 = "DROP TABLE " + playlist;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query3);
            System.out.println("table " + playlist + " was dropped.");
        }
        if (!trackIds.isEmpty()) {
            StringBuilder query4 =
                    new StringBuilder("DELETE FROM uploaded_tracks WHERE id_track IN (");
            for (int i = 0; i < trackIds.size(); i++) {
                query4.append(trackIds.get(i));
                if (i < trackIds.size() - 1) {
                    query4.append(",");
                }
            }
            query4.append(");");
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query4.toString());
                System.out.println(
                        "rows of " + playlist + " was deleted from uploaded_tracks table.");
            } finally {
                for (String uuid : trackUuids) {
                    FileStorageHandler.deleteTrack(uuid);
                    System.out.println(
                            "file with uuid: " + uuid + " - deleted from file storage.");
                }
            }
        }
    }

    /*
    Метод для удаления трека
     */
    public static void deleteTrack(String playlist, Composition composition) {
        String query1 =
                "DELETE FROM " + playlist + " WHERE id = " + composition.getId() + ";";
        String query2 = "DELETE FROM uploaded_tracks WHERE uuid_track = '"
                + composition.getUuid() + "'";
        try {
            DatabaseHandler.executeUpdate(query1);
            DatabaseHandler.executeUpdate(query2);
        } catch (SQLException e) {
            System.err.println("Could not delete the row(s):\n" + e.getMessage());
        } finally {
            FileStorageHandler.deleteTrack(composition.getUuid());
            closeStatement();
            closeConnection();
        }
    }
}
