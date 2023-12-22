import java.security.SecureRandom;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Stream;


public class Main {
    private final Connect connect = Connect.getInstance();
    public Scanner sc = new Scanner(System.in);
    public void cls(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public void registerMenu() throws SQLException {
        String nama = "", kodeAkses = "", verifyKode = "", alamat = "", password = "", query = "";
        int cekNama = -1;
        // VALIDATE REGISTER
        System.out.println("========================================================================================");
        System.out.println("|                                    R E G I S T E R                                   |");
        System.out.println("========================================================================================");
        System.out.println("                           Daftarkan diri anda terlebih dahulu");

        // nama
        do {
            query = "";
            String namaTemp = "";
            cekNama = -1;
            System.out.print("\nMasukkan Username [minimal 8 karakter alphabeth, huruf kecil semua, tanpa spasi, dan tanpa angka]: ");
            nama = sc.nextLine();

            // cek nama kalo udh ada
            query = "SELECT Nama FROM Nasabah";
            connect.rs = connect.execQuery(query);
            while (connect.rs.next()){
                namaTemp = connect.rs.getString("Nama");
                if(namaTemp.equals(nama)){
                    cekNama = 0;
                    System.out.println("Username sudah digunakan.");
                    break;
                }
            }
        }while (nama.length() < 8 || !nama.matches("[a-zA-Z]+") || nama.matches(".*\\d.*") || nama.matches(".*[A-Z].*") || cekNama ==  0);

        // Password
        do{
            password = "";
            System.out.print("Masukkan Password [6 digit angka]: ");
            password = sc.nextLine();
        }while (password.length() != 6 || !password.matches("[0-9]+") || password.contains(" "));

        // kode akses
        do {
            verifyKode = "";
            do {
                kodeAkses = "";
                System.out.print("Masukkan Kode Akses [6 digit alphanumerik, tanpa spasi, huruf kecil semua, dan harus mengandung angka]: ");
                kodeAkses = sc.nextLine();
            } while (kodeAkses.length() != 6 || !kodeAkses.equals(kodeAkses.toLowerCase()) || !kodeAkses.matches(".*\\d.*") || !kodeAkses.matches("[a-z0-9]+") || !kodeAkses.matches(".*[a-z].*"));
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
        int x1, x2, x3, x4, x5, x6, x7, x8, x9, x10;
        int cek = -1;

        String rekening, rekTemp;
        SecureRandom rand = new SecureRandom();
        do{
            rekTemp = "";
            query = "";
            x1 = rand.nextInt(10);
            x2 = rand.nextInt(10);
            x3 = rand.nextInt(10);
            x4 = rand.nextInt(10);
            x5 = rand.nextInt(10);
            x6 = rand.nextInt(10);
            x7 = rand.nextInt(10);
            x8 = rand.nextInt(10);
            x9 = rand.nextInt(10);
            x10 = rand.nextInt(10);
            rekTemp = Integer.toString(x1) +
                    Integer.toString(x2)+
                    Integer.toString(x3)+
                    Integer.toString(x4)+
                    Integer.toString(x5)+
                    Integer.toString(x6)+
                    Integer.toString(x7)+
                    Integer.toString(x8)+
                    Integer.toString(x9)+
                    Integer.toString(x10);

            cek = -1;
            // cek apakah ada yg duplikat rekeningnya
            rekening = "";
            query = "SELECT Rekening FROM Akun";
            connect.rs = connect.execQuery(query);
            while (connect.rs.next()){
                rekening = connect.rs.getString("Rekening");
                if(rekening.equals(rekTemp)){
                    cek = 0;
                    break;
                }
            }
        }while (cek == 0);

        rekening = rekTemp;

//      input data ke database
        Nasabah nasabah = new Nasabah();
        Akun akun = new Akun();
        DaftarRekening daftarRekening = new DaftarRekening();
        nasabah.insertCustomer(nama, alamat, sqlDate);

        // add nasabahID di tabel Akun
        int nasabahID = 0;
        query = "SELECT NasabahID FROM nasabah";
        connect.rs = connect.execQuery(query);
        while (connect.rs.next()){
            nasabahID = connect.rs.getInt("NasabahID");
        }

        akun.insertAkun(nasabahID, rekening, kodeAkses, password);

        System.out.println("\nAkun berhasil dibuat.\nSilahkan login kembali untuk akun yang telah anda buat.\n\nKlik Enter untuk lanjut Menu Utama.");
        sc.nextLine(); sc.nextLine();
    }

    public String formatSaldo(double saldoTemp){
        DecimalFormat currencyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        currencyFormat.setPositivePrefix("Rp ");
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        return currencyFormat.format(saldoTemp);
    }

    public void appMenu(String namaOld, String kodeOld) throws SQLException, ParseException {
        String nameRefresh = namaOld, kodeRefresh = kodeOld, formatSaldo;
        int choice = 0, flag;
        while(true) {
            choice = 0;
            cls();
            System.out.println("========================================================================================");
            System.out.println("                                     Hi " + nameRefresh + ",                               ");
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
                        formatSaldo = "";
                        System.out.println("========================================================================================");
                        System.out.println("|                                     P R O F I L E                                    |");
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
                            if(namaTemp.equals(nameRefresh) && kodeAksesTemp.equals(kodeRefresh))
                                break;
                        }
                        formatSaldo = formatSaldo(saldoTemp);
                        System.out.printf("| Username: %s\n| Kode Akses: %s\n| Saldo: %s\n| Alamat: %s\n| Tanggal lahir: %s\n| No. Rekening: %s\n", namaTemp, kodeAksesTemp, formatSaldo, alamatTemp, dobTemp, rekeningTemp);
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
                            Nasabah nasabah = new Nasabah();
                            Akun akun = new Akun();
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
                                        System.out.print("\nMasukkan Username baru [minimal 8 karakter alphabeth, huruf kecil semua, tanpa spasi, dan tanpa angka]: ");
                                        namaNew = sc.nextLine();
                                    }while (namaNew.length() < 8 || !namaNew.matches("[a-zA-Z]+") || namaNew.matches(".*\\d.*") || namaNew.matches(".*[A-Z].*"));

                                    // insert ke db
                                    DaftarRekening dr = new DaftarRekening();
                                    dr.updateDaftarRekening(namaNew, nameRefresh, kodeRefresh);
                                    nasabah.updateNama(namaNew, nameRefresh, kodeRefresh);
                                    nameRefresh = namaNew;
                                    System.out.println("\nUsername berhasil diupdate. Klik Enter untuk kembali ke Profile");
                                    sc.nextLine();
                                    break;
                                case 2:
                                    String kodeNew;
                                    do {
                                        kodeNew = "";
                                        System.out.print("\nMasukkan Kode Akses [terdiri dari 6 digit alphanumerik, tanpa spasi, huruf kecil semua, dan harus mengandung angka]: ");
                                        kodeNew = sc.nextLine();
                                    } while (!(kodeNew.matches("^[a-z0-9]{6}$") && kodeNew.matches(".*\\d.*")));

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
                                        System.out.print("\nMasukkan tanggal lahir [yyyy-mm-dd]: ");
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
                                        sc.nextLine(); sc.nextLine();
                                    }
                                    break;
                                case 5:
                                    repeat = true;
                                    break;
                            }
                        }else
                            repeat = false;
                    }while (repeat);
                    break;
                    
                case 2:
                    cls();
                    int choose;
                    double jumlah;
                    boolean ulang;

                    do {
                        ulang = false;
                        int cekPw = -1, cekRekAll = -1, cekRekSelf = -1,  akunIdTemp = 0, cekNamaRek = 0;
                        String password = "", query = "", namaRekTujuan = "", namaRekQry = "", rekTujuan = "", listRekTemp = "", namaRekTemp= "", rekAllTemp = "", rekSelf = "";
                        jumlah = 0.0;
                        Timestamp now = null;
                        int akunId = 0;
                        choose = 0;
                        SimpleDateFormat formatter = null;
                        String timeStampFormat = null;
                        Timestamp timestamp = null;
                        java.util.Date parsedDate = null;
                        formatSaldo = "";
                        PreparedStatement ps = null;
                        Boolean validateKode = false;
                        String passTemp = "";
                        double saldoSelf = 0.0;

                        Akun akun = new Akun();

                        int pilihanBalik = 0;
                        cls();
                        System.out.println("========================================================================================");
                        System.out.println("|                                  T R A N S A K S I                                   |");
                        System.out.println("========================================================================================");
                        System.out.println("\n1. Deposit\n2. Withdraw\n3. Daftar rekening\n4. Transfer antar rekening\n5. Kembali ke Menu");
                        do {
                            while (true) {
                                try{
                                    System.out.print("> ");
                                    choose = sc.nextInt();
                                    sc.nextLine();
                                    break;
                                }catch (InputMismatchException e) {
                                    sc.nextLine();
                                    continue;
                                }
                            }
                        } while (choose < 1 || choose > 5);
                        switch (choose) {
                            case 1:
                                ps = null;
                                cls();
                                System.out.println("========================================================================================");
                                System.out.println("|                                    D E P O S I T                                     |");
                                System.out.println("========================================================================================");
                                do {
                                    do {
                                        while (true) {
                                            try {
                                                System.out.print("\nMasukkan jumlah deposit: ");
                                                jumlah = sc.nextDouble();
                                                sc.nextLine();
                                                break;
                                            } catch (InputMismatchException e) {
                                                sc.nextLine();
                                                continue;
                                            }
                                        }
                                        if (jumlah < 10000)
                                            System.out.println("Minimal deposit Rp 10.000,00");
                                    }while (jumlah < 10000);

                                    System.out.print("Masukkan password: ");
                                    password = sc.nextLine();

                                    // cek pw kalo udh ada
                                    query = "SELECT Password FROM Akun AS A JOIN Nasabah AS N " +
                                            "ON A.NasabahID = N.NasabahID " +
                                            "WHERE Nama = ? AND KodeAkses = ?";
                                    ps = connect.preparedStatement(query);
                                    try {
                                        ps.setString(1, nameRefresh);
                                        ps.setString(2, kodeRefresh);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    connect.rs = ps.executeQuery();
                                    while (connect.rs.next()){
                                        passTemp = connect.rs.getString("Password");
                                        if(passTemp.equals(password)){
                                            cekPw = 0;
                                            break;
                                        }
                                    }
                                    if(cekPw == -1){
                                        System.out.println("Password salah.");
                                        continue;
                                    }
                                }while (cekPw == -1);

                                // search AKUNID buat DI TRANSAKSI
                                query = "SELECT AkunID FROM Akun";
                                connect.rs = connect.execQuery(query);
                                while (connect.rs.next()){
                                    akunId = connect.rs.getInt("AkunID");
                                }

                                // tanggal & waktu transaksi today
                                now = new Timestamp(System.currentTimeMillis());
                                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                timeStampFormat = formatter.format(now);
                                parsedDate = formatter.parse(timeStampFormat);
                                timestamp = new Timestamp(parsedDate.getTime());

                                // insert ke db transaksi
                                Deposit deposit = new Deposit(nameRefresh, kodeRefresh, jumlah);
                                deposit.insertTransaksi(akunId, "Deposit", timestamp);

                                // update saldo
                                akun.updateSaldoIn(jumlah, kodeRefresh, nameRefresh);

                                deposit.notif(); sc.nextLine();
                                ulang = true;

                                break;
                            case 2:
                                cls();
                                int cekBalikKeTransfer;
                                System.out.println("========================================================================================");
                                System.out.println("|                                    W I T H D R A W                                   |");
                                System.out.println("========================================================================================");
                                do {
                                    cekBalikKeTransfer = -1;
                                    cekPw = -1;
                                    pilihanBalik = -1;
                                    while (true) {
                                        try{
                                            System.out.print("\nMasukkan jumlah withdraw: ");
                                            jumlah = sc.nextDouble(); sc.nextLine();
                                            break;
                                        }catch (InputMismatchException e){
                                            sc.nextLine();
                                            continue;
                                        }
                                    }

                                    System.out.print("Masukkan password: ");
                                    password = sc.nextLine();

                                    // cek pw kalo udh ada
                                    query = "SELECT Password FROM Akun AS A JOIN Nasabah AS N " +
                                            "ON A.NasabahID = N.NasabahID " +
                                            "WHERE Nama = ? AND KodeAkses = ?";
                                    ps = connect.preparedStatement(query);
                                    try {
                                        ps.setString(1, nameRefresh);
                                        ps.setString(2, kodeRefresh);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    connect.rs = ps.executeQuery();
                                    while (connect.rs.next()){
                                        passTemp = connect.rs.getString("Password");
                                        if(passTemp.equals(password)){
                                            cekPw = 0;
                                            break;
                                        }
                                    }
                                    if(cekPw == -1 && jumlah < 10000){
                                        System.out.println("Minimal withdraw Rp 10.000,00 dan Password salah.");
                                        continue;
                                    }
                                    if (jumlah < 10000){
                                        System.out.println("Minimal withdraw Rp 10.000,00");
                                        continue;
                                    }
                                    if(cekPw == -1){
                                        System.out.println("Password salah.");
                                        continue;
                                    }


                                    // cek apakah yg di tf tu cukup dari saldonya
                                    query = "";
                                    saldoSelf = 0.0;
                                    query = "SELECT Saldo " +
                                            "FROM Akun AS A JOIN Nasabah AS N " +
                                            "ON A.NasabahID = N.NasabahID " +
                                            "WHERE Nama = ? AND KodeAkses = ?";
                                    ps = connect.preparedStatement(query);
                                    try {
                                        ps.setString(1, nameRefresh);
                                        ps.setString(2, kodeRefresh);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    connect.rs = ps.executeQuery();
                                    while (connect.rs.next()) {
                                        saldoSelf = connect.rs.getDouble("Saldo");
                                    }
                                    if (jumlah > saldoSelf) {
                                        System.out.println("\nSaldo tidak cukup.");
                                        System.out.println("\nKembali ke Menu Transaksi ?\n1. Ya\n2. Tidak");
                                        do {
                                            while (true) {
                                                try {
                                                    System.out.print("> ");
                                                    pilihanBalik = sc.nextInt();
                                                    sc.nextLine();
                                                    break;
                                                } catch (InputMismatchException e) {
                                                    sc.nextLine();
                                                    continue;
                                                }
                                            }
                                        } while (pilihanBalik < 1 || pilihanBalik > 2);
                                    }

                                    if (pilihanBalik == 1) {
                                        break;
                                    } else if (pilihanBalik == 2)
                                        cekBalikKeTransfer = 0;
                                }while (jumlah < 10000 || cekPw == -1 || cekBalikKeTransfer == 0);
                                if(pilihanBalik == 1){
                                    ulang = true;
                                    break;
                                }

                                // search AKUNID buat DI TRANSAKSI
                                query = "SELECT AkunID FROM Akun";
                                connect.rs = connect.execQuery(query);
                                while (connect.rs.next()){
                                    akunId = connect.rs.getInt("AkunID");
                                }

                                // tanggal & waktu transaksi today
                                // tanggal & waktu transaksi today
                                now = new Timestamp(System.currentTimeMillis());
                                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                timeStampFormat = formatter.format(now);
                                parsedDate = formatter.parse(timeStampFormat);
                                timestamp = new Timestamp(parsedDate.getTime());

                                // insert ke db transaksi
                                Withdraw withdraw = new Withdraw(nameRefresh, kodeRefresh, jumlah);
                                withdraw.insertTransaksi(akunId, "Withdraw", timestamp);

                                // update saldo
                                akun.updateSaldoOut(jumlah, kodeRefresh, nameRefresh);

                                withdraw.notif(); sc.nextLine();

                                ulang = true;
                                break;

                            case 3:
                                cls();

                                System.out.println("========================================================================================");
                                System.out.println("|                          D A F T A R    R E K E N I N G                              |");
                                System.out.println("========================================================================================");
                                do {
                                    rekSelf = "";
                                    cekRekAll = -1;
                                    listRekTemp = "";
                                    namaRekTemp = "";
                                    rekTujuan = "";
                                    cekRekSelf = -1;
                                    query = "";
                                    rekAllTemp = "";
                                    ps = null;
                                    cekPw = -1;
                                    rekAllTemp = "";

                                    System.out.print("\nMasukkan Rekening tujuan: ");
                                    rekTujuan = sc.nextLine();

                                    // cek apakah rekTujuan = rekSELF
                                    query = "SELECT Rekening FROM Akun AS A JOIN Nasabah AS N " +
                                            "ON A.NasabahID = N.NasabahID " +
                                            "WHERE Nama = ? AND KodeAkses = ?";
                                    ps = connect.preparedStatement(query);
                                    try {
                                        ps.setString(1, nameRefresh);
                                        ps.setString(2, kodeRefresh);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    connect.rs = ps.executeQuery();
                                    while (connect.rs.next()) {
                                        rekSelf = connect.rs.getString("Rekening");
                                        if (rekSelf.equals(rekTujuan)) {
                                            System.out.println("Tidak dapat menambahkan rekening sendiri.");
                                            cekRekSelf = 0;
                                            break;
                                        }
                                    }
                                    if(cekRekSelf == 0)
                                        continue;

                                    // cek all data akun customer, apa ada rekening yang dituju
                                    query = "SELECT Rekening FROM Akun";
                                    connect.rs = connect.execQuery(query);
                                    while (connect.rs.next()) {
                                        rekAllTemp = connect.rs.getString("Rekening");
                                        if (rekAllTemp.equals(rekTujuan)) {
                                            cekRekAll = 0;
                                            break;
                                        }
                                    }

                                    // kalo rekening ga ketemu
                                    if (cekRekAll == -1){
                                        System.out.println("Rekening tidak ditemukan.");

                                        System.out.println("\nKembali ke Menu Transaksi ?\n1. Ya\n2. Tidak");
                                        do {
                                            while (true){
                                                try {
                                                    System.out.print("> ");
                                                    pilihanBalik = sc.nextInt(); sc.nextLine();
                                                    break;
                                                }catch (InputMismatchException e){
                                                    sc.nextLine();
                                                    continue;
                                                }
                                            }
                                        }while (pilihanBalik < 1 || pilihanBalik > 2);
                                        if(pilihanBalik == 1){
                                            ulang = true;
                                            break;
                                        }
                                    }else{ //kalo ada rekeing yg dituju : dari rekTujuan
                                        // mulai cek daftar rekening di suatu akun (kalo rekening yg dituju ada yg sama, input rekening lagi & kasi notif) + sekaligus dapetin akunID punya pengguna now buat dimasukin ke DR
                                        query = "SELECT listRek " +
                                                "FROM DaftarRekening AS DR JOIN Akun AS A ON DR.AkunID = A.AkunID " +
                                                "JOIN Nasabah AS N ON A.NasabahID = N.NasabahID " +
                                                "WHERE Nama = ? AND KodeAkses = ?";
                                        ps = connect.preparedStatement(query);
                                        try {
                                            ps.setString(1, nameRefresh);
                                            ps.setString(2, kodeRefresh);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        connect.rs = ps.executeQuery();
                                        while (connect.rs.next()) {
                                            listRekTemp = connect.rs.getString("ListRek");
                                            if (listRekTemp.equals(rekTujuan)) {
                                                cekRekSelf = 0;
                                                System.out.println("Rekening sudah terdaftar. Silahkan masukkan ulang.");
                                                break;
                                            }
                                        }
                                        if(cekRekSelf == 0)
                                            continue;


                                        // validasi pw dulu sebelum masukin rekening baru
                                        do {
                                            System.out.print("Masukkan password: ");
                                            password = sc.nextLine();

                                            query = "SELECT Password FROM Akun AS A JOIN Nasabah AS N " +
                                                    "ON A.NasabahID = N.NasabahID " +
                                                    "WHERE Nama = ? AND KodeAkses = ?";
                                            ps = connect.preparedStatement(query);
                                            try {
                                                ps.setString(1, nameRefresh);
                                                ps.setString(2, kodeRefresh);
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }
                                            connect.rs = ps.executeQuery();
                                            while (connect.rs.next()) {
                                                passTemp = connect.rs.getString("Password");
                                                if (passTemp.equals(password)) {
                                                    cekPw = 0;
                                                    break;
                                                }
                                            }
                                            if (cekPw == -1) {
                                                System.out.println("Password salah. Silahkan masukkan ulang\n");
                                            }
                                        }while (cekPw == -1);
                                    }
                                    if(cekPw == 0) {
                                        break;
                                    }
                                }while (pilihanBalik == 2 || cekRekSelf == 0);

                                // kalo balik menu transaksi
                                if(pilihanBalik == 1){
                                    break;
                                }

                                // kalo lanjut 1 -> (dapetin nilai namaRek dari akun rekTujuan) : NB: LIST REK GA PERLU DIMASUKIN SOALNYA NGAMBIL DARI REKTUJUAN
                                query = "SELECT Nama " +
                                        "FROM Nasabah AS N JOIN Akun A ON N.NasabahID = A.NasabahID " +
                                        "WHERE Rekening = ?";
                                ps = connect.preparedStatement(query);
                                try{
                                    ps.setString(1, rekTujuan);
                                }catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                connect.rs = ps.executeQuery();
                                while (connect.rs.next()){
                                    namaRekTemp = connect.rs.getString("Nama");
                                }


                                // lanjutan -> (rekening ketemu & ga ada yg sama -> tinggal input aja)
                                DaftarRekening daftarRekening = new DaftarRekening();

                                // cari akunID user yg login buat dimasukin ke daftar rekening
                                query = "SELECT AkunID " +
                                        "FROM Akun AS A " +
                                        "JOIN Nasabah AS N ON A.NasabahID = N.NasabahID " +
                                        "WHERE Nama = ? AND KodeAkses = ?";
                                ps = connect.preparedStatement(query);
                                try {
                                    ps.setString(1, nameRefresh);
                                    ps.setString(2, kodeRefresh);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                connect.rs = ps.executeQuery();
                                while (connect.rs.next()) {
                                    akunIdTemp = connect.rs.getInt("AkunID");
                                }
                                daftarRekening.insertDaftarRekening(akunIdTemp, namaRekTemp, rekTujuan); // AkunIdTemp : akun user skg yg dipake buat login ; namaRekTemp+rekTujuan : nama & rek dari rekening yg dituju
                                System.out.printf("Rekening berhasil ditambahkan.\nNo. Rek: %s\nNama: %s\n\nKlik Enter untuk kembali ke Menu Transaksi.\n", rekTujuan, namaRekTemp); sc.nextLine();
                                ulang = true;
                                break;

                            case 4:
                                cls();
                                cekBalikKeTransfer = -1;

                                System.out.println("========================================================================================");
                                System.out.println("|                    T R A N S F E R   A N T A R   R E K E N I N G                     |");
                                System.out.println("========================================================================================");
                                do {
                                    do {
                                        cekBalikKeTransfer = -1;
                                        namaRekTujuan = "";
                                        cekNamaRek = -1;
                                        cekRekSelf = -1;
                                        pilihanBalik = -1;

                                        System.out.print("\nMasukkan Nama Rekening: ");
                                        namaRekTujuan = sc.nextLine();

                                        // cek apakah rekTujuan = rekSELF
                                        query = "";
                                        query = "SELECT Rekening FROM Akun AS A JOIN Nasabah AS N " +
                                                "ON A.NasabahID = N.NasabahID " +
                                                "WHERE Nama = ? AND KodeAkses = ?";
                                        ps = connect.preparedStatement(query);
                                        try {
                                            ps.setString(1, nameRefresh);
                                            ps.setString(2, kodeRefresh);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        connect.rs = ps.executeQuery();
                                        while (connect.rs.next()) {
                                            rekSelf = connect.rs.getString("Rekening");
                                            if (rekSelf.equals(rekTujuan)) {
                                                System.out.println("Tidak dapat menambahkan rekening sendiri.");
                                                cekRekSelf = 0;
                                                break;
                                            }
                                        }
                                        if (cekRekSelf == 0)
                                            continue;

                                        // search : apa ada rekening yg dituju
                                        query = "SELECT NamaRek " +
                                                "FROM DaftarRekening " +
                                                "WHERE AkunID IN ( " +
                                                "   SELECT AkunID " +
                                                "   FROM Akun AS A JOIN Nasabah AS N ON A.NasabahID = N.NasabahID " +
                                                "   WHERE Nama = ? AND KodeAkses = ? " +
                                                ")";
                                        ps = connect.preparedStatement(query);
                                        try {
                                            ps.setString(1, nameRefresh);
                                            ps.setString(2, kodeRefresh);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        connect.rs = ps.executeQuery();
                                        while (connect.rs.next()) {
                                            namaRekQry = connect.rs.getString("NamaRek");
                                            if (namaRekQry.equalsIgnoreCase(namaRekTujuan)) {
                                                cekNamaRek = 0;
                                                break;
                                            }
                                        }

                                        // kalo rekening ga ketemu
                                        if (cekNamaRek == -1) {
                                            System.out.println("Nama Rekening tidak ditemukan.");

                                            System.out.println("\nKembali ke Menu Transaksi ?\n1. Ya\n2. Tidak");
                                            do {
                                                while (true) {
                                                    try {
                                                        System.out.print("> ");
                                                        pilihanBalik = sc.nextInt();
                                                        sc.nextLine();
                                                        break;
                                                    } catch (InputMismatchException e) {
                                                        sc.nextLine();
                                                        continue;
                                                    }
                                                }
                                            } while (pilihanBalik < 1 || pilihanBalik > 2);
                                        }
                                    } while (pilihanBalik == 2 || cekRekSelf == 0);

                                    // kalo balek ke menu transaksi
                                    if (pilihanBalik == 1) {
                                        ulang = true;
                                        break;
                                    }

                                    // kalo ketemu
                                    while (true) {
                                        try {
                                            System.out.print("Masukkan jumlah transfer: ");
                                            jumlah = sc.nextDouble();
                                            sc.nextLine();
                                            break;
                                        } catch (InputMismatchException e) {
                                            sc.nextLine();
                                            continue;
                                        }
                                    }

                                    // cek apakah yg di tf tu cukup dari saldonya
                                    query = "";
                                    saldoSelf = 0.0;
                                    query = "SELECT Saldo " +
                                            "FROM Akun AS A JOIN Nasabah AS N " +
                                            "ON A.NasabahID = N.NasabahID " +
                                            "WHERE Nama = ? AND KodeAkses = ?";
                                    ps = connect.preparedStatement(query);
                                    try {
                                        ps.setString(1, nameRefresh);
                                        ps.setString(2, kodeRefresh);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    connect.rs = ps.executeQuery();
                                    while (connect.rs.next()) {
                                        saldoSelf = connect.rs.getDouble("Saldo");
                                    }
                                    if (jumlah > saldoSelf) {
                                        System.out.println("\nSaldo tidak cukup.");
                                        System.out.println("\nKembali ke Menu Transaksi ?\n1. Ya\n2. Tidak");
                                        do {
                                            while (true) {
                                                try {
                                                    System.out.print("> ");
                                                    pilihanBalik = sc.nextInt();
                                                    sc.nextLine();
                                                    break;
                                                } catch (InputMismatchException e) {
                                                    sc.nextLine();
                                                    continue;
                                                }
                                            }
                                        } while (pilihanBalik < 1 || pilihanBalik > 2);
                                    }
                                    if (pilihanBalik == 1) {
                                        break;
                                    } else if (pilihanBalik == 2)
                                        cekBalikKeTransfer = 0;

                                }while (cekBalikKeTransfer == 0);
                                if(pilihanBalik == 1){
                                    ulang = true;
                                    break;
                                }

                                // kalo saldo cukup , input ke database
                                // saldoku ngurang, trus saldo yg di akun penerima jd nambah

                                // ngurangi di saldoku
                                akun.updateSaldoOut(jumlah, kodeRefresh, nameRefresh);

                                // nambah saldo di penerima
                                akun.updateSaldoInVersi2(jumlah, namaRekTujuan);

                                Deposit depo = new Deposit(nameRefresh, kodeRefresh, jumlah);
                                now = new Timestamp(System.currentTimeMillis());
                                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                timeStampFormat = formatter.format(now);
                                parsedDate = formatter.parse(timeStampFormat);
                                timestamp = new Timestamp(parsedDate.getTime());

                                query = "SELECT AkunID FROM Akun";
                                connect.rs = connect.execQuery(query);
                                while (connect.rs.next()){
                                    akunId = connect.rs.getInt("AkunID");
                                }
                                depo.insertTransaksi(akunId, "Transfer", timestamp);


                                Transfer tf = new Transfer(nameRefresh,kodeRefresh,jumlah);
                                tf.notif(); sc.nextLine();

                                ulang = true;
                                break;

                            case 5:
                                ulang = false;
                                break;
                        }
                    }while (ulang);
                    break;
                case 3:
                    return;
            }
        }
    }

    public void loginMenu() throws SQLException, ParseException {
        String kode = "", name = "", alamat = "";
        Date dob = null;
        int flag, count = 1;
        String nameTemp = "", kodeTemp = "";
        System.out.println("========================================================================================");
        System.out.println("|                                      L O G I N                                       |");
        System.out.println("========================================================================================");
        do {
            kode = "";
            name = "";
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
                if(nameTemp.equals(name)){
                    flag = 0;
                    break;
                }
            }
            if(flag == 0){
                if(kodeTemp.equals(kode)){

                }else{
                    if(count >= 3){
                        System.out.println("Kode Akses salah. Klik Enter untuk kembali ke menu utama."); sc.nextLine();
                        return;
                    }
                    System.out.printf("Kode Akses salah. Kesempatan (%d / 3).\n\n", count);

                    flag = -1;
                    count++;

                }
            }
            else {
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
        }while(flag == -1);

        System.out.println("Login berhasil.\nKlik Enter untuk melanjutkan aplikasi.");
        sc.nextLine();
        cls();
        appMenu(name, kode);
    }

    public Main() throws SQLException, ParseException {


        int choice;

        while(true) {
            cls();
            choice = 0;
            System.out.println("========================================================================================");
            System.out.println("|                                 M A I N   M E N U                                    |");
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
                    System.out.println("Terimakasih telah menggunakan Basuan M-Banking");
                    System.exit(0);
            }
        }
    }

    public static void main(String[] args) throws SQLException, ParseException {
        new Main();
    }
}