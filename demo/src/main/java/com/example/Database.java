package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    public static String url = "jdbc:postgresql://ep-lively-cherry-a26b94ho-pooler.eu-central-1.aws.neon.tech/neondb?sslmode=require";
    public static String user = "neondb_owner";
    public static String password = "npg_D3d4jRMXJbfS"; //pls dont hack me

    public static Connection conn = null;

    public static void connect() throws SQLException {
        conn = DriverManager.getConnection(url, user, password);

        // String sql = "INSERT INTO dogodek_log(cas, data) VALUES(NOW(), ?)";
        PreparedStatement s = conn.prepareStatement("INSERT INTO dogodek_log(cas, data) VALUES(NOW(), ?)");
        s.setString(1, "John Doe");

        int rowsAffected = s.executeUpdate();
    }
}