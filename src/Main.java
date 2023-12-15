import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.sql.Date;
import java.time.ZoneId;
import java.util.InputMismatchException;
import java.util.Scanner;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
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
        System.out.println("|------\\        /---\\        /-----------|   |--|  |--|        /---\\        |----\\  |--|");
        System.out.println("|  |\\   |      /  /\\ \\       |  /--------|   |  |  |  |       /  /\\ \\       |     \\ |  |");
        System.out.println("|    __/      /  /__\\ \\      |  \\________    |  |  |  |      /  /__\\ \\      |  |\\  \\|  |");
        System.out.println("|   _  \\     /  _____  \\     |_________  |   |  |  |  |     /  _____  \\     |  | \\     |");
        System.out.println("|  |_\\  |   /  /     \\  \\    _________|  |   |  \\__/  |    /  /     \\  \\    |  |  \\    |");
        System.out.println("|------/   /__/       \\__\\   |___________/   \\________/   /__/       \\__\\   |__|   \\___|");
        System.out.println("========================================================================================");
        System.out.println("                                     R E G I S T E R");
        System.out.println("========================================================================================");
        System.out.println("                           Daftarkan diri anda terlebih dahulu\n ");

        // nama
        do {
            nama = "";
            System.out.print("Masukkan Username [minimal 8 karakter alphabeth, huruf kecil semua, tanpa spasi, dan tanpa angka]: ");
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
        System.out.println("|------\\        /---\\        /-----------|   |--|  |--|        /---\\        |----\\  |--|");
        System.out.println("|  |\\   |      /  /\\ \\       |  /--------|   |  |  |  |       /  /\\ \\       |     \\ |  |");
        System.out.println("|    __/      /  /__\\ \\      |  \\________    |  |  |  |      /  /__\\ \\      |  |\\  \\|  |");
        System.out.println("|   _  \\     /  _____  \\     |_________  |   |  |  |  |     /  _____  \\     |  | \\     |");
        System.out.println("|  |_\\  |   /  /     \\  \\    _________|  |   |  \\__/  |    /  /     \\  \\    |  |  \\    |");
        System.out.println("|------/   /__/       \\__\\   |___________/   \\________/   /__/       \\__\\   |__|   \\___|");
        System.out.println("========================================================================================");
        System.out.println("                                     R E G I S T E R");
        System.out.println("========================================================================================");
        System.out.println("                Lengkapi biodata anda untuk melanjutkan proses registrasi\n");

        System.out.print("Masukkan Alamat: ");
        alamat = sc.nextLine();
        boolean validInput = false;
        Date dobSQL = null;
        while (!validInput) {
            System.out.print("Masukkan tanggal lahir [dd/MM/yy]: ");
            String inputDOB = sc.nextLine();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            dateFormat.setLenient(false);

            try {
                dobSQL = (Date) dateFormat.parse(inputDOB);

                // Validasi umur minimal regist
                LocalDate tanggalLahir = dobSQL.toLocalDate();
                Period selisih = Period.between(tanggalLahir, LocalDate.now());
                if (selisih.getYears() < 17) {
                    System.out.println("Maaf, umur Anda belum cukup untuk membuka rekening. Minimal 17 tahun untuk membuka rekening.");
                    System.out.println("Silakan kembali apabila umur Anda sudah mencukupi.");
                    System.out.println("Tekan Enter untuk kembali ke Menu Utama. Terima kasih.");
                    sc.nextLine();
                    return;
                }

                validInput = true; // Input valid, keluar dari loop

            } catch (ParseException e) {
                System.out.println("Format tanggal yang Anda masukkan salah. Gunakan format dd/MM/yy.");
                System.out.println("Silakan coba lagi.");
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

        // input data ke database
        Nasabah nasabah = new Nasabah(nama, alamat, dobSQL);
        Akun akun = new Akun(rekening,0.00, kodeAkses);
        nasabah.insertCustomer(nama, alamat, dobSQL);
        akun.insertAkun(rekening, kodeAkses);

        System.out.println("Akun berhasil dibuat.\nSilahkan login kembali untuk akun yang telah anda buat.\nKlik Enter untuk lanjut Menu Utama.");
        sc.nextLine();
    }

    public void appMenu(ResultSet rs) throws SQLException {
        int choice = 0;
        while(true) {
            choice = 0;
            System.out.println("========================================================================================");
            System.out.println("|------\\        /---\\        /-----------|   |--|  |--|        /---\\        |----\\  |--|");
            System.out.println("|  |\\   |      /  /\\ \\       |  /--------|   |  |  |  |       /  /\\ \\       |     \\ |  |");
            System.out.println("|    __/      /  /__\\ \\      |  \\________    |  |  |  |      /  /__\\ \\      |  |\\  \\|  |");
            System.out.println("|   _  \\     /  _____  \\     |_________  |   |  |  |  |     /  _____  \\     |  | \\     |");
            System.out.println("|  |_\\  |   /  /     \\  \\    _________|  |   |  \\__/  |    /  /     \\  \\    |  |  \\    |");
            System.out.println("|------/   /__/       \\__\\   |___________/   \\________/   /__/       \\__\\   |__|   \\___|");
            System.out.println("========================================================================================");
            System.out.println("                            Selamat Datang di Basuan Mobile");
            System.out.println("========================================================================================");
            System.out.print("\n1. Cek Profile\n2. Transaksi\n3. Kembali\n> ");
            choice = sc.nextInt(); sc.nextLine();
            switch (choice) {
                case 1:
                    cls();
                    int choose = 0;
                    System.out.println("========================================================================================");
                    System.out.println("|------\\        /---\\        /-----------|   |--|  |--|        /---\\        |----\\  |--|");
                    System.out.println("|  |\\   |      /  /\\ \\       |  /--------|   |  |  |  |       /  /\\ \\       |     \\ |  |");
                    System.out.println("|    __/      /  /__\\ \\      |  \\________    |  |  |  |      /  /__\\ \\      |  |\\  \\|  |");
                    System.out.println("|   _  \\     /  _____  \\     |_________  |   |  |  |  |     /  _____  \\     |  | \\     |");
                    System.out.println("|  |_\\  |   /  /     \\  \\    _________|  |   |  \\__/  |    /  /     \\  \\    |  |  \\    |");
                    System.out.println("|------/   /__/       \\__\\   |___________/   \\________/   /__/       \\__\\   |__|   \\___|");
                    System.out.println("========================================================================================");
                    System.out.println("                                      P R O F I L E");
                    System.out.println("========================================================================================");
                    String nama = rs.getString("Nama");
                    String alamat = rs.getString("Alamat");
                    Date dob = rs.getDate("DOB");
                    String birth = dob.toString();
                    String rekening = rs.getString("Rekening");
                    Double saldo = rs.getDouble("Saldo");
                    String kodeAkses = rs.getString("KodeAkses");
                    System.out.printf("Username: %s\nKode Akses: %s\nSaldo: %.2f\nAlamat: %s\nTanggal lahir: %s\nNo. Rekening: %s\n", nama, kodeAkses, saldo, alamat, birth, rekening);
                    System.out.println("================================================================");
                    System.out.println("Ingin update data?\n1. Ya\n2. Tidak");
                    do {
                        System.out.print("> ");
                        choose = sc.nextInt(); sc.nextLine();
                    }while(choose < 1 || choose > 2);

                    // kalau update
                    if(choose == 1){
                        int pilih = 0;
                        System.out.print("\nPilih data yang ingin diupdate:\n1. Username\n2. Kode Akses\n3. Alamat\n4. Tanggal Lahir\n> ");
                        pilih = sc.nextInt(); sc.nextLine();
                        if(pilih == 1){
                            String name;
                            do {
                                name = "";
                                System.out.print("Masukkan Username baru [minimal 8 karakter alphabeth, huruf kecil semua, tanpa spasi, dan tanpa angka]: ");
                                name = sc.nextLine();
                            }while (name.length() < 8 || !name.matches("[a-zA-Z]+") || name.matches(".*\\d.*") || name.matches(".*[A-Z].*"));
                            String query = "UPDATE Nasabah" +
                                            "SET Nama = ? WHERE Nama = ?";
                        }
                    }
                    break;
                case 3:
                    return;
            }
        }
    }

    public void loginMenu() throws SQLException{
        String kode = "", name = "";
        int flag, count = 1;
        System.out.println("========================================================================================");
        System.out.println("|------\\        /---\\        /-----------|   |--|  |--|        /---\\        |----\\  |--|");
        System.out.println("|  |\\   |      /  /\\ \\       |  /--------|   |  |  |  |       /  /\\ \\       |     \\ |  |");
        System.out.println("|    __/      /  /__\\ \\      |  \\________    |  |  |  |      /  /__\\ \\      |  |\\  \\|  |");
        System.out.println("|   _  \\     /  _____  \\     |_________  |   |  |  |  |     /  _____  \\     |  | \\     |");
        System.out.println("|  |_\\  |   /  /     \\  \\    _________|  |   |  \\__/  |    /  /     \\  \\    |  |  \\    |");
        System.out.println("|------/   /__/       \\__\\   |___________/   \\________/   /__/       \\__\\   |__|   \\___|");
        System.out.println("========================================================================================");
        System.out.println("                                       L O G I N");
        System.out.println("========================================================================================");
        do {
            flag = -1;
            System.out.println("Masukkan Username: ");
            name = sc.nextLine();
            System.out.print("Masukkan Kode Akses: ");
            kode = sc.nextLine();

            // validasi
            String query = "SELECT * FROM nasabah n JOIN akun a ON n.NasabahID = a.NasabahID" +
                            "WHERE Nama = ? AND KodeAkses = ?";
            connect.rs = connect.execQuery(query);

            if(connect.rs == null){
                System.out.printf("Kode Akses salah. Kesempatan (%d / 3).", count);
                count++;
                if(count > 3)
                    return;
                flag = -1;
            }else
                flag = 0;
        }while(flag == -1);

        System.out.println("Login berhasil.\nKlik Enter untuk melanjutkan aplikasi.");
        sc.nextLine();
        cls();
        appMenu(connect.rs);
    }

    public Main() throws SQLException {

        int choice;

        while(true) {
            cls();
            choice = 0;
            System.out.println("========================================================================================");
            System.out.println("|------\\        /---\\        /-----------|   |--|  |--|        /---\\        |----\\  |--|");
            System.out.println("|  |\\   |      /  /\\ \\       |  /--------|   |  |  |  |       /  /\\ \\       |     \\ |  |");
            System.out.println("|    __/      /  /__\\ \\      |  \\________    |  |  |  |      /  /__\\ \\      |  |\\  \\|  |");
            System.out.println("|   _  \\     /  _____  \\     |_________  |   |  |  |  |     /  _____  \\     |  | \\     |");
            System.out.println("|  |_\\  |   /  /     \\  \\    _________|  |   |  \\__/  |    /  /     \\  \\    |  |  \\    |");
            System.out.println("|------/   /__/       \\__\\   |___________/   \\________/   /__/       \\__\\   |__|   \\___|");
            System.out.println("========================================================================================");
            System.out.println("                                  M A I N   M E N U");
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