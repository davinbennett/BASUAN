import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class test
{
    public test() throws ParseException {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

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
                    System.out.println("Maaf, umur Anda belum cukup untuk membuka rekening. Minimal 17 tahun untuk membuka rekening.");
                    System.out.println("Silakan kembali apabila umur Anda sudah mencukupi.");
                    System.out.println("Tekan Enter untuk kembali ke Menu Utama. Terima kasih.");
                    sc.nextLine();
                    return;
                }
                System.out.println(selisih.getYears());
                formatBenar = true;

            } catch (ParseException e) {
                System.out.println("Format tanggal yang Anda masukkan salah. Gunakan format yyyy/mm/dd.");
                System.out.println("Silakan coba lagi.\n");
            }
        }

    }
    public static void main(String[] args) throws ParseException {
        new test();
    }

}
