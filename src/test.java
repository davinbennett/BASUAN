import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {

    public static void main(String[] args) {
        String dateString = "2023-12-19 14:30:00"; // Ganti dengan string tanggal-waktu yang sesuai

        // Gunakan SimpleDateFormat untuk mengonversi string ke objek Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parsedDate;

        try {
            parsedDate = dateFormat.parse(dateString);

            // Mengonversi objek Date ke java.sql.Timestamp
            Timestamp timestamp = new Timestamp(parsedDate.getTime());

            System.out.println("String asli: " + dateString);
            System.out.println("java.sql.Timestamp: " + timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
