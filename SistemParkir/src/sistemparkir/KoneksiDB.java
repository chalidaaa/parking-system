package sistemparkir;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDB {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_sistemparkir";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                System.out.println("✅ Koneksi database berhasil.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC Driver tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Gagal konek ke database: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("🔌 Koneksi database ditutup.");
            } catch (SQLException e) {
                System.err.println("❌ Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }

    // Opsional: Tes koneksi
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("✅ Tes koneksi sukses!");
            closeConnection();
        }
    }
}