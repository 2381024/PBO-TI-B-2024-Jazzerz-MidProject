import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.*;

public class SistemPenyewaanJas {
    public static final String fileName = "users.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int pilihMenu;
        String pilihLogin;
        String username = "";
        String password;
        boolean login = false;
        boolean admin;
        boolean daftar;
        boolean check = true;

        System.out.print("Apakah anda seorang admin?(true,false): ");
        admin = scanner.nextBoolean();

        if (!admin) {
            // Login user
            do {
                pilihLogin = loginAtauDaftar();
            }
            while (pilihLogin == "");

            do {
                switch (pilihLogin) {
                    case "login": {
                        System.out.println("Selamat Datang Kembali!");
                        System.out.print("Masukkan Username: ");
                        username = scanner.nextLine();
                        System.out.print("Masukkan Password: ");
                        password = scanner.nextLine();
                        login = loginUser(username, password);

                        if (!login) {
                            check = noAccountCheck();
                        }

                        if (check) {
                            pilihLogin = "daftar";
                        }
                        break;

                    }

                    case "daftar": {
                        System.out.println("Halo, Selamat datang di Jazzerz");
                        System.out.print("Masukkan Username: ");
                        String newUsername = scanner.nextLine();
                        System.out.print("Masukkan Password: ");
                        String newPassword = scanner.nextLine();
                        daftar = daftarUser(newUsername, newPassword);
                        if (daftar) {
                            pilihLogin = "login";
                        }
                        break;
                    }

                    case "keluar":
                        return;
                }
            }
            while (!login);

            //-----------------------------------------------

            mainMenuUser(username);
            System.out.println("Masukkan pilihan anda: ");
            pilihMenu = scanner.nextInt();

            switch(pilihMenu){
                case 1:{
                    // Daftar Jas
                    // Arvel
                }
                case 2:{
                    // Sewa Jas & Masukkan keranjang dll
                    // Arthur

                }
                case 3:{
                    // Lihat Keranjang & Biaya Sewa & Checkout dimasukkan ke dalam riwayat penyewaan jas
                    // Arthur
                }
                case 4:{
                    // Pengembalian Jas & Mengganti dari riwayat penyewaan jas
                    // Andrew
                }
                case 5:{
                    // Riwayat Penyewaan Jas
                    // Andrew
                }
            }

        } else {
            // Manajemen admin (CRUD)
            // Andrew

            // Laporan Transaksi
            // Andrew
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // Login Page Code

    public static String loginAtauDaftar() {
        Scanner scanner = new Scanner(System.in);

        String loginAtauDaftar = "";
        System.out.println("Selamat Datang Di Jazzerz.");
        System.out.println("1.Login");
        System.out.println("2.Daftar");
        System.out.println("3.Keluar");
        System.out.print("Masukkan pilihan anda: ");

        int pilihAkun = scanner.nextInt();

        switch (pilihAkun) {
            case 1:
                loginAtauDaftar = "login";
                break;
            case 2:
                loginAtauDaftar = "daftar";
                break;
            case 3:
                loginAtauDaftar = "keluar";
                break;
            default:
                System.out.println("Masukkan pilihan yang benar");
        }
        return loginAtauDaftar;
    }


    public static boolean noAccountCheck() {
        Scanner scanner = new Scanner(System.in);
        boolean check;
        System.out.print("Belum ada akun? (true,false): ");
        check = scanner.nextBoolean();
        if (check) {
            System.out.println();
            return true;
        } else {
            return false;
        }
    }

    public static Boolean loginUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(username) && data[1].equals(password)) {
                    System.out.println("Login berhasil!");
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Username atau password salah.");
        return false;
    }

    public static Boolean daftarUser(String username, String password) {
        if (usernameCheck(username)) {
            System.out.println("Username sudah terdaftar.");
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(username + "," + password);
            writer.newLine();
            System.out.println("Registrasi berhasil, Silahkan Login");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean usernameCheck(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ----------------------------------------------------------------------------------------------------------
    // Main Menu User Code

    public static void mainMenuUser(String username) {
        System.out.println("Halo, " + username + ". Apa yang kamu mau lakukan hari ini?");
        System.out.println("1. Lihat daftar jas");
        System.out.println("2. Sewa jas");
        System.out.println("3. Lihat Keranjang jas");
        System.out.println("4. Pengembalian jas");
        System.out.println("5. Riwayat penyewaan jas");
    }
}