package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement psInsert;
    private static PreparedStatement psSelect;

    private class UserData {
        String login;
        String password;
        String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

    private List<UserData> users;

    public SimpleAuthService() {
        users = new ArrayList<>();
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        try {
            psSelect.setString(1, login);
            psSelect.setString(2, password);
            ResultSet rs = psSelect.executeQuery();
            if (rs.getString("login").equals(login) && rs.getString("pass").equals(password)) {
                return rs.getString("nick");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT login, nick FROM users");
            while (rs.next()) {
                if (rs.getString("login").equals(login) || rs.getString("nick").equals(nickname))
                    return false;
            }
            psInsert.setString(1, login);
            psInsert.setString(2, password);
            psInsert.setString(3, nickname);
            psInsert.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:server/main.db");
        stmt = connection.createStatement();
    }

    public static void disconnect() throws SQLException {
        connection.close();
        stmt.close();
    }

    public static void prepareAllStatements() throws SQLException {
        psSelect = connection.prepareStatement("SELECT * FROM users WHERE login = ? AND pass = ?;");
        psInsert = connection.prepareStatement("INSERT INTO users (login, pass, nick) VALUES (?, ?, ?);");
    }

}
