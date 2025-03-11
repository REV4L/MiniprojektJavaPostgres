package com.example;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Database {
    public static String url = "jdbc:postgresql://ep-lively-cherry-a26b94ho-pooler.eu-central-1.aws.neon.tech/neondb?sslmode=require";
    public static String user = "neondb_owner";
    public static String password = "npg_D3d4jRMXJbfS"; // pls dont hack me

    public static Connection conn = null;

    public static int organizatorId = -1;
    public static Organizator organizator = null;

    public static boolean loggedIn() {
        return organizator != null;
    }

    public static void connect() throws SQLException {
        conn = DriverManager.getConnection(url, user, password);

        // String sql = "INSERT INTO dogodek_log(cas, data) VALUES(NOW(), ?)";
        PreparedStatement s = conn.prepareStatement("INSERT INTO dogodek_log(cas, data) VALUES(NOW(), ?)");
        s.setString(1, "John Doe");

        int rowsAffected = s.executeUpdate();
    }

    public static boolean login(String email, String password) {
        String loginFunction = "{? = call login(?, ?)}"; // CallableStatement for the login function
        int userId = -1; // Default to -1 (invalid login)

        try {
            // Ensure the connection is established
            if (conn == null || conn.isClosed()) {
                connect();
            }

            // Create a CallableStatement to call the login function
            try (CallableStatement stmt = conn.prepareCall(loginFunction)) {
                // Set input parameters
                stmt.setString(2, email);
                stmt.setString(3, password);

                // Register the output parameter (userid)
                stmt.registerOutParameter(1, Types.INTEGER);

                // Execute the function
                stmt.execute();

                // Retrieve the result (userid)
                userId = stmt.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error during login query execution.");
        }

        organizatorId = userId;
        organizator = getOrganizator(userId);
        return userId >= 0;
    }

    public static void logOut() {
        organizatorId = -1;
        organizator = null;
    }

    public static void getOrganizator() {
        if (organizatorId >= 0)
            organizator = getOrganizator(organizatorId);
    }

    public static Organizator getOrganizator(int userId) {
        Organizator organizator = null;
        String query = "SELECT * FROM getOrganizers(?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    organizator = new Organizator(
                            rs.getInt("id"),
                            rs.getString("ime"),
                            rs.getString("opis"),
                            rs.getString("email"),
                            rs.getString("pasw"),
                            rs.getString("telefon"),
                            rs.getString("naslov"),
                            rs.getInt("kraj_id"),
                            rs.getInt("settings_id"),
                            rs.getInt("st_dogodkov"));
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return organizator;
    }

    public static void updateOrganizator(int id, String ime, String opis, String email, String telefon,
            String naslov, int krajId) {
        String query = "SELECT update_organizator(?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id); // p_id
            stmt.setString(2, ime); // p_ime
            stmt.setString(3, opis); // p_ime
            stmt.setString(4, email); // p_email
            stmt.setString(5, telefon); // p_telefon
            stmt.setString(6, naslov); // p_naslov
            stmt.setInt(7, krajId); // p_naslov

            // Execute the update
            stmt.executeUpdate();
            // conn.commit();
            System.out.println("Organizator updated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Kraj> getKraji() {
        ObservableList<Kraj> krajiList = FXCollections.observableArrayList();
        String query = "SELECT * FROM getKraji()";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                krajiList.add(new Kraj(
                        rs.getInt("id"),
                        rs.getString("ime"),
                        rs.getString("postna"),
                        rs.getString("vel_uporabnik")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return krajiList;
    }
}