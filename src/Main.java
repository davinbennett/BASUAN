import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {
    private final Connect connect = Connect.getInstance();
    public Scanner sc = new Scanner(System.in);
    public void cls(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public void registerMenu() throws SQLException {
        String nama = "", kodeAkses = "", verifyKode = "", alamat = "";

        // VALIDATE REGISTER
        System.out.println("========================================================================================");
        System.out.println("|                                    R E G I S T E R                                   |");
        System.out.println("========================================================================================");
        System.out.println("                           Daftarkan diri anda terlebih dahulu");

        // nama
        do {
            nama = "";
            System.out.print("\nMasukkan Username [minimal 8 karakter alphabeth, huruf kecil semua, tanpa spasi, dan tanpa angka]: ");
            nama = sc.nextLine();
        }while (nama.length() < 8 || !nama.matches("[a-zA-Z]+") || nama.matches(".*\\d.*") || nama.matches(".*[A-Z].*"));

        // kode akses
        do {
            verifyKode = "";
            do {
                kodeAkses = "";
                System.out.print("Masukkan Kode Akses [terdiri dari 6 digit alphanumerik, tanpa spasi, huruf kecil semua, dan harus mengandung angka]: ");
                kodeAkses = sc.nextLine();
            } while (kodeAkses.length() != 6 || !kodeAkses.matches("[a-z0-9]+") || kodeAkses.contains(" "));
            System.out.print("Masukkan kembali Kode Akses: ");
            verifyKode = sc.nextLine();
        }while (!verifyKode.equals(kodeAkses));

        System.out.println("\nKlik Enter untuk melanjutkan proses registrasi");
        sc.nextLine();

        cls();
        System.out.println("========================================================================================");
        System.out.println("|                                    R E G I S T E R                                    |");
        System.out.println("========================================================================================");
        System.out.println("                Lengkapi biodata anda untuk melanjutkan proses registrasi\n");

        System.out.print("Masukkan Alamat: ");
        alamat = sc.nextLine();

        // masukin umur
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        Date sqlDate = null;
        boolean formatBenar = false;
        java.util.Date tanggalLahir = null;
        while (!formatBenar) {
            System.out.print("Masukkan tanggal lahir [yyyy-mm-dd]: ");
            String inputTanggal = sc.next();

            try {
                java.util.Date date = dateFormat.parse(inputTanggal);
                tanggalLahir = new java.util.Date(date.getTime());

//              Validasi umur minimal 17
                String dateTemp = dateFormat.format(tanggalLahir);
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDateTemp = LocalDate.parse(dateTemp, dateFormatter);

                Period selisih = Period.between(localDateTemp, LocalDate.now());
                if (selisih.getYears() < 17) {
                    System.out.println("\nMaaf, umur Anda belum cukup untuk membuka rekening. Minimal 17 tahun untuk membuka rekening.");
                    System.out.println("Silakan kembali apabila umur Anda sudah mencukupi.");
                    System.out.println("\nTekan Enter untuk kembali ke Menu Utama. Terima kasih.");
                    sc.nextLine(); sc.nextLine();
                    return;
                }
                sqlDate = Date.valueOf(localDateTemp);
                formatBenar = true;

            } catch (ParseException e) {
                System.out.println("Format tanggal yang Anda masukkan salah. Gunakan format yyyy/mm/dd.");
                System.out.println("Silakan coba lagi.\n");
                sc.nextLine();
            }
        }

        // generate rekening
        int tes;
        SecureRandom rand = new SecureRandom();
        do{
            tes = rand.nextInt();
        }while (tes < 1000000000);
        String rekening = "";
        rekening = String.valueOf(tes);

//      input data ke database
        Nasabah nasabah = new Nasabah(nama, alamat, sqlDate);
        Akun akun = new Akun(rekening,0.00, kodeAkses);
        nasabah.insertCustomer(nama, alamat, sqlDate);

        // add nasabahID di tabel Akun
        String nasabahID = "";
        String query = "SELECT NasabahID FROM nasabah";
        connect.rs = connect.execQuery(query);
        while (connect.rs.next()){
            nasabahID = connect.rs.getString("NasabahID");
        }

        akun.insertAkun(nasabahID, rekening, kodeAkses);

        System.out.println("\nAkun berhasil dibuat.\nSilahkan login kembali untuk akun yang telah anda buat.\n\nKlik Enter untuk lanjut Menu Utama.");
        sc.nextLine(); sc.nextLine();
    }

    public void appMenu(String namaOld, String kodeOld) throws SQLException {
        String nameRefresh = namaOld, kodeRefresh = kodeOld;
        int choice = 0, flag;
        while(true) {
            choice = 0;
            cls();
            System.out.println("========================================================================================");
            System.out.println("|                                     Hi " + nameRefresh + ",                               |");
            System.out.println("|                           Selamat Datang di Basuan M-Banking                          |");
            System.out.println("========================================================================================");
            System.out.println("\n1. Cek Profile\n2. Transaksi\n3. Keluar");
            do{
                while (true){
                    try {
                        System.out.print("> ");
                        choice = sc.nextInt(); sc.nextLine();
                        break;
                    } catch (InputMismatchException e) {
                        sc.nextLine();
                        continue;
                    }
                }
            }while (choice < 1 || choice > 3);

            switch (choice) {
                case 1:
                    boolean repeat = false;
                    do{
                        cls();
                        String namaTemp = "", alamatTemp = "", rekeningTemp = "", kodeAksesTemp = "";
                        double saldoTemp = 0.0;
                        Date dobTemp = null;
                        int choose = 0;
                        System.out.println("========================================================================================");
                        System.out.println("|                                     P R O F I L E                                     |");
                        System.out.println("========================================================================================");
                        String query = "SELECT Nama, Alamat, DOB, Rekening, KodeAkses, Saldo " +
                                "FROM nasabah n JOIN akun a ON n.NasabahID = a.NasabahID";

                        connect.rs = connect.execQuery(query);
                        while (connect.rs.next()){
                            namaTemp = connect.rs.getString("Nama");
                            alamatTemp = connect.rs.getString("Alamat");
                            dobTemp = connect.rs.getDate("DOB");
                            rekeningTemp = connect.rs.getString("Rekening");
                            kodeAksesTemp = connect.rs.getString("KodeAkses");
                            saldoTemp = connect.rs.getDouble("Saldo");
                            if(namaTemp.equals(namaOld) && kodeAksesTemp.equals(kodeOld))
                                break;
                        }
                        System.out.printf("| Username: %s\n| Kode Akses: %s\n| Saldo: %.2f\n| Alamat: %s\n| Tanggal lahir: %s\n| No. Rekening: %s\n", namaTemp, kodeAksesTemp, saldoTemp, alamatTemp, dobTemp, rekeningTemp);
                        System.out.println("========================================================================================");
                        System.out.println("Ingin update data?\n1. Ya\n2. Tidak");
                        do {
                            while (true){
                                try {
                                    System.out.print("> ");
                                    choose = sc.nextInt(); sc.nextLine();
                                    break;
                                } catch (InputMismatchException e) {
                                    sc.nextLine();
                                    continue;
                                }
                            }
                        }while(choose < 1 || choose > 2);

                        // kalau mau update
                        if(choose == 1){
                            Nasabah nasabah = new Nasabah("", "",null);
                            Akun akun = new Akun("", 0.0,null);
                            int pilih = 0;
                            System.out.print("\nPilih data yang ingin diupdate:\n1. Username\n2. Kode Akses\n3. Alamat\n4. Tanggal Lahir\n5. Kembali\n");
                            do{
                                while (true){
                                    try {
                                        System.out.print("> ");
                                        pilih = sc.nextInt(); sc.nextLine();
                                        break;
                                    } catch (InputMismatchException e) {
                                        sc.nextLine();
                                        continue;
                                    }
                                }
                            }while (pilih < 1 || pilih > 5);
                            switch (pilih){
                                case 1:
                                    String namaNew;
                                    do {
                                        namaNew = "";
                                        System.out.print("Masukkan Username baru [minimal 8 karakter alphabeth, huruf kecil semua, tanpa spasi, dan tanpa angka]: ");
                                        namaNew = sc.nextLine();
                                    }while (namaNew.length() < 8 || !namaNew.matches("[a-zA-Z]+") || namaNew.matches(".*\\d.*") || namaNew.matches(".*[A-Z].*"));

                                    // insert ke db
                                    nasabah.updateNama(namaNew, nameRefresh, kodeRefresh);
                                    nameRefresh = namaNew;
                                    System.out.println("\nUsername berhasil diupdate. Klik Enter untuk kembali ke Profile");
                                    sc.nextLine();
                                    break;
                                case 2:
                                    String kodeNew;
                                    do {
                                        kodeNew = "";
                                        System.out.print("Masukkan Kode Akses [terdiri dari 6 digit alphanumerik, tanpa spasi, huruf kecil semua, dan harus mengandung angka]: ");
                                        kodeNew = sc.nextLine();
                                    } while (kodeNew.length() != 6 || !kodeNew.matches("[a-z0-9]+") || kodeNew.contains(" "));

                                    // insert ke db
                                    akun.updateKode(kodeNew, kodeRefresh, nameRefresh);
                                    kodeRefresh = kodeNew;
                                    System.out.println("\nKode Akses berhasil diupdate. Klik Enter untuk kembali ke Profile");
                                    sc.nextLine();
                                    break;
                                case 3:
                                    String alamatNew;
                                    System.out.print("Masukkan Alamat: ");
                                    alamatNew = sc.nextLine();

                                    // insert ke db
                                    nasabah.updateAlamat(alamatNew, nameRefresh, kodeRefresh);
                                    System.out.println("\nAlamat berhasil diupdate. Klik Enter untuk kembali ke Profile");
                                    sc.nextLine();
                                    break;
                                case 4:
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    dateFormat.setLenient(false);
                                    Date dobNew = null;
                                    boolean formatBenar = false;
                                    java.util.Date tanggalLahir = null;
                                    Period selisih = null;
                                    while (!formatBenar) {
                                        System.out.print("Masukkan tanggal lahir [yyyy-mm-dd]: ");
                                        String inputTanggal = sc.next();

                                        try {
                                            java.util.Date date = dateFormat.parse(inputTanggal);
                                            tanggalLahir = new java.util.Date(date.getTime());

                                            // Validasi umur minimal 17
                                            String dateTemp = dateFormat.format(tanggalLahir);
                                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                            LocalDate localDateTemp = LocalDate.parse(dateTemp, dateFormatter);

                                            selisih = Period.between(localDateTemp, LocalDate.now());
                                            if (selisih.getYears() < 17) {
                                                System.out.println("Maaf, umur Anda belum cukup untuk membuka rekening. Minimal 17 tahun untuk membuka rekening.");
                                                System.out.println("Silakan kembali apabila umur Anda sudah mencukupi.");
                                                System.out.println("Tekan Enter untuk kembali ke Menu Utama. Terima kasih.");
                                                sc.nextLine();sc.nextLine();
                                                break;
                                            }
                                            dobNew = Date.valueOf(localDateTemp);
                                            formatBenar = true;
                                        } catch (ParseException e) {
                                            System.out.println("Format tanggal yang Anda masukkan salah. Gunakan format yyyy/mm/dd.");
                                            System.out.println("Silakan coba lagi.");
                                            sc.nextLine();
                                        }
                                    }
                                    // insert ke db
                                    if (selisih.getYears() >= 17){
                                        nasabah.updateDOB(dobNew, nameRefresh, kodeRefresh);
                                        System.out.println("\nUsername berhasil diupdate. Klik Enter untuk kembali ke Profile");
                                        sc.nextLine();
                                    }
                                    break;
                                case 5:
                                    repeat = true;
                                    break;
                            }
                        }
                    }while (repeat);
                    break;
                case 3:
                    return;
            }
        }
    }

    public void loginMenu() throws SQLException{
        String kode = "", name = "", alamat = "";
        Date dob = null;
        int flag, count = 1;
        String nameTemp = "", kodeTemp = "";
        System.out.println("========================================================================================");
        System.out.println("|                                      L O G I N                                        |");
        System.out.println("========================================================================================");
        do {
            flag = -1;
            System.out.print("\nMasukkan Username: ");
            name = sc.nextLine();

            System.out.print("Masukkan Kode Akses: ");
            kode = sc.nextLine();

            // cek username & kode
            String query = "SELECT Nama, KodeAkses, Alamat, DOB " +
                    "FROM nasabah n JOIN akun a ON n.NasabahID = a.NasabahID";

            connect.rs = connect.execQuery(query);
            while (connect.rs.next()){
                nameTemp = connect.rs.getString("Nama");
                kodeTemp = connect.rs.getString("KodeAkses");
                String alamatTemp = connect.rs.getString("Alamat");
                Date date = connect.rs.getDate("DOB");
                if(nameTemp.equals(name) && kodeTemp.equals(kode)){
                    flag = 1;
                    break;
                }
            }

            if(flag == -1){
                if(nameTemp.equals(name) && !kodeTemp.equals(kode))
                    System.out.printf("Kode Akses salah. Kesempatan (%d / 3).\n\n", count);
                else{
                    int pilihan = 0;
                    System.out.println("Username tidak ditemukan.\n");
                    System.out.println("Lanjutkan login ?\n1. Lanjut\n2. Tidak");
                    do{
                        while (true){
                            try{
                                System.out.print("> ");
                                pilihan = sc.nextInt(); sc.nextLine();
                                break;
                            }catch (InputMismatchException e){
                                sc.nextLine();
                                continue;
                            }
                        }
                    }while(pilihan < 1 || pilihan > 2);
                    if(pilihan == 2)
                        return;
                    continue;
                }
                count++;
                if(count > 3){
                    System.out.println("Lupa kata sandi?");
                    return;
                }

            }
        }while(flag == -1);

        System.out.println("Login berhasil.\nKlik Enter untuk melanjutkan aplikasi.");
        sc.nextLine();
        cls();
        appMenu(name, kode);
    }

    public Main() throws SQLException {


        int choice;

        while(true) {
            cls();
            choice = 0;
            System.out.println("========================================================================================");
            System.out.println("|                                 M A I N   M E N U                                     |");
            System.out.println("========================================================================================");

            System.out.println("1. Login\n2. Register\n3. Exit");
            do{
                while (true) {
                    try{
                        System.out.print("> ");
                        choice = sc.nextInt(); sc.nextLine();
                        break;
                    }catch (InputMismatchException e){
                        sc.nextLine();
                        continue;
                    }
                }
            }while(choice < 1 || choice > 3);

            switch(choice) {
                case 1:
                    cls();
                    loginMenu();
                    break;
                case 2:
                    cls();
                    registerMenu();
                    break;
                case 3:
                    cls();
                    System.out.println("Terimakasih telah menggunakan Basuan Mobile");
                    System.exit(0);
            }

        }
    }

    public static void main(String[] args) throws SQLException {
        new Main();
    }
}