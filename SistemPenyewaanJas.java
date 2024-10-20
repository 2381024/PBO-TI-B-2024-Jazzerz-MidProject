import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SistemPenyewaanJas {
    public static final String fileName = "users.txt";
    public static ArrayList<Jas> keranjang = new ArrayList<>();
    public static HashMap<String, Integer> daftarJas = new HashMap<>();
    public static ArrayList<Jas> riwayatPenyewaan = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int pilihMenu;
        String pilihLogin;
        String username = "";
        String password;
        boolean login = false;
        int role;
        boolean daftar;
        boolean check = true;
        daftarJas.put("Jas Hitam", 100000);
        daftarJas.put("Jas Biru", 120000);
        daftarJas.put("Jas Abu-abu", 110000);

        while(true)
        {
            System.out.println("Penyewaan Online Jas Jazzerz");
            System.out.println("1. Pelanggan");
            System.out.println("2. Admin");
            System.out.println("3. Keluar");
            System.out.print("Masukkan Pilihan Anda: ");
            role = scanner.nextInt();
            scanner.nextLine();

            if (role == 1) {
                // Login user
                do {
                    pilihLogin = loginAtauDaftar();
                }
                while (pilihLogin.equals(""));

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
                } while (!login);

                //-----------------------------------------------

                boolean kembaliKeMenu = false;
                bacaRiwayatDariFile(username);  // Membaca riwayat penyewaan dari file saat login
                while (!kembaliKeMenu) {
                    mainMenuUser(username);
                    System.out.print("Masukkan pilihan anda: ");
                    pilihMenu = scanner.nextInt();
                    scanner.nextLine();

                    switch (pilihMenu) {
                        case 1: {
                            lihatDaftarJas(scanner);
                            break;
                        }
                        case 2: {
                            sewaJas(scanner, username);
                            break;
                        }
                        case 3: {
                            lihatKeranjang(scanner);
                            break;
                        }
                        case 4: {
                            pengembalianJas(scanner, username);
                            break;
                        }
                        case 5: {
                            riwayatPenyewaan(scanner, username);
                            break;
                        }
                        case 6: {
                            checkout(scanner, username);
                            break;
                        }
                        case 7: {
                            kembaliKeMenu = true;
                            break;
                        }
                        default:
                            System.out.println("Pilihan tidak valid.");
                    }
                }
            }
            else if (role == 2) {
                // Manajemen admin (CRUD) dan Laporan Transaksi
                boolean kembaliKeMenu = false;
                while (!kembaliKeMenu) {
                    System.out.println("1. Manajemen Stok Jas");
                    System.out.println("2. Laporan Transaksi");
                    System.out.println("3. Kembali ke menu utama");
                    System.out.print("Masukkan Pilihan Anda: ");
                    int pilihAdmin = scanner.nextInt();
                    scanner.nextLine();  // Konsumsi newline setelah nextInt()

                    switch (pilihAdmin) {
                        case 1:
                            manajemenStokJas(scanner);
                            break;
                        case 2:
                            laporanTransaksi(scanner);
                            break;
                        case 3:
                            kembaliKeMenu = true;
                            break;
                        default:
                            System.out.println("Pilihan tidak valid.");
                    }
                }
            }
            else if (role == 3){
                return;
            }

            else {
                System.out.println("Pilihan Tidak Valid!");
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // Kode Login dan Pendaftaran

    public static String loginAtauDaftar() {
        Scanner scanner = new Scanner(System.in);

        String loginAtauDaftar = "";
        System.out.println("Selamat Datang Di Jazzerz.");
        System.out.println("1.Login");
        System.out.println("2.Daftar");
        System.out.println("3.Kembali ke menu utama");
        System.out.print("Masukkan pilihan anda: ");

        int pilihAkun = scanner.nextInt();
        scanner.nextLine();  // Konsumsi newline setelah nextInt()

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
        scanner.nextLine();  // Konsumsi newline setelah nextBoolean()
        return check;
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
    // Menu Utama Pengguna

    public static void mainMenuUser(String username) {
        System.out.println("Halo, " + username + ". Apa yang kamu mau lakukan hari ini?");
        System.out.println("1. Lihat daftar jas");
        System.out.println("2. Sewa jas");
        System.out.println("3. Lihat Keranjang jas");
        System.out.println("4. Pengembalian jas");
        System.out.println("5. Riwayat penyewaan jas");
        System.out.println("6. Checkout");
        System.out.println("7. Kembali ke menu utama");
    }


    // =================================================================================================================================================================================
    // Kelas Jas dideklarasikan sebagai static

    static class Jas {
        private String nama;
        private int harga;
        private int durasi;

        public Jas(String nama, int harga, int durasi) {
            this.nama = nama;
            this.harga = harga;
            this.durasi = durasi;
        }

        public String getNama() {
            return nama;
        }

        public int getHarga() {
            return harga;
        }

        public int getDurasi() {
            return durasi;
        }
    }

    // ----------------------------------------------------------------------------------------------------------
    // Fitur Sewa Jas

    public static void sewaJas(Scanner scanner, String username) {
        boolean kembaliKeMenu = false;

        while (!kembaliKeMenu) {
            System.out.println("Pilih jas yang ingin disewa:");
            int i = 1;
            for (String namaJas : daftarJas.keySet()) {
                System.out.println(i + ". " + namaJas + " - Rp " + daftarJas.get(namaJas) + "/hari");
                i++;
            }
            System.out.println(i + ". Kembali ke menu utama");
            System.out.print("Masukkan pilihan (angka): ");
            int pilihanJas = scanner.nextInt();
            scanner.nextLine();

            if (pilihanJas == i) {
                kembaliKeMenu = true;
            } else if (pilihanJas > 0 && pilihanJas < i) {
                System.out.print("Masukkan durasi sewa (hari): ");
                int durasi = scanner.nextInt();
                scanner.nextLine();

                String namaJas = (String) daftarJas.keySet().toArray()[pilihanJas - 1];
                int harga = daftarJas.get(namaJas);
                Jas jas = new Jas(namaJas, harga, durasi);
                keranjang.add(jas);
                riwayatPenyewaan.add(jas);
                simpanRiwayatKeFile(username);
                System.out.println("Jas berhasil ditambahkan ke keranjang.");
            } else {
                System.out.println("Pilihan tidak valid, coba lagi.");
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------------
    // Fitur Lihat Keranjang

    public static void lihatKeranjang(Scanner scanner) {
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang kosong.");
        } else {
            int totalBiaya = 0;
            System.out.println("Keranjang Anda:");
            for (Jas jas : keranjang) {
                int biaya = jas.getHarga() * jas.getDurasi();
                totalBiaya += biaya;
                System.out.println(jas.getNama() + " - Rp " + jas.getHarga() + "/hari x " + jas.getDurasi() + " hari = Rp " + biaya);
            }
            System.out.println("Total biaya sewa: Rp " + totalBiaya);
        }
    }

    // ----------------------------------------------------------------------------------------------------------
    // Fitur Checkout

    public static void checkout(Scanner scanner, String username) {
        boolean kembaliKeMenu = false;

        while (!kembaliKeMenu) {
            lihatKeranjang(scanner);
            System.out.println("Pilih metode pembayaran:");
            System.out.println("1. E-Wallet");
            System.out.println("2. Transfer Bank");
            System.out.println("3. Cash");
            System.out.println("4. Credit Card");
            System.out.println("5. Kembali ke menu utama");
            System.out.print("Masukkan pilihan anda: ");
            int metodePembayaran = scanner.nextInt();
            scanner.nextLine();  // Konsumsi newline setelah nextInt()

            if (metodePembayaran == 5) {
                kembaliKeMenu = true;
            } else {
                switch (metodePembayaran) {
                    case 1:
                        System.out.println("Pembayaran menggunakan E-Wallet diproses...");
                        break;
                    case 2:
                        System.out.println("Pembayaran menggunakan Transfer Bank diproses...");
                        break;
                    case 3:
                        System.out.println("Pembayaran menggunakan Cash diproses...");
                        break;
                    case 4:
                        System.out.println("Pembayaran menggunakan Credit Card diproses...");
                        break;
                    default:
                        System.out.println("Metode pembayaran tidak valid.");
                        return;
                }
                System.out.println("Pembayaran sukses. Terima kasih sudah menyewa di Jazzerz!");
                keranjang.clear();  // Kosongkan keranjang setelah pembayaran
                kembaliKeMenu = true;
            }
        }
    }

    // =================================================================================================================================================================================
    // Fitur Riwayat Penyewaan Jas

    public static void riwayatPenyewaan(Scanner scanner, String username) {
        boolean kembaliKeMenu = false;

        while (!kembaliKeMenu) {
            if (riwayatPenyewaan.isEmpty()) {
                System.out.println("Belum ada riwayat penyewaan.");
            } else {
                System.out.println("Riwayat Penyewaan:");
                for (Jas jas : riwayatPenyewaan) {
                    System.out.println(jas.getNama() + " - Rp " + jas.getHarga() + "/hari x " + jas.getDurasi() + " hari");
                }
            }
            System.out.println("1. Kembali ke menu utama");
            System.out.print("Masukkan pilihan: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();  // Konsumsi newline setelah nextInt()

            if (pilihan == 1) {
                kembaliKeMenu = true;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------------
    // Fitur Pengembalian Jas

    public static void pengembalianJas(Scanner scanner, String username) {
        boolean kembaliKeMenu = false;

        while (!kembaliKeMenu) {
            if (riwayatPenyewaan.isEmpty()) {
                System.out.println("Tidak ada jas untuk dikembalikan.");
            } else {
                System.out.println("Pilih jas yang akan dikembalikan:");
                for (int i = 0; i < riwayatPenyewaan.size(); i++) {
                    Jas jas = riwayatPenyewaan.get(i);
                    System.out.println((i + 1) + ". " + jas.getNama() + " - Durasi sewa: " + jas.getDurasi() + " hari");
                }
                System.out.println(riwayatPenyewaan.size() + 1 + ". Kembali ke menu utama");
                System.out.print("Masukkan pilihan anda: ");
                int pilihan = scanner.nextInt();
                scanner.nextLine();  // Konsumsi newline

                if (pilihan == riwayatPenyewaan.size() + 1) {
                    kembaliKeMenu = true;
                } else if (pilihan > 0 && pilihan <= riwayatPenyewaan.size()) {
                    Jas dikembalikan = riwayatPenyewaan.remove(pilihan - 1);
                    System.out.println("Jas " + dikembalikan.getNama() + " telah dikembalikan.");
                    simpanRiwayatKeFile(username);  // Update riwayat di file setelah pengembalian
                } else {
                    System.out.println("Pilihan tidak valid.");
                }
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------------
    // Fitur Manajemen Stok Jas (Admin)

    public static void manajemenStokJas(Scanner scanner) {
        boolean kembaliKeMenu = false;

        while (!kembaliKeMenu) {
            System.out.println("Manajemen Stok Jas:");
            System.out.println("1. Tambah Jas");
            System.out.println("2. Hapus Jas");
            System.out.println("3. Lihat Stok Jas");
            System.out.println("4. Kembali ke menu utama");
            System.out.print("Masukkan pilihan anda: ");

            int pilihan = scanner.nextInt();
            scanner.nextLine();  // Konsumsi newline

            switch (pilihan) {
                case 1:
                    System.out.print("Masukkan nama jas: ");
                    String namaJas = scanner.nextLine();
                    System.out.print("Masukkan harga sewa per hari: ");
                    int hargaJas = scanner.nextInt();
                    daftarJas.put(namaJas, hargaJas);
                    System.out.println("Jas berhasil ditambahkan.");
                    break;

                case 2:
                    System.out.print("Masukkan nama jas yang akan dihapus: ");
                    String hapusJas = scanner.nextLine();
                    daftarJas.remove(hapusJas);
                    System.out.println("Jas berhasil dihapus.");
                    break;

                case 3:
                    lihatDaftarJas(scanner);
                    break;

                case 4:
                    kembaliKeMenu = true;
                    break;

                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------------
    // Fitur Laporan Transaksi (Admin)

    public static void laporanTransaksi(Scanner scanner) {
        boolean kembaliKeMenu = false;

        while (!kembaliKeMenu) {
            System.out.println("Laporan Transaksi:");
            bacaSemuaTransaksi();  // Baca semua transaksi dari file untuk laporan admin

            if (riwayatPenyewaan.isEmpty()) {
                System.out.println("Belum ada transaksi.");
            } else {
                int totalPendapatan = 0;
                for (Jas jas : riwayatPenyewaan) {
                    int biaya = jas.getHarga() * jas.getDurasi();
                    totalPendapatan += biaya;
                    System.out.println(jas.getNama() + " - Rp " + jas.getHarga() + "/hari x " + jas.getDurasi() + " hari = Rp " + biaya);
                }
                System.out.println("Total pendapatan: Rp " + totalPendapatan);
            }
            System.out.println("1. Kembali ke menu utama");
            System.out.print("Masukkan pilihan: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();  // Konsumsi newline setelah nextInt()

            if (pilihan == 1) {
                kembaliKeMenu = true;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------------
    // Fitur Lihat Daftar Jas

    public static void lihatDaftarJas(Scanner scanner) {
        boolean kembaliKeMenu = false;

        while (!kembaliKeMenu) {
            System.out.println("Daftar Jas yang tersedia:");
            for (String namaJas : daftarJas.keySet()) {
                System.out.println(namaJas + " - Rp " + daftarJas.get(namaJas) + "/hari");
            }
            System.out.println("1. Kembali ke menu utama");
            System.out.print("Masukkan pilihan: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();  // Konsumsi newline setelah nextInt()

            if (pilihan == 1) {
                kembaliKeMenu = true;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // Fungsi untuk membaca semua transaksi (admin)
    public static void bacaSemuaTransaksi() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            riwayatPenyewaan.clear();  // Bersihkan riwayat sebelumnya untuk menghindari duplikasi
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    // Data format: username, namaJas, harga, durasi
                    Jas jas = new Jas(data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                    riwayatPenyewaan.add(jas);  // Tambahkan semua transaksi ke riwayatPenyewaan
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk menyimpan riwayat penyewaan ke file
    public static void simpanRiwayatKeFile(String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (Jas jas : riwayatPenyewaan) {
                writer.write(username + "," + jas.getNama() + "," + jas.getHarga() + "," + jas.getDurasi());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void bacaRiwayatDariFile(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            riwayatPenyewaan.clear();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(username) && data.length == 4) {
                    Jas jas = new Jas(data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                    riwayatPenyewaan.add(jas);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
