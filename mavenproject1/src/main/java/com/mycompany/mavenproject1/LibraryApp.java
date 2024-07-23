/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mavenproject1;

/**
 *
 * @author YAYASAN IPI MALANG
 */
import java.util.Scanner;
import java.sql.*;
public class LibraryApp {
    private static final String DB_URL = "jdbc:sqlite:library.db";

    public static void main(String[] args) {
        createTableIfNotExists();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Tambah Buku");
            System.out.println("2. Lihat Daftar Buku");
            System.out.println("3. Hapus Buku");
            System.out.println("4. Keluar");
            System.out.print("Pilih opsi: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // membersihkan buffer
            switch (choice) {
                case 1:
                    tambahBuku(scanner);
                    break;
                case 2:
                    lihatDaftarBuku();
                    break;
                case 3:
                    hapusBuku(scanner);
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opsi tidak valid. Coba lagi.");
            }
        }
    }

    private static void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "title TEXT NOT NULL, " +
                     "author TEXT NOT NULL, " +
                     "year INTEGER NOT NULL)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void tambahBuku(Scanner scanner) {
        System.out.print("Masukkan judul buku: ");
        String title = scanner.nextLine();
        System.out.print("Masukkan nama penulis: ");
        String author = scanner.nextLine();
        System.out.print("Masukkan tahun terbit: ");
        int year = scanner.nextInt();
        scanner.nextLine();  // membersihkan buffer

        String sql = "INSERT INTO books(title, author, year) VALUES(?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, year);
            pstmt.executeUpdate();
            System.out.println("Buku berhasil ditambahkan.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void lihatDaftarBuku() {
        String sql = "SELECT id, title, author, year FROM books ORDER BY year DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Judul: " + rs.getString("title"));
                System.out.println("Penulis: " + rs.getString("author"));
                System.out.println("Tahun Terbit: " + rs.getInt("year"));
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void hapusBuku(Scanner scanner) {
        System.out.print("Masukkan ID buku yang akan dihapus: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // membersihkan buffer

        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Buku berhasil dihapus.");
            } else {
                System.out.println("Buku dengan ID tersebut tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}


   